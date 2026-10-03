package com.zhi.system.mapper;

import com.zhi.system.domain.BlogArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 博客文章Mapper单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@MapperScan("com.zhi.system.mapper")
@TestPropertySource(properties = {
    "mybatis.mapperLocations=classpath:mapper/system/BlogArticleMapper.xml",
    "mybatis.typeAliasesPackage=com.zhi.system.domain",
    "spring.sql.init.mode=always"
})
@Sql(scripts = "/schema.sql")
class BlogArticleMapperTest {

    @Autowired
    private BlogArticleMapper blogArticleMapper;

    private BlogArticle testArticle;

    @Test
    void insertBlogArticleShouldLetDatabaseGenerateTimestamps() throws Exception {
        String mapperXml = new String(
                getClass().getResourceAsStream("/mapper/system/BlogArticleMapper.xml").readAllBytes(),
                StandardCharsets.UTF_8);
        int insertStart = mapperXml.indexOf("<insert id=\"insertBlogArticle\"");
        int insertEnd = mapperXml.indexOf("</insert>", insertStart);

        assertTrue(insertStart >= 0, "应存在 insertBlogArticle 映射");
        String insertSql = mapperXml.substring(insertStart, insertEnd);

        assertFalse(insertSql.contains("#{createTime}"), "新增文章不能信任外部 createTime 参数");
        assertFalse(insertSql.contains("#{updateTime}"), "新增文章不能信任外部 updateTime 参数");
        assertTrue(insertSql.contains("NOW()"), "新增文章时间应由数据库当前时间生成");
    }

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testArticle = new BlogArticle();
        testArticle.setTitle("Mapper测试文章标题");
        testArticle.setContent("Mapper测试文章内容");
        testArticle.setStatus(0L);
        testArticle.setCategoryId(1L);
    }

    /**
     * 测试CRUD操作的完整流程
     */
    @Test
    void testCRUDOperations() {
        try {
            // 测试新增
            int insertResult = blogArticleMapper.insertBlogArticle(testArticle);
            assertEquals(1, insertResult, "新增文章失败");
            assertNotNull(testArticle.getId(), "新增文章后ID应该不为空");

            // 测试根据ID查询
            BlogArticle selectResult = blogArticleMapper.selectBlogArticleById(testArticle.getId());
            assertNotNull(selectResult, "根据ID查询文章失败");
            assertEquals("Mapper测试文章标题", selectResult.getTitle(), "文章标题不匹配");

            // 测试根据标题查询
            BlogArticle selectByTitle = blogArticleMapper.selectBlogArticleByTitle("Mapper测试文章标题");
            assertNotNull(selectByTitle, "根据标题查询文章失败");

            // 测试修改
            selectResult.setTitle("修改后的标题");
            int updateResult = blogArticleMapper.updateBlogArticle(selectResult);
            assertEquals(1, updateResult, "修改文章失败");
            BlogArticle updatedArticle = blogArticleMapper.selectBlogArticleById(testArticle.getId());
            assertEquals("修改后的标题", updatedArticle.getTitle(), "文章标题修改失败");

            // 测试查询列表
            BlogArticle queryParam = new BlogArticle();
            queryParam.setTitle("修改后的标题");
            List<BlogArticle> articleList = blogArticleMapper.selectBlogArticleList(queryParam);
            assertFalse(articleList.isEmpty(), "查询文章列表失败");

            // 测试浏览量增加
            Long oldViewCount = updatedArticle.getViewCount() != null ? updatedArticle.getViewCount() : 0L;
            blogArticleMapper.addViewCount(testArticle.getId());
            BlogArticle viewCountArticle = blogArticleMapper.selectBlogArticleById(testArticle.getId());
            assertEquals(oldViewCount + 1, viewCountArticle.getViewCount(), "浏览量增加失败");

            // 测试删除
            int deleteResult = blogArticleMapper.deleteBlogArticleById(testArticle.getId());
            assertEquals(1, deleteResult, "删除文章失败");
            BlogArticle deletedArticle = blogArticleMapper.selectBlogArticleById(testArticle.getId());
            assertNull(deletedArticle, "删除文章后仍然能查询到");
        } catch (Exception e) {
            e.printStackTrace();
            fail("测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试批量删除功能
     */
    @Test
    void testDeleteBlogArticleByIds() {
        try {
            // 新增两篇文章用于测试
            BlogArticle article1 = new BlogArticle();
            article1.setTitle("批量删除测试文章1");
            article1.setContent("内容1");
            article1.setStatus(0L);
            article1.setCategoryId(1L);
            
            BlogArticle article2 = new BlogArticle();
            article2.setTitle("批量删除测试文章2");
            article2.setContent("内容2");
            article2.setStatus(0L);
            article2.setCategoryId(1L);
            
            blogArticleMapper.insertBlogArticle(article1);
            blogArticleMapper.insertBlogArticle(article2);
            
            // 批量删除
            int deleteResult = blogArticleMapper.deleteBlogArticleByIds(new Long[]{article1.getId(), article2.getId()});
            assertTrue(deleteResult >= 1, "批量删除文章失败");
            
            // 验证删除结果
            assertNull(blogArticleMapper.selectBlogArticleById(article1.getId()), "文章1未被删除");
            assertNull(blogArticleMapper.selectBlogArticleById(article2.getId()), "文章2未被删除");
        } catch (Exception e) {
            e.printStackTrace();
            fail("批量删除测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试搜索功能
     */
    @Test
    void testSearchArticles() {
        try {
            // 确保有测试数据 - 注意：搜索查询要求 status = 1（已发布）
            BlogArticle searchArticle = new BlogArticle();
            searchArticle.setTitle("搜索测试文章");
            searchArticle.setContent("这是一篇用于测试搜索功能的文章");
            searchArticle.setStatus(1L);  // 设置为已发布状态
            searchArticle.setCategoryId(1L);

            int insertResult = blogArticleMapper.insertBlogArticle(searchArticle);
            assertEquals(1, insertResult, "插入文章失败");

            // 验证 ID 已生成
            assertNotNull(searchArticle.getId(), "插入后ID应该不为空");

            // 测试搜索
            BlogArticle queryParam = new BlogArticle();
            queryParam.setStatus(1L);  // 搜索已发布的文章
            List<BlogArticle> searchResults = blogArticleMapper.searchArticles("搜索测试", queryParam);

            // 打印调试信息
            System.out.println("插入的文章ID: " + searchArticle.getId());
            System.out.println("搜索结果数量: " + searchResults.size());
            searchResults.forEach(article -> System.out.println("搜索结果: ID=" + article.getId() + ", Title=" + article.getTitle()));

            boolean found = searchResults.stream()
                    .anyMatch(article -> article.getId().equals(searchArticle.getId()));

            assertTrue(found, "搜索功能未找到预期文章");

            // 清理数据
            if (searchArticle.getId() != null) {
                blogArticleMapper.deleteBlogArticleById(searchArticle.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("搜索测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试统计功能
     */
    @Test
    void testStatisticsFunctions() {
        // 测试文章数量统计
        BlogArticle queryParam = new BlogArticle();
        queryParam.setStatus(0L);
        Long articleCount = blogArticleMapper.selectBlogArticleCount(queryParam);
        assertNotNull(articleCount, "文章数量统计失败");
        assertTrue(articleCount >= 0, "文章数量不能为负数");

        // 测试总浏览量统计 - 注意：没有数据时可能返回 null
        Long totalViewCount = blogArticleMapper.selectTotalViewCount();
        // 当没有文章时，SUM 返回 NULL 是正常的
        if (articleCount > 0) {
            assertNotNull(totalViewCount, "有文章时总浏览量不应为null");
            assertTrue(totalViewCount >= 0, "总浏览量不能为负数");
        }

        // 测试平均浏览量统计 - 注意：没有数据时可能返回 null
        Double averageViewCount = blogArticleMapper.selectAverageViewCount();
        // 当没有文章时，AVG 返回 NULL 是正常的
        if (articleCount > 0) {
            assertNotNull(averageViewCount, "有文章时平均浏览量不应为null");
            assertTrue(averageViewCount >= 0, "平均浏览量不能为负数");
        }
    }

    /**
     * 归档查询按创建月份分组，只统计已发布（status=1）且未删除（del_flag=0）的文章，
     * 并按月份倒序返回。DATE_FORMAT 由 schema.sql 里的 H2 别名提供，
     * 所以这条 SQL 是真的被执行、真的在断言结果，而不是被禁用。
     */
    @Test
    @Sql(scripts = "/schema.sql", statements = {
        "DELETE FROM blog_article",
        "INSERT INTO blog_article (title, status, del_flag, create_time) VALUES ('归档-1月A', 1, '0', '2026-01-15 10:00:00')",
        "INSERT INTO blog_article (title, status, del_flag, create_time) VALUES ('归档-1月B', 1, '0', '2026-01-28 10:00:00')",
        "INSERT INTO blog_article (title, status, del_flag, create_time) VALUES ('归档-2月A', 1, '0', '2026-02-03 10:00:00')",
        "INSERT INTO blog_article (title, status, del_flag, create_time) VALUES ('草稿', 0, '0', '2026-02-04 10:00:00')",
        "INSERT INTO blog_article (title, status, del_flag, create_time) VALUES ('已删除', 1, '1', '2026-02-05 10:00:00')"
    })
    void getArticleArchive按创建月份分组并排除草稿与已删除() {
        List<Map<String, Object>> archiveList = blogArticleMapper.getArticleArchive();

        assertEquals(2, archiveList.size(), "应只聚出 2026-01 与 2026-02 两个分组，草稿和已删除文章不参与");
        assertEquals("2026-02", String.valueOf(firstValue(archiveList.get(0), "archive_date")),
                "倒序排列，第一个分组应为最近月份");
        assertEquals(1L, ((Number) firstValue(archiveList.get(0), "article_count")).longValue(),
                "2026-02 只有 1 篇已发布文章");
        assertEquals("2026-01", String.valueOf(firstValue(archiveList.get(1), "archive_date")));
        assertEquals(2L, ((Number) firstValue(archiveList.get(1), "article_count")).longValue(),
                "2026-01 的 2 篇已发布文章应归入同一分组");
    }

    /** H2 返回的列标签是大写，MySQL 是小写，取键时忽略大小写以免绑定数据库实现。 */
    private static Object firstValue(Map<String, Object> row, String column) {
        return row.entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(column))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new AssertionError("归档行缺少列 " + column + "，实际键: " + row.keySet()));
    }
}
