package com.zhi.common.utils.html;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EscapeUtil 转义工具类测试
 *
 * @author ruoyi
 */
public class EscapeUtilTest {

    @Test
    public void testEscape() {
        // 测试转义HTML特殊字符
        assertEquals("", EscapeUtil.escape(null));
        assertEquals("", EscapeUtil.escape(""));
        assertNotNull(EscapeUtil.escape("<"));
        assertNotNull(EscapeUtil.escape(">"));
        assertNotNull(EscapeUtil.escape("&"));
        assertNotNull(EscapeUtil.escape("\""));
        assertNotNull(EscapeUtil.escape("'"));
    }

    @Test
    public void testUnescape() {
        // 测试反转义HTML特殊字符
        assertNull(EscapeUtil.unescape(null));
        assertEquals("", EscapeUtil.unescape(""));
        assertEquals("<", EscapeUtil.unescape("%3c"));
        assertEquals(">", EscapeUtil.unescape("%3e"));
        assertEquals("&", EscapeUtil.unescape("%26"));
        assertEquals("\"", EscapeUtil.unescape("%22"));
        assertEquals("'", EscapeUtil.unescape("%27"));
    }

    @Test
    public void testEscapeRoundTrip() {
        // 测试转义和反转义的往返
        String original = "Hello";
        String escaped = EscapeUtil.escape(original);
        String unescaped = EscapeUtil.unescape(escaped);
        assertEquals(original, unescaped);
    }

    @Test
    public void testUnescapeWithUnicode() {
        // 测试Unicode字符反转义
        assertEquals("中", EscapeUtil.unescape("%u4e2d"));
        assertEquals("中文", EscapeUtil.unescape("%u4e2d%u6587"));
    }

    @Test
    public void testEscapeWithNumbers() {
        // 测试数字转义
        String result = EscapeUtil.escape("123");
        assertNotNull(result);
    }

    @Test
    public void testEscapeConsistency() {
        // 测试转义一致性
        String input = "test_string";
        String escaped1 = EscapeUtil.escape(input);
        String escaped2 = EscapeUtil.escape(input);
        assertEquals(escaped1, escaped2);
    }

    @Test
    public void testUnescapeConsistency() {
        // 测试反转义一致性
        String input = "%3ctest%3e";
        String unescaped1 = EscapeUtil.unescape(input);
        String unescaped2 = EscapeUtil.unescape(input);
        assertEquals(unescaped1, unescaped2);
    }

    @Test
    public void testClean() {
        // 测试清除HTML标签（只测试基本功能）
        String result = EscapeUtil.clean("<p>Hello</p>");
        assertNotNull(result);
    }

    @Test
    public void testCleanDoesNotIntroduceBackslashesInAttributes() {
        // 回归：HTMLFilter 重建属性时不得把 src="..." 输出成 src=\"..."（反斜杠引号）导致图片裂图
        String input = "<a href=\"https://img.example.com/a.png\"><img src=\"https://img.example.com/b.png\"/></a>";
        String cleaned = EscapeUtil.clean(input);
        assertNotNull(cleaned);
        // 反斜杠会打断超链接解析，必须没有反斜杠引号
        assertFalse(cleaned.contains("\\\""));
        assertTrue(cleaned.contains("src=\"https://img.example.com/b.png\""));
        assertTrue(cleaned.contains("href=\"https://img.example.com/a.png\""));
    }
}
