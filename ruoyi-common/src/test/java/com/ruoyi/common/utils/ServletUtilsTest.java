package com.ruoyi.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ServletUtils 工具类测试
 *
 * @author ruoyi
 */
@ExtendWith(MockitoExtension.class)
public class ServletUtilsTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private HttpSession mockSession;

    @BeforeEach
    public void setUp() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testGetParameterWithoutRequestContext() {
        // 测试在没有请求上下文的情况下调用 getParameter
        String result = ServletUtils.getParameter("testKey");
        assertNull(result);
    }

    @Test
    public void testGetParameterWithRequestContext() {
        // 设置请求上下文
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("testKey", "testValue");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        String result = ServletUtils.getParameter("testKey");
        assertEquals("testValue", result);

        // 清理
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testGetParameterWithDefaultValue() {
        // 测试获取参数并使用默认值
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("testKey", "testValue");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        String result = ServletUtils.getParameter("testKey", "defaultValue");
        assertEquals("testValue", result);

        // 测试参数不存在时使用默认值
        String result2 = ServletUtils.getParameter("nonExistentKey", "defaultValue");
        assertEquals("defaultValue", result2);

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testGetParameterToIntWithoutRequestContext() {
        // 测试在没有请求上下文的情况下调用 getParameterToInt
        Integer result = ServletUtils.getParameterToInt("testKey");
        assertNull(result);
    }

    @Test
    public void testGetParameterToIntWithRequestContext() {
        // 测试获取整数参数
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("testKey", "123");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        Integer result = ServletUtils.getParameterToInt("testKey");
        assertEquals(123, result);

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testGetParameterToIntWithDefaultValue() {
        // 测试获取整数参数并使用默认值
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("testKey", "123");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        Integer result = ServletUtils.getParameterToInt("testKey", 0);
        assertEquals(123, result);

        // 测试参数不存在时使用默认值
        Integer result2 = ServletUtils.getParameterToInt("nonExistentKey", 0);
        assertEquals(0, result2);

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testGetParameterToBoolWithoutRequestContext() {
        // 测试在没有请求上下文的情况下调用 getParameterToBool
        Boolean result = ServletUtils.getParameterToBool("testKey");
        assertNull(result);
    }

    @Test
    public void testGetParameterToBoolWithRequestContext() {
        // 测试获取布尔参数
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("testKey", "true");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        Boolean result = ServletUtils.getParameterToBool("testKey");
        assertTrue(result);

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testGetParameterToBoolWithDefaultValue() {
        // 测试获取布尔参数并使用默认值
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("testKey", "true");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        Boolean result = ServletUtils.getParameterToBool("testKey", false);
        assertTrue(result);

        // 测试参数不存在时使用默认值
        Boolean result2 = ServletUtils.getParameterToBool("nonExistentKey", false);
        assertFalse(result2);

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testGetRequestWithoutRequestContext() {
        // 测试在没有请求上下文的情况下调用 getRequest
        HttpServletRequest request = ServletUtils.getRequest();
        assertNull(request);
    }

    @Test
    public void testGetRequestWithRequestContext() {
        // 测试在有请求上下文的情况下调用 getRequest
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        HttpServletRequest result = ServletUtils.getRequest();
        assertNotNull(result);
        assertEquals(request, result);

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testGetResponseWithoutRequestContext() {
        // 测试在没有请求上下文的情况下调用 getResponse
        HttpServletResponse response = ServletUtils.getResponse();
        assertNull(response);
    }

    @Test
    public void testGetResponseWithRequestContext() {
        // 测试在有请求上下文的情况下调用 getResponse
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletRequestAttributes attributes = new ServletRequestAttributes(request, response);
        RequestContextHolder.setRequestAttributes(attributes);

        HttpServletResponse result = ServletUtils.getResponse();
        assertNotNull(result);
        assertEquals(response, result);

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testGetSessionWithoutRequestContext() {
        // 测试在没有请求上下文的情况下调用 getSession
        HttpSession session = ServletUtils.getSession();
        assertNull(session);
    }

    @Test
    public void testGetSessionWithRequestContext() {
        // 测试在有请求上下文的情况下调用 getSession
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        HttpSession result = ServletUtils.getSession();
        assertNotNull(result);

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testRenderString() throws Exception {
        // 测试渲染字符串到响应
        MockHttpServletResponse response = new MockHttpServletResponse();

        ServletUtils.renderString(response, "test response");

        // 验证响应状态和内容类型
        assertEquals(200, response.getStatus());
        assertTrue(response.getContentType().startsWith("application/json"));
        assertEquals("utf-8", response.getCharacterEncoding());

        // 验证响应内容
        String content = response.getContentAsString();
        assertEquals("test response", content.trim());
    }

    @Test
    public void testIsAjaxRequestWithAcceptHeader() {
        // 测试通过 Accept 头判断 Ajax 请求
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("accept", "application/json");

        boolean result = ServletUtils.isAjaxRequest(request);
        assertTrue(result);
    }

    @Test
    public void testIsAjaxRequestWithXRequestedWithHeader() {
        // 测试通过 X-Requested-With 头判断 Ajax 请求
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Requested-With", "XMLHttpRequest");

        boolean result = ServletUtils.isAjaxRequest(request);
        assertTrue(result);
    }

    @Test
    public void testIsAjaxRequestWithAjaxParameter() {
        // 测试通过 __ajax 参数判断 Ajax 请求
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("__ajax", "json");

        boolean result = ServletUtils.isAjaxRequest(request);
        assertTrue(result);
    }

    @Test
    public void testIsAjaxRequestNotAjax() {
        // 测试非 Ajax 请求
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test.html");

        boolean result = ServletUtils.isAjaxRequest(request);
        assertFalse(result);
    }

    @Test
    public void testGetParams() {
        // 测试获取请求参数
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("key1", "value1");
        request.setParameter("key2", "value2");

        Map<String, String[]> params = ServletUtils.getParams(request);
        assertNotNull(params);
        assertEquals(2, params.size());
        assertArrayEquals(new String[]{"value1"}, params.get("key1"));
        assertArrayEquals(new String[]{"value2"}, params.get("key2"));
    }

    @Test
    public void testGetParamMap() {
        // 测试获取请求参数 Map
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("key1", "value1");
        request.setParameter("key2", "value2");

        Map<String, String> paramMap = ServletUtils.getParamMap(request);
        assertNotNull(paramMap);
        assertEquals(2, paramMap.size());
        assertEquals("value1", paramMap.get("key1"));
        assertEquals("value2", paramMap.get("key2"));
    }

    @Test
    public void testUrlEncode() {
        // 测试 URL 编码
        String encoded = ServletUtils.urlEncode("测试中文");
        assertNotNull(encoded);
        assertNotEquals("测试中文", encoded);
    }

    @Test
    public void testUrlDecode() {
        // 测试 URL 解码
        String decoded = ServletUtils.urlDecode("%E6%B5%8B%E8%AF%95%E4%B8%AD%E6%96%87");
        assertNotNull(decoded);
        assertEquals("测试中文", decoded);
    }
}