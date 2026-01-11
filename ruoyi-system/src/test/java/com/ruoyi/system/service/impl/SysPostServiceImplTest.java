package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.mapper.SysPostMapper;
import com.ruoyi.system.mapper.SysUserPostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 岗位信息服务层单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class SysPostServiceImplTest {

    @Mock
    private SysPostMapper postMapper;

    @Mock
    private SysUserPostMapper userPostMapper;

    @InjectMocks
    private SysPostServiceImpl sysPostService;

    private SysPost testPost;

    @BeforeEach
    void setUp() {
        testPost = new SysPost();
        testPost.setPostId(1L);
        testPost.setPostName("测试岗位");
        testPost.setPostCode("test_post");
        testPost.setPostSort(1);
    }

    /**
     * 测试查询岗位信息集合
     */
    @Test
    void testSelectPostList() {
        List<SysPost> postList = Arrays.asList(testPost);
        when(postMapper.selectPostList(any(SysPost.class))).thenReturn(postList);

        List<SysPost> result = sysPostService.selectPostList(new SysPost());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试岗位", result.get(0).getPostName());
        verify(postMapper).selectPostList(any(SysPost.class));
    }

    /**
     * 测试查询岗位信息集合 - 空结果
     */
    @Test
    void testSelectPostList_Empty() {
        when(postMapper.selectPostList(any(SysPost.class))).thenReturn(Collections.emptyList());

        List<SysPost> result = sysPostService.selectPostList(new SysPost());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(postMapper).selectPostList(any(SysPost.class));
    }

    /**
     * 测试查询所有岗位
     */
    @Test
    void testSelectPostAll() {
        List<SysPost> postList = Arrays.asList(testPost);
        when(postMapper.selectPostAll()).thenReturn(postList);

        List<SysPost> result = sysPostService.selectPostAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(postMapper).selectPostAll();
    }

    /**
     * 测试查询所有岗位 - 空结果
     */
    @Test
    void testSelectPostAll_Empty() {
        when(postMapper.selectPostAll()).thenReturn(Collections.emptyList());

        List<SysPost> result = sysPostService.selectPostAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(postMapper).selectPostAll();
    }

    /**
     * 测试通过岗位ID查询岗位信息
     */
    @Test
    void testSelectPostById() {
        when(postMapper.selectPostById(1L)).thenReturn(testPost);

        SysPost result = sysPostService.selectPostById(1L);

        assertNotNull(result);
        assertEquals("测试岗位", result.getPostName());
        verify(postMapper).selectPostById(1L);
    }

    /**
     * 测试通过岗位ID查询岗位信息 - 不存在
     */
    @Test
    void testSelectPostById_NotFound() {
        when(postMapper.selectPostById(999L)).thenReturn(null);

        SysPost result = sysPostService.selectPostById(999L);

        assertNull(result);
        verify(postMapper).selectPostById(999L);
    }

    /**
     * 测试根据用户ID获取岗位选择框列表
     */
    @Test
    void testSelectPostListByUserId() {
        List<Long> postIds = Arrays.asList(1L, 2L);
        when(postMapper.selectPostListByUserId(1L)).thenReturn(postIds);

        List<Long> result = sysPostService.selectPostListByUserId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(postMapper).selectPostListByUserId(1L);
    }

    /**
     * 测试根据用户ID获取岗位选择框列表 - 空结果
     */
    @Test
    void testSelectPostListByUserId_Empty() {
        when(postMapper.selectPostListByUserId(1L)).thenReturn(Collections.emptyList());

        List<Long> result = sysPostService.selectPostListByUserId(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(postMapper).selectPostListByUserId(1L);
    }

    /**
     * 测试校验岗位名称是否唯一 - 唯一
     */
    @Test
    void testCheckPostNameUnique_Unique() {
        when(postMapper.checkPostNameUnique("测试岗位")).thenReturn(null);

        boolean result = sysPostService.checkPostNameUnique(testPost);

        assertEquals(UserConstants.UNIQUE, result);
        verify(postMapper).checkPostNameUnique("测试岗位");
    }

    /**
     * 测试校验岗位名称是否唯一 - 重复
     */
    @Test
    void testCheckPostNameUnique_NotUnique() {
        SysPost existingPost = new SysPost();
        existingPost.setPostId(2L);
        when(postMapper.checkPostNameUnique("测试岗位")).thenReturn(existingPost);

        boolean result = sysPostService.checkPostNameUnique(testPost);

        assertEquals(UserConstants.NOT_UNIQUE, result);
        verify(postMapper).checkPostNameUnique("测试岗位");
    }

    /**
     * 测试校验岗位名称是否唯一 - 同一岗位
     */
    @Test
    void testCheckPostNameUnique_SamePost() {
        testPost.setPostId(1L);
        when(postMapper.checkPostNameUnique("测试岗位")).thenReturn(testPost);

        boolean result = sysPostService.checkPostNameUnique(testPost);

        assertEquals(UserConstants.UNIQUE, result);
        verify(postMapper).checkPostNameUnique("测试岗位");
    }

    /**
     * 测试校验岗位名称是否唯一 - 新岗位
     */
    @Test
    void testCheckPostNameUnique_NewPost() {
        testPost.setPostId(null);
        when(postMapper.checkPostNameUnique("测试岗位")).thenReturn(null);

        boolean result = sysPostService.checkPostNameUnique(testPost);

        assertEquals(UserConstants.UNIQUE, result);
        verify(postMapper).checkPostNameUnique("测试岗位");
    }

    /**
     * 测试校验岗位编码是否唯一 - 唯一
     */
    @Test
    void testCheckPostCodeUnique_Unique() {
        when(postMapper.checkPostCodeUnique("test_post")).thenReturn(null);

        boolean result = sysPostService.checkPostCodeUnique(testPost);

        assertEquals(UserConstants.UNIQUE, result);
        verify(postMapper).checkPostCodeUnique("test_post");
    }

    /**
     * 测试校验岗位编码是否唯一 - 重复
     */
    @Test
    void testCheckPostCodeUnique_NotUnique() {
        SysPost existingPost = new SysPost();
        existingPost.setPostId(2L);
        when(postMapper.checkPostCodeUnique("test_post")).thenReturn(existingPost);

        boolean result = sysPostService.checkPostCodeUnique(testPost);

        assertEquals(UserConstants.NOT_UNIQUE, result);
        verify(postMapper).checkPostCodeUnique("test_post");
    }

    /**
     * 测试校验岗位编码是否唯一 - 同一岗位
     */
    @Test
    void testCheckPostCodeUnique_SamePost() {
        testPost.setPostId(1L);
        when(postMapper.checkPostCodeUnique("test_post")).thenReturn(testPost);

        boolean result = sysPostService.checkPostCodeUnique(testPost);

        assertEquals(UserConstants.UNIQUE, result);
        verify(postMapper).checkPostCodeUnique("test_post");
    }

    /**
     * 测试校验岗位编码是否唯一 - 新岗位
     */
    @Test
    void testCheckPostCodeUnique_NewPost() {
        testPost.setPostId(null);
        when(postMapper.checkPostCodeUnique("test_post")).thenReturn(null);

        boolean result = sysPostService.checkPostCodeUnique(testPost);

        assertEquals(UserConstants.UNIQUE, result);
        verify(postMapper).checkPostCodeUnique("test_post");
    }

    /**
     * 测试通过岗位ID查询岗位使用数量
     */
    @Test
    void testCountUserPostById() {
        when(userPostMapper.countUserPostById(1L)).thenReturn(5);

        int result = sysPostService.countUserPostById(1L);

        assertEquals(5, result);
        verify(userPostMapper).countUserPostById(1L);
    }

    /**
     * 测试通过岗位ID查询岗位使用数量 - 无用户
     */
    @Test
    void testCountUserPostById_Zero() {
        when(userPostMapper.countUserPostById(1L)).thenReturn(0);

        int result = sysPostService.countUserPostById(1L);

        assertEquals(0, result);
        verify(userPostMapper).countUserPostById(1L);
    }

    /**
     * 测试删除岗位信息
     */
    @Test
    void testDeletePostById() {
        when(postMapper.deletePostById(1L)).thenReturn(1);

        int result = sysPostService.deletePostById(1L);

        assertEquals(1, result);
        verify(postMapper).deletePostById(1L);
    }

    /**
     * 测试删除岗位信息 - 不存在
     */
    @Test
    void testDeletePostById_NotFound() {
        when(postMapper.deletePostById(999L)).thenReturn(0);

        int result = sysPostService.deletePostById(999L);

        assertEquals(0, result);
        verify(postMapper).deletePostById(999L);
    }

    /**
     * 测试批量删除岗位信息 - 成功
     */
    @Test
    void testDeletePostByIds_Success() {
        when(postMapper.selectPostById(1L)).thenReturn(testPost);
        when(userPostMapper.countUserPostById(1L)).thenReturn(0);
        when(postMapper.deletePostByIds(any(Long[].class))).thenReturn(1);

        int result = sysPostService.deletePostByIds(new Long[]{1L});

        assertEquals(1, result);
        verify(postMapper).selectPostById(1L);
        verify(userPostMapper).countUserPostById(1L);
        verify(postMapper).deletePostByIds(any(Long[].class));
    }

    /**
     * 测试批量删除岗位信息 - 已分配用户
     */
    @Test
    void testDeletePostByIds_HasUsers() {
        when(postMapper.selectPostById(1L)).thenReturn(testPost);
        when(userPostMapper.countUserPostById(1L)).thenReturn(5);

        assertThrows(ServiceException.class, () -> {
            sysPostService.deletePostByIds(new Long[]{1L});
        });

        verify(postMapper).selectPostById(1L);
        verify(userPostMapper).countUserPostById(1L);
        verify(postMapper, never()).deletePostByIds(any(Long[].class));
    }

    /**
     * 测试批量删除岗位信息 - 空数组
     */
    @Test
    void testDeletePostByIds_Empty() {
        when(postMapper.deletePostByIds(any(Long[].class))).thenReturn(0);

        int result = sysPostService.deletePostByIds(new Long[]{});

        assertEquals(0, result);
        verify(postMapper).deletePostByIds(any(Long[].class));
    }

    /**
     * 测试新增保存岗位信息
     */
    @Test
    void testInsertPost() {
        when(postMapper.insertPost(any(SysPost.class))).thenReturn(1);

        int result = sysPostService.insertPost(testPost);

        assertEquals(1, result);
        verify(postMapper).insertPost(any(SysPost.class));
    }

    /**
     * 测试新增保存岗位信息 - 失败
     */
    @Test
    void testInsertPost_Failure() {
        when(postMapper.insertPost(any(SysPost.class))).thenReturn(0);

        int result = sysPostService.insertPost(testPost);

        assertEquals(0, result);
        verify(postMapper).insertPost(any(SysPost.class));
    }

    /**
     * 测试修改保存岗位信息
     */
    @Test
    void testUpdatePost() {
        when(postMapper.updatePost(any(SysPost.class))).thenReturn(1);

        int result = sysPostService.updatePost(testPost);

        assertEquals(1, result);
        verify(postMapper).updatePost(any(SysPost.class));
    }

    /**
     * 测试修改保存岗位信息 - 失败
     */
    @Test
    void testUpdatePost_Failure() {
        when(postMapper.updatePost(any(SysPost.class))).thenReturn(0);

        int result = sysPostService.updatePost(testPost);

        assertEquals(0, result);
        verify(postMapper).updatePost(any(SysPost.class));
    }
}