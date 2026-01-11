package com.ruoyi.system.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.system.domain.SysUserOnline;

/**
 * 在线用户服务层测试
 *
 * @author ruoyi
 */
@ExtendWith(MockitoExtension.class)
class SysUserOnlineServiceImplTest {

    @InjectMocks
    private SysUserOnlineServiceImpl userOnlineService;

    private LoginUser testLoginUser;

    @BeforeEach
    void setUp() {
        testLoginUser = new LoginUser();
        testLoginUser.setToken("test-token-123");
        testLoginUser.setIpaddr("192.168.1.100");
        testLoginUser.setLoginLocation("北京市");
        testLoginUser.setBrowser("Chrome");
        testLoginUser.setOs("Windows 10");
        testLoginUser.setLoginTime(System.currentTimeMillis());

        SysUser user = new SysUser();
        user.setUserId(1L);
        user.setUserName("testuser");
        user.setNickName("测试用户");

        SysDept dept = new SysDept();
        dept.setDeptId(100L);
        dept.setDeptName("研发部");
        user.setDept(dept);

        testLoginUser.setUser(user);
    }

    /**
     * 测试通过IP地址查询在线用户 - 匹配
     */
    @Test
    void testSelectOnlineByIpaddr_Match() {
        SysUserOnline result = userOnlineService.selectOnlineByIpaddr("192.168.1.100", testLoginUser);

        assertNotNull(result);
        assertEquals("test-token-123", result.getTokenId());
        assertEquals("testuser", result.getUserName());
        assertEquals("192.168.1.100", result.getIpaddr());
        assertEquals("北京市", result.getLoginLocation());
        assertEquals("Chrome", result.getBrowser());
        assertEquals("Windows 10", result.getOs());
        assertEquals("研发部", result.getDeptName());
    }

    /**
     * 测试通过IP地址查询在线用户 - 不匹配
     */
    @Test
    void testSelectOnlineByIpaddr_NotMatch() {
        SysUserOnline result = userOnlineService.selectOnlineByIpaddr("192.168.1.200", testLoginUser);

        assertNull(result);
    }

    /**
     * 测试通过用户名称查询在线用户 - 匹配
     */
    @Test
    void testSelectOnlineByUserName_Match() {
        SysUserOnline result = userOnlineService.selectOnlineByUserName("testuser", testLoginUser);

        assertNotNull(result);
        assertEquals("test-token-123", result.getTokenId());
        assertEquals("testuser", result.getUserName());
        assertEquals("192.168.1.100", result.getIpaddr());
    }

    /**
     * 测试通过用户名称查询在线用户 - 不匹配
     */
    @Test
    void testSelectOnlineByUserName_NotMatch() {
        SysUserOnline result = userOnlineService.selectOnlineByUserName("admin", testLoginUser);

        assertNull(result);
    }

    /**
     * 测试通过IP地址和用户名称查询在线用户 - 全部匹配
     */
    @Test
    void testSelectOnlineByInfo_AllMatch() {
        SysUserOnline result = userOnlineService.selectOnlineByInfo("192.168.1.100", "testuser", testLoginUser);

        assertNotNull(result);
        assertEquals("test-token-123", result.getTokenId());
        assertEquals("testuser", result.getUserName());
        assertEquals("192.168.1.100", result.getIpaddr());
    }

    /**
     * 测试通过IP地址和用户名称查询在线用户 - IP不匹配
     */
    @Test
    void testSelectOnlineByInfo_IpNotMatch() {
        SysUserOnline result = userOnlineService.selectOnlineByInfo("192.168.1.200", "testuser", testLoginUser);

        assertNull(result);
    }

    /**
     * 测试通过IP地址和用户名称查询在线用户 - 用户名不匹配
     */
    @Test
    void testSelectOnlineByInfo_UserNameNotMatch() {
        SysUserOnline result = userOnlineService.selectOnlineByInfo("192.168.1.100", "admin", testLoginUser);

        assertNull(result);
    }

    /**
     * 测试转换LoginUser为SysUserOnline - 正常情况
     */
    @Test
    void testLoginUserToUserOnline_Normal() {
        SysUserOnline result = userOnlineService.loginUserToUserOnline(testLoginUser);

        assertNotNull(result);
        assertEquals("test-token-123", result.getTokenId());
        assertEquals("testuser", result.getUserName());
        assertEquals("192.168.1.100", result.getIpaddr());
        assertEquals("北京市", result.getLoginLocation());
        assertEquals("Chrome", result.getBrowser());
        assertEquals("Windows 10", result.getOs());
        assertEquals("研发部", result.getDeptName());
    }

    /**
     * 测试转换LoginUser为SysUserOnline - LoginUser为null
     */
    @Test
    void testLoginUserToUserOnline_NullLoginUser() {
        SysUserOnline result = userOnlineService.loginUserToUserOnline(null);

        assertNull(result);
    }

    /**
     * 测试转换LoginUser为SysUserOnline - User为null
     */
    @Test
    void testLoginUserToUserOnline_NullUser() {
        LoginUser loginUser = new LoginUser();
        loginUser.setToken("test-token-123");
        loginUser.setIpaddr("192.168.1.100");
        loginUser.setLoginLocation("北京市");
        loginUser.setBrowser("Chrome");
        loginUser.setOs("Windows 10");
        loginUser.setLoginTime(System.currentTimeMillis());

        SysUserOnline result = userOnlineService.loginUserToUserOnline(loginUser);

        assertNull(result);
    }

    /**
     * 测试转换LoginUser为SysUserOnline - Dept为null
     */
    @Test
    void testLoginUserToUserOnline_NullDept() {
        LoginUser loginUser = new LoginUser();
        loginUser.setToken("test-token-123");
        loginUser.setIpaddr("192.168.1.100");
        loginUser.setLoginLocation("北京市");
        loginUser.setBrowser("Chrome");
        loginUser.setOs("Windows 10");
        loginUser.setLoginTime(System.currentTimeMillis());

        SysUser user = new SysUser();
        user.setUserId(1L);
        user.setUserName("testuser");
        user.setNickName("测试用户");
        user.setDept(null);

        loginUser.setUser(user);

        SysUserOnline result = userOnlineService.loginUserToUserOnline(loginUser);

        assertNotNull(result);
        assertEquals("test-token-123", result.getTokenId());
        assertEquals("testuser", result.getUserName());
        assertNull(result.getDeptName());
    }

    /**
     * 测试通过IP地址查询在线用户 - 空IP地址
     */
    @Test
    void testSelectOnlineByIpaddr_EmptyIpaddr() {
        SysUserOnline result = userOnlineService.selectOnlineByIpaddr("", testLoginUser);

        assertNull(result);
    }

    /**
     * 测试通过用户名称查询在线用户 - 空用户名
     */
    @Test
    void testSelectOnlineByUserName_EmptyUserName() {
        SysUserOnline result = userOnlineService.selectOnlineByUserName("", testLoginUser);

        assertNull(result);
    }
}