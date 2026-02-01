package com.ruoyi.common.utils.sign;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Md5Utils 加密工具类测试
 *
 * @author ruoyi
 */
public class Md5UtilsTest {

    @Test
    public void testHashEmptyString() {
        String result = Md5Utils.hash("");
        assertNotNull(result);
        assertEquals(32, result.length());
    }

    @Test
    public void testHashSimpleString() {
        String result = Md5Utils.hash("hello");
        assertNotNull(result);
        assertEquals(32, result.length());
    }

    @Test
    public void testHashWithNumbers() {
        String result = Md5Utils.hash("123456");
        assertNotNull(result);
        assertEquals(32, result.length());
    }

    @Test
    public void testHashWithChineseCharacters() {
        String result = Md5Utils.hash("测试中文");
        assertNotNull(result);
        assertEquals(32, result.length());
    }

    @Test
    public void testHashConsistency() {
        String input = "test_string";
        String result1 = Md5Utils.hash(input);
        String result2 = Md5Utils.hash(input);
        assertEquals(result1, result2);
    }

    @Test
    public void testHashUniqueness() {
        String result1 = Md5Utils.hash("string1");
        String result2 = Md5Utils.hash("string2");
        assertNotEquals(result1, result2);
    }

    @Test
    public void testHashCaseSensitivity() {
        String result1 = Md5Utils.hash("Hello");
        String result2 = Md5Utils.hash("hello");
        assertNotEquals(result1, result2);
    }

    @Test
    public void testHashOutputFormat() {
        String result = Md5Utils.hash("test");
        assertEquals(32, result.length());
        assertTrue(result.matches("[a-f0-9]{32}"));
    }

    @Test
    public void testHashDeterministic() {
        String input = "deterministic_test";
        String hash1 = Md5Utils.hash(input);
        String hash2 = Md5Utils.hash(input);
        String hash3 = Md5Utils.hash(input);
        assertEquals(hash1, hash2);
        assertEquals(hash2, hash3);
    }

    @Test
    public void testHashLength() {
        assertEquals(32, Md5Utils.hash("a").length());
        assertEquals(32, Md5Utils.hash("abcdefghijklmnopqrstuvwxyz").length());
    }
}