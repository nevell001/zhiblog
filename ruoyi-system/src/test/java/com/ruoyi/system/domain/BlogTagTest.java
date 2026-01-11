package com.ruoyi.system.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 博客标签对象单元测试
 *
 * @author test
 * @date 2026-01-11
 */
class BlogTagTest {

    /**
     * 测试 getDelFlag 方法 - delFlag 为 null
     */
    @Test
    void testGetDelFlag_Null() {
        BlogTag tag = new BlogTag();
        tag.setDelFlag(null);
        
        Integer result = tag.getDelFlag();
        
        // 验证返回默认值 0
        assertEquals(0, result);
    }

    /**
     * 测试 getDelFlag 方法 - delFlag 为 0
     */
    @Test
    void testGetDelFlag_Zero() {
        BlogTag tag = new BlogTag();
        tag.setDelFlag(0);
        
        Integer result = tag.getDelFlag();
        
        // 验证返回 0
        assertEquals(0, result);
    }

    /**
     * 测试 getDelFlag 方法 - delFlag 为 1
     */
    @Test
    void testGetDelFlag_One() {
        BlogTag tag = new BlogTag();
        tag.setDelFlag(1);
        
        Integer result = tag.getDelFlag();
        
        // 验证返回 1
        assertEquals(1, result);
    }

    /**
     * 测试兼容性方法 - setTagId 和 getTagId
     */
    @Test
    void testTagIdCompatibility() {
        BlogTag tag = new BlogTag();
        tag.setTagId(100L);
        
        // 验证通过 setTagId 设置的值可以通过 getId 和 getTagId 获取
        assertEquals(100L, tag.getId());
        assertEquals(100L, tag.getTagId());
    }

    /**
     * 测试兼容性方法 - setTagName 和 getTagName
     */
    @Test
    void testTagNameCompatibility() {
        BlogTag tag = new BlogTag();
        tag.setTagName("测试标签");
        
        // 验证通过 setTagName 设置的值可以通过 getName 和 getTagName 获取
        assertEquals("测试标签", tag.getName());
        assertEquals("测试标签", tag.getTagName());
    }
}