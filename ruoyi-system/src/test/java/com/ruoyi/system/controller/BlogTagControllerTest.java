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
 * 博客标签控制器单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@WebMvcTest(controllers = BlogTagController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class BlogTagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private com.ruoyi.system.service.IBlogTagService blogTagService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    /**
     * 测试获取标签列表接口
     */
    @Test
    void testGetTagList() throws Exception {
        // 模拟数据
        List<com.ruoyi.system.domain.BlogTag> tagList = new ArrayList<>();
        com.ruoyi.system.domain.BlogTag tag = new com.ruoyi.system.domain.BlogTag();
        tag.setId(1L);
        tag.setName("测试标签");
        tagList.add(tag);

        when(blogTagService.selectBlogTagList(any(com.ruoyi.system.domain.BlogTag.class)))
            .thenReturn(tagList);

        // 执行测试
        mockMvc.perform(get("/system/tag/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows[0].name").value("测试标签"));

        verify(blogTagService).selectBlogTagList(any(com.ruoyi.system.domain.BlogTag.class));
    }

    /**
     * 测试获取标签详情接口
     */
    @Test
    void testGetTagDetail() throws Exception {
        // 模拟数据
        com.ruoyi.system.domain.BlogTag tag = new com.ruoyi.system.domain.BlogTag();
        tag.setId(1L);
        tag.setName("测试标签");

        when(blogTagService.selectBlogTagById(1L)).thenReturn(tag);

        // 执行测试
        mockMvc.perform(get("/system/tag/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("测试标签"));

        verify(blogTagService).selectBlogTagById(1L);
    }

    /**
     * 测试新增标签接口 - 成功
     */
    @Test
    void testAddTag_Success() throws Exception {
        // 模拟标签名称唯一
        when(blogTagService.checkTagNameUnique(any(com.ruoyi.system.domain.BlogTag.class)))
            .thenReturn(true);
        when(blogTagService.insertBlogTag(any(com.ruoyi.system.domain.BlogTag.class)))
            .thenReturn(1);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("name", "测试标签");
        params.put("color", "#ff0000");

        // 执行测试
        mockMvc.perform(post("/system/tag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogTagService).insertBlogTag(any(com.ruoyi.system.domain.BlogTag.class));
    }

    /**
     * 测试新增标签接口 - 名称重复
     */
    @Test
    void testAddTag_DuplicateName() throws Exception {
        // 模拟标签名称重复
        when(blogTagService.checkTagNameUnique(any(com.ruoyi.system.domain.BlogTag.class)))
            .thenReturn(false);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("name", "测试标签");

        // 执行测试
        mockMvc.perform(post("/system/tag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));

        verify(blogTagService, never()).insertBlogTag(any(com.ruoyi.system.domain.BlogTag.class));
    }

    /**
     * 测试更新标签接口 - 成功
     */
    @Test
    void testEditTag_Success() throws Exception {
        // 模拟标签名称唯一
        when(blogTagService.checkTagNameUnique(any(com.ruoyi.system.domain.BlogTag.class)))
            .thenReturn(true);
        when(blogTagService.updateBlogTag(any(com.ruoyi.system.domain.BlogTag.class)))
            .thenReturn(1);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("name", "修改后的标签");

        // 执行测试
        mockMvc.perform(put("/system/tag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogTagService).updateBlogTag(any(com.ruoyi.system.domain.BlogTag.class));
    }

    /**
     * 测试删除标签接口 - 成功
     */
    @Test
    void testRemoveTag_Success() throws Exception {
        // 模拟标签未关联文章
        when(blogTagService.checkTagExistArticle(anyLong())).thenReturn(false);
        when(blogTagService.deleteBlogTagByIds(any(Long[].class))).thenReturn(1);

        // 执行测试
        mockMvc.perform(delete("/system/tag/1,2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogTagService).deleteBlogTagByIds(any(Long[].class));
    }

    /**
     * 测试删除标签接口 - 标签已关联文章
     */
    @Test
    void testRemoveTag_HasArticles() throws Exception {
        // 模拟标签已关联文章
        when(blogTagService.checkTagExistArticle(anyLong())).thenReturn(true);

        // 执行测试
        mockMvc.perform(delete("/system/tag/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));

        verify(blogTagService, never()).deleteBlogTagByIds(any(Long[].class));
    }

    /**
     * 测试获取所有标签接口
     */
    @Test
    void testGetAllTags() throws Exception {
        // 模拟数据
        List<com.ruoyi.system.domain.BlogTag> tagList = new ArrayList<>();
        com.ruoyi.system.domain.BlogTag tag = new com.ruoyi.system.domain.BlogTag();
        tag.setId(1L);
        tag.setName("测试标签");
        tagList.add(tag);

        when(blogTagService.selectAllTagList()).thenReturn(tagList);

        // 执行测试
        mockMvc.perform(get("/system/tag/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].name").value("测试标签"));

        verify(blogTagService).selectAllTagList();
    }
}