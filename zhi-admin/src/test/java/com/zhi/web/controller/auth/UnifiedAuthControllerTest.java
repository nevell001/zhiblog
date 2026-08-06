package com.zhi.web.controller.auth;

import com.zhi.common.core.domain.entity.SysUser;
import com.zhi.common.core.domain.model.LoginUser;
import com.zhi.framework.web.service.SysLoginService;
import com.zhi.framework.web.service.TokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 统一认证控制器测试
 * 覆盖 /auth/login、/auth/user/info、/auth/logout 三条统一认证链路
 */
class UnifiedAuthControllerTest {

    private SysLoginService loginService;
    private TokenService tokenService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        loginService = mock(SysLoginService.class);
        tokenService = mock(TokenService.class);

        UnifiedAuthController controller = new UnifiedAuthController();
        ReflectionTestUtils.setField(controller, "loginService", loginService);
        ReflectionTestUtils.setField(controller, "tokenService", tokenService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void loginShouldReturnToken() throws Exception {
        when(loginService.login(eq("admin"), eq("password"), any(), any(), any())).thenReturn("token-123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"password\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.token").value("token-123"));
    }

    @Test
    void loginShouldRejectEmptyUsernameOrPassword() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"\",\"password\":\"password\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(500))
            .andExpect(jsonPath("$.msg").value("用户名和密码不能为空"));

        verify(loginService, never()).login(any(), any(), any(), any(), any());
    }

    @Test
    void loginShouldReturnErrorWhenServiceFails() throws Exception {
        when(loginService.login(eq("admin"), eq("wrong-password"), any(), any(), any()))
            .thenThrow(new RuntimeException("认证失败"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"wrong-password\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(500))
            .andExpect(jsonPath("$.msg").value("登录失败：认证失败"));
    }

    @Test
    void getUserInfoShouldReturnUserData() throws Exception {
        SysUser user = new SysUser();
        user.setUserId(1L);
        user.setUserName("admin");
        user.setNickName("管理员");
        user.setUserType("00");
        LoginUser loginUser = new LoginUser(user, Collections.emptySet());
        loginUser.setToken("token-123");
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(loginUser, null));

        mockMvc.perform(get("/auth/user/info"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.userName").value("admin"))
            .andExpect(jsonPath("$.data.nickName").value("管理员"))
            .andExpect(jsonPath("$.data.userType").value("00"));
    }

    @Test
    void getUserInfoShouldReturn401WhenAnonymous() throws Exception {
        mockMvc.perform(get("/auth/user/info"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(401));
    }

    @Test
    void logoutShouldDeleteToken() throws Exception {
        SysUser user = new SysUser();
        user.setUserId(1L);
        user.setUserName("admin");
        LoginUser loginUser = new LoginUser(user, Collections.emptySet());
        loginUser.setToken("token-123");
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(loginUser, null));

        mockMvc.perform(post("/auth/logout"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200));

        verify(tokenService).delLoginUser("token-123");
    }
}
