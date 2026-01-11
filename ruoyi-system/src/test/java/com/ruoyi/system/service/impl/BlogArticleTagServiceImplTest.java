package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.BlogArticleTag;
import com.ruoyi.system.mapper.BlogArticleTagMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 文章标签关联服务层单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class BlogArticleTagServiceImplTest {

    @Mock
    private BlogArticleTagMapper blogArticleTagMapper;

    @InjectMocks
    private BlogArticleTagServiceImpl blogArticleTagService;

    private BlogArticleTag testArticleTag;

    @BeforeEach
    void setUp() {
        testArticleTag = new BlogArticleTag();
        testArticleTag.setId(1L);
        testArticleTag.setArticleId(1L);
        testArticleTag.setId(1L);
    }

    /**
     * 测试查询文章标签关联列表
     */
    @Test
    void testSelectBlogArticleTagList() {
        // 准备数据
        List<BlogArticleTag> articleTagList = Arrays.asList(testArticleTag);
        when(blogArticleTagMapper.selectBlogArticleTagList(any(BlogArticleTag.class)))
            .thenReturn(articleTagList);

        // 执行测试
        List<BlogArticleTag> result = blogArticleTagService.selectBlogArticleTagList(new BlogArticleTag());

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getArticleId());
        verify(blogArticleTagMapper).selectBlogArticleTagList(any(BlogArticleTag.class));
    }

    /**
     * 测试根据ID查询文章标签关联
     */
    @Test
    void testSelectBlogArticleTagById() {
        // 准备数据
        when(blogArticleTagMapper.selectBlogArticleTagById(1L)).thenReturn(testArticleTag);

        // 执行测试
        BlogArticleTag result = blogArticleTagService.selectBlogArticleTagById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getArticleId());
        verify(blogArticleTagMapper).selectBlogArticleTagById(1L);
    }

    /**
     * 测试新增文章标签关联
     */
    @Test
    void testInsertBlogArticleTag() {
        // 准备数据
        when(blogArticleTagMapper.insertBlogArticleTag(any(BlogArticleTag.class))).thenReturn(1);

        // 执行测试
        int result = blogArticleTagService.insertBlogArticleTag(testArticleTag);

        // 验证结果
        assertEquals(1, result);
        verify(blogArticleTagMapper).insertBlogArticleTag(any(BlogArticleTag.class));
    }

    /**
     * 测试更新文章标签关联
     */
    @Test
    void testUpdateBlogArticleTag() {
        // 准备数据
        when(blogArticleTagMapper.updateBlogArticleTag(any(BlogArticleTag.class))).thenReturn(1);

        // 执行测试
        int result = blogArticleTagService.updateBlogArticleTag(testArticleTag);

        // 验证结果
        assertEquals(1, result);
        verify(blogArticleTagMapper).updateBlogArticleTag(any(BlogArticleTag.class));
    }

    /**
     * 测试删除文章标签关联
     */
    @Test
    void testDeleteBlogArticleTagById() {
        // 准备数据
        when(blogArticleTagMapper.deleteBlogArticleTagById(1L)).thenReturn(1);

        // 执行测试
        int result = blogArticleTagService.deleteBlogArticleTagById(1L);

        // 验证结果
        assertEquals(1, result);
        verify(blogArticleTagMapper).deleteBlogArticleTagById(1L);
    }

    /**
     * 测试批量删除文章标签关联
     */
    @Test
    void testDeleteBlogArticleTagByIds() {
        // 准备数据
        when(blogArticleTagMapper.deleteBlogArticleTagByIds(any(Long[].class))).thenReturn(1);

        // 执行测试
        int result = blogArticleTagService.deleteBlogArticleTagByIds(new Long[]{1L, 2L});

        // 验证结果
        assertEquals(1, result);
        verify(blogArticleTagMapper).deleteBlogArticleTagByIds(any(Long[].class));
    }

    /**
     * 测试通过文章ID查询标签ID列表
     */
    @Test
    void testSelectTagIdsByArticleId() {
        // 准备数据
        List<Long> tagIds = Arrays.asList(1L, 2L, 3L);
        when(blogArticleTagMapper.selectTagIdsByArticleId(1L)).thenReturn(tagIds);

        // 执行测试
        List<Long> result = blogArticleTagService.selectTagIdsByArticleId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1L, result.get(0));
        assertEquals(2L, result.get(1));
        assertEquals(3L, result.get(2));
        verify(blogArticleTagMapper).selectTagIdsByArticleId(1L);
    }

    /**
     * 测试通过文章ID查询标签ID列表 - 空结果
     */
    @Test
    void testSelectTagIdsByArticleId_Empty() {
        // 模拟空结果
        when(blogArticleTagMapper.selectTagIdsByArticleId(1L)).thenReturn(java.util.Collections.emptyList());

        // 执行测试
        List<Long> result = blogArticleTagService.selectTagIdsByArticleId(1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(blogArticleTagMapper).selectTagIdsByArticleId(1L);
    }

    /**
     * 测试通过标签ID查询文章ID列表
     */
    @Test
    void testSelectArticleIdsByTagId() {
        // 准备数据
        List<Long> articleIds = Arrays.asList(1L, 2L, 3L);
        when(blogArticleTagMapper.selectArticleIdsByTagId(1L)).thenReturn(articleIds);

        // 执行测试
        List<Long> result = blogArticleTagService.selectArticleIdsByTagId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1L, result.get(0));
        assertEquals(2L, result.get(1));
        assertEquals(3L, result.get(2));
        verify(blogArticleTagMapper).selectArticleIdsByTagId(1L);
    }

    /**
     * 测试通过标签ID查询文章ID列表 - 空结果
     */
    @Test
    void testSelectArticleIdsByTagId_Empty() {
        // 模拟空结果
        when(blogArticleTagMapper.selectArticleIdsByTagId(1L)).thenReturn(java.util.Collections.emptyList());

        // 执行测试
        List<Long> result = blogArticleTagService.selectArticleIdsByTagId(1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(blogArticleTagMapper).selectArticleIdsByTagId(1L);
    }

    /**
     * 测试批量新增文章标签关联
     */
    @Test
    void testBatchInsertArticleTag() {
        // 准备数据
        BlogArticleTag articleTag1 = new BlogArticleTag();
        articleTag1.setArticleId(1L);
        articleTag1.setTagId(1L);

        BlogArticleTag articleTag2 = new BlogArticleTag();
        articleTag2.setArticleId(1L);
        articleTag2.setTagId(2L);

        List<BlogArticleTag> articleTagList = Arrays.asList(articleTag1, articleTag2);
        when(blogArticleTagMapper.batchInsertArticleTag(any(List.class))).thenReturn(2);

        // 执行测试
        int result = blogArticleTagService.batchInsertArticleTag(articleTagList);

        // 验证结果
        assertEquals(2, result);
        verify(blogArticleTagMapper).batchInsertArticleTag(any(List.class));
    }

    /**
     * 测试批量新增文章标签关联 - 空列表
     */
    @Test
    void testBatchInsertArticleTag_Empty() {
        // 准备数据
        when(blogArticleTagMapper.batchInsertArticleTag(any(List.class))).thenReturn(0);

        // 执行测试
        int result = blogArticleTagService.batchInsertArticleTag(java.util.Collections.emptyList());

        // 验证结果
        assertEquals(0, result);
        verify(blogArticleTagMapper).batchInsertArticleTag(any(List.class));
    }

    /**
     * 测试通过文章ID删除文章和标签关联
     */
    @Test
    void testDeleteByArticleId() {
        // 准备数据
        when(blogArticleTagMapper.deleteByArticleId(1L)).thenReturn(2);

        // 执行测试
        int result = blogArticleTagService.deleteByArticleId(1L);

        // 验证结果
        assertEquals(2, result);
        verify(blogArticleTagMapper).deleteByArticleId(1L);
    }

    /**
     * 测试通过文章ID删除文章和标签关联 - 不存在
     */
    @Test
    void testDeleteByArticleId_NotFound() {
        // 准备数据
        when(blogArticleTagMapper.deleteByArticleId(999L)).thenReturn(0);

        // 执行测试
        int result = blogArticleTagService.deleteByArticleId(999L);

        // 验证结果
        assertEquals(0, result);
        verify(blogArticleTagMapper).deleteByArticleId(999L);
    }

    /**
     * 测试查询文章标签关联列表 - 空结果
     */
    @Test
    void testSelectBlogArticleTagList_Empty() {
        // 模拟空结果
        when(blogArticleTagMapper.selectBlogArticleTagList(any(BlogArticleTag.class)))
            .thenReturn(java.util.Collections.emptyList());

        // 执行测试
        List<BlogArticleTag> result = blogArticleTagService.selectBlogArticleTagList(new BlogArticleTag());

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(blogArticleTagMapper).selectBlogArticleTagList(any(BlogArticleTag.class));
    }

    /**
     * 测试根据ID查询文章标签关联 - 不存在
     */
    @Test
    void testSelectBlogArticleTagById_NotFound() {
        // 模拟数据不存在
        when(blogArticleTagMapper.selectBlogArticleTagById(999L)).thenReturn(null);

        // 执行测试
        BlogArticleTag result = blogArticleTagService.selectBlogArticleTagById(999L);

        // 验证结果
        assertNull(result);
        verify(blogArticleTagMapper).selectBlogArticleTagById(999L);
    }

    /**
     * 测试新增文章标签关联 - 失败
     */
    @Test
    void testInsertBlogArticleTag_Failure() {
        // 准备数据
        when(blogArticleTagMapper.insertBlogArticleTag(any(BlogArticleTag.class))).thenReturn(0);

        // 执行测试
        int result = blogArticleTagService.insertBlogArticleTag(testArticleTag);

        // 验证结果
        assertEquals(0, result);
        verify(blogArticleTagMapper).insertBlogArticleTag(any(BlogArticleTag.class));
    }

    /**
     * 测试更新文章标签关联 - 失败
     */
    @Test
    void testUpdateBlogArticleTag_Failure() {
        // 准备数据
        when(blogArticleTagMapper.updateBlogArticleTag(any(BlogArticleTag.class))).thenReturn(0);

        // 执行测试
        int result = blogArticleTagService.updateBlogArticleTag(testArticleTag);

        // 验证结果
        assertEquals(0, result);
        verify(blogArticleTagMapper).updateBlogArticleTag(any(BlogArticleTag.class));
    }

    /**
     * 测试删除文章标签关联 - 不存在
     */
    @Test
    void testDeleteBlogArticleTagById_NotFound() {
        // 准备数据
        when(blogArticleTagMapper.deleteBlogArticleTagById(999L)).thenReturn(0);

        // 执行测试
        int result = blogArticleTagService.deleteBlogArticleTagById(999L);

        // 验证结果
        assertEquals(0, result);
        verify(blogArticleTagMapper).deleteBlogArticleTagById(999L);
    }

    /**
     * 测试批量删除文章标签关联 - 空列表
     */
    @Test
    void testDeleteBlogArticleTagByIds_Empty() {
        // 准备数据
        when(blogArticleTagMapper.deleteBlogArticleTagByIds(any(Long[].class))).thenReturn(0);

        // 执行测试
        int result = blogArticleTagService.deleteBlogArticleTagByIds(new Long[]{});

        // 验证结果
        assertEquals(0, result);
        verify(blogArticleTagMapper).deleteBlogArticleTagByIds(any(Long[].class));
    }
}