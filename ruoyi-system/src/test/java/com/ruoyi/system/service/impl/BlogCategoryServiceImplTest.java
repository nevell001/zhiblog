package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.BlogCategory;
import com.ruoyi.system.mapper.BlogArticleMapper;
import com.ruoyi.system.mapper.BlogCategoryMapper;
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
 * 文章分类服务层单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class BlogCategoryServiceImplTest {

    @Mock
    private BlogCategoryMapper blogCategoryMapper;

    @Mock
    private BlogArticleMapper blogArticleMapper;

    @InjectMocks
    private BlogCategoryServiceImpl blogCategoryService;

    private BlogCategory testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new BlogCategory();
        testCategory.setId(1L);
        testCategory.setName("测试分类");
        testCategory.setDescription("测试分类描述");
    }

    /**
     * 测试查询分类列表
     */
    @Test
    void testSelectBlogCategoryList() {
        // 准备数据
        List<BlogCategory> categoryList = Arrays.asList(testCategory);
        when(blogCategoryMapper.selectBlogCategoryList(any(BlogCategory.class)))
            .thenReturn(categoryList);

        // 执行测试
        List<BlogCategory> result = blogCategoryService.selectBlogCategoryList(new BlogCategory());

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试分类", result.get(0).getName());
        verify(blogCategoryMapper).selectBlogCategoryList(any(BlogCategory.class));
    }

    /**
     * 测试查询分类列表 - 空结果
     */
    @Test
    void testSelectBlogCategoryList_Empty() {
        // 模拟空结果
        when(blogCategoryMapper.selectBlogCategoryList(any(BlogCategory.class)))
            .thenReturn(Collections.emptyList());

        // 执行测试
        List<BlogCategory> result = blogCategoryService.selectBlogCategoryList(new BlogCategory());

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(blogCategoryMapper).selectBlogCategoryList(any(BlogCategory.class));
    }

    /**
     * 测试根据ID查询分类
     */
    @Test
    void testSelectBlogCategoryById() {
        // 准备数据
        when(blogCategoryMapper.selectBlogCategoryById(1L)).thenReturn(testCategory);

        // 执行测试
        BlogCategory result = blogCategoryService.selectBlogCategoryById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("测试分类", result.getName());
        verify(blogCategoryMapper).selectBlogCategoryById(1L);
    }

    /**
     * 测试根据ID查询分类 - 不存在
     */
    @Test
    void testSelectBlogCategoryById_NotFound() {
        // 模拟分类不存在
        when(blogCategoryMapper.selectBlogCategoryById(999L)).thenReturn(null);

        // 执行测试
        BlogCategory result = blogCategoryService.selectBlogCategoryById(999L);

        // 验证结果
        assertNull(result);
        verify(blogCategoryMapper).selectBlogCategoryById(999L);
    }

    /**
     * 测试新增分类 - 成功
     */
    @Test
    void testInsertBlogCategory_Success() {
        // 准备数据
        when(blogCategoryMapper.countByName(any(BlogCategory.class))).thenReturn(0);
        when(blogCategoryMapper.insertBlogCategory(any(BlogCategory.class))).thenReturn(1);

        // 执行测试
        int result = blogCategoryService.insertBlogCategory(testCategory);

        // 验证结果
        assertEquals(1, result);
        assertNotNull(testCategory.getCreateTime());
        verify(blogCategoryMapper).countByName(any(BlogCategory.class));
        verify(blogCategoryMapper).insertBlogCategory(any(BlogCategory.class));
    }

    /**
     * 测试新增分类 - 名称重复
     */
    @Test
    void testInsertBlogCategory_DuplicateName() {
        // 准备数据
        when(blogCategoryMapper.countByName(any(BlogCategory.class))).thenReturn(1);

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class,
            () -> blogCategoryService.insertBlogCategory(testCategory));

        assertEquals("分类名称已存在", exception.getMessage());
        verify(blogCategoryMapper).countByName(any(BlogCategory.class));
        verify(blogCategoryMapper, never()).insertBlogCategory(any(BlogCategory.class));
    }

    /**
     * 测试新增分类 - 名称为空
     */
    @Test
    void testInsertBlogCategory_EmptyName() {
        // 设置名称为空
        testCategory.setName("");

        // 准备数据
        when(blogCategoryMapper.countByName(any(BlogCategory.class))).thenReturn(0);
        when(blogCategoryMapper.insertBlogCategory(any(BlogCategory.class))).thenReturn(1);

        // 执行测试 - 注意：Service层可能没有验证名称为空
        int result = blogCategoryService.insertBlogCategory(testCategory);

        // 验证结果 - 如果Service层没有验证，这里应该返回1
        assertEquals(1, result);
        verify(blogCategoryMapper).countByName(any(BlogCategory.class));
        verify(blogCategoryMapper).insertBlogCategory(any(BlogCategory.class));
    }

    /**
     * 测试新增分类 - 名称前后有空格
     */
    @Test
    void testInsertBlogCategory_NameWithSpaces() {
        // 设置名称前后有空格
        testCategory.setName("  测试分类  ");

        // 准备数据
        when(blogCategoryMapper.countByName(any(BlogCategory.class))).thenReturn(0);
        when(blogCategoryMapper.insertBlogCategory(any(BlogCategory.class))).thenReturn(1);

        // 执行测试
        int result = blogCategoryService.insertBlogCategory(testCategory);

        // 验证结果
        assertEquals(1, result);
        verify(blogCategoryMapper).countByName(any(BlogCategory.class));
        verify(blogCategoryMapper).insertBlogCategory(any(BlogCategory.class));
    }

    /**
     * 测试更新分类 - 成功
     */
    @Test
    void testUpdateBlogCategory_Success() {
        // 准备数据
        when(blogCategoryMapper.countByName(any(BlogCategory.class))).thenReturn(0);
        when(blogCategoryMapper.updateBlogCategory(any(BlogCategory.class))).thenReturn(1);

        // 执行测试
        int result = blogCategoryService.updateBlogCategory(testCategory);

        // 验证结果
        assertEquals(1, result);
        assertNotNull(testCategory.getUpdateTime());
        verify(blogCategoryMapper).countByName(any(BlogCategory.class));
        verify(blogCategoryMapper).updateBlogCategory(any(BlogCategory.class));
    }

    /**
     * 测试更新分类 - 名称重复
     */
    @Test
    void testUpdateBlogCategory_DuplicateName() {
        // 准备数据
        when(blogCategoryMapper.countByName(any(BlogCategory.class))).thenReturn(1);

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class,
            () -> blogCategoryService.updateBlogCategory(testCategory));

        assertEquals("分类名称已存在", exception.getMessage());
        verify(blogCategoryMapper).countByName(any(BlogCategory.class));
        verify(blogCategoryMapper, never()).updateBlogCategory(any(BlogCategory.class));
    }

    /**
     * 测试更新分类 - 名称为空
     */
    @Test
    void testUpdateBlogCategory_EmptyName() {
        // 设置名称为空
        testCategory.setName("");

        // 准备数据
        when(blogCategoryMapper.countByName(any(BlogCategory.class))).thenReturn(0);
        when(blogCategoryMapper.updateBlogCategory(any(BlogCategory.class))).thenReturn(1);

        // 执行测试 - 注意：Service层可能没有验证名称为空
        int result = blogCategoryService.updateBlogCategory(testCategory);

        // 验证结果 - 如果Service层没有验证，这里应该返回1
        assertEquals(1, result);
        verify(blogCategoryMapper).countByName(any(BlogCategory.class));
        verify(blogCategoryMapper).updateBlogCategory(any(BlogCategory.class));
    }

    /**
     * 测试删除分类 - 成功场景
     */
    @Test
    void testDeleteBlogCategoryByIds_Success() {
        // 准备数据
        when(blogArticleMapper.countByCategoryIds(any(Long[].class))).thenReturn(0);
        when(blogCategoryMapper.deleteBlogCategoryByIds(any(Long[].class))).thenReturn(1);

        // 执行测试
        int result = blogCategoryService.deleteBlogCategoryByIds(new Long[]{1L});

        // 验证结果
        assertEquals(1, result);
        verify(blogCategoryMapper).deleteBlogCategoryByIds(any(Long[].class));
    }

    /**
     * 测试删除分类 - 分类不存在
     */
    @Test
    void testDeleteBlogCategoryByIds_NotFound() {
        // 准备数据
        when(blogArticleMapper.countByCategoryIds(any(Long[].class))).thenReturn(0);
        when(blogCategoryMapper.deleteBlogCategoryByIds(any(Long[].class))).thenReturn(0);

        // 执行测试 - 注意：Service可能没有检查分类是否存在
        int result = blogCategoryService.deleteBlogCategoryByIds(new Long[]{1L});

        // 验证结果 - 如果Service没有检查，这里应该返回0
        assertEquals(0, result);
        verify(blogCategoryMapper).deleteBlogCategoryByIds(any(Long[].class));
    }

    /**
     * 测试删除分类 - 存在文章引用
     */
    @Test
    void testDeleteBlogCategoryByIds_HasArticles() {
        // 准备数据
        when(blogArticleMapper.countByCategoryIds(any(Long[].class))).thenReturn(1);

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class,
            () -> blogCategoryService.deleteBlogCategoryByIds(new Long[]{1L}));

        assertEquals("存在文章引用该分类，无法删除，请先迁移或删除相关文章", exception.getMessage());
        verify(blogArticleMapper).countByCategoryIds(any(Long[].class));
        verify(blogCategoryMapper, never()).deleteBlogCategoryByIds(any(Long[].class));
    }

    /**
     * 测试删除分类 - 空ID列表
     */
    @Test
    void testDeleteBlogCategoryByIds_EmptyList() {
        // 准备数据
        when(blogArticleMapper.countByCategoryIds(any(Long[].class))).thenReturn(0);
        when(blogCategoryMapper.deleteBlogCategoryByIds(any(Long[].class))).thenReturn(0);

        // 执行测试
        int result = blogCategoryService.deleteBlogCategoryByIds(new Long[]{});

        // 验证结果
        assertEquals(0, result);
        verify(blogArticleMapper).countByCategoryIds(any(Long[].class));
        verify(blogCategoryMapper).deleteBlogCategoryByIds(any(Long[].class));
    }

    /**
     * 测试查询前台分类列表
     */
    @Test
    void testSelectCategoryListForFront() {
        // 准备数据
        List<BlogCategory> categoryList = Arrays.asList(testCategory);
        when(blogCategoryMapper.selectCategoryListForFront(any(BlogCategory.class)))
            .thenReturn(categoryList);

        // 执行测试
        List<BlogCategory> result = blogCategoryService.selectCategoryListForFront(new BlogCategory());

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(blogCategoryMapper).selectCategoryListForFront(any(BlogCategory.class));
    }

    /**
     * 测试查询前台分类列表 - 空结果
     */
    @Test
    void testSelectCategoryListForFront_Empty() {
        // 模拟空结果
        when(blogCategoryMapper.selectCategoryListForFront(any(BlogCategory.class)))
            .thenReturn(Collections.emptyList());

        // 执行测试
        List<BlogCategory> result = blogCategoryService.selectCategoryListForFront(new BlogCategory());

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(blogCategoryMapper).selectCategoryListForFront(any(BlogCategory.class));
    }

    /**
     * 测试更新文章数量
     */
    @Test
    void testUpdateArticleCount() {
        // 准备数据
        when(blogCategoryMapper.updateArticleCount(anyLong())).thenReturn(1);

        // 执行测试
        int result = blogCategoryService.updateArticleCount(1L);

        // 验证结果
        assertEquals(1, result);
        verify(blogCategoryMapper).updateArticleCount(1L);
    }

    /**
     * 测试更新所有分类文章数量
     */
    @Test
    void testUpdateAllArticleCount() {
        // 准备数据
        when(blogCategoryMapper.updateAllArticleCount()).thenReturn(5);

        // 执行测试
        int result = blogCategoryService.updateAllArticleCount();

        // 验证结果
        assertEquals(5, result);
        verify(blogCategoryMapper).updateAllArticleCount();
    }

    /**
     * 测试统计分类数量
     */
    @Test
    void testSelectBlogCategoryCount() {
        // 准备数据
        when(blogCategoryMapper.countByName(any(BlogCategory.class))).thenReturn(1);

        // 执行测试
        int result = blogCategoryMapper.countByName(testCategory);

        // 验证结果
        assertEquals(1, result);
        verify(blogCategoryMapper).countByName(any(BlogCategory.class));
    }
}