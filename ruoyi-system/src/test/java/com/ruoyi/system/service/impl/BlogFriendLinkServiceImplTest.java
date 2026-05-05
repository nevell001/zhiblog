package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.BlogFriendLink;
import com.ruoyi.system.mapper.BlogFriendLinkMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ruoyi.common.utils.SecurityUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 友情链接服务层单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class BlogFriendLinkServiceImplTest {

    @Mock
    private BlogFriendLinkMapper blogFriendLinkMapper;

    @InjectMocks
    private BlogFriendLinkServiceImpl blogFriendLinkService;

    private BlogFriendLink testFriendLink;

    @BeforeEach
    void setUp() {
        testFriendLink = new BlogFriendLink();
        testFriendLink.setId(1L);
        testFriendLink.setName("测试友情链接");
        testFriendLink.setUrl("https://example.com");
        testFriendLink.setDescription("测试描述");
        testFriendLink.setStatus("0"); // 启用
    }

    /**
     * 测试查询友情链接列表
     */
    @Test
    void testSelectBlogFriendLinkList() {
        // 准备数据
        List<BlogFriendLink> linkList = Arrays.asList(testFriendLink);
        when(blogFriendLinkMapper.selectBlogFriendLinkList(any(BlogFriendLink.class)))
            .thenReturn(linkList);

        // 执行测试
        List<BlogFriendLink> result = blogFriendLinkService.selectBlogFriendLinkList(new BlogFriendLink());

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试友情链接", result.get(0).getName());
        verify(blogFriendLinkMapper).selectBlogFriendLinkList(any(BlogFriendLink.class));
    }

    /**
     * 测试查询友情链接列表 - 空结果
     */
    @Test
    void testSelectBlogFriendLinkList_Empty() {
        // 模拟空结果
        when(blogFriendLinkMapper.selectBlogFriendLinkList(any(BlogFriendLink.class)))
            .thenReturn(Collections.emptyList());

        // 执行测试
        List<BlogFriendLink> result = blogFriendLinkService.selectBlogFriendLinkList(new BlogFriendLink());

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(blogFriendLinkMapper).selectBlogFriendLinkList(any(BlogFriendLink.class));
    }

    /**
     * 测试根据ID查询友情链接
     */
    @Test
    void testSelectBlogFriendLinkById() {
        // 准备数据
        when(blogFriendLinkMapper.selectBlogFriendLinkById(1L)).thenReturn(testFriendLink);

        // 执行测试
        BlogFriendLink result = blogFriendLinkService.selectBlogFriendLinkById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("测试友情链接", result.getName());
        verify(blogFriendLinkMapper).selectBlogFriendLinkById(1L);
    }

    /**
     * 测试根据ID查询友情链接 - 不存在
     */
    @Test
    void testSelectBlogFriendLinkById_NotFound() {
        // 模拟友情链接不存在
        when(blogFriendLinkMapper.selectBlogFriendLinkById(999L)).thenReturn(null);

        // 执行测试
        BlogFriendLink result = blogFriendLinkService.selectBlogFriendLinkById(999L);

        // 验证结果
        assertNull(result);
        verify(blogFriendLinkMapper).selectBlogFriendLinkById(999L);
    }

    /**
     * 测试新增友情链接 - 成功
     */
    @Test
    void testInsertBlogFriendLink_Success() {
        // 准备数据
        when(blogFriendLinkMapper.insertBlogFriendLink(any(BlogFriendLink.class))).thenReturn(1);

        // Mock SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("testUser");

            // 执行测试
            int result = blogFriendLinkService.insertBlogFriendLink(testFriendLink);

            // 验证结果
            assertEquals(1, result);
            verify(blogFriendLinkMapper).insertBlogFriendLink(any(BlogFriendLink.class));
        }
    }

    /**
     * 测试修改友情链接 - 成功
     */
    @Test
    void testUpdateBlogFriendLink_Success() {
        // 准备数据
        when(blogFriendLinkMapper.updateBlogFriendLink(any(BlogFriendLink.class))).thenReturn(1);

        // Mock SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("testUser");

            // 执行测试
            int result = blogFriendLinkService.updateBlogFriendLink(testFriendLink);

            // 验证结果
            assertEquals(1, result);
            verify(blogFriendLinkMapper).updateBlogFriendLink(any(BlogFriendLink.class));
        }
    }

    /**
     * 测试删除友情链接 - 成功
     */
    @Test
    void testDeleteBlogFriendLinkById_Success() {
        // 准备数据
        when(blogFriendLinkMapper.deleteBlogFriendLinkById(1L)).thenReturn(1);

        // 执行测试
        int result = blogFriendLinkService.deleteBlogFriendLinkById(1L);

        // 验证结果
        assertEquals(1, result);
        verify(blogFriendLinkMapper).deleteBlogFriendLinkById(1L);
    }

    /**
     * 测试删除友情链接 - 不存在
     */
    @Test
    void testDeleteBlogFriendLinkById_NotFound() {
        // 准备数据
        when(blogFriendLinkMapper.deleteBlogFriendLinkById(999L)).thenReturn(0);

        // 执行测试
        int result = blogFriendLinkService.deleteBlogFriendLinkById(999L);

        // 验证结果
        assertEquals(0, result);
        verify(blogFriendLinkMapper).deleteBlogFriendLinkById(999L);
    }

    /**
     * 测试批量删除友情链接 - 成功
     */
    @Test
    void testDeleteBlogFriendLinkByIds_Success() {
        // 准备数据
        when(blogFriendLinkMapper.deleteBlogFriendLinkByIds(any(Long[].class))).thenReturn(2);

        // 执行测试
        int result = blogFriendLinkService.deleteBlogFriendLinkByIds(new Long[]{1L, 2L});

        // 验证结果
        assertEquals(2, result);
        verify(blogFriendLinkMapper).deleteBlogFriendLinkByIds(any(Long[].class));
    }

    /**
     * 测试批量删除友情链接 - 空列表
     */
    @Test
    void testDeleteBlogFriendLinkByIds_EmptyList() {
        // 执行测试
        int result = blogFriendLinkService.deleteBlogFriendLinkByIds(new Long[]{});

        // 验证结果
        assertEquals(0, result);
        verify(blogFriendLinkMapper).deleteBlogFriendLinkByIds(any(Long[].class));
    }

    /**
     * 测试查询前台友情链接列表
     */
    @Test
    void testSelectFrontFriendLinkList() {
        // 准备数据
        List<BlogFriendLink> linkList = Arrays.asList(testFriendLink);
        when(blogFriendLinkMapper.selectFrontFriendLinkList()).thenReturn(linkList);

        // 执行测试
        List<BlogFriendLink> result = blogFriendLinkService.selectFrontFriendLinkList();

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(blogFriendLinkMapper).selectFrontFriendLinkList();
    }

    /**
     * 测试查询前台友情链接列表 - 空结果
     */
    @Test
    void testSelectFrontFriendLinkList_Empty() {
        // 模拟空结果
        when(blogFriendLinkMapper.selectFrontFriendLinkList()).thenReturn(Collections.emptyList());

        // 执行测试
        List<BlogFriendLink> result = blogFriendLinkService.selectFrontFriendLinkList();

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(blogFriendLinkMapper).selectFrontFriendLinkList();
    }

    /**
     * 测试友情链接状态更新
     */
    @Test
    void testUpdateBlogFriendLink_Status() {
        // 更新状态
        testFriendLink.setStatus("1");
        when(blogFriendLinkMapper.updateBlogFriendLink(any(BlogFriendLink.class))).thenReturn(1);

        // Mock SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("testUser");

            // 执行测试
            int result = blogFriendLinkService.updateBlogFriendLink(testFriendLink);

            // 验证结果
            assertEquals(1, result);
            verify(blogFriendLinkMapper).updateBlogFriendLink(any(BlogFriendLink.class));
        }
    }

    /**
     * 测试友情链接URL格式
     */
    @Test
    void testInsertBlogFriendLink_DifferentUrls() {
        // 测试不同格式的URL
        String[] urls = {
            "https://example.com",
            "http://example.com",
            "https://www.example.com",
            "https://example.com/path"
        };

        // 准备数据
        when(blogFriendLinkMapper.insertBlogFriendLink(any(BlogFriendLink.class))).thenReturn(1);

        // Mock SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("testUser");

            // 执行测试
            for (String url : urls) {
                testFriendLink.setUrl(url);
                int result = blogFriendLinkService.insertBlogFriendLink(testFriendLink);
                assertEquals(1, result);
            }
        }
    }
}