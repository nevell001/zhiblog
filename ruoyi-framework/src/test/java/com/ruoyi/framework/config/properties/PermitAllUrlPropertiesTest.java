package com.ruoyi.framework.config.properties;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PermitAllUrlProperties 测试类
 * 
 * @author ruoyi
 */
public class PermitAllUrlPropertiesTest
{
    @Test
    public void testGetUrls()
    {
        // 测试获取 URL 列表
        PermitAllUrlProperties properties = new PermitAllUrlProperties();
        List<String> urls = properties.getUrls();
        assertNotNull(urls);
        // 初始时应该是空列表
        assertTrue(urls.isEmpty());
    }

    @Test
    public void testSetUrls()
    {
        // 测试设置 URL 列表
        PermitAllUrlProperties properties = new PermitAllUrlProperties();
        List<String> newUrls = new ArrayList<>();
        newUrls.add("/api/public/**");
        newUrls.add("/api/auth/**");
        
        properties.setUrls(newUrls);
        List<String> urls = properties.getUrls();
        assertEquals(2, urls.size());
        assertTrue(urls.contains("/api/public/**"));
        assertTrue(urls.contains("/api/auth/**"));
    }

    @Test
    public void testSetEmptyUrls()
    {
        // 测试设置空的 URL 列表
        PermitAllUrlProperties properties = new PermitAllUrlProperties();
        properties.setUrls(new ArrayList<>());
        assertTrue(properties.getUrls().isEmpty());
    }

    @Test
    public void testSetNullUrls()
    {
        // 测试设置 null URL 列表
        PermitAllUrlProperties properties = new PermitAllUrlProperties();
        properties.setUrls(null);
        assertNull(properties.getUrls());
    }

    @Test
    public void testSetUrlsWithDuplicates()
    {
        // 测试设置包含重复的 URL 列表
        PermitAllUrlProperties properties = new PermitAllUrlProperties();
        List<String> newUrls = new ArrayList<>();
        newUrls.add("/api/public/**");
        newUrls.add("/api/auth/**");
        newUrls.add("/api/public/**"); // 重复
        
        properties.setUrls(newUrls);
        List<String> urls = properties.getUrls();
        // setUrls 方法直接设置，不自动去重
        assertEquals(3, urls.size());
    }
}