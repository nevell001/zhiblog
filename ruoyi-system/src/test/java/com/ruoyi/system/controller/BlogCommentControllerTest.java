package com.ruoyi.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
 * 博客评论控制器单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@WebMvcTest(controllers = BlogCommentController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class BlogCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private com.ruoyi.system.service.IBlogCommentService blogCommentService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    /**
     * 测试获取评论列表接口
     */
    @Test
    void testGetCommentList() throws Exception {
        // 模拟数据
        List<com.ruoyi.system.domain.BlogComment> commentList = new ArrayList<>();
        com.ruoyi.system.domain.BlogComment comment = new com.ruoyi.system.domain.BlogComment();
        comment.setId(1L);
        comment.setContent("测试评论");
        commentList.add(comment);

        when(blogCommentService.selectBlogCommentList(any(com.ruoyi.system.domain.BlogComment.class)))
            .thenReturn(commentList);

        // 执行测试
        mockMvc.perform(get("/system/comment/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows[0].content").value("测试评论"));

        verify(blogCommentService).selectBlogCommentList(any(com.ruoyi.system.domain.BlogComment.class));
    }

    /**
     * 测试获取评论详情接口
     */
    @Test
    void testGetCommentDetail() throws Exception {
        // 模拟数据
        com.ruoyi.system.domain.BlogComment comment = new com.ruoyi.system.domain.BlogComment();
        comment.setId(1L);
        comment.setContent("测试评论");

        when(blogCommentService.selectBlogCommentById(1L)).thenReturn(comment);

        // 执行测试
        mockMvc.perform(get("/system/comment/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content").value("测试评论"));

        verify(blogCommentService).selectBlogCommentById(1L);
    }

    /**
     * 测试新增评论接口
     */
    @Test
    void testAddComment() throws Exception {
        // 模拟成功添加
        when(blogCommentService.insertBlogComment(any(com.ruoyi.system.domain.BlogComment.class)))
            .thenReturn(1);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("articleId", 1L);
        params.put("content", "测试评论");
        params.put("userId", 1L);

        // 执行测试
        mockMvc.perform(post("/system/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogCommentService).insertBlogComment(any(com.ruoyi.system.domain.BlogComment.class));
    }

    /**
     * 测试更新评论接口
     */
    @Test
    void testEditComment() throws Exception {
        // 模拟成功更新
        when(blogCommentService.updateBlogComment(any(com.ruoyi.system.domain.BlogComment.class)))
            .thenReturn(1);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("content", "修改后的评论");

        // 执行测试
        mockMvc.perform(put("/system/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogCommentService).updateBlogComment(any(com.ruoyi.system.domain.BlogComment.class));
    }

    /**
     * 测试删除评论接口
     */
    @Test
    void testRemoveComment() throws Exception {
        // 模拟成功删除
        when(blogCommentService.deleteBlogCommentByIds(any(Long[].class))).thenReturn(1);

        // 执行测试
        mockMvc.perform(delete("/system/comment/1,2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogCommentService).deleteBlogCommentByIds(any(Long[].class));
    }

    /**
     * 测试审核通过评论接口
     */
    @Test
    void testAuditComment() throws Exception {
        // 模拟成功审核
        when(blogCommentService.auditBlogCommentByIds(any(Long[].class))).thenReturn(1);

        // 执行测试
        mockMvc.perform(put("/system/comment/audit/1,2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogCommentService).auditBlogCommentByIds(any(Long[].class));
    }

    /**
     * 测试审核拒绝评论接口
     */
    @Test
    void testRejectComment() throws Exception {
        // 模拟成功拒绝
        when(blogCommentService.rejectBlogCommentByIds(any(Long[].class))).thenReturn(1);

        // 执行测试
        mockMvc.perform(put("/system/comment/reject/1,2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogCommentService).rejectBlogCommentByIds(any(Long[].class));
    }
}