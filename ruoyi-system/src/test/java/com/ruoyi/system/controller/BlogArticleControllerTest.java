package com.ruoyi.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.system.TestApplication;
import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.service.IBlogArticleService;
import com.ruoyi.system.service.IBlogCategoryService;
import com.ruoyi.system.service.IBlogTagService;
import org.junit.jupiter.api.BeforeEach;
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
 * 博客文章控制器单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@WebMvcTest(controllers = BlogArticleController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)  // 禁用安全过滤器，跳过权限检查
class BlogArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBlogArticleService blogArticleService;

    @MockBean
    private IBlogCategoryService blogCategoryService;

    @MockBean
    private IBlogTagService blogTagService;

    private ObjectMapper objectMapper;
    private BlogArticle testArticle;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // 初始化测试数据
        testArticle = new BlogArticle();
        testArticle.setId(1L);
        testArticle.setTitle("测试文章标题");
        testArticle.setContent("测试文章内容");
        testArticle.setStatus(0L);
    }

    /**
     * 测试获取文章列表接口
     */
    @Test
    void testGetArticleList() throws Exception {
        // 模拟数据
        List<BlogArticle> articleList = new ArrayList<>();
        articleList.add(testArticle);
        when(blogArticleService.selectBlogArticleList(any(BlogArticle.class))).thenReturn(articleList);

        // 执行测试
        mockMvc.perform(get("/system/article/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows[0].title").value("测试文章标题"));

        verify(blogArticleService).selectBlogArticleList(any(BlogArticle.class));
    }

    /**
     * 测试获取文章详情接口
     */
    @Test
    void testGetArticleDetail() throws Exception {
        // 模拟数据
        when(blogArticleService.selectBlogArticleById(1L)).thenReturn(testArticle);

        // 执行测试 - 使用正确的端点路径 /{id}
        mockMvc.perform(get("/system/article/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.title").value("测试文章标题"));

        verify(blogArticleService).selectBlogArticleById(1L);
    }

    /**
     * 测试新增文章接口
     */
    @Test
    void testAddArticle() throws Exception {
        // 模拟成功添加
        when(blogArticleService.insertBlogArticle(any(BlogArticle.class))).thenReturn(1);

        // 准备请求体 - 使用Map因为实际接口接收Map参数
        Map<String, Object> params = new HashMap<>();
        params.put("title", "测试文章标题");
        params.put("content", "测试文章内容");
        params.put("status", 0L);

        // 执行测试 - 使用正确的端点路径 POST /
        mockMvc.perform(post("/system/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogArticleService).insertBlogArticle(any(BlogArticle.class));
    }

    /**
     * 测试更新文章接口
     */
    @Test
    void testEditArticle() throws Exception {
        // 模拟成功更新
        when(blogArticleService.updateBlogArticle(any(BlogArticle.class))).thenReturn(1);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("title", "测试文章标题");
        params.put("content", "测试文章内容");
        params.put("status", 0L);

        // 执行测试 - 使用正确的端点路径 PUT /
        mockMvc.perform(put("/system/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogArticleService).updateBlogArticle(any(BlogArticle.class));
    }

    /**
     * 测试删除文章接口
     */
    @Test
    void testRemoveArticle() throws Exception {
        // 模拟成功删除
        when(blogArticleService.deleteBlogArticleByIds(any(Long[].class))).thenReturn(1);

        // 执行测试 - 使用正确的端点路径 DELETE /{ids}
        mockMvc.perform(delete("/system/article/1,2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogArticleService).deleteBlogArticleByIds(any(Long[].class));
    }

    /**
     * 测试更新文章状态接口
     */
    @Test
    void testChangeStatus() throws Exception {
        // 模拟成功更新状态
        when(blogArticleService.updateArticleStatus(anyList(), anyInt())).thenReturn(1);

        // 准备请求体
        Map<String, Object> articleStatus = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        articleStatus.put("ids", ids);
        articleStatus.put("status", 1);

        // 执行测试 - 使用正确的端点路径 PUT /status
        mockMvc.perform(put("/system/article/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleStatus)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogArticleService).updateArticleStatus(anyList(), anyInt());
    }

    /**
     * 测试搜索文章接口
     */
    @Test
    void testSearch() throws Exception {
        // 模拟数据
        List<BlogArticle> articleList = new ArrayList<>();
        articleList.add(testArticle);
        when(blogArticleService.searchArticles(anyString(), any(BlogArticle.class))).thenReturn(articleList);

        // 执行测试
        mockMvc.perform(get("/system/article/search")
                .param("keyword", "测试")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows[0].title").value("测试文章标题"));

        verify(blogArticleService).searchArticles(anyString(), any(BlogArticle.class));
    }

    /**
     * 测试获取分类和标签选项接口
     */
    @Test
    void testGetOptions() throws Exception {
        // 模拟数据
        List<com.ruoyi.system.domain.BlogCategory> categories = new ArrayList<>();
        com.ruoyi.system.domain.BlogCategory category = new com.ruoyi.system.domain.BlogCategory();
        category.setId(1L);
        category.setName("测试分类");
        categories.add(category);

        List<com.ruoyi.system.domain.BlogTag> tags = new ArrayList<>();
        com.ruoyi.system.domain.BlogTag tag = new com.ruoyi.system.domain.BlogTag();
        tag.setId(1L);
        tag.setTagName("测试标签");
        tags.add(tag);

        when(blogCategoryService.selectBlogCategoryList(any(com.ruoyi.system.domain.BlogCategory.class))).thenReturn(categories);
        when(blogTagService.selectBlogTagList(any(com.ruoyi.system.domain.BlogTag.class))).thenReturn(tags);

        // 执行测试
        mockMvc.perform(get("/system/article/options")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.categories").isArray())
                .andExpect(jsonPath("$.data.tags").isArray());

        verify(blogCategoryService).selectBlogCategoryList(any(com.ruoyi.system.domain.BlogCategory.class));
        verify(blogTagService).selectBlogTagList(any(com.ruoyi.system.domain.BlogTag.class));
    }

    /**
     * 测试根据分类获取文章接口
     */
    @Test
    void testGetByCategory() throws Exception {
        // 模拟数据
        List<BlogArticle> articleList = new ArrayList<>();
        articleList.add(testArticle);
        when(blogArticleService.selectBlogArticleList(any(BlogArticle.class))).thenReturn(articleList);

        // 执行测试
        mockMvc.perform(get("/system/article/category/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows").isArray());

        verify(blogArticleService).selectBlogArticleList(any(BlogArticle.class));
    }

    /**
     * 测试根据分类获取文章接口 - 空结果
     */
    @Test
    void testGetByCategory_Empty() throws Exception {
        // 模拟空结果
        when(blogArticleService.selectBlogArticleList(any(BlogArticle.class))).thenReturn(new ArrayList<>());

        // 执行测试
        mockMvc.perform(get("/system/article/category/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows").isEmpty());

        verify(blogArticleService).selectBlogArticleList(any(BlogArticle.class));
    }

    /**
     * 测试根据标签获取文章接口
     */
    @Test
    void testGetByTag() throws Exception {
        // 模拟数据
        List<BlogArticle> articleList = new ArrayList<>();
        articleList.add(testArticle);
        when(blogArticleService.selectArticlesByTagId(1L)).thenReturn(articleList);

        // 执行测试
        mockMvc.perform(get("/system/article/tag/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows").isArray())
                .andExpect(jsonPath("$.rows[0].title").value("测试文章标题"));

        verify(blogArticleService).selectArticlesByTagId(1L);
    }

    /**
     * 测试根据标签获取文章接口 - 空结果
     */
    @Test
    void testGetByTag_Empty() throws Exception {
        // 模拟空结果
        when(blogArticleService.selectArticlesByTagId(1L)).thenReturn(new ArrayList<>());

        // 执行测试
        mockMvc.perform(get("/system/article/tag/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows").isEmpty());

        verify(blogArticleService).selectArticlesByTagId(1L);
    }

    /**
     * 测试获取文章详情接口 - 文章不存在
     */
    @Test
    void testGetArticleDetail_NotFound() throws Exception {
        // 模拟文章不存在
        when(blogArticleService.selectBlogArticleById(999L)).thenReturn(null);

        // 执行测试
        mockMvc.perform(get("/system/article/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));

        verify(blogArticleService).selectBlogArticleById(999L);
    }

    /**
     * 测试新增文章接口 - 失败
     */
    @Test
    void testAddArticle_Failure() throws Exception {
        // 模拟添加失败
        when(blogArticleService.insertBlogArticle(any(BlogArticle.class))).thenReturn(0);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("title", "测试文章标题");
        params.put("content", "测试文章内容");
        params.put("status", 0L);

        // 执行测试
        mockMvc.perform(post("/system/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));

        verify(blogArticleService).insertBlogArticle(any(BlogArticle.class));
    }

    /**
     * 测试更新文章接口 - 失败
     */
    @Test
    void testEditArticle_Failure() throws Exception {
        // 模拟更新失败
        when(blogArticleService.updateBlogArticle(any(BlogArticle.class))).thenReturn(0);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("title", "测试文章标题");
        params.put("content", "测试文章内容");
        params.put("status", 0L);

        // 执行测试
        mockMvc.perform(put("/system/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));

        verify(blogArticleService).updateBlogArticle(any(BlogArticle.class));
    }

    /**
     * 测试更新文章状态接口 - 空ID列表
     */
    @Test
    void testChangeStatus_EmptyIds() throws Exception {
        // 准备请求体 - 空ID列表
        Map<String, Object> articleStatus = new HashMap<>();
        articleStatus.put("ids", new ArrayList<>());
        articleStatus.put("status", 1);

        // 执行测试
        mockMvc.perform(put("/system/article/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleStatus)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    /**
     * 测试搜索文章接口 - 空结果
     */
    @Test
    void testSearch_Empty() throws Exception {
        // 模拟空结果
        when(blogArticleService.searchArticles(anyString(), any(BlogArticle.class))).thenReturn(new ArrayList<>());

        // 执行测试
        mockMvc.perform(get("/system/article/search")
                .param("keyword", "不存在的关键词")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows").isEmpty());

        verify(blogArticleService).searchArticles(anyString(), any(BlogArticle.class));
    }

    /**
     * 测试新增文章接口 - 带标签
     */
    @Test
    void testAddArticle_WithTags() throws Exception {
        // 模拟成功添加
        when(blogArticleService.insertBlogArticle(any(BlogArticle.class))).thenReturn(1);

        // 准备请求体 - 包含标签
        Map<String, Object> params = new HashMap<>();
        params.put("title", "测试文章标题");
        params.put("content", "测试文章内容");
        params.put("status", 0L);
        List<Long> tagIds = new ArrayList<>();
        tagIds.add(1L);
        tagIds.add(2L);
        params.put("tagIds", tagIds);

        // 执行测试
        mockMvc.perform(post("/system/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogArticleService).insertBlogArticle(any(BlogArticle.class));
    }

    /**
     * 测试新增文章接口 - 带标签（Integer类型）
     */
    @Test
    void testAddArticle_WithTagsInteger() throws Exception {
        // 模拟成功添加
        when(blogArticleService.insertBlogArticle(any(BlogArticle.class))).thenReturn(1);

        // 准备请求体 - 包含标签（Integer类型）
        Map<String, Object> params = new HashMap<>();
        params.put("title", "测试文章标题");
        params.put("content", "测试文章内容");
        params.put("status", 0L);
        List<Integer> tagIds = new ArrayList<>();
        tagIds.add(1);
        tagIds.add(2);
        params.put("tagIds", tagIds);

        // 执行测试
        mockMvc.perform(post("/system/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogArticleService).insertBlogArticle(any(BlogArticle.class));
    }

    /**
     * 测试新增文章接口 - 带标签（String类型）
     */
    @Test
    void testAddArticle_WithTagsString() throws Exception {
        // 模拟成功添加
        when(blogArticleService.insertBlogArticle(any(BlogArticle.class))).thenReturn(1);

        // 准备请求体 - 包含标签（String类型）
        Map<String, Object> params = new HashMap<>();
        params.put("title", "测试文章标题");
        params.put("content", "测试文章内容");
        params.put("status", 0L);
        List<String> tagIds = new ArrayList<>();
        tagIds.add("1");
        tagIds.add("2");
        params.put("tagIds", tagIds);

        // 执行测试
        mockMvc.perform(post("/system/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogArticleService).insertBlogArticle(any(BlogArticle.class));
    }

    /**
     * 测试新增文章接口 - 带无效标签
     */
    @Test
    void testAddArticle_WithInvalidTags() throws Exception {
        // 模拟成功添加
        when(blogArticleService.insertBlogArticle(any(BlogArticle.class))).thenReturn(1);

        // 准备请求体 - 包含无效标签
        Map<String, Object> params = new HashMap<>();
        params.put("title", "测试文章标题");
        params.put("content", "测试文章内容");
        params.put("status", 0L);
        List<String> tagIds = new ArrayList<>();
        tagIds.add("invalid");
        tagIds.add("2");
        params.put("tagIds", tagIds);

        // 执行测试
        mockMvc.perform(post("/system/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogArticleService).insertBlogArticle(any(BlogArticle.class));
    }

    /**
     * 测试更新文章接口 - 带标签
     */
    @Test
    void testEditArticle_WithTags() throws Exception {
        // 模拟成功更新
        when(blogArticleService.updateBlogArticle(any(BlogArticle.class))).thenReturn(1);

        // 准备请求体 - 包含标签
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("title", "测试文章标题");
        params.put("content", "测试文章内容");
        params.put("status", 0L);
        List<Long> tagIds = new ArrayList<>();
        tagIds.add(1L);
        tagIds.add(2L);
        params.put("tagIds", tagIds);

        // 执行测试
        mockMvc.perform(put("/system/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogArticleService).updateBlogArticle(any(BlogArticle.class));
    }

    /**
     * 测试新增文章接口 - 带所有可选字段
     */
    @Test
    void testAddArticle_WithAllFields() throws Exception {
        // 模拟成功添加
        when(blogArticleService.insertBlogArticle(any(BlogArticle.class))).thenReturn(1);

        // 准备请求体 - 包含所有字段
        Map<String, Object> params = new HashMap<>();
        params.put("title", "测试文章标题");
        params.put("summary", "测试摘要");
        params.put("content", "测试文章内容");
        params.put("coverUrl", "http://example.com/cover.jpg");
        params.put("categoryId", 1L);
        params.put("status", 0L);
        params.put("isTop", 1L);
        params.put("isRecommend", 0L);
        params.put("authorId", 1L);
        params.put("authorName", "测试作者");

        // 执行测试
        mockMvc.perform(post("/system/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogArticleService).insertBlogArticle(any(BlogArticle.class));
    }

    /**
     * 测试更新文章状态接口 - 异常情况
     */
    @Test
    void testChangeStatus_Exception() throws Exception {
        // 模拟异常
        when(blogArticleService.updateArticleStatus(anyList(), anyInt())).thenThrow(new RuntimeException("数据库错误"));

        // 准备请求体
        Map<String, Object> articleStatus = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        articleStatus.put("ids", ids);
        articleStatus.put("status", 1);

        // 执行测试
        mockMvc.perform(put("/system/article/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleStatus)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));

        verify(blogArticleService).updateArticleStatus(anyList(), anyInt());
    }

    /**
     * 测试新增文章接口 - 异常情况
     */
    @Test
    void testAddArticle_Exception() throws Exception {
        // 模拟异常
        when(blogArticleService.insertBlogArticle(any(BlogArticle.class))).thenThrow(new RuntimeException("数据库错误"));

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("title", "测试文章标题");
        params.put("content", "测试文章内容");
        params.put("status", 0L);

        // 执行测试
        mockMvc.perform(post("/system/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));

        verify(blogArticleService).insertBlogArticle(any(BlogArticle.class));
    }

    /**
     * 测试更新文章接口 - 异常情况
     */
    @Test
    void testEditArticle_Exception() throws Exception {
        // 模拟异常
        when(blogArticleService.updateBlogArticle(any(BlogArticle.class))).thenThrow(new RuntimeException("数据库错误"));

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("title", "测试文章标题");
        params.put("content", "测试文章内容");
        params.put("status", 0L);

        // 执行测试
        mockMvc.perform(put("/system/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));

        verify(blogArticleService).updateBlogArticle(any(BlogArticle.class));
    }
}