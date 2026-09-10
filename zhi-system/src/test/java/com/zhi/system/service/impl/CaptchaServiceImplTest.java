package com.zhi.system.service.impl;

import com.zhi.common.cache.UnifiedCacheManager;
import com.zhi.common.constant.CacheConstants;
import com.zhi.common.exception.user.CaptchaException;
import com.zhi.common.exception.user.CaptchaExpireException;
import com.zhi.system.service.ISysConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 图形验证码校验服务单元测试
 *
 * @author test
 * @date 2026-09-10
 */
@ExtendWith(MockitoExtension.class)
class CaptchaServiceImplTest {

    @Mock
    private UnifiedCacheManager unifiedCacheManager;

    @Mock
    private ISysConfigService configService;

    @InjectMocks
    private CaptchaServiceImpl captchaService;

    @BeforeEach
    void setUp() {
        // 环境变量 CAPTCHA_ENABLED 在测试进程中通常不存在，走数据库开关分支
        lenient().when(configService.selectCaptchaEnabled()).thenReturn(true);
    }

    @Test
    void testIsCaptchaEnabledFollowsDbSwitch() {
        when(configService.selectCaptchaEnabled()).thenReturn(false);
        assertFalse(captchaService.isCaptchaEnabled());

        when(configService.selectCaptchaEnabled()).thenReturn(true);
        assertTrue(captchaService.isCaptchaEnabled());
    }

    @Test
    void testValidateSkippedWhenCaptchaDisabled() {
        when(configService.selectCaptchaEnabled()).thenReturn(false);

        captchaService.validate(null, null);

        verify(unifiedCacheManager, never()).get(anyString(), any());
    }

    @Test
    void testValidatePassesWithMatchingCode() {
        when(unifiedCacheManager.get(CacheConstants.CAPTCHA_CODE_KEY + "uuid-1", String.class))
            .thenReturn("a1b2");

        assertDoesNotThrow(() -> captchaService.validate("A1B2", "uuid-1"));

        // 一次性使用：校验后删除
        verify(unifiedCacheManager).delete(CacheConstants.CAPTCHA_CODE_KEY + "uuid-1");
    }

    @Test
    void testValidateThrowsWhenCodeMismatch() {
        when(unifiedCacheManager.get(CacheConstants.CAPTCHA_CODE_KEY + "uuid-2", String.class))
            .thenReturn("a1b2");

        assertThrows(CaptchaException.class, () -> captchaService.validate("wrong", "uuid-2"));
        verify(unifiedCacheManager).delete(CacheConstants.CAPTCHA_CODE_KEY + "uuid-2");
    }

    @Test
    void testValidateThrowsWhenCodeMissingInInput() {
        when(unifiedCacheManager.get(CacheConstants.CAPTCHA_CODE_KEY + "uuid-3", String.class))
            .thenReturn("a1b2");

        assertThrows(CaptchaException.class, () -> captchaService.validate(null, "uuid-3"));
    }

    @Test
    void testValidateThrowsWhenExpired() {
        when(unifiedCacheManager.get(CacheConstants.CAPTCHA_CODE_KEY + "uuid-4", String.class))
            .thenReturn(null);

        assertThrows(CaptchaExpireException.class, () -> captchaService.validate("a1b2", "uuid-4"));
    }

    @Test
    void testValidateHandlesNullUuid() {
        when(unifiedCacheManager.get(CacheConstants.CAPTCHA_CODE_KEY, String.class)).thenReturn(null);

        assertThrows(CaptchaExpireException.class, () -> captchaService.validate("a1b2", null));
    }

    @Test
    void testValidateAndGetAgeReturnsMinusOneWhenCaptchaDisabled() {
        when(configService.selectCaptchaEnabled()).thenReturn(false);

        assertEquals(-1L, captchaService.validateAndGetAgeSeconds("a1b2", "uuid-5"));
    }

    @Test
    void testValidateAndGetAgeComputesElapsedSeconds() {
        String key = CacheConstants.CAPTCHA_CODE_KEY + "uuid-6";
        when(unifiedCacheManager.get(key, String.class)).thenReturn("a1b2");
        // 总有效期 2 分钟，剩余 115 秒 => 已过 5 秒
        when(unifiedCacheManager.getExpire(key)).thenReturn(115L);

        assertEquals(5L, captchaService.validateAndGetAgeSeconds("A1B2", "uuid-6"));
        verify(unifiedCacheManager).delete(key);
    }

    @Test
    void testValidateAndGetAgeReturnsMinusOneWhenTtlUnavailable() {
        String key = CacheConstants.CAPTCHA_CODE_KEY + "uuid-7";
        when(unifiedCacheManager.get(key, String.class)).thenReturn("a1b2");
        when(unifiedCacheManager.getExpire(key)).thenReturn(0L);

        assertEquals(-1L, captchaService.validateAndGetAgeSeconds("a1b2", "uuid-7"));
    }

    @Test
    void testValidateAndGetAgeReturnsMinusOneWhenTtlBeyondTotal() {
        String key = CacheConstants.CAPTCHA_CODE_KEY + "uuid-8";
        when(unifiedCacheManager.get(key, String.class)).thenReturn("a1b2");
        when(unifiedCacheManager.getExpire(key)).thenReturn(999L);

        assertEquals(-1L, captchaService.validateAndGetAgeSeconds("a1b2", "uuid-8"));
    }

    @Test
    void testValidateAndGetAgeStillThrowsOnMismatch() {
        String key = CacheConstants.CAPTCHA_CODE_KEY + "uuid-9";
        when(unifiedCacheManager.get(key, String.class)).thenReturn("a1b2");

        assertThrows(CaptchaException.class,
            () -> captchaService.validateAndGetAgeSeconds("wrong", "uuid-9"));
    }
}
