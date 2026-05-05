package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.BlogTag;
import com.ruoyi.system.mapper.BlogTagMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ruoyi.common.utils.SecurityUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 博客标签服务层单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class BlogTagServiceImplTest {

    @Mock
    private BlogTagMapper blogTagMapper;

    @InjectMocks
    private BlogTagServiceImpl blogTagService;

    private BlogTag testTag;

    @BeforeEach
    void setUp() {
        testTag = new BlogTag();
        testTag.setId(1L);
        testTag.setName("测试标签");
        testTag.setColor("#ff0000");
    }

    /**
     * 测试查询标签列表
     */
    @Test
    void testSelectBlogTagList() {
        // 准备数据
        List<BlogTag> tagList = Arrays.asList(testTag);
        when(blogTagMapper.selectBlogTagList(any(BlogTag.class))).thenReturn(tagList);

        // 执行测试
        List<BlogTag> result = blogTagService.selectBlogTagList(new BlogTag());

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试标签", result.get(0).getName());
        verify(blogTagMapper).selectBlogTagList(any(BlogTag.class));
    }

    /**
     * 测试查询标签列表 - 空结果
     */
    @Test
    void testSelectBlogTagList_Empty() {
        // 模拟空结果
        when(blogTagMapper.selectBlogTagList(any(BlogTag.class)))
            .thenReturn(Collections.emptyList());

        // 执行测试
        List<BlogTag> result = blogTagService.selectBlogTagList(new BlogTag());

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(blogTagMapper).selectBlogTagList(any(BlogTag.class));
    }

    /**
     * 测试查询所有标签列表
     */
    @Test
    void testSelectAllTagList() {
        // 准备数据
        List<BlogTag> tagList = Arrays.asList(testTag);
        when(blogTagMapper.selectAllTagList()).thenReturn(tagList);

        // 执行测试
        List<BlogTag> result = blogTagService.selectAllTagList();

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(blogTagMapper).selectAllTagList();
    }

    /**
     * 测试查询所有标签列表 - 空结果
     */
    @Test
    void testSelectAllTagList_Empty() {
        // 模拟空结果
        when(blogTagMapper.selectAllTagList()).thenReturn(Collections.emptyList());

        // 执行测试
        List<BlogTag> result = blogTagService.selectAllTagList();

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(blogTagMapper).selectAllTagList();
    }

    /**
     * 测试根据ID查询标签
     */
    @Test
    void testSelectBlogTagById() {
        // 准备数据
        when(blogTagMapper.selectBlogTagById(1L)).thenReturn(testTag);

        // 执行测试
        BlogTag result = blogTagService.selectBlogTagById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("测试标签", result.getName());
        verify(blogTagMapper).selectBlogTagById(1L);
    }

    /**
     * 测试根据ID查询标签 - 不存在
     */
    @Test
    void testSelectBlogTagById_NotFound() {
        // 模拟标签不存在
        when(blogTagMapper.selectBlogTagById(999L)).thenReturn(null);

        // 执行测试
        BlogTag result = blogTagService.selectBlogTagById(999L);

        // 验证结果
        assertNull(result);
        verify(blogTagMapper).selectBlogTagById(999L);
    }

    /**
     * 测试新增标签
     */
    @Test
    void testInsertBlogTag() {
        // 准备数据
        when(blogTagMapper.insertBlogTag(any(BlogTag.class))).thenReturn(1);

        // Mock SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("testUser");

            // 执行测试
            int result = blogTagService.insertBlogTag(testTag);

            // 验证结果
            assertEquals(1, result);
            verify(blogTagMapper).insertBlogTag(any(BlogTag.class));
        }
    }

    /**
     * 测试新增标签 - 名称为空
     */
    @Test
    void testInsertBlogTag_EmptyName() {
        // 设置名称为空
        testTag.setName("");

        // 准备数据
        when(blogTagMapper.insertBlogTag(any(BlogTag.class))).thenReturn(1);

        // Mock SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("testUser");

            // 执行测试 - 注意：Service层可能没有验证名称为空
            int result = blogTagService.insertBlogTag(testTag);

            // 验证结果 - 如果Service层没有验证，这里应该返回1
            assertEquals(1, result);
            verify(blogTagMapper).insertBlogTag(any(BlogTag.class));
        }
    }

    /**
     * 测试新增标签 - 名称前后有空格
     */
    @Test
    void testInsertBlogTag_NameWithSpaces() {
        // 设置名称前后有空格
        testTag.setName("  测试标签  ");

        // 准备数据
        when(blogTagMapper.insertBlogTag(any(BlogTag.class))).thenReturn(1);

        // Mock SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("testUser");

            // 执行测试
            int result = blogTagService.insertBlogTag(testTag);

            // 验证结果
            assertEquals(1, result);
            verify(blogTagMapper).insertBlogTag(any(BlogTag.class));
        }
    }

    /**
     * 测试修改标签
     */
    @Test
    void testUpdateBlogTag() {
        // 准备数据
        when(blogTagMapper.updateBlogTag(any(BlogTag.class))).thenReturn(1);

        // Mock SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("testUser");

            // 执行测试
            int result = blogTagService.updateBlogTag(testTag);

            // 验证结果
            assertEquals(1, result);
            verify(blogTagMapper).updateBlogTag(any(BlogTag.class));
        }
    }

    /**
     * 测试修改标签 - 名称为空
     */
    @Test
    void testUpdateBlogTag_EmptyName() {
        // 设置名称为空
        testTag.setName("");

        // 准备数据
        when(blogTagMapper.updateBlogTag(any(BlogTag.class))).thenReturn(1);

        // Mock SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("testUser");

            // 执行测试 - 注意：Service层可能没有验证名称为空
            int result = blogTagService.updateBlogTag(testTag);

            // 验证结果 - 如果Service层没有验证，这里应该返回1
            assertEquals(1, result);
            verify(blogTagMapper).updateBlogTag(any(BlogTag.class));
        }
    }

    /**
     * 测试删除标签
     */
    @Test
    void testDeleteBlogTagById() {
        // 准备数据
        when(blogTagMapper.deleteBlogTagById(1L)).thenReturn(1);

        // 执行测试
        int result = blogTagService.deleteBlogTagById(1L);

        // 验证结果
        assertEquals(1, result);
        verify(blogTagMapper).deleteBlogTagById(1L);
    }

    /**
     * 测试删除标签 - 不存在
     */
    @Test
    void testDeleteBlogTagById_NotFound() {
        // 准备数据
        when(blogTagMapper.deleteBlogTagById(999L)).thenReturn(0);

        // 执行测试
        int result = blogTagService.deleteBlogTagById(999L);

        // 验证结果
        assertEquals(0, result);
        verify(blogTagMapper).deleteBlogTagById(999L);
    }

    /**
     * 测试批量删除标签
     */
    @Test
    void testDeleteBlogTagByIds() {
        // 准备数据
        when(blogTagMapper.deleteBlogTagByIds(any(Long[].class))).thenReturn(2);

        // 执行测试
        int result = blogTagService.deleteBlogTagByIds(new Long[]{1L, 2L});

        // 验证结果
        assertEquals(2, result);
        verify(blogTagMapper).deleteBlogTagByIds(any(Long[].class));
    }

    /**
     * 测试批量删除标签 - 空列表
     */
    @Test
    void testDeleteBlogTagByIds_EmptyList() {
        // 准备数据
        when(blogTagMapper.deleteBlogTagByIds(any(Long[].class))).thenReturn(0);

        // 执行测试
        int result = blogTagService.deleteBlogTagByIds(new Long[]{});

        // 验证结果
        assertEquals(0, result);
        verify(blogTagMapper).deleteBlogTagByIds(any(Long[].class));
    }

    /**
     * 测试检查标签是否关联文章 - 未关联
     */
    @Test
    void testCheckTagExistArticle_NotExist() {
        // 准备数据
        when(blogTagMapper.checkTagExistArticle(1L)).thenReturn(0);

        // 执行测试
        boolean result = blogTagService.checkTagExistArticle(1L);

        // 验证结果
        assertFalse(result);
        verify(blogTagMapper).checkTagExistArticle(1L);
    }

    /**
     * 测试检查标签是否关联文章 - 已关联
     */
    @Test
    void testCheckTagExistArticle_Exist() {
        // 准备数据
        when(blogTagMapper.checkTagExistArticle(1L)).thenReturn(1);

        // 执行测试
        boolean result = blogTagService.checkTagExistArticle(1L);

        // 验证结果
        assertTrue(result);
        verify(blogTagMapper).checkTagExistArticle(1L);
    }

    /**
     * 测试检查标签名称唯一性 - 唯一
     */
    @Test
    void testCheckTagNameUnique_Unique() {
        // 准备数据
        when(blogTagMapper.checkTagNameUnique(anyString())).thenReturn(null);

        // 执行测试
        boolean result = blogTagService.checkTagNameUnique(testTag);

        // 验证结果
        assertTrue(result);
        verify(blogTagMapper).checkTagNameUnique(anyString());
    }

    /**
     * 测试检查标签名称唯一性 - 不唯一
     */
    @Test
    void testCheckTagNameUnique_NotUnique() {
        // 准备数据
        BlogTag existingTag = new BlogTag();
        existingTag.setId(2L);
        when(blogTagMapper.checkTagNameUnique(anyString())).thenReturn(existingTag);

        // 执行测试
        boolean result = blogTagService.checkTagNameUnique(testTag);

        // 验证结果
        assertFalse(result);
        verify(blogTagMapper).checkTagNameUnique(anyString());
    }

    /**
     * 测试检查标签名称唯一性 - 更新自身
     */
    @Test
    void testCheckTagNameUnique_UpdateSelf() {
        // 准备数据
        when(blogTagMapper.checkTagNameUnique(anyString())).thenReturn(testTag);

        // 执行测试
        boolean result = blogTagService.checkTagNameUnique(testTag);

        // 验证结果
        assertTrue(result);
        verify(blogTagMapper).checkTagNameUnique(anyString());
    }

    /**
     * 测试获取标签云
     */
    @Test
    void testGetTagCloud() {
        // 准备数据
        List<Map<String, Object>> tagCloud = Collections.emptyList();
        when(blogTagMapper.getTagCloud()).thenReturn(tagCloud);

        // 执行测试
        List<Map<String, Object>> result = blogTagService.getTagCloud();

        // 验证结果
        assertNotNull(result);
        verify(blogTagMapper).getTagCloud();
    }

    /**
     * 测试根据文章ID查询标签列表
     */
    @Test
    void testSelectTagsByArticleId() {
        // 准备数据
        List<BlogTag> tagList = Arrays.asList(testTag);
        when(blogTagMapper.selectTagsByArticleId(1L)).thenReturn(tagList);

        // 执行测试
        List<BlogTag> result = blogTagService.selectTagsByArticleId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(blogTagMapper).selectTagsByArticleId(1L);
    }

    /**
     * 测试根据文章ID查询标签列表 - 空结果
     */
    @Test
    void testSelectTagsByArticleId_Empty() {
        // 模拟空结果
        when(blogTagMapper.selectTagsByArticleId(1L)).thenReturn(Collections.emptyList());

        // 执行测试
        List<BlogTag> result = blogTagService.selectTagsByArticleId(1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(blogTagMapper).selectTagsByArticleId(1L);
    }
}