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
 * 友情链接控制器单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@WebMvcTest(controllers = BlogFriendLinkController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class BlogFriendLinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private com.ruoyi.system.service.IBlogFriendLinkService blogFriendLinkService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    /**
     * 测试获取友情链接列表接口
     */
    @Test
    void testGetFriendLinkList() throws Exception {
        // 模拟数据
        List<com.ruoyi.system.domain.BlogFriendLink> linkList = new ArrayList<>();
        com.ruoyi.system.domain.BlogFriendLink link = new com.ruoyi.system.domain.BlogFriendLink();
        link.setId(1L);
        link.setName("测试友情链接");
        link.setUrl("https://example.com");
        linkList.add(link);

        when(blogFriendLinkService.selectBlogFriendLinkList(any(com.ruoyi.system.domain.BlogFriendLink.class)))
            .thenReturn(linkList);

        // 执行测试
        mockMvc.perform(get("/system/friendLink/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows[0].name").value("测试友情链接"));

        verify(blogFriendLinkService).selectBlogFriendLinkList(any(com.ruoyi.system.domain.BlogFriendLink.class));
    }

    /**
     * 测试获取前台友情链接列表接口
     */
    @Test
    void testGetFrontFriendLinkList() throws Exception {
        // 模拟数据
        List<com.ruoyi.system.domain.BlogFriendLink> linkList = new ArrayList<>();
        com.ruoyi.system.domain.BlogFriendLink link = new com.ruoyi.system.domain.BlogFriendLink();
        link.setId(1L);
        link.setName("测试友情链接");
        link.setUrl("https://example.com");
        link.setStatus("0"); // 启用状态
        linkList.add(link);

        when(blogFriendLinkService.selectFrontFriendLinkList()).thenReturn(linkList);

        // 执行测试
        mockMvc.perform(get("/system/friendLink/front/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].name").value("测试友情链接"));

        verify(blogFriendLinkService).selectFrontFriendLinkList();
    }

    /**
     * 测试获取友情链接详情接口
     */
    @Test
    void testGetFriendLinkDetail() throws Exception {
        // 模拟数据
        com.ruoyi.system.domain.BlogFriendLink link = new com.ruoyi.system.domain.BlogFriendLink();
        link.setId(1L);
        link.setName("测试友情链接");
        link.setUrl("https://example.com");

        when(blogFriendLinkService.selectBlogFriendLinkById(1L)).thenReturn(link);

        // 执行测试
        mockMvc.perform(get("/system/friendLink/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("测试友情链接"));

        verify(blogFriendLinkService).selectBlogFriendLinkById(1L);
    }

    /**
     * 测试新增友情链接接口
     */
    @Test
    void testAddFriendLink() throws Exception {
        // 模拟成功添加
        when(blogFriendLinkService.insertBlogFriendLink(any(com.ruoyi.system.domain.BlogFriendLink.class)))
            .thenReturn(1);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("name", "测试友情链接");
        params.put("url", "https://example.com");
        params.put("status", "0");

        // 执行测试
        mockMvc.perform(post("/system/friendLink")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogFriendLinkService).insertBlogFriendLink(any(com.ruoyi.system.domain.BlogFriendLink.class));
    }

    /**
     * 测试更新友情链接接口
     */
    @Test
    void testEditFriendLink() throws Exception {
        // 模拟成功更新
        when(blogFriendLinkService.updateBlogFriendLink(any(com.ruoyi.system.domain.BlogFriendLink.class)))
            .thenReturn(1);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("name", "修改后的友情链接");
        params.put("url", "https://example.com");

        // 执行测试
        mockMvc.perform(put("/system/friendLink")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogFriendLinkService).updateBlogFriendLink(any(com.ruoyi.system.domain.BlogFriendLink.class));
    }

    /**
     * 测试删除友情链接接口
     */
    @Test
    void testRemoveFriendLink() throws Exception {
        // 模拟成功删除
        when(blogFriendLinkService.deleteBlogFriendLinkByIds(any(Long[].class))).thenReturn(1);

        // 执行测试
        mockMvc.perform(delete("/system/friendLink/1,2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogFriendLinkService).deleteBlogFriendLinkByIds(any(Long[].class));
    }

    /**
     * 测试导出友情链接接口
     */
    @Test
    void testExportFriendLink() throws Exception {
        // 模拟数据
        List<com.ruoyi.system.domain.BlogFriendLink> linkList = new ArrayList<>();
        com.ruoyi.system.domain.BlogFriendLink link = new com.ruoyi.system.domain.BlogFriendLink();
        link.setId(1L);
        link.setName("测试友情链接");
        link.setUrl("https://example.com");
        linkList.add(link);

        when(blogFriendLinkService.selectBlogFriendLinkList(any(com.ruoyi.system.domain.BlogFriendLink.class)))
            .thenReturn(linkList);

        // 执行测试
        mockMvc.perform(post("/system/friendLink/export")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(blogFriendLinkService).selectBlogFriendLinkList(any(com.ruoyi.system.domain.BlogFriendLink.class));
    }

    /**
     * 测试新增友情链接接口 - 失败
     */
    @Test
    void testAddFriendLink_Failure() throws Exception {
        // 模拟添加失败
        when(blogFriendLinkService.insertBlogFriendLink(any(com.ruoyi.system.domain.BlogFriendLink.class)))
            .thenReturn(0);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("name", "测试友情链接");
        params.put("url", "https://example.com");
        params.put("status", "0");

        // 执行测试
        mockMvc.perform(post("/system/friendLink")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));

        verify(blogFriendLinkService).insertBlogFriendLink(any(com.ruoyi.system.domain.BlogFriendLink.class));
    }

    /**
     * 测试更新友情链接接口 - 失败
     */
    @Test
    void testEditFriendLink_Failure() throws Exception {
        // 模拟更新失败
        when(blogFriendLinkService.updateBlogFriendLink(any(com.ruoyi.system.domain.BlogFriendLink.class)))
            .thenReturn(0);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("name", "修改后的友情链接");
        params.put("url", "https://example.com");

        // 执行测试
        mockMvc.perform(put("/system/friendLink")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));

        verify(blogFriendLinkService).updateBlogFriendLink(any(com.ruoyi.system.domain.BlogFriendLink.class));
    }

    /**
     * 测试删除友情链接接口 - 失败
     */
    @Test
    void testRemoveFriendLink_Failure() throws Exception {
        // 模拟删除失败
        when(blogFriendLinkService.deleteBlogFriendLinkByIds(any(Long[].class))).thenReturn(0);

        // 执行测试
        mockMvc.perform(delete("/system/friendLink/1,2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));

        verify(blogFriendLinkService).deleteBlogFriendLinkByIds(any(Long[].class));
    }
}