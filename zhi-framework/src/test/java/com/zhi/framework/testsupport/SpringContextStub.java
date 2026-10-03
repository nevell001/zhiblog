package com.zhi.framework.testsupport;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ConcurrentHashMap;

import org.mockito.Mockito;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.test.util.ReflectionTestUtils;

import com.zhi.common.utils.spring.SpringUtils;

/**
 * 给纯单元测试装上 SpringUtils 依赖的最小 beanFactory。
 *
 * <p>两件事必须成立，否则任何"业务拒绝"路径都会在断言之前先炸出 NPE：
 * <ol>
 *   <li>{@code BaseException#getMessage()} 与 {@code MessageUtils} 要能取到 MessageSource；</li>
 *   <li>{@code AsyncFactory}/{@code SpringUtils.getBean(X.class)} 这类异步或按类型取 bean 的调用
 *       要拿到一个可回收的 mock（异步登录日志就是这样落库的）。</li>
 * </ol>
 * 未注册的 Class 一律返回按类型缓存的 mock，测试可以再用 {@link #stub} 覆盖成自己的假实现。</p>
 */
public final class SpringContextStub
{
    private static final Map<Class<?>, Object> BEANS = new ConcurrentHashMap<>();

    /** AsyncManager 在类初始化时按名字取这个线程池，并且把结果缓存在单例里，所以必须在首次触发前就位 */
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE =
            Executors.newSingleThreadScheduledExecutor();

    private static ConfigurableListableBeanFactory beanFactory;

    private SpringContextStub()
    {
    }

    public static synchronized void install()
    {
        if (beanFactory != null)
        {
            return;
        }
        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);
        beanFactory = Mockito.mock(ConfigurableListableBeanFactory.class);
        Mockito.when(beanFactory.getBean(Mockito.any(Class.class))).thenAnswer(invocation -> {
            Class<?> type = invocation.getArgument(0);
            return type.equals(MessageSource.class) ? messageSource : beanOfType(type);
        });
        Mockito.when(beanFactory.getBean(Mockito.anyString())).thenAnswer(invocation -> {
            String name = invocation.getArgument(0);
            return "scheduledExecutorService".equals(name) ? SCHEDULED_EXECUTOR_SERVICE : null;
        });
        ReflectionTestUtils.setField(SpringUtils.class, "beanFactory", beanFactory);
    }

    private static Object beanOfType(Class<?> type)
    {
        return BEANS.computeIfAbsent(type, Mockito::mock);
    }

    /**
     * 用指定实例替换某类型的默认 mock，用于断言异步落库/远程调用等副作用。
     */
    public static <T> T stub(Class<T> type, T instance)
    {
        BEANS.put(type, instance);
        return instance;
    }

    /**
     * 取出该类型当前生效的实例（未显式 stub 时是自动生成的 mock）。
     */
    @SuppressWarnings("unchecked")
    public static <T> T bean(Class<T> type)
    {
        install();
        return (T) beanOfType(type);
    }
}
