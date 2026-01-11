package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.BlogFriendLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 友情链接Mapper单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class BlogFriendLinkMapperTest {

    @Mock
    private BlogFriendLinkMapper blogFriendLinkMapper;

    private BlogFriendLink testFriendLink;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testFriendLink = new BlogFriendLink();
        testFriendLink.setId(1L);
        testFriendLink.setName("Mapper测试友情链接");
        testFriendLink.setUrl("https://example.com");
        testFriendLink.setDescription("测试描述");
        testFriendLink.setStatus("0"); // 启用
    }

    /**
     * 测试CRUD操作的完整流程
     */
    @Test
    void testCRUDOperations() {
        // 测试新增
        when(blogFriendLinkMapper.insertBlogFriendLink(any(BlogFriendLink.class))).thenReturn(1);
        int insertResult = blogFriendLinkMapper.insertBlogFriendLink(testFriendLink);
        assertEquals(1, insertResult, "新增友情链接失败");
        verify(blogFriendLinkMapper).insertBlogFriendLink(any(BlogFriendLink.class));

        // 测试根据ID查询
        when(blogFriendLinkMapper.selectBlogFriendLinkById(1L)).thenReturn(testFriendLink);
        BlogFriendLink selectResult = blogFriendLinkMapper.selectBlogFriendLinkById(1L);
        assertNotNull(selectResult, "根据ID查询友情链接失败");
        assertEquals("Mapper测试友情链接", selectResult.getName(), "友情链接名称不匹配");
        verify(blogFriendLinkMapper).selectBlogFriendLinkById(1L);

        // 测试修改
        when(blogFriendLinkMapper.updateBlogFriendLink(any(BlogFriendLink.class))).thenReturn(1);
        testFriendLink.setName("修改后的友情链接");
        int updateResult = blogFriendLinkMapper.updateBlogFriendLink(testFriendLink);
        assertEquals(1, updateResult, "修改友情链接失败");
        verify(blogFriendLinkMapper).updateBlogFriendLink(any(BlogFriendLink.class));

        // 测试查询列表
        when(blogFriendLinkMapper.selectBlogFriendLinkList(any(BlogFriendLink.class)))
            .thenReturn(Arrays.asList(testFriendLink));
        List<BlogFriendLink> friendLinkList = blogFriendLinkMapper.selectBlogFriendLinkList(new BlogFriendLink());
        assertFalse(friendLinkList.isEmpty(), "查询友情链接列表失败");
        verify(blogFriendLinkMapper).selectBlogFriendLinkList(any(BlogFriendLink.class));

        // 测试删除
        when(blogFriendLinkMapper.deleteBlogFriendLinkById(1L)).thenReturn(1);
        int deleteResult = blogFriendLinkMapper.deleteBlogFriendLinkById(1L);
        assertEquals(1, deleteResult, "删除友情链接失败");
        verify(blogFriendLinkMapper).deleteBlogFriendLinkById(1L);
    }

    /**
     * 测试批量删除功能
     */
    @Test
    void testDeleteBlogFriendLinkByIds() {
        when(blogFriendLinkMapper.deleteBlogFriendLinkByIds(any(Long[].class))).thenReturn(2);
        int deleteResult = blogFriendLinkMapper.deleteBlogFriendLinkByIds(new Long[]{1L, 2L});
        assertEquals(2, deleteResult, "批量删除友情链接失败");
        verify(blogFriendLinkMapper).deleteBlogFriendLinkByIds(any(Long[].class));
    }

    /**
     * 测试查询前台友情链接列表
     */
    @Test
    void testSelectFrontFriendLinkList() {
        when(blogFriendLinkMapper.selectFrontFriendLinkList())
            .thenReturn(Arrays.asList(testFriendLink));
        List<BlogFriendLink> frontList = blogFriendLinkMapper.selectFrontFriendLinkList();
        assertNotNull(frontList, "前台友情链接列表不应为null");
        assertEquals(1, frontList.size(), "前台友情链接列表大小不正确");
        assertEquals("Mapper测试友情链接", frontList.get(0).getName(), "友情链接名称不匹配");
        verify(blogFriendLinkMapper).selectFrontFriendLinkList();
    }

    /**
     * 测试边界条件：空值处理
     */
    @Test
    void testNullHandling() {
        // 测试查询不存在的ID
        when(blogFriendLinkMapper.selectBlogFriendLinkById(999L)).thenReturn(null);
        BlogFriendLink result = blogFriendLinkMapper.selectBlogFriendLinkById(999L);
        assertNull(result, "查询不存在的ID应返回null");
        verify(blogFriendLinkMapper).selectBlogFriendLinkById(999L);

        // 测试空列表查询
        when(blogFriendLinkMapper.selectBlogFriendLinkList(any(BlogFriendLink.class)))
            .thenReturn(Arrays.asList());
        List<BlogFriendLink> list = blogFriendLinkMapper.selectBlogFriendLinkList(new BlogFriendLink());
        assertTrue(list.isEmpty(), "查询结果应为空列表");
        verify(blogFriendLinkMapper).selectBlogFriendLinkList(any(BlogFriendLink.class));
    }

    /**
     * 测试边界条件：空名称
     */
    @Test
    void testEmptyName() {
        BlogFriendLink emptyNameLink = new BlogFriendLink();
        emptyNameLink.setName("");
        emptyNameLink.setUrl("https://example.com");
        
        when(blogFriendLinkMapper.insertBlogFriendLink(any(BlogFriendLink.class))).thenReturn(1);
        int result = blogFriendLinkMapper.insertBlogFriendLink(emptyNameLink);
        assertEquals(1, result, "空名称友情链接插入失败");
        verify(blogFriendLinkMapper).insertBlogFriendLink(any(BlogFriendLink.class));
    }

    /**
     * 测试边界条件：空URL
     */
    @Test
    void testEmptyUrl() {
        BlogFriendLink emptyUrlLink = new BlogFriendLink();
        emptyUrlLink.setName("测试链接");
        emptyUrlLink.setUrl("");
        
        when(blogFriendLinkMapper.insertBlogFriendLink(any(BlogFriendLink.class))).thenReturn(1);
        int result = blogFriendLinkMapper.insertBlogFriendLink(emptyUrlLink);
        assertEquals(1, result, "空URL友情链接插入失败");
        verify(blogFriendLinkMapper).insertBlogFriendLink(any(BlogFriendLink.class));
    }

    /**
     * 测试边界条件：状态值
     */
    @Test
    void testStatusValues() {
        // 测试启用状态
        BlogFriendLink enabledLink = new BlogFriendLink();
        enabledLink.setName("启用链接");
        enabledLink.setStatus("0");
        when(blogFriendLinkMapper.insertBlogFriendLink(any(BlogFriendLink.class))).thenReturn(1);
        int result1 = blogFriendLinkMapper.insertBlogFriendLink(enabledLink);
        assertEquals(1, result1, "启用状态友情链接插入失败");

        // 测试禁用状态
        BlogFriendLink disabledLink = new BlogFriendLink();
        disabledLink.setName("禁用链接");
        disabledLink.setStatus("1");
        when(blogFriendLinkMapper.insertBlogFriendLink(any(BlogFriendLink.class))).thenReturn(1);
        int result2 = blogFriendLinkMapper.insertBlogFriendLink(disabledLink);
        assertEquals(1, result2, "禁用状态友情链接插入失败");

        verify(blogFriendLinkMapper, times(2)).insertBlogFriendLink(any(BlogFriendLink.class));
    }
}