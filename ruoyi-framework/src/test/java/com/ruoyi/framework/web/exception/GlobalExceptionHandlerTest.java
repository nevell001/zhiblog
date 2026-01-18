package com.ruoyi.framework.web.exception;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.DemoModeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 全局异常处理器单元测试
 *
 * @author nevell
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
        request.setRequestURI("/test/api");
    }

    @Test
    void testHandleAccessDeniedException() {
        AccessDeniedException e = new AccessDeniedException("Access denied");
        AjaxResult result = globalExceptionHandler.handleAccessDeniedException(e, request);

        assertEquals(HttpStatus.FORBIDDEN, result.get("code"));
        assertTrue(result.get("msg").toString().contains("没有权限"));
    }

    @Test
    void testHandleHttpRequestMethodNotSupported() {
        HttpRequestMethodNotSupportedException e = new HttpRequestMethodNotSupportedException("POST");
        AjaxResult result = globalExceptionHandler.handleHttpRequestMethodNotSupported(e, request);

        assertEquals(HttpStatus.ERROR, result.get("code"));
        assertNotNull(result.get("msg"));
    }

    @Test
    void testHandleServiceException() {
        String errorMessage = "业务异常";
        ServiceException e = new ServiceException(errorMessage);
        AjaxResult result = globalExceptionHandler.handleServiceException(e, request);

        assertEquals(HttpStatus.ERROR, result.get("code"));
        assertEquals(errorMessage, result.get("msg"));
    }

    @Test
    void testHandleServiceExceptionWithCode() {
        Integer errorCode = 400;
        String errorMessage = "参数错误";
        ServiceException e = new ServiceException(errorMessage, errorCode);
        AjaxResult result = globalExceptionHandler.handleServiceException(e, request);

        assertEquals(errorCode, result.get("code"));
        assertEquals(errorMessage, result.get("msg"));
    }

    @Test
    void testHandleRuntimeException() {
        String errorMessage = "运行时异常";
        RuntimeException e = new RuntimeException(errorMessage);
        AjaxResult result = globalExceptionHandler.handleRuntimeException(e, request);

        assertEquals(HttpStatus.ERROR, result.get("code"));
        assertEquals(errorMessage, result.get("msg"));
    }

    @Test
    void testHandleException() {
        String errorMessage = "系统异常";
        Exception e = new Exception(errorMessage);
        AjaxResult result = globalExceptionHandler.handleException(e, request);

        assertEquals(HttpStatus.ERROR, result.get("code"));
        assertNotNull(result.get("msg"));
    }

    @Test
    void testHandleDemoModeException() {
        DemoModeException e = new DemoModeException();
        AjaxResult result = globalExceptionHandler.handleDemoModeException(e);

        assertEquals(HttpStatus.ERROR, result.get("code"));
        assertEquals("演示模式，不允许操作", result.get("msg"));
    }

    @Test
    void testHandleNoHandlerFoundException() {
        String requestURI = "/nonexistent/page";
        NoHandlerFoundException e = new NoHandlerFoundException("GET", requestURI, null);
        request.setRequestURI(requestURI);

        AjaxResult result = globalExceptionHandler.handleNoHandlerFoundException(e, request);

        assertEquals(HttpStatus.NOT_FOUND, result.get("code"));
        assertTrue(result.get("msg").toString().contains("404"));
    }

    @Test
    void testHandleServiceExceptionWithNullMessage() {
        ServiceException e = new ServiceException();
        AjaxResult result = globalExceptionHandler.handleServiceException(e, request);

        assertEquals(HttpStatus.ERROR, result.get("code"));
        // ServiceException 没有设置消息时，msg 可能为 null，这是预期行为
        // GlobalExceptionHandler 会直接传递给 AjaxResult
    }

    @Test
    void testHandleRuntimeExceptionWithComplexMessage() {
        String complexMessage = "数据库连接失败: Connection timeout";
        RuntimeException e = new RuntimeException(complexMessage);
        AjaxResult result = globalExceptionHandler.handleRuntimeException(e, request);

        assertEquals(HttpStatus.ERROR, result.get("code"));
        assertTrue(result.get("msg").toString().contains("数据库连接失败"));
    }
}
