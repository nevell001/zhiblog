package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.BlogArticleTag;
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
 * 文章标签关联Mapper单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class BlogArticleTagMapperTest {

    @Mock
    private BlogArticleTagMapper blogArticleTagMapper;

    private BlogArticleTag testArticleTag;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testArticleTag = new BlogArticleTag();
        testArticleTag.setId(1L);
        testArticleTag.setArticleId(1L);
        testArticleTag.setTagId(1L);
    }

    /**
     * 测试CRUD操作的完整流程
     */
    @Test
    void testCRUDOperations() {
        // 测试新增
        when(blogArticleTagMapper.insertBlogArticleTag(any(BlogArticleTag.class))).thenReturn(1);
        int insertResult = blogArticleTagMapper.insertBlogArticleTag(testArticleTag);
        assertEquals(1, insertResult, "新增文章标签关联失败");
        verify(blogArticleTagMapper).insertBlogArticleTag(any(BlogArticleTag.class));

        // 测试根据ID查询
        when(blogArticleTagMapper.selectBlogArticleTagById(1L)).thenReturn(testArticleTag);
        BlogArticleTag selectResult = blogArticleTagMapper.selectBlogArticleTagById(1L);
        assertNotNull(selectResult, "根据ID查询文章标签关联失败");
        assertEquals(1L, selectResult.getArticleId(), "文章ID不匹配");
        verify(blogArticleTagMapper).selectBlogArticleTagById(1L);

        // 测试修改
        when(blogArticleTagMapper.updateBlogArticleTag(any(BlogArticleTag.class))).thenReturn(1);
        testArticleTag.setTagId(2L);
        int updateResult = blogArticleTagMapper.updateBlogArticleTag(testArticleTag);
        assertEquals(1, updateResult, "修改文章标签关联失败");
        verify(blogArticleTagMapper).updateBlogArticleTag(any(BlogArticleTag.class));

        // 测试查询列表
        when(blogArticleTagMapper.selectBlogArticleTagList(any(BlogArticleTag.class)))
            .thenReturn(Arrays.asList(testArticleTag));
        List<BlogArticleTag> articleTagList = blogArticleTagMapper.selectBlogArticleTagList(new BlogArticleTag());
        assertFalse(articleTagList.isEmpty(), "查询文章标签关联列表失败");
        verify(blogArticleTagMapper).selectBlogArticleTagList(any(BlogArticleTag.class));

        // 测试删除
        when(blogArticleTagMapper.deleteBlogArticleTagById(1L)).thenReturn(1);
        int deleteResult = blogArticleTagMapper.deleteBlogArticleTagById(1L);
        assertEquals(1, deleteResult, "删除文章标签关联失败");
        verify(blogArticleTagMapper).deleteBlogArticleTagById(1L);
    }

    /**
     * 测试批量删除功能
     */
    @Test
    void testDeleteBlogArticleTagByIds() {
        when(blogArticleTagMapper.deleteBlogArticleTagByIds(any(Long[].class))).thenReturn(2);
        int deleteResult = blogArticleTagMapper.deleteBlogArticleTagByIds(new Long[]{1L, 2L});
        assertEquals(2, deleteResult, "批量删除文章标签关联失败");
        verify(blogArticleTagMapper).deleteBlogArticleTagByIds(any(Long[].class));
    }

    /**
     * 测试根据文章ID删除标签关联
     */
    @Test
    void testDeleteByArticleId() {
        when(blogArticleTagMapper.deleteByArticleId(anyLong())).thenReturn(1);
        int result = blogArticleTagMapper.deleteByArticleId(1L);
        assertEquals(1, result, "根据文章ID删除标签关联失败");
        verify(blogArticleTagMapper).deleteByArticleId(anyLong());
    }

    /**
     * 测试边界条件：空值处理
     */
    @Test
    void testNullHandling() {
        // 测试查询不存在的ID
        when(blogArticleTagMapper.selectBlogArticleTagById(999L)).thenReturn(null);
        BlogArticleTag result = blogArticleTagMapper.selectBlogArticleTagById(999L);
        assertNull(result, "查询不存在的ID应返回null");
        verify(blogArticleTagMapper).selectBlogArticleTagById(999L);

        // 测试空列表查询
        when(blogArticleTagMapper.selectBlogArticleTagList(any(BlogArticleTag.class)))
            .thenReturn(Arrays.asList());
        List<BlogArticleTag> list = blogArticleTagMapper.selectBlogArticleTagList(new BlogArticleTag());
        assertTrue(list.isEmpty(), "查询结果应为空列表");
        verify(blogArticleTagMapper).selectBlogArticleTagList(any(BlogArticleTag.class));
    }

    /**
     * 测试边界条件：零ID
     */
    @Test
    void testZeroIds() {
        BlogArticleTag zeroIdArticleTag = new BlogArticleTag();
        zeroIdArticleTag.setArticleId(0L);
        zeroIdArticleTag.setTagId(0L);
        
        when(blogArticleTagMapper.insertBlogArticleTag(any(BlogArticleTag.class))).thenReturn(1);
        int result = blogArticleTagMapper.insertBlogArticleTag(zeroIdArticleTag);
        assertEquals(1, result, "零ID文章标签关联插入失败");
        verify(blogArticleTagMapper).insertBlogArticleTag(any(BlogArticleTag.class));
    }

    /**
     * 测试边界条件：负ID
     */
    @Test
    void testNegativeIds() {
        BlogArticleTag negativeIdArticleTag = new BlogArticleTag();
        negativeIdArticleTag.setArticleId(-1L);
        negativeIdArticleTag.setTagId(-1L);
        
        when(blogArticleTagMapper.insertBlogArticleTag(any(BlogArticleTag.class))).thenReturn(1);
        int result = blogArticleTagMapper.insertBlogArticleTag(negativeIdArticleTag);
        assertEquals(1, result, "负ID文章标签关联插入失败");
        verify(blogArticleTagMapper).insertBlogArticleTag(any(BlogArticleTag.class));
    }

    /**
     * 测试边界条件：大ID值
     */
    @Test
    void testLargeIds() {
        BlogArticleTag largeIdArticleTag = new BlogArticleTag();
        largeIdArticleTag.setArticleId(Long.MAX_VALUE);
        largeIdArticleTag.setTagId(Long.MAX_VALUE);
        
        when(blogArticleTagMapper.insertBlogArticleTag(any(BlogArticleTag.class))).thenReturn(1);
        int result = blogArticleTagMapper.insertBlogArticleTag(largeIdArticleTag);
        assertEquals(1, result, "大ID值文章标签关联插入失败");
        verify(blogArticleTagMapper).insertBlogArticleTag(any(BlogArticleTag.class));
    }

    /**
     * 测试边界条件：同一文章多个标签
     */
    @Test
    void testMultipleTagsForSameArticle() {
        BlogArticleTag tag1 = new BlogArticleTag();
        tag1.setArticleId(1L);
        tag1.setTagId(1L);
        
        BlogArticleTag tag2 = new BlogArticleTag();
        tag2.setArticleId(1L);
        tag2.setTagId(2L);
        
        BlogArticleTag tag3 = new BlogArticleTag();
        tag3.setArticleId(1L);
        tag3.setTagId(3L);
        
        when(blogArticleTagMapper.insertBlogArticleTag(any(BlogArticleTag.class))).thenReturn(1);
        int result1 = blogArticleTagMapper.insertBlogArticleTag(tag1);
        int result2 = blogArticleTagMapper.insertBlogArticleTag(tag2);
        int result3 = blogArticleTagMapper.insertBlogArticleTag(tag3);
        
        assertEquals(1, result1, "标签1插入失败");
        assertEquals(1, result2, "标签2插入失败");
        assertEquals(1, result3, "标签3插入失败");
        
        verify(blogArticleTagMapper, times(3)).insertBlogArticleTag(any(BlogArticleTag.class));
    }

    /**
     * 测试边界条件：同一标签多篇文章
     */
    @Test
    void testMultipleArticlesForSameTag() {
        BlogArticleTag article1 = new BlogArticleTag();
        article1.setArticleId(1L);
        article1.setTagId(1L);
        
        BlogArticleTag article2 = new BlogArticleTag();
        article2.setArticleId(2L);
        article2.setTagId(1L);
        
        BlogArticleTag article3 = new BlogArticleTag();
        article3.setArticleId(3L);
        article3.setTagId(1L);
        
        when(blogArticleTagMapper.insertBlogArticleTag(any(BlogArticleTag.class))).thenReturn(1);
        int result1 = blogArticleTagMapper.insertBlogArticleTag(article1);
        int result2 = blogArticleTagMapper.insertBlogArticleTag(article2);
        int result3 = blogArticleTagMapper.insertBlogArticleTag(article3);
        
        assertEquals(1, result1, "文章1插入失败");
        assertEquals(1, result2, "文章2插入失败");
        assertEquals(1, result3, "文章3插入失败");
        
        verify(blogArticleTagMapper, times(3)).insertBlogArticleTag(any(BlogArticleTag.class));
    }
}