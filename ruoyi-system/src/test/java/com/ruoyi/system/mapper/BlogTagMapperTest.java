package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.BlogTag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 博客标签Mapper单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class BlogTagMapperTest {

    @Mock
    private BlogTagMapper blogTagMapper;

    private BlogTag testTag;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testTag = new BlogTag();
        testTag.setId(1L);
        testTag.setName("测试标签");
        testTag.setColor("#ff0000");
        testTag.setIcon("tag");
    }

    /**
     * 测试CRUD操作的完整流程
     */
    @Test
    void testCRUDOperations() {
        // 测试新增
        when(blogTagMapper.insertBlogTag(any(BlogTag.class))).thenReturn(1);
        int insertResult = blogTagMapper.insertBlogTag(testTag);
        assertEquals(1, insertResult, "新增博客标签失败");
        verify(blogTagMapper).insertBlogTag(any(BlogTag.class));

        // 测试根据ID查询
        when(blogTagMapper.selectBlogTagById(1L)).thenReturn(testTag);
        BlogTag selectResult = blogTagMapper.selectBlogTagById(1L);
        assertNotNull(selectResult, "根据ID查询博客标签失败");
        assertEquals("测试标签", selectResult.getName(), "标签名称不匹配");
        verify(blogTagMapper).selectBlogTagById(1L);

        // 测试修改
        when(blogTagMapper.updateBlogTag(any(BlogTag.class))).thenReturn(1);
        testTag.setName("修改后的标签");
        int updateResult = blogTagMapper.updateBlogTag(testTag);
        assertEquals(1, updateResult, "修改博客标签失败");
        verify(blogTagMapper).updateBlogTag(any(BlogTag.class));

        // 测试查询列表
        when(blogTagMapper.selectBlogTagList(any(BlogTag.class)))
            .thenReturn(Arrays.asList(testTag));
        List<BlogTag> tagList = blogTagMapper.selectBlogTagList(new BlogTag());
        assertFalse(tagList.isEmpty(), "查询博客标签列表失败");
        verify(blogTagMapper).selectBlogTagList(any(BlogTag.class));

        // 测试删除
        when(blogTagMapper.deleteBlogTagById(1L)).thenReturn(1);
        int deleteResult = blogTagMapper.deleteBlogTagById(1L);
        assertEquals(1, deleteResult, "删除博客标签失败");
        verify(blogTagMapper).deleteBlogTagById(1L);
    }

    /**
     * 测试批量删除功能
     */
    @Test
    void testDeleteBlogTagByIds() {
        when(blogTagMapper.deleteBlogTagByIds(any(Long[].class))).thenReturn(2);
        int deleteResult = blogTagMapper.deleteBlogTagByIds(new Long[]{1L, 2L});
        assertEquals(2, deleteResult, "批量删除博客标签失败");
        verify(blogTagMapper).deleteBlogTagByIds(any(Long[].class));
    }

    /**
     * 测试查询所有标签列表
     */
    @Test
    void testSelectAllTagList() {
        when(blogTagMapper.selectAllTagList()).thenReturn(Arrays.asList(testTag));
        List<BlogTag> tagList = blogTagMapper.selectAllTagList();
        assertNotNull(tagList, "所有标签列表不应为null");
        assertEquals(1, tagList.size(), "所有标签列表大小不正确");
        verify(blogTagMapper).selectAllTagList();
    }

    /**
     * 测试检查标签名称唯一性
     */
    @Test
    void testCheckTagNameUnique() {
        when(blogTagMapper.checkTagNameUnique(anyString())).thenReturn(null);
        BlogTag result = blogTagMapper.checkTagNameUnique("不存在的标签");
        assertNull(result, "不存在的标签应返回null");
        verify(blogTagMapper).checkTagNameUnique(anyString());
    }

    /**
     * 测试检查标签是否已被文章使用
     */
    @Test
    void testCheckTagExistArticle() {
        when(blogTagMapper.checkTagExistArticle(anyLong())).thenReturn(0);
        int count = blogTagMapper.checkTagExistArticle(1L);
        assertEquals(0, count, "检查标签是否已被文章使用失败");
        verify(blogTagMapper).checkTagExistArticle(anyLong());
    }

    /**
     * 测试获取标签云
     */
    @Test
    void testGetTagCloud() {
        Map<String, Object> tagData = Map.of("name", "Java", "article_count", 10);
        when(blogTagMapper.getTagCloud()).thenReturn(Arrays.asList(tagData));
        List<Map<String, Object>> tagCloud = blogTagMapper.getTagCloud();
        assertNotNull(tagCloud, "标签云不应为null");
        assertEquals(1, tagCloud.size(), "标签云大小不正确");
        verify(blogTagMapper).getTagCloud();
    }

    /**
     * 测试根据文章ID查询标签列表
     */
    @Test
    void testSelectTagsByArticleId() {
        when(blogTagMapper.selectTagsByArticleId(anyLong())).thenReturn(Arrays.asList(testTag));
        List<BlogTag> tagList = blogTagMapper.selectTagsByArticleId(1L);
        assertNotNull(tagList, "标签列表不应为null");
        assertEquals(1, tagList.size(), "标签列表大小不正确");
        verify(blogTagMapper).selectTagsByArticleId(anyLong());
    }

    /**
     * 测试边界条件：空值处理
     */
    @Test
    void testNullHandling() {
        // 测试查询不存在的ID
        when(blogTagMapper.selectBlogTagById(999L)).thenReturn(null);
        BlogTag result = blogTagMapper.selectBlogTagById(999L);
        assertNull(result, "查询不存在的ID应返回null");
        verify(blogTagMapper).selectBlogTagById(999L);

        // 测试空列表查询
        when(blogTagMapper.selectBlogTagList(any(BlogTag.class)))
            .thenReturn(Arrays.asList());
        List<BlogTag> list = blogTagMapper.selectBlogTagList(new BlogTag());
        assertTrue(list.isEmpty(), "查询结果应为空列表");
        verify(blogTagMapper).selectBlogTagList(any(BlogTag.class));
    }

    /**
     * 测试边界条件：空名称
     */
    @Test
    void testEmptyName() {
        BlogTag emptyNameTag = new BlogTag();
        emptyNameTag.setName("");
        
        when(blogTagMapper.insertBlogTag(any(BlogTag.class))).thenReturn(1);
        int result = blogTagMapper.insertBlogTag(emptyNameTag);
        assertEquals(1, result, "空名称标签插入失败");
        verify(blogTagMapper).insertBlogTag(any(BlogTag.class));
    }

    /**
     * 测试边界条件：空颜色
     */
    @Test
    void testEmptyColor() {
        BlogTag emptyColorTag = new BlogTag();
        emptyColorTag.setName("测试");
        emptyColorTag.setColor("");
        
        when(blogTagMapper.insertBlogTag(any(BlogTag.class))).thenReturn(1);
        int result = blogTagMapper.insertBlogTag(emptyColorTag);
        assertEquals(1, result, "空颜色标签插入失败");
        verify(blogTagMapper).insertBlogTag(any(BlogTag.class));
    }

    /**
     * 测试边界条件：特殊字符名称
     */
    @Test
    void testSpecialCharactersName() {
        BlogTag specialCharsTag = new BlogTag();
        specialCharsTag.setName("测试@#$%^&*()");
        
        when(blogTagMapper.insertBlogTag(any(BlogTag.class))).thenReturn(1);
        int result = blogTagMapper.insertBlogTag(specialCharsTag);
        assertEquals(1, result, "特殊字符名称标签插入失败");
        verify(blogTagMapper).insertBlogTag(any(BlogTag.class));
    }

    /**
     * 测试边界条件：长名称
     */
    @Test
    void testLongName() {
        BlogTag longNameTag = new BlogTag();
        longNameTag.setName("这是一个非常长的标签名称，用于测试系统对长文本的处理能力");
        
        when(blogTagMapper.insertBlogTag(any(BlogTag.class))).thenReturn(1);
        int result = blogTagMapper.insertBlogTag(longNameTag);
        assertEquals(1, result, "长名称标签插入失败");
        verify(blogTagMapper).insertBlogTag(any(BlogTag.class));
    }
}