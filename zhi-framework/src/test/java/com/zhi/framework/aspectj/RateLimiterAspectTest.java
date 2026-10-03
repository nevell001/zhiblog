package com.zhi.framework.aspectj;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zhi.common.annotation.RateLimiter;
import com.zhi.common.enums.LimitType;
import com.zhi.common.exception.ServiceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

/**
 * {@code @RateLimiter} 切面单元测试。
 *
 * <p>评论、浏览量、登录、上传这些匿名/半匿名接口都靠它兜底，必须锁住三件事：
 * 超过阈值拒绝（而不是放行）、Redis 异常时按"服务器限流异常"拒绝而不是漏放行，
 * 以及 IP 维度的 key 确实带上调用方 IP。</p>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RateLimiterAspectTest
{
    /** 提供注解元数据的夹具 */
    static class Fixture
    {
        @RateLimiter(key = "rl:comment:", time = 60, count = 10)
        public void globalLimited()
        {
        }

        @RateLimiter(key = "rl:view:", time = 60, count = 20, limitType = LimitType.IP)
        public void ipLimited()
        {
        }
    }

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private RedisScript<Long> limitScript;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private MethodSignature signature;

    private RateLimiterAspect aspect;

    @BeforeEach
    void setUp()
    {
        aspect = new RateLimiterAspect();
        aspect.setRedisTemplate1(redisTemplate);
        aspect.setLimitScript(limitScript);
        when(joinPoint.getSignature()).thenReturn(signature);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("203.0.113.9");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterEach
    void clearRequest()
    {
        RequestContextHolder.resetRequestAttributes();
    }

    private RateLimiter annotationOf(String method) throws Exception
    {
        Method target = Fixture.class.getDeclaredMethod(method);
        when(signature.getMethod()).thenReturn(target);
        return target.getAnnotation(RateLimiter.class);
    }

    private void stubExecute(Long hits)
    {
        when(redisTemplate.execute(any(RedisScript.class), anyList(), any(), any())).thenReturn(hits);
    }

    @Test
    void requestWithinTheQuotaIsLetThrough() throws Exception
    {
        RateLimiter limiter = annotationOf("globalLimited");
        stubExecute(10L);

        assertThatCode(() -> aspect.doBefore(joinPoint, limiter)).doesNotThrowAnyException();
    }

    @Test
    void requestOverTheQuotaIsRejectedAsBusinessError() throws Exception
    {
        RateLimiter limiter = annotationOf("globalLimited");
        stubExecute(11L);

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> aspect.doBefore(joinPoint, limiter))
                .withMessage("访问过于频繁，请稍候再试");
    }

    @Test
    void missingRedisCounterIsTreatedAsRateLimitedNotAsFreePass() throws Exception
    {
        RateLimiter limiter = annotationOf("globalLimited");
        stubExecute(null);

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> aspect.doBefore(joinPoint, limiter));
    }

    @Test
    void redisFailureBecomesServerSideRejection() throws Exception
    {
        RateLimiter limiter = annotationOf("globalLimited");
        when(redisTemplate.execute(any(RedisScript.class), anyList(), any(), any()))
                .thenThrow(new IllegalStateException("connection refused"));

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> aspect.doBefore(joinPoint, limiter))
                .withMessage("服务器限流异常，请稍候再试")
                .withNoCause();
    }

    @Test
    void quotaIsPassedToTheScriptAsCountAndWindow() throws Throwable
    {
        RateLimiter limiter = annotationOf("globalLimited");
        stubExecute(1L);

        aspect.doBefore(joinPoint, limiter);

        Mockito.verify(redisTemplate).execute(limitScript,
                Collections.singletonList(aspect.getCombineKey(limiter, joinPoint)), 10, 60);
    }

    @Test
    void globalKeyIsNamespacedByTheAnnotatedMethod() throws Exception
    {
        RateLimiter limiter = annotationOf("globalLimited");

        assertThat(aspect.getCombineKey(limiter, joinPoint))
                .isEqualTo("rl:comment:" + Fixture.class.getName() + "-globalLimited");
    }

    @Test
    void ipScopedKeyCarriesTheCallerAddress() throws Exception
    {
        RateLimiter limiter = annotationOf("ipLimited");
        List<String> key = Collections.singletonList(aspect.getCombineKey(limiter, joinPoint));

        assertThat(key.get(0)).startsWith("rl:view:203.0.113.9-")
                .endsWith(Fixture.class.getName() + "-ipLimited");
    }
}
