package com.ruoyi.common.utils;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StringUtils 工具类测试
 *
 * @author ruoyi
 */
public class StringUtilsTest {

    @Test
    public void testNvl() {
        // 测试 nvl 方法
        String str1 = "test";
        String str2 = null;

        assertEquals("test", StringUtils.nvl(str1, "default"));
        assertEquals("default", StringUtils.nvl(str2, "default"));
    }

    @Test
    public void testIsEmptyCollection() {
        // 测试 Collection 是否为空
        List<String> list1 = null;
        List<String> list2 = new ArrayList<>();
        List<String> list3 = Arrays.asList("a", "b");

        assertTrue(com.ruoyi.common.utils.StringUtils.isEmpty(list1));
        assertTrue(com.ruoyi.common.utils.StringUtils.isEmpty(list2));
        assertFalse(com.ruoyi.common.utils.StringUtils.isEmpty(list3));
    }

    @Test
    public void testIsNotEmptyCollection() {
        // 测试 Collection 是否非空
        List<String> list1 = null;
        List<String> list2 = new ArrayList<>();
        List<String> list3 = Arrays.asList("a", "b");

        assertFalse(com.ruoyi.common.utils.StringUtils.isNotEmpty(list1));
        assertFalse(com.ruoyi.common.utils.StringUtils.isNotEmpty(list2));
        assertTrue(com.ruoyi.common.utils.StringUtils.isNotEmpty(list3));
    }

    @Test
    public void testIsEmptyArray() {
        // 测试数组是否为空
        String[] arr1 = null;
        String[] arr2 = new String[0];
        String[] arr3 = new String[]{"a", "b"};

        assertTrue(com.ruoyi.common.utils.StringUtils.isEmpty(arr1));
        assertTrue(com.ruoyi.common.utils.StringUtils.isEmpty(arr2));
        assertFalse(com.ruoyi.common.utils.StringUtils.isEmpty(arr3));
    }

    @Test
    public void testIsNotEmptyArray() {
        // 测试数组是否非空
        String[] arr1 = null;
        String[] arr2 = new String[0];
        String[] arr3 = new String[]{"a", "b"};

        assertFalse(com.ruoyi.common.utils.StringUtils.isNotEmpty(arr1));
        assertFalse(com.ruoyi.common.utils.StringUtils.isNotEmpty(arr2));
        assertTrue(com.ruoyi.common.utils.StringUtils.isNotEmpty(arr3));
    }

    @Test
    public void testIsEmptyMap() {
        // 测试 Map 是否为空
        Map<String, String> map1 = null;
        Map<String, String> map2 = new HashMap<>();
        Map<String, String> map3 = new HashMap<>();
        map3.put("key", "value");

        assertTrue(com.ruoyi.common.utils.StringUtils.isEmpty(map1));
        assertTrue(com.ruoyi.common.utils.StringUtils.isEmpty(map2));
        assertFalse(com.ruoyi.common.utils.StringUtils.isEmpty(map3));
    }

    @Test
    public void testIsNotEmptyMap() {
        // 测试 Map 是否非空
        Map<String, String> map1 = null;
        Map<String, String> map2 = new HashMap<>();
        Map<String, String> map3 = new HashMap<>();
        map3.put("key", "value");

        assertFalse(com.ruoyi.common.utils.StringUtils.isNotEmpty(map1));
        assertFalse(com.ruoyi.common.utils.StringUtils.isNotEmpty(map2));
        assertTrue(com.ruoyi.common.utils.StringUtils.isNotEmpty(map3));
    }

    @Test
    public void testIsEmptyString() {
        // 测试字符串是否为空
        assertTrue(com.ruoyi.common.utils.StringUtils.isEmpty((String)null));
        assertTrue(com.ruoyi.common.utils.StringUtils.isEmpty(""));
        assertTrue(com.ruoyi.common.utils.StringUtils.isEmpty("   "));
        assertFalse(com.ruoyi.common.utils.StringUtils.isEmpty("test"));
    }

    @Test
    public void testIsNotEmptyString() {
        // 测试字符串是否非空
        assertFalse(com.ruoyi.common.utils.StringUtils.isNotEmpty((String)null));
        assertFalse(com.ruoyi.common.utils.StringUtils.isNotEmpty(""));
        assertFalse(com.ruoyi.common.utils.StringUtils.isNotEmpty("   "));
        assertTrue(com.ruoyi.common.utils.StringUtils.isNotEmpty("test"));
    }

    @Test
    public void testIsNull() {
        // 测试对象是否为空
        assertTrue(StringUtils.isNull(null));
        assertFalse(StringUtils.isNull("test"));
        assertFalse(StringUtils.isNull(0));
        assertFalse(StringUtils.isNull(false));
    }

    @Test
    public void testIsNotNull() {
        // 测试对象是否非空
        assertFalse(StringUtils.isNotNull(null));
        assertTrue(StringUtils.isNotNull("test"));
        assertTrue(StringUtils.isNotNull(0));
        assertTrue(StringUtils.isNotNull(false));
    }

    @Test
    public void testIsArray() {
        // 测试对象是否是数组
        assertFalse(StringUtils.isArray(null));
        assertFalse(StringUtils.isArray("test"));
        assertTrue(StringUtils.isArray(new int[]{1, 2, 3}));
        assertTrue(StringUtils.isArray(new String[]{"a", "b"}));
    }

    @Test
    public void testTrim() {
        // 测试去空格
        assertEquals("", StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim("   "));
        assertEquals("test", StringUtils.trim("  test  "));
    }

    @Test
    public void testHide() {
        // 测试字符串隐藏
        assertEquals("", StringUtils.hide(null, 1, 3));
        assertEquals("t**t", StringUtils.hide("test", 1, 3));
        assertEquals("t***", StringUtils.hide("test", 1, 10));
        assertEquals("", StringUtils.hide("test", 5, 3));
        assertEquals("", StringUtils.hide("test", 3, 1));
    }

    @Test
    public void testSubstring() {
        // 测试截取字符串
        assertEquals("", StringUtils.substring(null, 1));
        assertEquals("", StringUtils.substring("test", 10));
        assertEquals("est", StringUtils.substring("test", 1));
        assertEquals("t", StringUtils.substring("test", -1));
        assertEquals("test", StringUtils.substring("test", -10));

        assertEquals("", StringUtils.substring(null, 1, 3));
        assertEquals("", StringUtils.substring("test", 10, 15));
        assertEquals("es", StringUtils.substring("test", 1, 3));
        assertEquals("s", StringUtils.substring("test", -2, -1));
        assertEquals("es", StringUtils.substring("test", 1, -1));
    }

    @Test
    public void testSubstringBetweenLast() {
        // 测试截取字符串（第一个 open 和最后一个 close 之间）
        assertEquals("", StringUtils.substringBetweenLast(null, "<", ">"));
        assertEquals("", StringUtils.substringBetweenLast("test", null, ">"));
        assertEquals("", StringUtils.substringBetweenLast("test", "<", null));
        assertEquals("", StringUtils.substringBetweenLast("test", "<", ">"));
        assertEquals("content", StringUtils.substringBetweenLast("<content>", "<", ">"));
        assertEquals("content1>content2", StringUtils.substringBetweenLast("<content1>content2>", "<", ">"));
    }

    @Test
    public void testHasText() {
        // 测试字符串是否有文本
        assertFalse(StringUtils.hasText(null));
        assertFalse(StringUtils.hasText(""));
        assertFalse(StringUtils.hasText("   "));
        assertTrue(StringUtils.hasText("test"));
        assertTrue(StringUtils.hasText("  test  "));
    }

    @Test
    public void testFormat() {
        // 测试格式化文本
        assertEquals("this is a for b", StringUtils.format("this is {} for {}", "a", "b"));
        assertEquals("this is {} for a", StringUtils.format("this is \\{} for {}", "a", "b"));
        assertEquals("this is \\a for b", StringUtils.format("this is \\\\{} for {}", "a", "b"));
        assertNull(StringUtils.format(null, "a", "b"));
        assertEquals("test", StringUtils.format("test"));
    }

    @Test
    public void testIshttp() {
        // 测试是否为 http(s):// 开头
        assertTrue(StringUtils.ishttp("http://example.com"));
        assertTrue(StringUtils.ishttp("https://example.com"));
        assertFalse(StringUtils.ishttp("ftp://example.com"));
        assertFalse(StringUtils.ishttp("example.com"));
        assertFalse(StringUtils.ishttp(null));
    }

    @Test
    public void testStr2Set() {
        // 测试字符串转 set
        Set<String> set1 = StringUtils.str2Set("a,b,c", ",");
        assertEquals(3, set1.size());
        assertTrue(set1.contains("a"));
        assertTrue(set1.contains("b"));
        assertTrue(set1.contains("c"));

        Set<String> set2 = StringUtils.str2Set(null, ",");
        assertTrue(set2.isEmpty());

        Set<String> set3 = StringUtils.str2Set("a,b,c", ",");
        assertEquals(3, set3.size());
    }

    @Test
    public void testStr2List() {
        // 测试字符串转 list
        List<String> list1 = StringUtils.str2List("a,b,c", ",");
        assertEquals(3, list1.size());
        assertEquals("a", list1.get(0));
        assertEquals("b", list1.get(1));
        assertEquals("c", list1.get(2));

        List<String> list2 = StringUtils.str2List(null, ",");
        assertTrue(list2.isEmpty());

        List<String> list3 = StringUtils.str2List("a, b, c", ",");
        assertEquals(3, list3.size());

        List<String> list4 = StringUtils.str2List("a, , c", ",", true, true);
        assertEquals(2, list4.size());

        List<String> list5 = StringUtils.str2List("a, b, c", ",", false, true);
        assertEquals(3, list5.size());
        assertEquals("a", list5.get(0));
        assertEquals("b", list5.get(1));
        assertEquals("c", list5.get(2));
    }

    @Test
    public void testContainsAny() {
        // 测试集合是否包含数组中的任意元素
        Collection<String> collection = Arrays.asList("a", "b", "c");

        assertTrue(com.ruoyi.common.utils.StringUtils.containsAny(collection, "a", "d"));
        assertTrue(com.ruoyi.common.utils.StringUtils.containsAny(collection, "d", "b"));
        assertFalse(com.ruoyi.common.utils.StringUtils.containsAny(collection, "d", "e"));
        assertFalse(com.ruoyi.common.utils.StringUtils.containsAny((Collection<String>)null, "a", "b"));
        assertFalse(com.ruoyi.common.utils.StringUtils.containsAny(collection, (String[]) null));
    }

    @Test
    public void testContainsAnyIgnoreCase() {
        // 测试字符串是否包含任意一个字符串（忽略大小写）
        assertTrue(StringUtils.containsAnyIgnoreCase("Test String", "TEST", "hello"));
        assertTrue(StringUtils.containsAnyIgnoreCase("Test String", "hello", "STRING"));
        assertFalse(StringUtils.containsAnyIgnoreCase("Test String", "hello", "world"));
        assertFalse(StringUtils.containsAnyIgnoreCase(null, "test"));
        assertFalse(StringUtils.containsAnyIgnoreCase("Test String", (CharSequence[]) null));
    }

    @Test
    public void testToUnderScoreCase() {
        // 测试驼峰转下划线命名
        assertEquals("user_name", StringUtils.toUnderScoreCase("userName"));
        assertEquals("user_name", StringUtils.toUnderScoreCase("UserName"));
        assertEquals("user_name_test", StringUtils.toUnderScoreCase("userNameTest"));
        assertNull(StringUtils.toUnderScoreCase(null));
        assertEquals("", StringUtils.toUnderScoreCase(""));
    }

    @Test
    public void testInStringIgnoreCase() {
        // 测试字符串是否在数组中（忽略大小写）
        assertTrue(StringUtils.inStringIgnoreCase("TEST", "test", "hello"));
        assertTrue(StringUtils.inStringIgnoreCase("test", "TEST", "hello"));
        assertFalse(StringUtils.inStringIgnoreCase("test", "hello", "world"));
        assertFalse(StringUtils.inStringIgnoreCase(null, "test", "hello"));
        assertFalse(StringUtils.inStringIgnoreCase("test", (String[]) null));
    }

    @Test
    public void testConvertToCamelCase() {
        // 测试下划线转驼峰命名
        assertEquals("HelloWorld", StringUtils.convertToCamelCase("hello_world"));
        assertEquals("HelloWorld", StringUtils.convertToCamelCase("HELLO_WORLD"));
        assertEquals("HelloWorld", StringUtils.convertToCamelCase("__hello_world__"));
        assertEquals("Hello", StringUtils.convertToCamelCase("hello"));
        assertEquals("", StringUtils.convertToCamelCase(null));
        assertEquals("", StringUtils.convertToCamelCase(""));
    }

    @Test
    public void testToCamelCase() {
        // 测试下划线转驼峰命名（小驼峰）
        assertEquals("userName", StringUtils.toCamelCase("user_name"));
        assertEquals("userName", StringUtils.toCamelCase("USER_NAME"));
        assertEquals("user", StringUtils.toCamelCase("user"));
        assertNull(StringUtils.toCamelCase(null));
    }

    @Test
    public void testMatches() {
        // 测试字符串是否匹配列表中的任意一个模式
        List<String> patterns = Arrays.asList("/admin/**", "/user/**", "/login");

        assertTrue(StringUtils.matches("/admin/user/list", patterns));
        assertTrue(StringUtils.matches("/user/profile", patterns));
        assertTrue(StringUtils.matches("/login", patterns));
        assertFalse(StringUtils.matches("/test", patterns));
        assertFalse(StringUtils.matches(null, patterns));
        assertFalse(StringUtils.matches("/admin", null));
    }

    @Test
    public void testIsMatch() {
        // 测试字符串是否匹配模式
        assertTrue(StringUtils.isMatch("/admin/**", "/admin/user/list"));
        assertTrue(StringUtils.isMatch("/user/*", "/user/profile"));
        assertTrue(StringUtils.isMatch("/login", "/login"));
        assertFalse(StringUtils.isMatch("/admin/*", "/admin/user/list"));
        assertFalse(StringUtils.isMatch("/test", "/admin"));
    }

    @Test
    public void testCast() {
        // 测试类型转换
        String str = "test";
        String result = StringUtils.cast(str);
        assertEquals("test", result);

        Integer num = 123;
        Integer resultNum = StringUtils.cast(num);
        assertEquals(123, resultNum);
    }

    @Test
    public void testPadl() {
        // 测试数字左补齐
        assertEquals("00123", StringUtils.padl(123, 5));
        assertEquals("123", StringUtils.padl(123, 3));
        assertEquals("23", StringUtils.padl(123, 2));
    }

    @Test
    public void testPadlString() {
        // 测试字符串左补齐
        assertEquals("00123", StringUtils.padl("123", 5, '0'));
        assertEquals("123", StringUtils.padl("123", 3, '0'));
        assertEquals("23", StringUtils.padl("123", 2, '0'));
        assertEquals("00000", StringUtils.padl(null, 5, '0'));
        assertEquals("*****", StringUtils.padl(null, 5, '*'));
    }
}
