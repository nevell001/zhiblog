package com.zhi.common.cache.aspect;

import com.zhi.common.cache.BlogCacheManager;
import com.zhi.common.cache.annotation.BlogCacheEvict;
import com.zhi.common.constant.CacheConstants;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * 缓存切面单元测试
 * 
 * <p>重点覆盖 @BlogCacheEvict：博客设置写入后必须清除公开设置聚合缓存，
 * 否则前台最多 1 小时拿不到新值（曾导致后台开关“不生效”）。</p>
 *
 * @author test
 * @date 2026-09-10
 */
@ExtendWith(MockitoExtension.class)
class CacheAspectTest {

    @Mock
    private BlogCacheManager blogCacheManager;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private CacheAspect cacheAspect;

    @BeforeEach
    void setUp() throws Throwable {
        lenient().when(joinPoint.proceed()).thenReturn("ok");
    }

    private BlogCacheEvict evictAnnotation(String[] value, String keyPattern, boolean allEntries) {
        BlogCacheEvict annotation = mock(BlogCacheEvict.class);
        lenient().when(annotation.value()).thenReturn(value);
        lenient().when(annotation.keyPattern()).thenReturn(keyPattern);
        lenient().when(annotation.allEntries()).thenReturn(allEntries);
        return annotation;
    }

    @Test
    void testEvictDeletesConfiguredKey() throws Throwable {
        Object result = cacheAspect.handleCacheEvict(joinPoint,
            evictAnnotation(new String[] { CacheConstants.BLOG_SETTINGS_ALL }, "", false));

        assertEquals("ok", result);
        verify(blogCacheManager).delete(CacheConstants.BLOG_SETTINGS_ALL);
    }

    @Test
    void testEvictDeletesPatternWhenProvided() throws Throwable {
        cacheAspect.handleCacheEvict(joinPoint,
            evictAnnotation(new String[] {}, "blog:tag:*", false));

        verify(blogCacheManager).deleteByPattern("blog:tag:*");
    }

    @Test
    void testEvictAllEntries() throws Throwable {
        cacheAspect.handleCacheEvict(joinPoint, evictAnnotation(new String[] {}, "", true));

        verify(blogCacheManager).clearAllBlogCache();
    }

    @Test
    void testEvictStillReturnsResultWhenCacheFails() throws Throwable {
        doThrow(new RuntimeException("redis down")).when(blogCacheManager)
            .delete(CacheConstants.BLOG_SETTINGS_ALL);
        when(joinPoint.proceed()).thenReturn("ok");

        // 缓存清除失败不应影响业务结果（异常分支会再次执行目标方法）
        Object result = cacheAspect.handleCacheEvict(joinPoint,
            evictAnnotation(new String[] { CacheConstants.BLOG_SETTINGS_ALL }, "", false));

        assertEquals("ok", result);
    }
}
