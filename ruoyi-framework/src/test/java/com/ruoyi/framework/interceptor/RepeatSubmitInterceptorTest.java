package com.ruoyi.framework.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import com.ruoyi.common.annotation.RepeatSubmit;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 防止重复提交拦截器测试类
 * 
 * @author ruoyi
 */
public class RepeatSubmitInterceptorTest
{
    private RepeatSubmitInterceptor interceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    public void setUp()
    {
        // 创建测试用的拦截器实现
        interceptor = new RepeatSubmitInterceptor()
        {
            @Override
            public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation)
            {
                // 简单实现：总是返回 false（不重复提交）
                return false;
            }
        };
        
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testPreHandleWithHandlerMethod() throws Exception
    {
        // 测试处理 HandlerMethod
        HandlerMethod handlerMethod = createHandlerMethod("testMethod");
        boolean result = interceptor.preHandle(request, response, handlerMethod);
        assertTrue(result);
    }

    @Test
    public void testPreHandleWithoutRepeatSubmitAnnotation() throws Exception
    {
        // 测试没有 RepeatSubmit 注解的方法
        HandlerMethod handlerMethod = createHandlerMethod("testMethodWithoutAnnotation");
        boolean result = interceptor.preHandle(request, response, handlerMethod);
        assertTrue(result);
    }

    @Test
    public void testPreHandleWithRepeatSubmit() throws Exception
    {
        // 测试有 RepeatSubmit 注解的方法
        RepeatSubmitInterceptor repeatInterceptor = new RepeatSubmitInterceptor()
        {
            @Override
            public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation)
            {
                // 模拟重复提交
                return true;
            }
        };
        
        HandlerMethod handlerMethod = createHandlerMethod("testMethodWithAnnotation");
        boolean result = repeatInterceptor.preHandle(request, response, handlerMethod);
        assertFalse(result);
    }

    @Test
    public void testPreHandleWithNonHandlerMethod() throws Exception
    {
        // 测试非 HandlerMethod 类型
        Object handler = new Object();
        boolean result = interceptor.preHandle(request, response, handler);
        assertTrue(result);
    }

    @Test
    public void testPreHandleWithNullHandler() throws Exception
    {
        // 测试 null handler
        boolean result = interceptor.preHandle(request, response, null);
        assertTrue(result);
    }

    /**
     * 创建 HandlerMethod
     */
    private HandlerMethod createHandlerMethod(String methodName) throws NoSuchMethodException
    {
        Method method = TestController.class.getMethod(methodName);
        return new HandlerMethod(new TestController(), method);
    }

    /**
     * 测试用的控制器类
     */
    public static class TestController
    {
        public void testMethod()
        {
        }

        @RepeatSubmit(interval = 5000, message = "不允许重复提交")
        public void testMethodWithAnnotation()
        {
        }

        public void testMethodWithoutAnnotation()
        {
        }
    }
}