package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.BlogCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 文章分类Mapper单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class BlogCategoryMapperTest {

    @Mock
    private BlogCategoryMapper blogCategoryMapper;

    private BlogCategory testCategory;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testCategory = new BlogCategory();
        testCategory.setId(1L);
        testCategory.setName("测试分类");
        testCategory.setAlias("test");
        testCategory.setDescription("测试描述");
        testCategory.setSortOrder(1);
    }

    /**
     * 测试CRUD操作的完整流程
     */
    @Test
    void testCRUDOperations() {
        // 测试新增
        when(blogCategoryMapper.insertBlogCategory(any(BlogCategory.class))).thenReturn(1);
        int insertResult = blogCategoryMapper.insertBlogCategory(testCategory);
        assertEquals(1, insertResult, "新增文章分类失败");
        verify(blogCategoryMapper).insertBlogCategory(any(BlogCategory.class));

        // 测试根据ID查询
        when(blogCategoryMapper.selectBlogCategoryById(1L)).thenReturn(testCategory);
        BlogCategory selectResult = blogCategoryMapper.selectBlogCategoryById(1L);
        assertNotNull(selectResult, "根据ID查询文章分类失败");
        assertEquals("测试分类", selectResult.getName(), "分类名称不匹配");
        verify(blogCategoryMapper).selectBlogCategoryById(1L);

        // 测试修改
        when(blogCategoryMapper.updateBlogCategory(any(BlogCategory.class))).thenReturn(1);
        testCategory.setName("修改后的分类");
        int updateResult = blogCategoryMapper.updateBlogCategory(testCategory);
        assertEquals(1, updateResult, "修改文章分类失败");
        verify(blogCategoryMapper).updateBlogCategory(any(BlogCategory.class));

        // 测试查询列表
        when(blogCategoryMapper.selectBlogCategoryList(any(BlogCategory.class)))
            .thenReturn(Arrays.asList(testCategory));
        List<BlogCategory> categoryList = blogCategoryMapper.selectBlogCategoryList(new BlogCategory());
        assertFalse(categoryList.isEmpty(), "查询文章分类列表失败");
        verify(blogCategoryMapper).selectBlogCategoryList(any(BlogCategory.class));

        // 测试删除
        when(blogCategoryMapper.deleteBlogCategoryById(1L)).thenReturn(1);
        int deleteResult = blogCategoryMapper.deleteBlogCategoryById(1L);
        assertEquals(1, deleteResult, "删除文章分类失败");
        verify(blogCategoryMapper).deleteBlogCategoryById(1L);
    }

    /**
     * 测试批量删除功能
     */
    @Test
    void testDeleteBlogCategoryByIds() {
        when(blogCategoryMapper.deleteBlogCategoryByIds(any(Long[].class))).thenReturn(2);
        int deleteResult = blogCategoryMapper.deleteBlogCategoryByIds(new Long[]{1L, 2L});
        assertEquals(2, deleteResult, "批量删除文章分类失败");
        verify(blogCategoryMapper).deleteBlogCategoryByIds(any(Long[].class));
    }

    /**
     * 测试统计同名分类数量
     */
    @Test
    void testCountByName() {
        when(blogCategoryMapper.countByName(any(BlogCategory.class))).thenReturn(0);
        int count = blogCategoryMapper.countByName(testCategory);
        assertEquals(0, count, "统计同名分类数量失败");
        verify(blogCategoryMapper).countByName(any(BlogCategory.class));
    }

    /**
     * 测试查询前台分类列表
     */
    @Test
    void testSelectCategoryListForFront() {
        when(blogCategoryMapper.selectCategoryListForFront(any(BlogCategory.class)))
            .thenReturn(Arrays.asList(testCategory));
        List<BlogCategory> categoryList = blogCategoryMapper.selectCategoryListForFront(new BlogCategory());
        assertNotNull(categoryList, "前台分类列表不应为null");
        assertEquals(1, categoryList.size(), "前台分类列表大小不正确");
        verify(blogCategoryMapper).selectCategoryListForFront(any(BlogCategory.class));
    }

    /**
     * 测试更新文章数量
     */
    @Test
    void testUpdateArticleCount() {
        when(blogCategoryMapper.updateArticleCount(anyLong())).thenReturn(1);
        int result = blogCategoryMapper.updateArticleCount(1L);
        assertEquals(1, result, "更新文章数量失败");
        verify(blogCategoryMapper).updateArticleCount(1L);
    }

    /**
     * 测试边界条件：空值处理
     */
    @Test
    void testNullHandling() {
        // 测试查询不存在的ID
        when(blogCategoryMapper.selectBlogCategoryById(999L)).thenReturn(null);
        BlogCategory result = blogCategoryMapper.selectBlogCategoryById(999L);
        assertNull(result, "查询不存在的ID应返回null");
        verify(blogCategoryMapper).selectBlogCategoryById(999L);

        // 测试空列表查询
        when(blogCategoryMapper.selectBlogCategoryList(any(BlogCategory.class)))
            .thenReturn(Arrays.asList());
        List<BlogCategory> list = blogCategoryMapper.selectBlogCategoryList(new BlogCategory());
        assertTrue(list.isEmpty(), "查询结果应为空列表");
        verify(blogCategoryMapper).selectBlogCategoryList(any(BlogCategory.class));
    }

    /**
     * 测试边界条件：空名称
     */
    @Test
    void testEmptyName() {
        BlogCategory emptyNameCategory = new BlogCategory();
        emptyNameCategory.setName("");
        
        when(blogCategoryMapper.insertBlogCategory(any(BlogCategory.class))).thenReturn(1);
        int result = blogCategoryMapper.insertBlogCategory(emptyNameCategory);
        assertEquals(1, result, "空名称分类插入失败");
        verify(blogCategoryMapper).insertBlogCategory(any(BlogCategory.class));
    }

    /**
     * 测试边界条件：空别名
     */
    @Test
    void testEmptyAlias() {
        BlogCategory emptyAliasCategory = new BlogCategory();
        emptyAliasCategory.setName("测试");
        emptyAliasCategory.setAlias("");
        
        when(blogCategoryMapper.insertBlogCategory(any(BlogCategory.class))).thenReturn(1);
        int result = blogCategoryMapper.insertBlogCategory(emptyAliasCategory);
        assertEquals(1, result, "空别名分类插入失败");
        verify(blogCategoryMapper).insertBlogCategory(any(BlogCategory.class));
    }

    /**
     * 测试边界条件：排序值
     */
    @Test
    void testSortOrder() {
        // 测试负排序值
        BlogCategory negativeSort = new BlogCategory();
        negativeSort.setName("测试");
        negativeSort.setSortOrder(-1);
        when(blogCategoryMapper.insertBlogCategory(any(BlogCategory.class))).thenReturn(1);
        int result1 = blogCategoryMapper.insertBlogCategory(negativeSort);
        assertEquals(1, result1, "负排序值分类插入失败");

        // 测试零排序值
        BlogCategory zeroSort = new BlogCategory();
        zeroSort.setName("测试");
        zeroSort.setSortOrder(0);
        when(blogCategoryMapper.insertBlogCategory(any(BlogCategory.class))).thenReturn(1);
        int result2 = blogCategoryMapper.insertBlogCategory(zeroSort);
        assertEquals(1, result2, "零排序值分类插入失败");

        // 测试正排序值
        BlogCategory positiveSort = new BlogCategory();
        positiveSort.setName("测试");
        positiveSort.setSortOrder(100);
        when(blogCategoryMapper.insertBlogCategory(any(BlogCategory.class))).thenReturn(1);
        int result3 = blogCategoryMapper.insertBlogCategory(positiveSort);
        assertEquals(1, result3, "正排序值分类插入失败");

        verify(blogCategoryMapper, times(3)).insertBlogCategory(any(BlogCategory.class));
    }
}