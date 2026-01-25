package com.ruoyi.common.cache.aspect;

import com.ruoyi.common.cache.BlogCacheManager;
import com.ruoyi.common.cache.annotation.BlogCacheable;
import com.ruoyi.common.cache.annotation.BlogCacheEvict;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 缓存切面处理器
 * 处理 @BlogCacheable 和 @BlogCacheEvict 注解
 * 
 * @author nevell
 * @since 2025-12-20
 */
@Slf4j
@Aspect
@Component
@Order(1)
public class CacheAspect {

    @Autowired
    private BlogCacheManager blogCacheManager;

    /**
     * 处理 @BlogCacheable 注解
     */
    @Around("@annotation(blogCacheable)")
    public Object handleCacheable(ProceedingJoinPoint joinPoint, BlogCacheable blogCacheable) throws Throwable {
        String cacheKey = buildCacheKey(joinPoint, blogCacheable.key());
        log.debug("🔍 CacheAspect: 缓存键 = {}", cacheKey);
        
        try {
            // 尝试从缓存获取
            Object cachedValue = blogCacheManager.get(cacheKey, Object.class);
            if (cachedValue != null) {
                log.debug("✅ CacheAspect: 缓存命中，key = {}", cacheKey);
                return cachedValue;
            }
            
            log.debug("❌ CacheAspect: 缓存未命中，key = {}", cacheKey);
            
            // 缓存未命中，执行原方法
            Object result = joinPoint.proceed();
            
            // 将结果存入缓存
            if (result != null) {
                long ttl = blogCacheable.ttl();
                TimeUnit timeUnit = blogCacheable.timeUnit();
                blogCacheManager.set(cacheKey, result, ttl, timeUnit);
                log.info("💾 CacheAspect: 已缓存结果，key = {}, TTL = {} {}", cacheKey, ttl, timeUnit);
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("❌ CacheAspect: 缓存操作失败，key = {}, 执行原方法", cacheKey, e);
            // 缓存操作失败时，直接执行原方法
            return joinPoint.proceed();
        }
    }

    /**
     * 处理 @BlogCacheEvict 注解
     */
    @Around("@annotation(blogCacheEvict)")
    public Object handleCacheEvict(ProceedingJoinPoint joinPoint, BlogCacheEvict blogCacheEvict) throws Throwable {
        try {
            // 先执行原方法
            Object result = joinPoint.proceed();
            
            // 然后执行缓存清除
            if (blogCacheEvict.allEntries()) {
                // 清除所有博客相关缓存
                blogCacheManager.clearAllBlogCache();
                log.debug("Evicted all blog cache entries");
            } else {
                // 清除指定缓存
                String[] keys = blogCacheEvict.value();
                for (String key : keys) {
                    String resolvedKey = resolveSpEL(key, joinPoint);
                    blogCacheManager.delete(resolvedKey);
                    log.debug("Evicted cache key: {}", resolvedKey);
                }
                
                // 如果指定了keyPattern，按模式清除
                if (!blogCacheEvict.keyPattern().isEmpty()) {
                    blogCacheManager.deleteByPattern(blogCacheEvict.keyPattern());
                    log.debug("Evicted cache keys with pattern: {}", blogCacheEvict.keyPattern());
                }
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("Cache eviction failed", e);
            // 缓存清除失败时，仍然返回方法执行结果
            return joinPoint.proceed();
        }
    }

    /**
     * 构建缓存键
     */
    private String buildCacheKey(ProceedingJoinPoint joinPoint, String keyExpression) {
        if (keyExpression.isEmpty()) {
            // 如果没有指定key，使用方法签名生成默认key
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            return generateDefaultKey(signature, joinPoint.getArgs());
        }

        // 解析SpEL表达式或使用字符串模板
        String resolvedKey = resolveSpEL(keyExpression, joinPoint);
        log.info("🔑 CacheAspect: 缓存键解析 - 原始表达式: {}, 解析结果: {}", keyExpression, resolvedKey);
        return resolvedKey;
    }

    /**
     * 生成默认缓存键
     */
    private String generateDefaultKey(MethodSignature signature, Object[] args) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(signature.getDeclaringType().getSimpleName())
                  .append(".")
                  .append(signature.getMethod().getName());
        
        if (args.length > 0) {
            keyBuilder.append(":");
            for (int i = 0; i < args.length; i++) {
                if (i > 0) {
                    keyBuilder.append(",");
                }
                keyBuilder.append(args[i] != null ? args[i].toString() : "null");
            }
        }
        
        return keyBuilder.toString();
    }

    /**
     * 解析SpEL表达式或字符串模板
     * 简化实现，支持常见的参数引用
     */
    private String resolveSpEL(String expression, ProceedingJoinPoint joinPoint) {
        log.info("🔍 resolveSpEL: 开始解析表达式 = {}", expression);
        
        // 检查是否包含需要解析的参数引用（#开头的内容）
        if (expression.contains("#")) {
            // 简化的SpEL解析
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] paramNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();

            log.info("🔍 resolveSpEL: 参数名数组 = {}", java.util.Arrays.toString(paramNames));
            log.info("🔍 resolveSpEL: 参数值数组 = {}", java.util.Arrays.toString(args));

            String result = expression;

            // 首先处理按索引的引用 #p0, #p1, ..., #a0, #a1, ...
            for (int i = 0; i < args.length; i++) {
                Object argValue = args[i];

                // 替换 #p[i]
                result = result.replace("#p" + i,
                                      argValue != null ? argValue.toString() : "null");
                // 替换 #a[i] (Spring 也支持这种格式)
                result = result.replace("#a" + i,
                                      argValue != null ? argValue.toString() : "null");
            }

            // 然后处理按参数名的引用 #paramName
            if (paramNames != null && paramNames.length > 0) {
                for (int i = 0; i < paramNames.length; i++) {
                    String paramName = paramNames[i];
                    Object argValue = args[i];

                    log.info("🔍 resolveSpEL: 处理参数名 = {}, 值 = {}", paramName, argValue);
                    
                    // 使用 replaceAll 替换所有匹配项
                    result = result.replaceAll("#" + paramName,
                                              argValue != null ? argValue.toString() : "null");
                }
            }

            log.info("🔍 resolveSpEL: 解析完成，结果 = {}", result);
            return result;
        } else {
            // 普通字符串，直接返回
            log.info("🔍 resolveSpEL: 普通字符串，直接返回 = {}", expression);
            return expression;
        }
    }
}