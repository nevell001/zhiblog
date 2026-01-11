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

import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysRoleMapper;

/**
 * 部门服务层测试
 *
 * @author ruoyi
 */
@ExtendWith(MockitoExtension.class)
class SysDeptServiceImplTest {

    @Mock
    private SysDeptMapper deptMapper;

    @Mock
    private SysRoleMapper roleMapper;

    @InjectMocks
    private SysDeptServiceImpl deptService;

    private SysDept testDept;

    @BeforeEach
    void setUp() {
        testDept = new SysDept();
        testDept.setDeptId(1L);
        testDept.setParentId(0L);
        testDept.setDeptName("技术部");
        testDept.setOrderNum(1);
        testDept.setLeader("张三");
        testDept.setPhone("13800138000");
        testDept.setEmail("tech@example.com");
        testDept.setStatus("0");
    }

    /**
     * 测试查询部门列表
     */
    @Test
    void testSelectDeptList() {
        List<SysDept> deptList = Arrays.asList(testDept);
        when(deptMapper.selectDeptList(any(SysDept.class))).thenReturn(deptList);

        List<SysDept> result = deptService.selectDeptList(new SysDept());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("技术部", result.get(0).getDeptName());
        verify(deptMapper).selectDeptList(any(SysDept.class));
    }

    /**
     * 测试根据部门ID查询信息
     */
    @Test
    void testSelectDeptById() {
        when(deptMapper.selectDeptById(1L)).thenReturn(testDept);

        SysDept result = deptService.selectDeptById(1L);

        assertNotNull(result);
        assertEquals("技术部", result.getDeptName());
        verify(deptMapper).selectDeptById(1L);
    }

    /**
     * 测试根据ID查询所有子部门
     */
    @Test
    void testSelectNormalChildrenDeptById() {
        when(deptMapper.selectNormalChildrenDeptById(1L)).thenReturn(3);

        int result = deptService.selectNormalChildrenDeptById(1L);

        assertEquals(3, result);
        verify(deptMapper).selectNormalChildrenDeptById(1L);
    }

    /**
     * 测试是否存在子节点 - 有子节点
     */
    @Test
    void testHasChildByDeptId_HasChildren() {
        when(deptMapper.hasChildByDeptId(1L)).thenReturn(1);

        boolean result = deptService.hasChildByDeptId(1L);

        assertTrue(result);
        verify(deptMapper).hasChildByDeptId(1L);
    }

    /**
     * 测试是否存在子节点 - 无子节点
     */
    @Test
    void testHasChildByDeptId_NoChildren() {
        when(deptMapper.hasChildByDeptId(1L)).thenReturn(0);

        boolean result = deptService.hasChildByDeptId(1L);

        assertFalse(result);
        verify(deptMapper).hasChildByDeptId(1L);
    }

    /**
     * 测试校验部门名称是否唯一 - 唯一
     */
    @Test
    void testCheckDeptNameUnique_Unique() {
        when(deptMapper.checkDeptNameUnique("技术部", 0L)).thenReturn(null);

        boolean result = deptService.checkDeptNameUnique(testDept);

        assertTrue(result);
        verify(deptMapper).checkDeptNameUnique("技术部", 0L);
    }

    /**
     * 测试校验部门名称是否唯一 - 不唯一
     */
    @Test
    void testCheckDeptNameUnique_NotUnique() {
        SysDept existingDept = new SysDept();
        existingDept.setDeptId(2L);
        existingDept.setDeptName("技术部");

        when(deptMapper.checkDeptNameUnique("技术部", 0L)).thenReturn(existingDept);

        boolean result = deptService.checkDeptNameUnique(testDept);

        assertFalse(result);
        verify(deptMapper).checkDeptNameUnique("技术部", 0L);
    }

    /**
     * 测试校验部门名称是否唯一 - 同一部门
     */
    @Test
    void testCheckDeptNameUnique_SameDept() {
        when(deptMapper.checkDeptNameUnique("技术部", 0L)).thenReturn(testDept);

        boolean result = deptService.checkDeptNameUnique(testDept);

        assertTrue(result);
        verify(deptMapper).checkDeptNameUnique("技术部", 0L);
    }

    /**
     * 测试新增部门
     */
    @Test
    void testInsertDept() {
        testDept.setDeptId(null);

        SysDept parentDept = new SysDept();
        parentDept.setDeptId(0L);
        parentDept.setStatus("0");
        parentDept.setAncestors("0");

        when(deptMapper.selectDeptById(0L)).thenReturn(parentDept);
        when(deptMapper.insertDept(testDept)).thenReturn(1);

        int result = deptService.insertDept(testDept);

        assertEquals(1, result);
        verify(deptMapper).selectDeptById(0L);
        verify(deptMapper).insertDept(testDept);
    }

    /**
     * 测试修改部门
     */
    @Test
    void testUpdateDept() {
        when(deptMapper.updateDept(testDept)).thenReturn(1);

        int result = deptService.updateDept(testDept);

        assertEquals(1, result);
        verify(deptMapper).updateDept(testDept);
    }

    /**
     * 测试删除部门
     */
    @Test
    void testDeleteDeptById() {
        when(deptMapper.deleteDeptById(1L)).thenReturn(1);

        int result = deptService.deleteDeptById(1L);

        assertEquals(1, result);
        verify(deptMapper).deleteDeptById(1L);
    }

    /**
     * 测试构建部门树 - 简单树
     */
    @Test
    void testBuildDeptTree_SimpleTree() {
        SysDept parentDept = new SysDept();
        parentDept.setDeptId(1L);
        parentDept.setDeptName("技术部");
        parentDept.setParentId(0L);

        SysDept childDept = new SysDept();
        childDept.setDeptId(2L);
        childDept.setDeptName("开发组");
        childDept.setParentId(1L);

        List<SysDept> depts = Arrays.asList(parentDept, childDept);

        List<SysDept> result = deptService.buildDeptTree(depts);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("技术部", result.get(0).getDeptName());
        assertEquals(1, result.get(0).getChildren().size());
    }

    /**
     * 测试构建部门树 - 空列表
     */
    @Test
    void testBuildDeptTree_EmptyList() {
        List<SysDept> depts = Collections.emptyList();

        List<SysDept> result = deptService.buildDeptTree(depts);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * 测试构建部门树选择 - 简单树
     */
    @Test
    void testBuildDeptTreeSelect_SimpleTree() {
        SysDept parentDept = new SysDept();
        parentDept.setDeptId(1L);
        parentDept.setDeptName("技术部");
        parentDept.setParentId(0L);

        SysDept childDept = new SysDept();
        childDept.setDeptId(2L);
        childDept.setDeptName("开发组");
        childDept.setParentId(1L);

        List<SysDept> depts = Arrays.asList(parentDept, childDept);

        List<TreeSelect> result = deptService.buildDeptTreeSelect(depts);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("技术部", result.get(0).getLabel());
    }

    /**
     * 测试根据角色ID查询部门树信息
     */
    @Test
    void testSelectDeptListByRoleId() {
        Long roleId = 1L;
        SysRole role = new SysRole();
        role.setRoleId(roleId);
        role.setDeptCheckStrictly(false);

        List<Long> deptIds = Arrays.asList(1L, 2L, 3L);
        when(roleMapper.selectRoleById(roleId)).thenReturn(role);
        when(deptMapper.selectDeptListByRoleId(roleId, false)).thenReturn(deptIds);

        List<Long> result = deptService.selectDeptListByRoleId(roleId);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(roleMapper).selectRoleById(roleId);
        verify(deptMapper).selectDeptListByRoleId(roleId, false);
    }
}