package com.ruoyi.system.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.mapper.SysPostMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.SysUserPostMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysDeptService;

/**
 * 用户服务层测试
 *
 * @author ruoyi
 */
@ExtendWith(MockitoExtension.class)
class SysUserServiceImplTest {

    @Mock
    private SysUserMapper userMapper;

    @Mock
    private SysRoleMapper roleMapper;

    @Mock
    private SysPostMapper postMapper;

    @Mock
    private SysUserRoleMapper userRoleMapper;

    @Mock
    private SysUserPostMapper userPostMapper;

    @Mock
    private ISysConfigService configService;

    @Mock
    private ISysDeptService deptService;

    @InjectMocks
    private SysUserServiceImpl userService;

    private SysUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new SysUser();
        testUser.setUserId(1L);
        testUser.setUserName("testuser");
        testUser.setNickName("测试用户");
        testUser.setEmail("test@example.com");
        testUser.setPhonenumber("13800138000");
        testUser.setSex("0");
        testUser.setStatus("0");
        testUser.setDelFlag("0");
    }

    /**
     * 测试通过用户ID查询用户
     */
    @Test
    void testSelectUserById() {
        when(userMapper.selectUserById(1L)).thenReturn(testUser);

        SysUser result = userService.selectUserById(1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUserName());
        assertEquals("测试用户", result.getNickName());
        verify(userMapper).selectUserById(1L);
    }

    /**
     * 测试通过用户名查询用户
     */
    @Test
    void testSelectUserByUserName() {
        when(userMapper.selectUserByUserName("testuser")).thenReturn(testUser);

        SysUser result = userService.selectUserByUserName("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUserName());
        verify(userMapper).selectUserByUserName("testuser");
    }

    /**
     * 测试查询用户列表
     */
    @Test
    void testSelectUserList() {
        List<SysUser> userList = Arrays.asList(testUser);
        when(userMapper.selectUserList(any(SysUser.class))).thenReturn(userList);

        List<SysUser> result = userService.selectUserList(new SysUser());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUserName());
        verify(userMapper).selectUserList(any(SysUser.class));
    }

    /**
     * 测试查询已分配角色用户列表
     */
    @Test
    void testSelectAllocatedList() {
        List<SysUser> userList = Arrays.asList(testUser);
        when(userMapper.selectAllocatedList(any(SysUser.class))).thenReturn(userList);

        List<SysUser> result = userService.selectAllocatedList(new SysUser());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userMapper).selectAllocatedList(any(SysUser.class));
    }

    /**
     * 测试查询未分配角色用户列表
     */
    @Test
    void testSelectUnallocatedList() {
        List<SysUser> userList = Arrays.asList(testUser);
        when(userMapper.selectUnallocatedList(any(SysUser.class))).thenReturn(userList);

        List<SysUser> result = userService.selectUnallocatedList(new SysUser());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userMapper).selectUnallocatedList(any(SysUser.class));
    }

    /**
     * 测试查询用户角色组
     */
    @Test
    void testSelectUserRoleGroup() {
        SysRole role1 = new SysRole();
        role1.setRoleName("管理员");
        SysRole role2 = new SysRole();
        role2.setRoleName("普通用户");

        List<SysRole> roleList = Arrays.asList(role1, role2);
        when(roleMapper.selectRolesByUserName("testuser")).thenReturn(roleList);

        String result = userService.selectUserRoleGroup("testuser");

        assertNotNull(result);
        assertEquals("管理员,普通用户", result);
        verify(roleMapper).selectRolesByUserName("testuser");
    }

    /**
     * 测试查询用户角色组 - 无角色
     */
    @Test
    void testSelectUserRoleGroup_Empty() {
        when(roleMapper.selectRolesByUserName("testuser")).thenReturn(Collections.emptyList());

        String result = userService.selectUserRoleGroup("testuser");

        assertNotNull(result);
        assertEquals("", result);
        verify(roleMapper).selectRolesByUserName("testuser");
    }

    /**
     * 测试查询用户岗位组
     */
    @Test
    void testSelectUserPostGroup() {
        SysPost post1 = new SysPost();
        post1.setPostName("技术总监");
        SysPost post2 = new SysPost();
        post2.setPostName("开发工程师");

        List<SysPost> postList = Arrays.asList(post1, post2);
        when(postMapper.selectPostsByUserName("testuser")).thenReturn(postList);

        String result = userService.selectUserPostGroup("testuser");

        assertNotNull(result);
        assertEquals("技术总监,开发工程师", result);
        verify(postMapper).selectPostsByUserName("testuser");
    }

    /**
     * 测试查询用户岗位组 - 无岗位
     */
    @Test
    void testSelectUserPostGroup_Empty() {
        when(postMapper.selectPostsByUserName("testuser")).thenReturn(Collections.emptyList());

        String result = userService.selectUserPostGroup("testuser");

        assertNotNull(result);
        assertEquals("", result);
        verify(postMapper).selectPostsByUserName("testuser");
    }

    /**
     * 测试校验用户名唯一性 - 唯一
     */
    @Test
    void testCheckUserNameUnique_Unique() {
        when(userMapper.checkUserNameUnique("testuser")).thenReturn(null);

        boolean result = userService.checkUserNameUnique(testUser);

        assertTrue(result);
        verify(userMapper).checkUserNameUnique("testuser");
    }

    /**
     * 测试校验用户名唯一性 - 不唯一
     */
    @Test
    void testCheckUserNameUnique_NotUnique() {
        SysUser existingUser = new SysUser();
        existingUser.setUserId(2L);
        existingUser.setUserName("testuser");

        when(userMapper.checkUserNameUnique("testuser")).thenReturn(existingUser);

        boolean result = userService.checkUserNameUnique(testUser);

        assertFalse(result);
        verify(userMapper).checkUserNameUnique("testuser");
    }

    /**
     * 测试校验用户名唯一性 - 同一用户
     */
    @Test
    void testCheckUserNameUnique_SameUser() {
        when(userMapper.checkUserNameUnique("testuser")).thenReturn(testUser);

        boolean result = userService.checkUserNameUnique(testUser);

        assertTrue(result);
        verify(userMapper).checkUserNameUnique("testuser");
    }

    /**
     * 测试校验手机号唯一性 - 唯一
     */
    @Test
    void testCheckPhoneUnique_Unique() {
        when(userMapper.checkPhoneUnique("13800138000")).thenReturn(null);

        boolean result = userService.checkPhoneUnique(testUser);

        assertTrue(result);
        verify(userMapper).checkPhoneUnique("13800138000");
    }

    /**
     * 测试校验手机号唯一性 - 不唯一
     */
    @Test
    void testCheckPhoneUnique_NotUnique() {
        SysUser existingUser = new SysUser();
        existingUser.setUserId(2L);
        existingUser.setPhonenumber("13800138000");

        when(userMapper.checkPhoneUnique("13800138000")).thenReturn(existingUser);

        boolean result = userService.checkPhoneUnique(testUser);

        assertFalse(result);
        verify(userMapper).checkPhoneUnique("13800138000");
    }

    /**
     * 测试校验邮箱唯一性 - 唯一
     */
    @Test
    void testCheckEmailUnique_Unique() {
        when(userMapper.checkEmailUnique("test@example.com")).thenReturn(null);

        boolean result = userService.checkEmailUnique(testUser);

        assertTrue(result);
        verify(userMapper).checkEmailUnique("test@example.com");
    }

    /**
     * 测试校验邮箱唯一性 - 不唯一
     */
    @Test
    void testCheckEmailUnique_NotUnique() {
        SysUser existingUser = new SysUser();
        existingUser.setUserId(2L);
        existingUser.setEmail("test@example.com");

        when(userMapper.checkEmailUnique("test@example.com")).thenReturn(existingUser);

        boolean result = userService.checkEmailUnique(testUser);

        assertFalse(result);
        verify(userMapper).checkEmailUnique("test@example.com");
    }

    /**
     * 测试校验用户是否允许操作 - 普通用户
     */
    @Test
    void testCheckUserAllowed_NormalUser() {
        testUser.setUserId(2L);

        assertDoesNotThrow(() -> userService.checkUserAllowed(testUser));
    }

    /**
     * 测试校验用户是否允许操作 - 管理员用户
     */
    @Test
    void testCheckUserAllowed_AdminUser() {
        testUser.setUserId(1L);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.checkUserAllowed(testUser);
        });

        assertEquals("不允许操作超级管理员用户", exception.getMessage());
    }

    /**
     * 测试新增用户
     */
    @Test
    void testInsertUser() {
        testUser.setUserId(null);
        testUser.setPostIds(new Long[]{1L, 2L});
        testUser.setRoleIds(new Long[]{1L});

        when(userMapper.insertUser(testUser)).thenReturn(1);

        int result = userService.insertUser(testUser);

        assertEquals(1, result);
        verify(userMapper).insertUser(testUser);
    }

    /**
     * 测试注册用户
     */
    @Test
    void testRegisterUser() {
        testUser.setUserId(null);

        when(userMapper.insertUser(testUser)).thenReturn(1);

        boolean result = userService.registerUser(testUser);

        assertTrue(result);
        verify(userMapper).insertUser(testUser);
    }

    /**
     * 测试注册用户 - 失败
     */
    @Test
    void testRegisterUser_Failed() {
        testUser.setUserId(null);

        when(userMapper.insertUser(testUser)).thenReturn(0);

        boolean result = userService.registerUser(testUser);

        assertFalse(result);
        verify(userMapper).insertUser(testUser);
    }

    /**
     * 测试修改用户
     */
    @Test
    void testUpdateUser() {
        when(userMapper.updateUser(testUser)).thenReturn(1);

        int result = userService.updateUser(testUser);

        assertEquals(1, result);
        verify(userMapper).updateUser(testUser);
        verify(userRoleMapper).deleteUserRoleByUserId(1L);
        verify(userPostMapper).deleteUserPostByUserId(1L);
    }

    /**
     * 测试用户授权角色
     */
    @Test
    void testInsertUserAuth() {
        Long[] roleIds = new Long[]{1L, 2L};

        userService.insertUserAuth(1L, roleIds);

        verify(userRoleMapper).deleteUserRoleByUserId(1L);
    }

    /**
     * 测试修改用户状态
     */
    @Test
    void testUpdateUserStatus() {
        when(userMapper.updateUserStatus(1L, "0")).thenReturn(1);

        int result = userService.updateUserStatus(testUser);

        assertEquals(1, result);
        verify(userMapper).updateUserStatus(1L, "0");
    }

    /**
     * 测试修改用户基本信息
     */
    @Test
    void testUpdateUserProfile() {
        when(userMapper.updateUser(testUser)).thenReturn(1);

        int result = userService.updateUserProfile(testUser);

        assertEquals(1, result);
        verify(userMapper).updateUser(testUser);
    }

    /**
     * 测试修改用户头像
     */
    @Test
    void testUpdateUserAvatar() {
        when(userMapper.updateUserAvatar(1L, "/avatar/test.jpg")).thenReturn(1);

        boolean result = userService.updateUserAvatar(1L, "/avatar/test.jpg");

        assertTrue(result);
        verify(userMapper).updateUserAvatar(1L, "/avatar/test.jpg");
    }

    /**
     * 测试修改用户头像 - 失败
     */
    @Test
    void testUpdateUserAvatar_Failed() {
        when(userMapper.updateUserAvatar(1L, "/avatar/test.jpg")).thenReturn(0);

        boolean result = userService.updateUserAvatar(1L, "/avatar/test.jpg");

        assertFalse(result);
        verify(userMapper).updateUserAvatar(1L, "/avatar/test.jpg");
    }

    /**
     * 测试重置用户密码
     */
    @Test
    void testResetPwd() {
        testUser.setPassword("newPassword");

        when(userMapper.resetUserPwd(1L, "newPassword")).thenReturn(1);

        int result = userService.resetPwd(testUser);

        assertEquals(1, result);
        verify(userMapper).resetUserPwd(1L, "newPassword");
    }

    /**
     * 测试重置用户密码 - 使用用户ID和密码
     */
    @Test
    void testResetUserPwd() {
        when(userMapper.resetUserPwd(1L, "newPassword")).thenReturn(1);

        int result = userService.resetUserPwd(1L, "newPassword");

        assertEquals(1, result);
        verify(userMapper).resetUserPwd(1L, "newPassword");
    }

    /**
     * 测试删除用户
     */
    @Test
    void testDeleteUserById() {
        when(userMapper.deleteUserById(1L)).thenReturn(1);

        int result = userService.deleteUserById(1L);

        assertEquals(1, result);
        verify(userRoleMapper).deleteUserRoleByUserId(1L);
        verify(userPostMapper).deleteUserPostByUserId(1L);
        verify(userMapper).deleteUserById(1L);
    }

    /**
     * 测试批量删除用户 - 包含管理员
     */
    @Test
    void testDeleteUserByIds_ContainsAdmin() {
        SysUser adminUser = new SysUser();
        adminUser.setUserId(1L);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.deleteUserByIds(new Long[]{1L});
        });

        assertEquals("不允许操作超级管理员用户", exception.getMessage());
        verify(userMapper, never()).deleteUserByIds(any(Long[].class));
    }

    /**
     * 测试查询用户数量
     */
    @Test
    void testSelectUserCount() {
        when(userMapper.selectUserCount(any(SysUser.class))).thenReturn(10L);

        Long result = userService.selectUserCount(new SysUser());

        assertEquals(10L, result);
        verify(userMapper).selectUserCount(any(SysUser.class));
    }

    /**
     * 测试查询活跃用户数量
     */
    @Test
    void testSelectActiveUserCount() {
        when(userMapper.selectActiveUserCount(7)).thenReturn(50L);

        Long result = userService.selectActiveUserCount(7);

        assertEquals(50L, result);
        verify(userMapper).selectActiveUserCount(7);
    }

    /**
     * 测试查询新增用户数量
     */
    @Test
    void testSelectNewUserCount() {
        when(userMapper.selectNewUserCount(30)).thenReturn(100L);

        Long result = userService.selectNewUserCount(30);

        assertEquals(100L, result);
        verify(userMapper).selectNewUserCount(30);
    }

    /**
     * 测试查询管理员用户数量
     */
    @Test
    void testSelectAdminUserCount() {
        when(userMapper.selectAdminUserCount()).thenReturn(5L);

        Long result = userService.selectAdminUserCount();

        assertEquals(5L, result);
        verify(userMapper).selectAdminUserCount();
    }

    /**
     * 测试查询在线用户数量
     */
    @Test
    void testSelectOnlineUserCount() {
        when(userMapper.selectOnlineUserCount(30)).thenReturn(20L);

        Long result = userService.selectOnlineUserCount(30);

        assertEquals(20L, result);
        verify(userMapper).selectOnlineUserCount(30);
    }

    /**
     * 测试导入用户数据 - 空列表
     */
    @Test
    void testImportUser_EmptyList() {
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.importUser(null, false, "admin");
        });

        assertEquals("导入用户数据不能为空！", exception.getMessage());
    }

    /**
     * 测试导入用户数据 - 用户已存在
     */
    @Test
    void testImportUser_UserExists() {
        testUser.setUserId(null);
        List<SysUser> userList = Arrays.asList(testUser);

        when(userMapper.selectUserByUserName("testuser")).thenReturn(testUser);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.importUser(userList, false, "admin");
        });

        assertTrue(exception.getMessage().contains("已存在"));
    }
}