package com.ruoyi.framework.security.context;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 权限上下文测试类
 * 
 * @author ruoyi
 */
public class PermissionContextHolderTest
{
    private static final String TEST_PERMISSION = "system:user:list";

    @AfterEach
    public void tearDown()
    {
        // 每个测试后清理请求上下文
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testSetContext()
    {
        // 测试设置权限上下文
        setupRequestContext();
        PermissionContextHolder.setContext(TEST_PERMISSION);
        // 验证设置成功
        assertNotNull(PermissionContextHolder.getContext());
    }

    @Test
    public void testGetContext()
    {
        // 测试获取权限上下文
        setupRequestContext();
        PermissionContextHolder.setContext(TEST_PERMISSION);
        String permission = PermissionContextHolder.getContext();
        assertEquals(TEST_PERMISSION, permission);
    }

    @Test
    public void testGetContextWhenNotSet()
    {
        // 测试未设置权限时获取
        setupRequestContext();
        String permission = PermissionContextHolder.getContext();
        assertNull(permission);
    }

    @Test
    public void testSetMultiplePermissions()
    {
        // 测试设置多个权限
        setupRequestContext();
        
        String permission1 = "system:user:list";
        PermissionContextHolder.setContext(permission1);
        assertEquals(permission1, PermissionContextHolder.getContext());

        String permission2 = "system:user:add";
        PermissionContextHolder.setContext(permission2);
        assertEquals(permission2, PermissionContextHolder.getContext());
    }

    @Test
    public void testSetEmptyPermission()
    {
        // 测试设置空权限
        setupRequestContext();
        PermissionContextHolder.setContext("");
        String permission = PermissionContextHolder.getContext();
        assertEquals("", permission);
    }

    /**
     * 设置请求上下文
     */
    private void setupRequestContext()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }
}