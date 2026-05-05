package com.ruoyi.system.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 博客统计控制器单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@WebMvcTest(controllers = BlogStatisticsController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class BlogStatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private com.ruoyi.system.service.IBlogArticleService blogArticleService;

    @MockBean
    private com.ruoyi.system.service.IBlogCategoryService blogCategoryService;

    @MockBean
    private com.ruoyi.system.service.IBlogTagService blogTagService;

    @MockBean
    private com.ruoyi.system.service.IBlogCommentService blogCommentService;

    @MockBean
    private com.ruoyi.system.service.ISysUserService userService;

    @MockBean
    private com.ruoyi.system.service.ISysLogininforService logininforService;

    /**
     * 测试获取数据概览统计接口
     */
    @Test
    void testGetStatisticsOverview() throws Exception {
        // 模拟数据
        when(blogArticleService.selectBlogArticleCount(any(com.ruoyi.system.domain.BlogArticle.class)))
            .thenReturn(100L);
        when(userService.selectUserCount(any(com.ruoyi.common.core.domain.entity.SysUser.class)))
            .thenReturn(50L);
        when(blogCommentService.selectBlogCommentCount(any(com.ruoyi.system.domain.BlogComment.class)))
            .thenReturn(200L);
        when(blogArticleService.selectTotalViewCount()).thenReturn(1000L);
        when(userService.selectOnlineUserCount(anyInt())).thenReturn(10L);
        when(logininforService.selectTodayLoginCount()).thenReturn(50L);

        // 执行测试
        mockMvc.perform(get("/statistics/overview")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.articleCount").value(100))
                .andExpect(jsonPath("$.data.userCount").value(50))
                .andExpect(jsonPath("$.data.commentCount").value(200))
                .andExpect(jsonPath("$.data.viewCount").value(1000))
                .andExpect(jsonPath("$.data.onlineUsers").value(10))
                .andExpect(jsonPath("$.data.todayVisits").value(50));
    }

    /**
     * 测试获取文章统计接口
     */
    @Test
    void testGetArticleStatistics() throws Exception {
        // 模拟数据
        when(blogArticleService.selectBlogArticleCount(any(com.ruoyi.system.domain.BlogArticle.class)))
            .thenReturn(80L);
        when(blogArticleService.selectAverageViewCount()).thenReturn(15.5);

        // 执行测试
        mockMvc.perform(get("/statistics/article")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.publishedCount").value(80))
                .andExpect(jsonPath("$.data.draftCount").value(80))
                .andExpect(jsonPath("$.data.avgViews").value(15.5));
    }

    /**
     * 测试获取用户统计接口
     */
    @Test
    void testGetUserStatistics() throws Exception {
        // 模拟数据
        when(userService.selectUserCount(any(com.ruoyi.common.core.domain.entity.SysUser.class)))
            .thenReturn(100L);
        when(userService.selectActiveUserCount(anyInt())).thenReturn(60L);
        when(userService.selectNewUserCount(anyInt())).thenReturn(20L);
        when(userService.selectAdminUserCount()).thenReturn(5L);

        // 执行测试
        mockMvc.perform(get("/statistics/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalCount").value(100))
                .andExpect(jsonPath("$.data.activeCount").value(60))
                .andExpect(jsonPath("$.data.newCount").value(20))
                .andExpect(jsonPath("$.data.adminCount").value(5));
    }

    /**
     * 测试获取文章发布趋势接口
     */
    @Test
    void testGetArticleTrend() throws Exception {
        // 模拟数据
        List<Map<String, Object>> trendData = new ArrayList<>();
        Map<String, Object> month1 = new HashMap<>();
        month1.put("month", "2025-01");
        month1.put("count", 10);
        trendData.add(month1);

        Map<String, Object> month2 = new HashMap<>();
        month2.put("month", "2025-02");
        month2.put("count", 15);
        trendData.add(month2);

        when(blogArticleService.selectArticleTrend()).thenReturn(trendData);

        // 执行测试
        mockMvc.perform(get("/statistics/article/trend")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.labels").isArray())
                .andExpect(jsonPath("$.data.data").isArray());
    }

    /**
     * 测试获取用户活跃度接口
     */
    @Test
    void testGetUserActivity() throws Exception {
        // 模拟数据
        List<Map<String, Object>> activityData = new ArrayList<>();
        Map<String, Object> month1 = new HashMap<>();
        month1.put("month", "2025-01");
        month1.put("count", 50);
        activityData.add(month1);

        when(logininforService.selectUserActivity()).thenReturn(activityData);

        // 执行测试
        mockMvc.perform(get("/statistics/user/activity")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.labels").isArray())
                .andExpect(jsonPath("$.data.data").isArray());
    }

    /**
     * 测试获取用户注册趋势接口
     */
    @Test
    void testGetUserRegisterTrend() throws Exception {
        // 执行测试
        mockMvc.perform(get("/statistics/user/register-trend")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.labels").isArray())
                .andExpect(jsonPath("$.data.data").isArray());
    }

    /**
     * 测试获取用户角色分布接口
     */
    @Test
    void testGetUserRoleDistribution() throws Exception {
        // 执行测试
        mockMvc.perform(get("/statistics/user/role-distribution")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.labels").isArray())
                .andExpect(jsonPath("$.data.data").isArray());
    }

    /**
     * 测试获取文章分类分布接口
     */
    @Test
    void testGetArticleCategoryDistribution() throws Exception {
        // 模拟数据
        List<com.ruoyi.system.domain.BlogCategory> categoryList = new ArrayList<>();
        com.ruoyi.system.domain.BlogCategory category = new com.ruoyi.system.domain.BlogCategory();
        category.setName("技术");
        category.setArticleCount(20);
        categoryList.add(category);

        when(blogCategoryService.selectCategoryListForFront(any(com.ruoyi.system.domain.BlogCategory.class)))
            .thenReturn(categoryList);

        // 执行测试
        mockMvc.perform(get("/statistics/article/category-distribution")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.labels").isArray())
                .andExpect(jsonPath("$.data.data").isArray());
    }

    /**
     * 测试获取热门标签接口
     */
    @Test
    void testGetHotTags() throws Exception {
        // 模拟数据
        List<Map<String, Object>> tagList = new ArrayList<>();
        Map<String, Object> tag = new HashMap<>();
        tag.put("name", "Java");
        tag.put("article_count", 15);
        tagList.add(tag);

        when(blogTagService.getTagCloud()).thenReturn(tagList);

        // 执行测试
        mockMvc.perform(get("/statistics/article/hot-tags")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.labels").isArray())
                .andExpect(jsonPath("$.data.data").isArray());
    }

    /**
     * 测试获取文章发布趋势接口 - 空数据
     */
    @Test
    void testGetArticleTrend_Empty() throws Exception {
        // 模拟空数据
        when(blogArticleService.selectArticleTrend()).thenReturn(new ArrayList<>());

        // 执行测试
        mockMvc.perform(get("/statistics/article/trend")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.labels").isArray())
                .andExpect(jsonPath("$.data.data").isArray());
    }

    /**
     * 测试获取用户活跃度接口 - 空数据
     */
    @Test
    void testGetUserActivity_Empty() throws Exception {
        // 模拟空数据
        when(logininforService.selectUserActivity()).thenReturn(new ArrayList<>());

        // 执行测试
        mockMvc.perform(get("/statistics/user/activity")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.labels").isArray())
                .andExpect(jsonPath("$.data.data").isArray());
    }

    /**
     * 测试获取文章分类分布接口 - 空数据
     */
    @Test
    void testGetArticleCategoryDistribution_Empty() throws Exception {
        // 模拟空数据
        when(blogCategoryService.selectCategoryListForFront(any(com.ruoyi.system.domain.BlogCategory.class)))
            .thenReturn(new ArrayList<>());

        // 执行测试
        mockMvc.perform(get("/statistics/article/category-distribution")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.labels").isArray())
                .andExpect(jsonPath("$.data.data").isArray());
    }

    /**
     * 测试获取热门标签接口 - 空数据
     */
    @Test
    void testGetHotTags_Empty() throws Exception {
        // 模拟空数据
        when(blogTagService.getTagCloud()).thenReturn(new ArrayList<>());

        // 执行测试
        mockMvc.perform(get("/statistics/article/hot-tags")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.labels").isArray())
                .andExpect(jsonPath("$.data.data").isArray());
    }

    /**
     * 测试获取文章统计接口 - 平均浏览量为 null
     */
    @Test
    void testGetArticleStatistics_AvgViewsNull() throws Exception {
        // 模拟平均浏览量为 null
        when(blogArticleService.selectBlogArticleCount(any(com.ruoyi.system.domain.BlogArticle.class)))
            .thenReturn(80L);
        when(blogArticleService.selectAverageViewCount()).thenReturn(null);

        // 执行测试
        mockMvc.perform(get("/statistics/article")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.publishedCount").value(80))
                .andExpect(jsonPath("$.data.draftCount").value(80))
                .andExpect(jsonPath("$.data.avgViews").value(0.0));
    }

    /**
     * 测试获取用户统计接口 - null 值
     */
    @Test
    void testGetUserStatistics_NullValues() throws Exception {
        // 模拟 null 值
        when(userService.selectUserCount(any(com.ruoyi.common.core.domain.entity.SysUser.class)))
            .thenReturn(0L);
        when(userService.selectActiveUserCount(anyInt())).thenReturn(0L);
        when(userService.selectNewUserCount(anyInt())).thenReturn(0L);
        when(userService.selectAdminUserCount()).thenReturn(0L);

        // 执行测试
        mockMvc.perform(get("/statistics/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalCount").value(0))
                .andExpect(jsonPath("$.data.activeCount").value(0))
                .andExpect(jsonPath("$.data.newCount").value(0))
                .andExpect(jsonPath("$.data.adminCount").value(0));
    }
}