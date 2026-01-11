package com.ruoyi.system.domain;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 博客文章对象单元测试
 *
 * @author test
 * @date 2026-01-11
 */
class BlogArticleTest {

    /**
     * 测试 getDelFlag 方法 - delFlag 为 null
     */
    @Test
    void testGetDelFlag_Null() {
        BlogArticle article = new BlogArticle();
        article.setDelFlag(null);

        Long result = article.getDelFlag();

        // 验证返回默认值 0L
        assertEquals(0L, result);
    }

    /**
     * 测试 getDelFlag 方法 - delFlag 为 0
     */
    @Test
    void testGetDelFlag_Zero() {
        BlogArticle article = new BlogArticle();
        article.setDelFlag(0L);

        Long result = article.getDelFlag();

        // 验证返回 0L
        assertEquals(0L, result);
    }

    /**
     * 测试 getDelFlag 方法 - delFlag 为 1
     */
    @Test
    void testGetDelFlag_One() {
        BlogArticle article = new BlogArticle();
        article.setDelFlag(1L);

        Long result = article.getDelFlag();

        // 验证返回 1L
        assertEquals(1L, result);
    }

    /**
     * 测试 setCoverImage 和 getCoverImage 兼容性方法
     */
    @Test
    void testCoverImageCompatibility() {
        BlogArticle article = new BlogArticle();
        article.setCoverImage("/uploads/cover.jpg");

        // 验证通过 setCoverImage 设置的值可以通过 getCoverUrl 和 getCoverImage 获取
        assertEquals("/uploads/cover.jpg", article.getCoverUrl());
        assertEquals("/uploads/cover.jpg", article.getCoverImage());
    }

    /**
     * 测试 setCoverUrl 和 getCoverUrl 方法
     */
    @Test
    void testCoverUrl() {
        BlogArticle article = new BlogArticle();
        article.setCoverUrl("/uploads/cover.jpg");

        assertEquals("/uploads/cover.jpg", article.getCoverUrl());
    }

    /**
     * 测试 tagIds 列表
     */
    @Test
    void testTagIds() {
        BlogArticle article = new BlogArticle();
        List<Long> tagIds = Arrays.asList(1L, 2L, 3L);
        article.setTagIds(tagIds);

        assertEquals(3, article.getTagIds().size());
        assertEquals(1L, article.getTagIds().get(0));
    }

    /**
     * 测试 tags 列表
     */
    @Test
    void testTags() {
        BlogArticle article = new BlogArticle();
        BlogTag tag1 = new BlogTag();
        tag1.setId(1L);
        tag1.setName("Java");

        BlogTag tag2 = new BlogTag();
        tag2.setId(2L);
        tag2.setName("Spring");

        List<BlogTag> tags = Arrays.asList(tag1, tag2);
        article.setTags(tags);

        assertEquals(2, article.getTags().size());
        assertEquals("Java", article.getTags().get(0).getName());
    }

    /**
     * 测试所有基本属性
     */
    @Test
    void testAllProperties() {
        BlogArticle article = new BlogArticle();
        article.setId(1L);
        article.setTitle("测试文章");
        article.setSummary("这是摘要");
        article.setContent("这是内容");
        article.setCoverUrl("/uploads/cover.jpg");
        article.setCategoryId(1L);
        article.setCategoryName("技术");
        article.setAuthorId(1L);
        article.setAuthor("admin");
        article.setAuthorName("管理员");
        article.setIsTop(1L);
        article.setIsRecommend(1L);
        article.setStatus(1L);
        article.setViewCount(100L);
        article.setLikeCount(50L);
        article.setCommentCount(10L);
        article.setArchiveDate("2026-01");

        assertEquals(1L, article.getId());
        assertEquals("测试文章", article.getTitle());
        assertEquals("这是摘要", article.getSummary());
        assertEquals("这是内容", article.getContent());
        assertEquals("/uploads/cover.jpg", article.getCoverUrl());
        assertEquals(1L, article.getCategoryId());
        assertEquals("技术", article.getCategoryName());
        assertEquals(1L, article.getAuthorId());
        assertEquals("admin", article.getAuthor());
        assertEquals("管理员", article.getAuthorName());
        assertEquals(1L, article.getIsTop());
        assertEquals(1L, article.getIsRecommend());
        assertEquals(1L, article.getStatus());
        assertEquals(100L, article.getViewCount());
        assertEquals(50L, article.getLikeCount());
        assertEquals(10L, article.getCommentCount());
        assertEquals("2026-01", article.getArchiveDate());
    }

    /**
     * 测试 toString 方法
     */
    @Test
    void testToString() {
        BlogArticle article = new BlogArticle();
        article.setId(1L);
        article.setTitle("测试文章");

        String result = article.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("测试文章"));
    }
}