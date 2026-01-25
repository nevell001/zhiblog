package com.ruoyi.common.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ExceptionUtil 异常信息处理类测试
 *
 * @author ruoyi
 */
public class ExceptionUtilTest {

    @Test
    public void testGetExceptionMessage() {
        // 测试获取异常详细信息
        Exception e = new Exception("Test exception message");
        String message = ExceptionUtil.getExceptionMessage(e);

        assertNotNull(message);
        assertTrue(message.contains("Test exception message"));
        assertTrue(message.contains("Exception"));
    }

    @Test
    public void testGetExceptionMessageWithStackTrace() {
        // 测试获取异常堆栈信息
        try {
            // 故意抛出异常以生成堆栈信息
            throw new RuntimeException("Test runtime exception");
        } catch (RuntimeException e) {
            String message = ExceptionUtil.getExceptionMessage(e);

            assertNotNull(message);
            assertTrue(message.contains("Test runtime exception"));
            assertTrue(message.contains("RuntimeException"));
            assertTrue(message.contains("at"));
        }
    }

    @Test
    public void testGetExceptionMessageWithCause() {
        // 测试获取带有原因的异常信息
        Exception cause = new IOException("IO error");
        Exception e = new RuntimeException("Runtime error", cause);

        String message = ExceptionUtil.getExceptionMessage(e);

        assertNotNull(message);
        assertTrue(message.contains("Runtime error"));
        assertTrue(message.contains("IO error"));
    }

    @Test
    public void testGetRootErrorMessage() {
        // 测试获取根异常消息
        Exception rootCause = new IOException("Root cause error");
        Exception e = new RuntimeException("Wrapper error", rootCause);

        String message = ExceptionUtil.getRootErrorMessage(e);

        assertNotNull(message);
        assertEquals("Root cause error", message);
    }

    @Test
    public void testGetRootErrorMessageNoCause() {
        // 测试获取没有原因的异常消息
        Exception e = new Exception("Simple exception");

        String message = ExceptionUtil.getRootErrorMessage(e);

        assertNotNull(message);
        assertEquals("Simple exception", message);
    }

    @Test
    public void testGetRootErrorMessageNull() {
        // 测试处理 null 异常
        String message = ExceptionUtil.getRootErrorMessage(null);

        assertNotNull(message);
        assertEquals("", message);
    }

    @Test
    public void testGetRootErrorMessageNullMessage() {
        // 测试处理消息为 null 的异常
        Exception e = new Exception((String) null);

        String message = ExceptionUtil.getRootErrorMessage(e);

        assertNotNull(message);
        assertEquals("null", message);
    }

    @Test
    public void testGetRootErrorMessageEmptyMessage() {
        // 测试处理消息为空的异常
        Exception e = new Exception("");

        String message = ExceptionUtil.getRootErrorMessage(e);

        assertNotNull(message);
        assertEquals("", message);
    }

    @Test
    public void testGetExceptionMessageWithMultipleCauses() {
        // 测试获取多层嵌套异常信息
        Exception rootCause = new IOException("Root error");
        Exception middleCause = new RuntimeException("Middle error", rootCause);
        Exception e = new Exception("Top error", middleCause);

        String message = ExceptionUtil.getExceptionMessage(e);

        assertNotNull(message);
        assertTrue(message.contains("Top error"));
        assertTrue(message.contains("Middle error"));
        assertTrue(message.contains("Root error"));
    }

    @Test
    public void testGetRootErrorMessageWithMultipleCauses() {
        // 测试获取多层嵌套异常的根消息
        Exception rootCause = new IOException("Root error");
        Exception middleCause = new RuntimeException("Middle error", rootCause);
        Exception e = new Exception("Top error", middleCause);

        String message = ExceptionUtil.getRootErrorMessage(e);

        assertNotNull(message);
        assertEquals("Root error", message);
    }

    @Test
    public void testGetExceptionMessageFormat() {
        // 测试异常信息格式
        Exception e = new Exception("Test exception");
        String message = ExceptionUtil.getExceptionMessage(e);

        assertNotNull(message);
        assertTrue(message.contains("Exception"));
        assertTrue(message.contains("Test exception"));
        // 检查是否包含堆栈信息
        assertTrue(message.contains("at ") || message.contains("\n"));
    }
}
