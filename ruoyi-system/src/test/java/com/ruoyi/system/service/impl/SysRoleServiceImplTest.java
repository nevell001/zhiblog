package com.ruoyi.system.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.SysRoleDept;
import com.ruoyi.system.domain.SysRoleMenu;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.SysRoleDeptMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysRoleMenuMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;

/**
 * 角色服务层测试
 *
 * @author ruoyi
 */
@ExtendWith(MockitoExtension.class)
class SysRoleServiceImplTest {

    @Mock
    private SysRoleMapper roleMapper;

    @Mock
    private SysRoleMenuMapper roleMenuMapper;

    @Mock
    private SysUserRoleMapper userRoleMapper;

    @Mock
    private SysRoleDeptMapper roleDeptMapper;

    @InjectMocks
    private SysRoleServiceImpl roleService;

    private SysRole testRole;

    @BeforeEach
    void setUp() {
        testRole = new SysRole();
        testRole.setRoleId(1L);
        testRole.setRoleName("管理员");
        testRole.setRoleKey("admin");
        testRole.setRoleSort(1);
        testRole.setDataScope("1");
        testRole.setStatus("0");
    }

    /**
     * 测试查询角色列表
     */
    @Test
    void testSelectRoleList() {
        List<SysRole> roleList = Arrays.asList(testRole);
        when(roleMapper.selectRoleList(any(SysRole.class))).thenReturn(roleList);

        List<SysRole> result = roleService.selectRoleList(new SysRole());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("管理员", result.get(0).getRoleName());
        verify(roleMapper).selectRoleList(any(SysRole.class));
    }

    /**
     * 测试根据用户ID查询权限
     */
    @Test
    void testSelectRolePermissionByUserId() {
        Long userId = 1L;
        SysRole role1 = new SysRole();
        role1.setRoleKey("admin,user");
        SysRole role2 = new SysRole();
        role2.setRoleKey("system");

        List<SysRole> perms = Arrays.asList(role1, role2);
        when(roleMapper.selectRolePermissionByUserId(userId)).thenReturn(perms);

        Set<String> result = roleService.selectRolePermissionByUserId(userId);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("admin"));
        assertTrue(result.contains("user"));
        assertTrue(result.contains("system"));
        verify(roleMapper).selectRolePermissionByUserId(userId);
    }

    /**
     * 测试根据用户ID查询权限 - 空权限
     */
    @Test
    void testSelectRolePermissionByUserId_Empty() {
        Long userId = 1L;
        when(roleMapper.selectRolePermissionByUserId(userId)).thenReturn(Collections.emptyList());

        Set<String> result = roleService.selectRolePermissionByUserId(userId);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(roleMapper).selectRolePermissionByUserId(userId);
    }

    /**
     * 测试根据用户ID获取角色选择框列表
     */
    @Test
    void testSelectRoleListByUserId() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        when(roleMapper.selectRoleListByUserId(userId)).thenReturn(roleIds);

        List<Long> result = roleService.selectRoleListByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(roleMapper).selectRoleListByUserId(userId);
    }

    /**
     * 测试通过角色ID查询角色
     */
    @Test
    void testSelectRoleById() {
        when(roleMapper.selectRoleById(1L)).thenReturn(testRole);

        SysRole result = roleService.selectRoleById(1L);

        assertNotNull(result);
        assertEquals("管理员", result.getRoleName());
        verify(roleMapper).selectRoleById(1L);
    }

    /**
     * 测试校验角色名称唯一性 - 唯一
     */
    @Test
    void testCheckRoleNameUnique_Unique() {
        when(roleMapper.checkRoleNameUnique("管理员")).thenReturn(null);

        boolean result = roleService.checkRoleNameUnique(testRole);

        assertTrue(result);
        verify(roleMapper).checkRoleNameUnique("管理员");
    }

    /**
     * 测试校验角色名称唯一性 - 不唯一
     */
    @Test
    void testCheckRoleNameUnique_NotUnique() {
        SysRole existingRole = new SysRole();
        existingRole.setRoleId(2L);
        existingRole.setRoleName("管理员");

        when(roleMapper.checkRoleNameUnique("管理员")).thenReturn(existingRole);

        boolean result = roleService.checkRoleNameUnique(testRole);

        assertFalse(result);
        verify(roleMapper).checkRoleNameUnique("管理员");
    }

    /**
     * 测试校验角色名称唯一性 - 同一角色
     */
    @Test
    void testCheckRoleNameUnique_SameRole() {
        when(roleMapper.checkRoleNameUnique("管理员")).thenReturn(testRole);

        boolean result = roleService.checkRoleNameUnique(testRole);

        assertTrue(result);
        verify(roleMapper).checkRoleNameUnique("管理员");
    }

    /**
     * 测试校验角色权限唯一性 - 唯一
     */
    @Test
    void testCheckRoleKeyUnique_Unique() {
        when(roleMapper.checkRoleKeyUnique("admin")).thenReturn(null);

        boolean result = roleService.checkRoleKeyUnique(testRole);

        assertTrue(result);
        verify(roleMapper).checkRoleKeyUnique("admin");
    }

    /**
     * 测试校验角色权限唯一性 - 不唯一
     */
    @Test
    void testCheckRoleKeyUnique_NotUnique() {
        SysRole existingRole = new SysRole();
        existingRole.setRoleId(2L);
        existingRole.setRoleKey("admin");

        when(roleMapper.checkRoleKeyUnique("admin")).thenReturn(existingRole);

        boolean result = roleService.checkRoleKeyUnique(testRole);

        assertFalse(result);
        verify(roleMapper).checkRoleKeyUnique("admin");
    }

    /**
     * 测试校验角色权限唯一性 - 同一角色
     */
    @Test
    void testCheckRoleKeyUnique_SameRole() {
        when(roleMapper.checkRoleKeyUnique("admin")).thenReturn(testRole);

        boolean result = roleService.checkRoleKeyUnique(testRole);

        assertTrue(result);
        verify(roleMapper).checkRoleKeyUnique("admin");
    }

    /**
     * 测试校验角色是否允许操作 - 普通角色
     */
    @Test
    void testCheckRoleAllowed_NormalRole() {
        testRole.setRoleId(2L);

        assertDoesNotThrow(() -> roleService.checkRoleAllowed(testRole));
    }

    /**
     * 测试校验角色是否允许操作 - 管理员角色
     */
    @Test
    void testCheckRoleAllowed_AdminRole() {
        testRole.setRoleId(1L);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            roleService.checkRoleAllowed(testRole);
        });

        assertEquals("不允许操作超级管理员角色", exception.getMessage());
    }

    /**
     * 测试修改角色状态
     */
    @Test
    void testUpdateRoleStatus() {
        when(roleMapper.updateRole(testRole)).thenReturn(1);

        int result = roleService.updateRoleStatus(testRole);

        assertEquals(1, result);
        verify(roleMapper).updateRole(testRole);
    }

    /**
     * 测试删除角色
     */
    @Test
    void testDeleteRoleById() {
        when(roleMapper.deleteRoleById(1L)).thenReturn(1);

        int result = roleService.deleteRoleById(1L);

        assertEquals(1, result);
        verify(roleMapper).deleteRoleById(1L);
    }

    /**
     * 测试批量删除角色 - 包含管理员角色
     */
    @Test
    void testDeleteRoleByIds_ContainsAdmin() {
        SysRole adminRole = new SysRole();
        adminRole.setRoleId(1L);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            roleService.deleteRoleByIds(new Long[]{1L});
        });

        assertEquals("不允许操作超级管理员角色", exception.getMessage());
        verify(roleMapper, never()).deleteRoleByIds(any(Long[].class));
    }

    /**
     * 测试取消授权用户角色
     */
    @Test
    void testDeleteAuthUser() {
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(1L);
        userRole.setRoleId(2L);

        when(userRoleMapper.deleteUserRoleInfo(userRole)).thenReturn(1);

        int result = roleService.deleteAuthUser(userRole);

        assertEquals(1, result);
        verify(userRoleMapper).deleteUserRoleInfo(userRole);
    }

    /**
     * 测试批量取消授权用户角色
     */
    @Test
    void testDeleteAuthUsers() {
        Long[] userIds = new Long[]{1L, 2L};

        when(userRoleMapper.deleteUserRoleInfos(1L, userIds)).thenReturn(2);

        int result = roleService.deleteAuthUsers(1L, userIds);

        assertEquals(2, result);
        verify(userRoleMapper).deleteUserRoleInfos(1L, userIds);
    }

    /**
     * 测试批量选择授权用户角色
     */
    @Test
    void testInsertAuthUsers() {
        Long[] userIds = new Long[]{1L, 2L};

        // 创建一个非管理员角色用于测试
        SysRole normalRole = new SysRole();
        normalRole.setRoleId(2L);
        normalRole.setRoleName("普通角色");
        normalRole.setRoleKey("common");
        normalRole.setStatus("0");

        // Mock 角色查询返回有效的非管理员角色对象
        when(roleMapper.selectRoleById(2L)).thenReturn(normalRole);
        when(userRoleMapper.batchUserRole(anyList())).thenReturn(2);

        int result = roleService.insertAuthUsers(2L, userIds);

        assertEquals(2, result);
        verify(userRoleMapper).batchUserRole(anyList());
    }

    /**
     * 测试查询角色使用数量
     */
    @Test
    void testCountUserRoleByRoleId() {
        when(userRoleMapper.countUserRoleByRoleId(1L)).thenReturn(5);

        long result = roleService.countUserRoleByRoleId(1L);

        assertEquals(5L, result);
        verify(userRoleMapper).countUserRoleByRoleId(1L);
    }

    /**
     * 测试新增角色 - 包含菜单ID
     */
    @Test
    void testInsertRole_WithMenuIds() {
        SysRole newRole = new SysRole();
        newRole.setRoleName("测试角色");
        newRole.setRoleKey("test");
        newRole.setMenuIds(new Long[]{1L, 2L, 3L});

        when(roleMapper.insertRole(newRole)).thenReturn(1);
        when(roleMenuMapper.batchRoleMenu(anyList())).thenReturn(3);

        int result = roleService.insertRole(newRole);

        assertEquals(3, result);
        verify(roleMapper).insertRole(newRole);
        verify(roleMenuMapper).batchRoleMenu(anyList());
    }

    /**
     * 测试新增角色 - 不包含菜单ID
     */
    @Test
    void testInsertRole_WithoutMenuIds() {
        SysRole newRole = new SysRole();
        newRole.setRoleName("测试角色");
        newRole.setRoleKey("test");
        newRole.setMenuIds(null);

        when(roleMapper.insertRole(newRole)).thenReturn(1);

        int result = roleService.insertRole(newRole);

        assertEquals(1, result);
        verify(roleMapper).insertRole(newRole);
        verify(roleMenuMapper, never()).batchRoleMenu(anyList());
    }

    /**
     * 测试修改角色数据权限 - 包含部门ID
     */
    @Test
    void testAuthDataScope_WithDeptIds() {
        SysRole role = new SysRole();
        role.setRoleId(2L);
        role.setDeptIds(new Long[]{1L, 2L});

        when(roleMapper.updateRole(role)).thenReturn(1);
        when(roleDeptMapper.deleteRoleDeptByRoleId(2L)).thenReturn(2);
        when(roleDeptMapper.batchRoleDept(anyList())).thenReturn(2);

        int result = roleService.authDataScope(role);

        assertEquals(2, result);
        verify(roleMapper).updateRole(role);
        verify(roleDeptMapper).deleteRoleDeptByRoleId(2L);
        verify(roleDeptMapper).batchRoleDept(anyList());
    }

    /**
     * 测试修改角色数据权限 - 不包含部门ID
     */
    @Test
    void testAuthDataScope_WithoutDeptIds() {
        SysRole role = new SysRole();
        role.setRoleId(2L);
        role.setDeptIds(new Long[0]);

        when(roleMapper.updateRole(role)).thenReturn(1);
        when(roleDeptMapper.deleteRoleDeptByRoleId(2L)).thenReturn(0);

        int result = roleService.authDataScope(role);

        assertEquals(1, result);
        verify(roleMapper).updateRole(role);
        verify(roleDeptMapper).deleteRoleDeptByRoleId(2L);
        verify(roleDeptMapper, never()).batchRoleDept(anyList());
    }
}