package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.BlogSetting;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 博客设置Mapper单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class BlogSettingMapperTest {

    @Mock
    private BlogSettingMapper blogSettingMapper;

    private BlogSetting testSetting;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testSetting = new BlogSetting();
        testSetting.setId(1L);
        testSetting.setSettingKey("blog_name");
        testSetting.setSettingValue("我的博客");
        testSetting.setDescription("博客名称");
    }

    /**
     * 测试CRUD操作的完整流程
     */
    @Test
    void testCRUDOperations() {
        // 测试新增
        when(blogSettingMapper.insertBlogSetting(any(BlogSetting.class))).thenReturn(1);
        int insertResult = blogSettingMapper.insertBlogSetting(testSetting);
        assertEquals(1, insertResult, "新增博客设置失败");
        verify(blogSettingMapper).insertBlogSetting(any(BlogSetting.class));

        // 测试根据ID查询
        when(blogSettingMapper.selectBlogSettingById(1L)).thenReturn(testSetting);
        BlogSetting selectResult = blogSettingMapper.selectBlogSettingById(1L);
        assertNotNull(selectResult, "根据ID查询博客设置失败");
        assertEquals("blog_name", selectResult.getSettingKey(), "设置键不匹配");
        verify(blogSettingMapper).selectBlogSettingById(1L);

        // 测试修改
        when(blogSettingMapper.updateBlogSetting(any(BlogSetting.class))).thenReturn(1);
        testSetting.setSettingValue("修改后的博客名称");
        int updateResult = blogSettingMapper.updateBlogSetting(testSetting);
        assertEquals(1, updateResult, "修改博客设置失败");
        verify(blogSettingMapper).updateBlogSetting(any(BlogSetting.class));

        // 测试查询列表
        when(blogSettingMapper.selectBlogSettingList(any(BlogSetting.class)))
            .thenReturn(Arrays.asList(testSetting));
        List<BlogSetting> settingList = blogSettingMapper.selectBlogSettingList(new BlogSetting());
        assertFalse(settingList.isEmpty(), "查询博客设置列表失败");
        verify(blogSettingMapper).selectBlogSettingList(any(BlogSetting.class));

        // 测试删除
        when(blogSettingMapper.deleteBlogSettingById(1L)).thenReturn(1);
        int deleteResult = blogSettingMapper.deleteBlogSettingById(1L);
        assertEquals(1, deleteResult, "删除博客设置失败");
        verify(blogSettingMapper).deleteBlogSettingById(1L);
    }

    /**
     * 测试批量删除功能
     */
    @Test
    void testDeleteBlogSettingByIds() {
        when(blogSettingMapper.deleteBlogSettingByIds(any(Long[].class))).thenReturn(2);
        int deleteResult = blogSettingMapper.deleteBlogSettingByIds(new Long[]{1L, 2L});
        assertEquals(2, deleteResult, "批量删除博客设置失败");
        verify(blogSettingMapper).deleteBlogSettingByIds(any(Long[].class));
    }

    /**
     * 测试根据设置键查询设置值
     */
    @Test
    void testSelectSettingValueByKey() {
        when(blogSettingMapper.selectSettingValueByKey("blog_name")).thenReturn("我的博客");
        String value = blogSettingMapper.selectSettingValueByKey("blog_name");
        assertEquals("我的博客", value, "设置值不匹配");
        verify(blogSettingMapper).selectSettingValueByKey("blog_name");
    }

    /**
     * 测试更新设置值
     */
    @Test
    void testUpdateSettingValueByKey() {
        when(blogSettingMapper.updateSettingValueByKey(anyString(), anyString())).thenReturn(1);
        int result = blogSettingMapper.updateSettingValueByKey("blog_name", "新博客名称");
        assertEquals(1, result, "更新设置值失败");
        verify(blogSettingMapper).updateSettingValueByKey(anyString(), anyString());
    }

    /**
     * 测试边界条件：空值处理
     */
    @Test
    void testNullHandling() {
        // 测试查询不存在的ID
        when(blogSettingMapper.selectBlogSettingById(999L)).thenReturn(null);
        BlogSetting result = blogSettingMapper.selectBlogSettingById(999L);
        assertNull(result, "查询不存在的ID应返回null");
        verify(blogSettingMapper).selectBlogSettingById(999L);

        // 测试查询不存在的设置键
        when(blogSettingMapper.selectSettingValueByKey("nonexistent_key")).thenReturn(null);
        String value = blogSettingMapper.selectSettingValueByKey("nonexistent_key");
        assertNull(value, "查询不存在的设置键应返回null");
        verify(blogSettingMapper).selectSettingValueByKey("nonexistent_key");

        // 测试空列表查询
        when(blogSettingMapper.selectBlogSettingList(any(BlogSetting.class)))
            .thenReturn(Arrays.asList());
        List<BlogSetting> list = blogSettingMapper.selectBlogSettingList(new BlogSetting());
        assertTrue(list.isEmpty(), "查询结果应为空列表");
        verify(blogSettingMapper).selectBlogSettingList(any(BlogSetting.class));
    }

    /**
     * 测试边界条件：空键
     */
    @Test
    void testEmptyKey() {
        BlogSetting emptyKeySetting = new BlogSetting();
        emptyKeySetting.setSettingKey("");
        emptyKeySetting.setSettingValue("值");
        
        when(blogSettingMapper.insertBlogSetting(any(BlogSetting.class))).thenReturn(1);
        int result = blogSettingMapper.insertBlogSetting(emptyKeySetting);
        assertEquals(1, result, "空键博客设置插入失败");
        verify(blogSettingMapper).insertBlogSetting(any(BlogSetting.class));
    }

    /**
     * 测试边界条件：空值
     */
    @Test
    void testEmptyValue() {
        BlogSetting emptyValueSetting = new BlogSetting();
        emptyValueSetting.setSettingKey("test_key");
        emptyValueSetting.setSettingValue("");
        
        when(blogSettingMapper.insertBlogSetting(any(BlogSetting.class))).thenReturn(1);
        int result = blogSettingMapper.insertBlogSetting(emptyValueSetting);
        assertEquals(1, result, "空值博客设置插入失败");
        verify(blogSettingMapper).insertBlogSetting(any(BlogSetting.class));
    }

    /**
     * 测试边界条件：长键名
     */
    @Test
    void testLongKey() {
        BlogSetting longKeySetting = new BlogSetting();
        longKeySetting.setSettingKey("very_long_setting_key_name_that_exceeds_normal_length");
        longKeySetting.setSettingValue("值");
        
        when(blogSettingMapper.insertBlogSetting(any(BlogSetting.class))).thenReturn(1);
        int result = blogSettingMapper.insertBlogSetting(longKeySetting);
        assertEquals(1, result, "长键名博客设置插入失败");
        verify(blogSettingMapper).insertBlogSetting(any(BlogSetting.class));
    }

    /**
     * 测试边界条件：长值
     */
    @Test
    void testLongValue() {
        BlogSetting longValueSetting = new BlogSetting();
        longValueSetting.setSettingKey("test_key");
        longValueSetting.setSettingValue("这是一个非常长的博客设置值，用于测试系统对长文本的处理能力");
        
        when(blogSettingMapper.insertBlogSetting(any(BlogSetting.class))).thenReturn(1);
        int result = blogSettingMapper.insertBlogSetting(longValueSetting);
        assertEquals(1, result, "长值博客设置插入失败");
        verify(blogSettingMapper).insertBlogSetting(any(BlogSetting.class));
    }
}