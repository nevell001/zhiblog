package com.ruoyi.common.utils.sign;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Base64 工具类测试
 *
 * @author ruoyi
 */
public class Base64Test {

    @Test
    public void testEncodeNull() {
        assertNull(Base64.encode(null));
    }

    @Test
    public void testEncodeEmptyArray() {
        byte[] empty = new byte[0];
        assertEquals("", Base64.encode(empty));
    }

    @Test
    public void testEncodeSimpleString() {
        String original = "hello";
        byte[] bytes = original.getBytes(StandardCharsets.UTF_8);
        String encoded = Base64.encode(bytes);
        assertNotNull(encoded);
    }

    @Test
    public void testEncodeWithPadding() {
        byte[] bytes1 = "M".getBytes(StandardCharsets.UTF_8);
        String encoded1 = Base64.encode(bytes1);
        assertNotNull(encoded1);
        assertTrue(encoded1.endsWith("=="));

        byte[] bytes2 = "Ma".getBytes(StandardCharsets.UTF_8);
        String encoded2 = Base64.encode(bytes2);
        assertNotNull(encoded2);
        assertTrue(encoded2.endsWith("="));

        byte[] bytes3 = "Man".getBytes(StandardCharsets.UTF_8);
        String encoded3 = Base64.encode(bytes3);
        assertNotNull(encoded3);
        assertFalse(encoded3.endsWith("="));
    }

    @Test
    public void testDecodeNull() {
        assertNull(Base64.decode(null));
    }

    @Test
    public void testDecodeEmptyString() {
        byte[] result = Base64.decode("");
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testDecodeSimpleString() {
        String encoded = "aGVsbG8=";
        byte[] decoded = Base64.decode(encoded);
        assertNotNull(decoded);
        String original = new String(decoded, StandardCharsets.UTF_8);
        assertEquals("hello", original);
    }

    @Test
    public void testDecodeWithWhitespace() {
        String encoded = "aGVs bG8=";
        byte[] decoded = Base64.decode(encoded);
        assertNotNull(decoded);
        assertEquals("hello", new String(decoded, StandardCharsets.UTF_8));
    }

    @Test
    public void testDecodeInvalidBase64() {
        assertNull(Base64.decode("!!!invalid!!!"));
        assertNull(Base64.decode("abc")); // Not divisible by 4
    }

    @Test
    public void testEncodeDecodeRoundTrip() {
        String original = "Hello World!";
        byte[] originalBytes = original.getBytes(StandardCharsets.UTF_8);
        String encoded = Base64.encode(originalBytes);
        byte[] decoded = Base64.decode(encoded);
        String result = new String(decoded, StandardCharsets.UTF_8);
        assertEquals(original, result);
    }

    @Test
    public void testEncodeDecodeRoundTripWithBinary() {
        byte[] original = new byte[]{0x00, 0x01, 0x02, 0x03};
        String encoded = Base64.encode(original);
        byte[] decoded = Base64.decode(encoded);
        assertArrayEquals(original, decoded);
    }

    @Test
    public void testEncodeMultipleByteStrings() {
        String[] testStrings = {"", "a", "ab", "abc", "abcd"};
        for (String str : testStrings) {
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            String encoded = Base64.encode(bytes);
            byte[] decoded = Base64.decode(encoded);
            assertEquals(str, new String(decoded, StandardCharsets.UTF_8));
        }
    }
}