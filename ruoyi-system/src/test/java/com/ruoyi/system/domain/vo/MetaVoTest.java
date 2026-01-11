package com.ruoyi.system.domain.vo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * MetaVo 测试类
 * 
 * @author ruoyi
 */
class MetaVoTest {

    @Test
    void testDefaultConstructor() {
        MetaVo metaVo = new MetaVo();
        assertNull(metaVo.getTitle());
        assertNull(metaVo.getIcon());
        assertFalse(metaVo.isNoCache());
        assertNull(metaVo.getLink());
    }

    @Test
    void testConstructorWithTitleAndIcon() {
        MetaVo metaVo = new MetaVo("测试标题", "test-icon");
        assertEquals("测试标题", metaVo.getTitle());
        assertEquals("test-icon", metaVo.getIcon());
        assertFalse(metaVo.isNoCache());
        assertNull(metaVo.getLink());
    }

    @Test
    void testConstructorWithTitleIconAndNoCache() {
        MetaVo metaVo = new MetaVo("测试标题", "test-icon", true);
        assertEquals("测试标题", metaVo.getTitle());
        assertEquals("test-icon", metaVo.getIcon());
        assertTrue(metaVo.isNoCache());
        assertNull(metaVo.getLink());
    }

    @Test
    void testConstructorWithTitleIconAndLink() {
        MetaVo metaVo = new MetaVo("测试标题", "test-icon", "http://example.com");
        assertEquals("测试标题", metaVo.getTitle());
        assertEquals("test-icon", metaVo.getIcon());
        assertFalse(metaVo.isNoCache());
        assertEquals("http://example.com", metaVo.getLink());
    }

    @Test
    void testConstructorWithTitleIconNoCacheAndValidLink() {
        MetaVo metaVo = new MetaVo("测试标题", "test-icon", true, "http://example.com");
        assertEquals("测试标题", metaVo.getTitle());
        assertEquals("test-icon", metaVo.getIcon());
        assertTrue(metaVo.isNoCache());
        assertEquals("http://example.com", metaVo.getLink());
    }

    @Test
    void testConstructorWithTitleIconNoCacheAndHttpsLink() {
        MetaVo metaVo = new MetaVo("测试标题", "test-icon", true, "https://example.com");
        assertEquals("测试标题", metaVo.getTitle());
        assertEquals("test-icon", metaVo.getIcon());
        assertTrue(metaVo.isNoCache());
        assertEquals("https://example.com", metaVo.getLink());
    }

    @Test
    void testConstructorWithTitleIconNoCacheAndInvalidLink() {
        MetaVo metaVo = new MetaVo("测试标题", "test-icon", true, "invalid-link");
        assertEquals("测试标题", metaVo.getTitle());
        assertEquals("test-icon", metaVo.getIcon());
        assertTrue(metaVo.isNoCache());
        assertNull(metaVo.getLink());
    }

    @Test
    void testConstructorWithTitleIconNoCacheAndNullLink() {
        MetaVo metaVo = new MetaVo("测试标题", "test-icon", true, null);
        assertEquals("测试标题", metaVo.getTitle());
        assertEquals("test-icon", metaVo.getIcon());
        assertTrue(metaVo.isNoCache());
        assertNull(metaVo.getLink());
    }

    @Test
    void testSetTitle() {
        MetaVo metaVo = new MetaVo();
        metaVo.setTitle("新标题");
        assertEquals("新标题", metaVo.getTitle());
    }

    @Test
    void testSetIcon() {
        MetaVo metaVo = new MetaVo();
        metaVo.setIcon("new-icon");
        assertEquals("new-icon", metaVo.getIcon());
    }

    @Test
    void testSetNoCache() {
        MetaVo metaVo = new MetaVo();
        metaVo.setNoCache(true);
        assertTrue(metaVo.isNoCache());
        metaVo.setNoCache(false);
        assertFalse(metaVo.isNoCache());
    }

    @Test
    void testSetLink() {
        MetaVo metaVo = new MetaVo();
        metaVo.setLink("http://new-link.com");
        assertEquals("http://new-link.com", metaVo.getLink());
    }

    @Test
    void testSetNullLink() {
        MetaVo metaVo = new MetaVo();
        metaVo.setLink(null);
        assertNull(metaVo.getLink());
    }

    @Test
    void testChainedSetters() {
        MetaVo metaVo = new MetaVo();
        metaVo.setTitle("标题");
        metaVo.setIcon("icon");
        metaVo.setNoCache(true);
        metaVo.setLink("http://example.com");

        assertEquals("标题", metaVo.getTitle());
        assertEquals("icon", metaVo.getIcon());
        assertTrue(metaVo.isNoCache());
        assertEquals("http://example.com", metaVo.getLink());
    }
}