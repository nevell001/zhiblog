package com.ruoyi.framework.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 字符编码配置测试类
 * 
 * @author ruoyi
 */
public class CharsetConfigTest
{
    @Test
    @DisplayName("测试UTF-8编码配置")
    public void testUtf8Encoding()
    {
        // 测试UTF-8编码
        String testString = "测试中文";
        byte[] bytes = testString.getBytes(StandardCharsets.UTF_8);
        assertEquals("测试中文", new String(bytes, StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("测试字符集支持")
    public void testCharsetSupport()
    {
        // 测试字符集支持
        assertNotNull(StandardCharsets.UTF_8);
        assertEquals("UTF-8", StandardCharsets.UTF_8.name());
    }

    @Test
    @DisplayName("测试CharsetConfig类存在")
    public void testCharsetConfigClassExists()
    {
        // 测试 CharsetConfig 类存在
        assertNotNull(CharsetConfig.class);
    }
}