package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.BlogSetting;
import com.ruoyi.system.mapper.BlogSettingMapper;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 博客设置服务层单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class BlogSettingServiceImplTest {

    @Mock
    private BlogSettingMapper blogSettingMapper;

    @InjectMocks
    private BlogSettingServiceImpl blogSettingService;

    private BlogSetting testSetting;

    @BeforeEach
    void setUp() {
        testSetting = new BlogSetting();
        testSetting.setId(1L);
        testSetting.setSettingKey("blog_name");
        testSetting.setSettingValue("我的博客");
        testSetting.setDescription("博客名称");
    }

    /**
     * 测试查询设置列表
     */
    @Test
    void testSelectBlogSettingList() {
        // 准备数据
        List<BlogSetting> settingList = Arrays.asList(testSetting);
        when(blogSettingMapper.selectBlogSettingList(any(BlogSetting.class)))
            .thenReturn(settingList);

        // 执行测试
        List<BlogSetting> result = blogSettingService.selectBlogSettingList(new BlogSetting());

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("blog_name", result.get(0).getSettingKey());
        verify(blogSettingMapper).selectBlogSettingList(any(BlogSetting.class));
    }

    /**
     * 测试查询设置列表 - 空结果
     */
    @Test
    void testSelectBlogSettingList_Empty() {
        // 模拟空结果
        when(blogSettingMapper.selectBlogSettingList(any(BlogSetting.class)))
            .thenReturn(Collections.emptyList());

        // 执行测试
        List<BlogSetting> result = blogSettingService.selectBlogSettingList(new BlogSetting());

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(blogSettingMapper).selectBlogSettingList(any(BlogSetting.class));
    }

    /**
     * 测试根据ID查询设置
     */
    @Test
    void testSelectBlogSettingById() {
        // 准备数据
        when(blogSettingMapper.selectBlogSettingById(1L)).thenReturn(testSetting);

        // 执行测试
        BlogSetting result = blogSettingService.selectBlogSettingById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("blog_name", result.getSettingKey());
        verify(blogSettingMapper).selectBlogSettingById(1L);
    }

    /**
     * 测试根据ID查询设置 - 不存在
     */
    @Test
    void testSelectBlogSettingById_NotFound() {
        // 模拟设置不存在
        when(blogSettingMapper.selectBlogSettingById(999L)).thenReturn(null);

        // 执行测试
        BlogSetting result = blogSettingService.selectBlogSettingById(999L);

        // 验证结果
        assertNull(result);
        verify(blogSettingMapper).selectBlogSettingById(999L);
    }

    /**
     * 测试根据设置键查询设置值
     */
    @Test
    void testSelectSettingValueByKey() {
        // 准备数据
        when(blogSettingMapper.selectSettingValueByKey("blog_name")).thenReturn("我的博客");

        // 执行测试
        String result = blogSettingService.selectSettingValueByKey("blog_name");

        // 验证结果
        assertNotNull(result);
        assertEquals("我的博客", result);
        verify(blogSettingMapper).selectSettingValueByKey("blog_name");
    }

    /**
     * 测试根据设置键查询设置值 - 不存在
     */
    @Test
    void testSelectSettingValueByKey_NotFound() {
        // 模拟设置不存在
        when(blogSettingMapper.selectSettingValueByKey("non_existent_key")).thenReturn(null);

        // 执行测试
        String result = blogSettingService.selectSettingValueByKey("non_existent_key");

        // 验证结果
        assertNull(result);
        verify(blogSettingMapper).selectSettingValueByKey("non_existent_key");
    }

    /**
     * 测试新增设置 - 成功
     */
    @Test
    void testInsertBlogSetting_Success() {
        // 准备数据
        when(blogSettingMapper.insertBlogSetting(any(BlogSetting.class))).thenReturn(1);

        // Mock SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("testUser");

            // 执行测试
            int result = blogSettingService.insertBlogSetting(testSetting);

            // 验证结果
            assertEquals(1, result);
            verify(blogSettingMapper).insertBlogSetting(any(BlogSetting.class));
        }
    }

    /**
     * 测试更新设置 - 成功
     */
    @Test
    void testUpdateBlogSetting_Success() {
        // 准备数据
        when(blogSettingMapper.updateBlogSetting(any(BlogSetting.class))).thenReturn(1);

        // Mock SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("testUser");

            // 执行测试
            int result = blogSettingService.updateBlogSetting(testSetting);

            // 验证结果
            assertEquals(1, result);
            verify(blogSettingMapper).updateBlogSetting(any(BlogSetting.class));
        }
    }

    /**
     * 测试通过设置键更新设置值 - 成功
     */
    @Test
    void testUpdateSettingValueByKey_Success() {
        // 准备数据
        when(blogSettingMapper.updateSettingValueByKey(anyString(), anyString())).thenReturn(1);

        // 执行测试
        int result = blogSettingService.updateSettingValueByKey("blog_name", "新博客名称");

        // 验证结果
        assertEquals(1, result);
        verify(blogSettingMapper).updateSettingValueByKey("blog_name", "新博客名称");
    }

    /**
     * 测试删除设置 - 成功
     */
    @Test
    void testDeleteBlogSettingById_Success() {
        // 准备数据
        when(blogSettingMapper.deleteBlogSettingById(1L)).thenReturn(1);

        // 执行测试
        int result = blogSettingService.deleteBlogSettingById(1L);

        // 验证结果
        assertEquals(1, result);
        verify(blogSettingMapper).deleteBlogSettingById(1L);
    }

    /**
     * 测试删除设置 - 不存在
     */
    @Test
    void testDeleteBlogSettingById_NotFound() {
        // 准备数据
        when(blogSettingMapper.deleteBlogSettingById(999L)).thenReturn(0);

        // 执行测试
        int result = blogSettingService.deleteBlogSettingById(999L);

        // 验证结果
        assertEquals(0, result);
        verify(blogSettingMapper).deleteBlogSettingById(999L);
    }

    /**
     * 测试批量删除设置 - 成功
     */
    @Test
    void testDeleteBlogSettingByIds_Success() {
        // 准备数据
        when(blogSettingMapper.deleteBlogSettingByIds(any(Long[].class))).thenReturn(2);

        // 执行测试
        int result = blogSettingService.deleteBlogSettingByIds(new Long[]{1L, 2L});

        // 验证结果
        assertEquals(2, result);
        verify(blogSettingMapper).deleteBlogSettingByIds(any(Long[].class));
    }

    /**
     * 测试批量删除设置 - 空列表
     */
    @Test
    void testDeleteBlogSettingByIds_EmptyList() {
        // 执行测试
        int result = blogSettingService.deleteBlogSettingByIds(new Long[]{});

        // 验证结果
        assertEquals(0, result);
        verify(blogSettingMapper).deleteBlogSettingByIds(any(Long[].class));
    }

    /**
     * 测试设置值长度限制
     */
    @Test
    void testInsertBlogSetting_LongValue() {
        // 设置超长值
        StringBuilder longValue = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longValue.append("测试值");
        }
        testSetting.setSettingValue(longValue.toString());

        // 准备数据
        when(blogSettingMapper.insertBlogSetting(any(BlogSetting.class))).thenReturn(1);

        // Mock SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("testUser");

            // 执行测试
            int result = blogSettingService.insertBlogSetting(testSetting);

            // 验证结果
            assertEquals(1, result);
            verify(blogSettingMapper).insertBlogSetting(any(BlogSetting.class));
        }
    }

    /**
     * 测试设置键包含特殊字符
     */
    @Test
    void testInsertBlogSetting_SpecialCharacters() {
        // 设置包含特殊字符的键
        testSetting.setSettingKey("blog_name_test_123");

        // 准备数据
        when(blogSettingMapper.insertBlogSetting(any(BlogSetting.class))).thenReturn(1);

        // Mock SecurityUtils
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("testUser");

            // 执行测试
            int result = blogSettingService.insertBlogSetting(testSetting);

            // 验证结果
            assertEquals(1, result);
            verify(blogSettingMapper).insertBlogSetting(any(BlogSetting.class));
        }
    }

    /**
     * 测试查询多个设置值
     */
    @Test
    void testSelectMultipleSettingValues() {
        // 准备数据
        when(blogSettingMapper.selectSettingValueByKey("blog_name")).thenReturn("我的博客");
        when(blogSettingMapper.selectSettingValueByKey("blog_desc")).thenReturn("博客描述");
        when(blogSettingMapper.selectSettingValueByKey("blog_author")).thenReturn("作者");

        // 执行测试
        String name = blogSettingService.selectSettingValueByKey("blog_name");
        String desc = blogSettingService.selectSettingValueByKey("blog_desc");
        String author = blogSettingService.selectSettingValueByKey("blog_author");

        // 验证结果
        assertEquals("我的博客", name);
        assertEquals("博客描述", desc);
        assertEquals("作者", author);
    }
}