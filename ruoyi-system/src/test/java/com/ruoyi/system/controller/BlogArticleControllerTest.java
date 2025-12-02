package com.ruoyi.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.service.IBlogArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BlogArticleControllerTest {

    @Mock
    private IBlogArticleService blogArticleService;

    @InjectMocks
    private BlogArticleController blogArticleController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private BlogArticle testArticle;

    @BeforeEach
    void setUp() {
        // 初始化MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(blogArticleController).build();
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

        // 执行测试
        mockMvc.perform(get("/system/article/detail/1")
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

        // 执行测试
        mockMvc.perform(post("/system/article/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testArticle)))
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

        // 执行测试
        mockMvc.perform(put("/system/article/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testArticle)))
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

        // 执行测试
        mockMvc.perform(delete("/system/article/remove/1,2")
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

        // 准备请求体 - 使用Map来模拟请求体，因为BlogArticle可能没有setIds方法
        Map<String, Object> articleStatus = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        articleStatus.put("ids", ids);
        articleStatus.put("status", 1);

        // 执行测试
        mockMvc.perform(put("/system/article/changeStatus")
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
}