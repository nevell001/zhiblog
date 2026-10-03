package com.zhi.framework.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import com.zhi.common.annotation.RepeatSubmit;
import com.zhi.common.exception.ServiceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 防重复提交拦截器骨架测试。
 *
 * <p>只有打了 {@code @RepeatSubmit} 的处理器方法才会走子类的重复判定；命中重复时直接把
 * {@code AjaxResult.error(message)} 渲染成响应体并且不放行（返回 false）。</p>
 */
class RepeatSubmitInterceptorTest
{
    /** 只用于提供注解元数据 */
    static class Handler
    {
        @RepeatSubmit(interval = 10000, message = "请勿重复提交")
        public void submit()
        {
        }

        public void plain()
        {
        }
    }

    /** 用可控的重复判定替掉真实子类，单独验证骨架逻辑 */
    static class StubInterceptor extends RepeatSubmitInterceptor
    {
        private final AtomicInteger calls = new AtomicInteger();

        private boolean repeat;

        void setRepeat(boolean repeat)
        {
            this.repeat = repeat;
        }

        int calls()
        {
            return calls.get();
        }

        @Override
        public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation)
        {
            calls.incrementAndGet();
            return repeat;
        }
    }

    private final StubInterceptor interceptor = new StubInterceptor();

    private final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/system/user");

    private final MockHttpServletResponse response = new MockHttpServletResponse();

    private static HandlerMethod handlerOf(String method) throws NoSuchMethodException
    {
        Method target = Handler.class.getDeclaredMethod(method);
        return new HandlerMethod(new Handler(), target);
    }

    @Test
    void nonHandlerMethodRequestAlwaysPassesThrough() throws Exception
    {
        // 静态资源等非 HandlerMethod 处理器不参与重复提交判定
        assertThat(interceptor.preHandle(request, response, "static-resource")).isTrue();
        assertThat(interceptor.calls()).isZero();
    }

    @Test
    void methodWithoutTheAnnotationNeverAsksTheSubclass() throws Exception
    {
        assertThat(interceptor.preHandle(request, response, handlerOf("plain"))).isTrue();
        assertThat(interceptor.calls()).isZero();
    }

    @Test
    void annotatedMethodPassesWhenTheSubclassSaysItIsNotADuplicate() throws Exception
    {
        assertThat(interceptor.preHandle(request, response, handlerOf("submit"))).isTrue();
        assertThat(interceptor.calls()).isEqualTo(1);
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    void annotatedMethodIsBlockedWithTheAnnotationMessage() throws Exception
    {
        interceptor.setRepeat(true);

        boolean allowed = interceptor.preHandle(request, response, handlerOf("submit"));

        assertThat(allowed).isFalse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).startsWith("application/json");
        assertThat(response.getContentAsString()).contains("请勿重复提交").contains("500");
    }

    @Test
    void subclassFailuresPropagateInsteadOfSilentlyAllowingTheRequest() throws Exception
    {
        RepeatSubmitInterceptor failing = new RepeatSubmitInterceptor()
        {
            @Override
            public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation)
            {
                throw new ServiceException("缓存不可用");
            }
        };

        assertThatThrownBy(() -> failing.preHandle(request, response, handlerOf("submit")))
                .isInstanceOf(ServiceException.class)
                .hasMessage("缓存不可用");
    }
}