package com.zhi.framework.security.handle;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.zhi.common.constant.HttpStatus;
import com.zhi.common.core.domain.entity.SysUser;
import com.zhi.common.core.domain.model.LoginUser;
import com.zhi.framework.testsupport.SpringContextStub;
import com.zhi.framework.web.service.TokenService;
import com.zhi.system.service.ISysLogininforService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 认证失败与退出处理单元测试。
 *
 * <p>未登录必须拿到 HTTP 200 + code 401 的 JSON（前端拦截器依赖这个形状，而不是裸 401 状态码）；
 * 退出必须把会话从缓存里摘掉，否则同一个 JWT 在剩余 TTL 内仍然被当作有效身份。</p>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SecurityHandlersTest
{
    @Mock
    private TokenService tokenService;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp()
    {
        SpringContextStub.install();
        request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    private LoginUser session(String username, String token)
    {
        SysUser user = new SysUser();
        user.setUserName(username);
        LoginUser loginUser = new LoginUser(7L, 3L, user, Collections.emptySet());
        loginUser.setToken(token);
        return loginUser;
    }

    private static JSONObject bodyOf(MockHttpServletResponse response) throws Exception
    {
        return JSON.parseObject(response.getContentAsString());
    }

    /** 异步任务由 AsyncManager 在 10ms 后调度，断言前给它一个短轮询窗口 */
    private static void await(Runnable assertion) throws InterruptedException
    {
        long deadline = System.currentTimeMillis() + 3000L;
        AssertionError last = null;
        while (System.currentTimeMillis() < deadline)
        {
            try
            {
                assertion.run();
                return;
            }
            catch (AssertionError e)
            {
                last = e;
                Thread.sleep(50L);
            }
        }
        throw last;
    }

    @Test
    void unauthenticatedAccessAnswersWithCode401AndTheRequestedUri() throws Exception
    {
        AuthenticationEntryPointImpl entryPoint = new AuthenticationEntryPointImpl();
        MockHttpServletRequest target = new MockHttpServletRequest("GET", "/system/stats/overview");
        MockHttpServletResponse response = new MockHttpServletResponse();

        entryPoint.commence(target, response, new InsufficientAuthenticationException("Full authentication is required"));

        assertThat(response.getStatus()).isEqualTo(HttpStatus.SUCCESS);
        assertThat(response.getContentType()).contains("application/json");
        assertThat(bodyOf(response).getInteger("code")).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(bodyOf(response).getString("msg")).contains("/system/stats/overview").contains("认证失败");
    }

    @Test
    void logoutEvictsTheCachedSessionAndRecordsTheExit() throws Exception
    {
        LogoutSuccessHandlerImpl handler = new LogoutSuccessHandlerImpl();
        ReflectionTestUtils.setField(handler, "tokenService", tokenService);
        LoginUser loginUser = session("zhangsan", "uuid-logout");
        when(tokenService.getLoginUser(any())).thenReturn(loginUser);
        ISysLogininforService logininfor = SpringContextStub.bean(ISysLogininforService.class);
        MockHttpServletResponse response = new MockHttpServletResponse();

        handler.onLogoutSuccess(request, response,
                new UsernamePasswordAuthenticationToken(loginUser, null));

        verify(tokenService).delLoginUser("uuid-logout");
        assertThat(bodyOf(response).getInteger("code")).isEqualTo(HttpStatus.SUCCESS);
        // 退出日志走 AsyncManager（10ms 后调度），必须真的落到登录日志服务
        await(() -> verify(logininfor, atLeastOnce()).insertLogininfor(any()));
    }

    @Test
    void anonymousLogoutStillAnswersSuccessfullyWithoutTouchingTheCache() throws Exception
    {
        LogoutSuccessHandlerImpl handler = new LogoutSuccessHandlerImpl();
        ReflectionTestUtils.setField(handler, "tokenService", tokenService);
        when(tokenService.getLoginUser(any())).thenReturn(null);
        MockHttpServletResponse response = new MockHttpServletResponse();

        handler.onLogoutSuccess(request, response, null);

        assertThat(bodyOf(response).getInteger("code")).isEqualTo(HttpStatus.SUCCESS);
        verify(tokenService, never()).delLoginUser(anyString());
    }

    private void await(java.util.concurrent.Callable<Void> assertion) throws Exception
    {
        long deadline = System.currentTimeMillis() + 3000L;
        while (true)
        {
            try
            {
                assertion.call();
                return;
            }
            catch (Throwable expectedWhilePending)
            {
                if (System.currentTimeMillis() > deadline)
                {
                    throw new AssertionError("异步任务未在超时前执行", expectedWhilePending);
                }
                Thread.sleep(50L);
            }
        }
    }
}
