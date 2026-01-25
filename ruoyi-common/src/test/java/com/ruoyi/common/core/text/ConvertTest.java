package com.ruoyi.common.core.text;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Convert 类型转换器测试
 *
 * @author ruoyi
 */
public class ConvertTest {

    @Test
    public void testToStr() {
        // 测试转换为字符串
        assertEquals("test", Convert.toStr("test"));
        assertEquals("123", Convert.toStr(123));
        assertEquals("default", Convert.toStr(null, "default"));
        assertEquals("test", Convert.toStr("test", "default"));
    }

    @Test
    public void testToChar() {
        // 测试转换为字符
        assertEquals('a', Convert.toChar('a'));
        assertEquals('a', Convert.toChar("a"));
        assertEquals('t', Convert.toChar("test"));
        assertNull(Convert.toChar(null));
        assertEquals('d', Convert.toChar(null, 'd'));
        assertEquals('d', Convert.toChar("", 'd'));
    }

    @Test
    public void testToByte() {
        // 测试转换为 byte
        assertEquals((byte) 10, Convert.toByte((byte) 10));
        assertEquals((byte) 10, Convert.toByte(10));
        assertEquals((byte) 10, Convert.toByte("10"));
        assertNull(Convert.toByte(null));
        assertEquals((byte) 5, Convert.toByte(null, (byte) 5));
        assertEquals((byte) 5, Convert.toByte("", (byte) 5));
        assertEquals((byte) 5, Convert.toByte("invalid", (byte) 5));
    }

    @Test
    public void testToShort() {
        // 测试转换为 Short
        assertEquals((short) 100, Convert.toShort((short) 100));
        assertEquals((short) 100, Convert.toShort(100));
        assertEquals((short) 100, Convert.toShort("100"));
        assertNull(Convert.toShort(null));
        assertEquals((short) 50, Convert.toShort(null, (short) 50));
        assertEquals((short) 50, Convert.toShort("", (short) 50));
        assertEquals((short) 50, Convert.toShort("invalid", (short) 50));
    }

    @Test
    public void testToNumber() {
        // 测试转换为 Number
        assertEquals(123, Convert.toNumber(123));
        assertEquals(123L, Convert.toNumber(123L));
        assertEquals(123.45, Convert.toNumber(123.45));
        assertEquals(123L, Convert.toNumber("123")); // NumberFormat.parse 返回 Long 类型
        assertNull(Convert.toNumber(null));
        assertEquals(999, Convert.toNumber(null, 999));
        assertEquals(999, Convert.toNumber("", 999));
        assertEquals(999, Convert.toNumber("invalid", 999));
    }

    @Test
    public void testToInt() {
        // 测试转换为 int
        assertEquals(123, Convert.toInt(123));
        assertEquals(123, Convert.toInt(123L));
        assertEquals(123, Convert.toInt("123"));
        assertNull(Convert.toInt(null));
        assertEquals(999, Convert.toInt(null, 999));
        assertEquals(999, Convert.toInt("", 999));
        assertEquals(999, Convert.toInt("invalid", 999));
    }

    @Test
    public void testToIntArray() {
        // 测试转换为 Integer 数组
        assertArrayEquals(new Integer[]{1, 2, 3}, Convert.toIntArray("1,2,3"));
        assertArrayEquals(new Integer[]{1, 2, 3}, Convert.toIntArray(",", "1,2,3"));
        assertArrayEquals(new Integer[0], Convert.toIntArray(null));
        assertArrayEquals(new Integer[0], Convert.toIntArray(""));
        assertArrayEquals(new Integer[]{1, 0, 3}, Convert.toIntArray("1,abc,3"));
    }

    @Test
    public void testToLongArray() {
        // 测试转换为 Long 数组
        assertArrayEquals(new Long[]{1L, 2L, 3L}, Convert.toLongArray("1,2,3"));
        assertArrayEquals(new Long[]{1L, 2L, 3L}, Convert.toLongArray(",", "1,2,3"));
        assertArrayEquals(new Long[0], Convert.toLongArray(null));
        assertArrayEquals(new Long[0], Convert.toLongArray(""));
        assertArrayEquals(new Long[]{1L, null, 3L}, Convert.toLongArray("1,abc,3"));
    }

    @Test
    public void testToStrArray() {
        // 测试转换为 String 数组
        assertArrayEquals(new String[]{"a", "b", "c"}, Convert.toStrArray("a,b,c"));
        assertArrayEquals(new String[]{"a", "b", "c"}, Convert.toStrArray(",", "a,b,c"));
        assertArrayEquals(new String[0], Convert.toStrArray(null));
        assertArrayEquals(new String[0], Convert.toStrArray(""));
    }

    @Test
    public void testToLong() {
        // 测试转换为 long
        assertEquals(123L, Convert.toLong(123));
        assertEquals(123L, Convert.toLong(123L));
        assertEquals(123L, Convert.toLong("123"));
        assertEquals(100L, Convert.toLong("1e2")); // 科学计数法
        assertNull(Convert.toLong(null));
        assertEquals(999L, Convert.toLong(null, 999L));
        assertEquals(999L, Convert.toLong("", 999L));
        assertEquals(999L, Convert.toLong("invalid", 999L));
    }

    @Test
    public void testToDouble() {
        // 测试转换为 double
        assertEquals(123.45, Convert.toDouble(123.45));
        assertEquals(123.0, Convert.toDouble(123));
        assertEquals(123.45, Convert.toDouble("123.45"));
        assertEquals(100.0, Convert.toDouble("1e2")); // 科学计数法
        assertNull(Convert.toDouble(null));
        assertEquals(999.99, Convert.toDouble(null, 999.99));
        assertEquals(999.99, Convert.toDouble("", 999.99));
        assertEquals(999.99, Convert.toDouble("invalid", 999.99));
    }

    @Test
    public void testToFloat() {
        // 测试转换为 Float
        assertEquals(123.45f, Convert.toFloat(123.45f));
        assertEquals(123.0f, Convert.toFloat(123));
        assertEquals(123.45f, Convert.toFloat("123.45"));
        assertNull(Convert.toFloat(null));
        assertEquals(999.99f, Convert.toFloat(null, 999.99f));
        assertEquals(999.99f, Convert.toFloat("", 999.99f));
        assertEquals(999.99f, Convert.toFloat("invalid", 999.99f));
    }

    @Test
    public void testToBool() {
        // 测试转换为 boolean
        assertTrue(Convert.toBool(true));
        assertTrue(Convert.toBool("true"));
        assertTrue(Convert.toBool("yes"));
        assertTrue(Convert.toBool("ok"));
        assertTrue(Convert.toBool("1"));
        assertTrue(Convert.toBool("是"));
        assertFalse(Convert.toBool(false));
        assertFalse(Convert.toBool("false"));
        assertFalse(Convert.toBool("no"));
        assertFalse(Convert.toBool("0"));
        assertFalse(Convert.toBool("否"));
        assertNull(Convert.toBool(null));
        assertTrue(Convert.toBool(null, true));
        assertFalse(Convert.toBool("", false));
        assertFalse(Convert.toBool("invalid", false));
    }

    @Test
    public void testToEnum() {
        // 测试转换为 Enum
        enum TestEnum { VALUE1, VALUE2, VALUE3 }

        assertEquals(TestEnum.VALUE1, Convert.toEnum(TestEnum.class, "VALUE1"));
        assertEquals(TestEnum.VALUE2, Convert.toEnum(TestEnum.class, TestEnum.VALUE2));
        assertNull(Convert.toEnum(TestEnum.class, null));
        assertEquals(TestEnum.VALUE3, Convert.toEnum(TestEnum.class, null, TestEnum.VALUE3));
        assertEquals(TestEnum.VALUE1, Convert.toEnum(TestEnum.class, "", TestEnum.VALUE1));
        assertEquals(TestEnum.VALUE1, Convert.toEnum(TestEnum.class, "INVALID", TestEnum.VALUE1));
    }

    @Test
    public void testToBigInteger() {
        // 测试转换为 BigInteger
        assertEquals(new BigInteger("123"), Convert.toBigInteger(123));
        assertEquals(new BigInteger("123"), Convert.toBigInteger(123L));
        assertEquals(new BigInteger("123"), Convert.toBigInteger("123"));
        assertNull(Convert.toBigInteger(null));
        assertEquals(new BigInteger("999"), Convert.toBigInteger(null, new BigInteger("999")));
        assertEquals(new BigInteger("999"), Convert.toBigInteger("", new BigInteger("999")));
        assertEquals(new BigInteger("999"), Convert.toBigInteger("invalid", new BigInteger("999")));
    }

    @Test
    public void testToBigDecimal() {
        // 测试转换为 BigDecimal
        assertEquals(new BigDecimal("123.45"), Convert.toBigDecimal(123.45));
        assertEquals(new BigDecimal("123"), Convert.toBigDecimal(123));
        assertEquals(new BigDecimal("123"), Convert.toBigDecimal(123L));
        assertEquals(new BigDecimal("123.45"), Convert.toBigDecimal("123.45"));
        assertNull(Convert.toBigDecimal(null));
        assertEquals(new BigDecimal("999.99"), Convert.toBigDecimal(null, new BigDecimal("999.99")));
        assertEquals(new BigDecimal("999.99"), Convert.toBigDecimal("", new BigDecimal("999.99")));
        assertEquals(new BigDecimal("999.99"), Convert.toBigDecimal("invalid", new BigDecimal("999.99")));
    }

    @Test
    public void testUtf8Str() {
        // 测试 UTF-8 字符串转换
        assertEquals("test", Convert.utf8Str("test"));
        assertEquals("测试", Convert.utf8Str("测试"));
        assertNull(Convert.utf8Str(null));
    }

    @Test
    public void testStr() {
        // 测试字符串转换
        assertEquals("test", Convert.str("test", "UTF-8"));
        assertEquals("test", Convert.str("test", StandardCharsets.UTF_8));

        // 测试 byte[] 转换
        byte[] bytes = "test".getBytes(StandardCharsets.UTF_8);
        assertEquals("test", Convert.str(bytes, "UTF-8"));
        assertEquals("test", Convert.str(bytes, StandardCharsets.UTF_8));

        // 测试 ByteBuffer 转换
        ByteBuffer buffer1 = ByteBuffer.wrap(bytes);
        assertEquals("test", Convert.str(buffer1, "UTF-8"));
        ByteBuffer buffer2 = ByteBuffer.wrap(bytes);
        assertEquals("test", Convert.str(buffer2, StandardCharsets.UTF_8));

        // 测试 null
        assertNull(Convert.str((Object) null, "UTF-8"));
        assertNull(Convert.str((byte[]) null, "UTF-8"));
        assertNull(Convert.str((ByteBuffer) null, "UTF-8"));
    }

    @Test
    public void testToSBC() {
        // 测试半角转全角
        assertEquals("ｔｅｓｔ", Convert.toSBC("test"));
        assertEquals("　ｔｅｓｔ　", Convert.toSBC(" test "));
        assertEquals("ｔｅｓｔ", Convert.toSBC("test", null));

        // 测试不替换的字符集合
        Set<Character> notConvertSet = new HashSet<>();
        notConvertSet.add('e');
        assertEquals("ｔeｓｔ", Convert.toSBC("test", notConvertSet));
    }

    @Test
    public void testToDBC() {
        // 测试全角转半角
        assertEquals("test", Convert.toDBC("ｔｅｓｔ"));
        assertEquals(" test ", Convert.toDBC("　ｔｅｓｔ　"));
        assertEquals("test", Convert.toDBC("ｔｅｓｔ", null));

        // 测试不替换的字符集合
        Set<Character> notConvertSet = new HashSet<>();
        notConvertSet.add('ｅ');
        assertEquals("tｅst", Convert.toDBC("ｔｅｓｔ", notConvertSet));
    }

    @Test
    public void testDigitUppercase() {
        // 测试数字金额大写转换
        assertEquals("零元整", Convert.digitUppercase(0));
        assertEquals("壹元整", Convert.digitUppercase(1));
        assertEquals("壹拾元整", Convert.digitUppercase(10));
        assertEquals("壹佰元整", Convert.digitUppercase(100));
        assertEquals("壹仟元整", Convert.digitUppercase(1000));
        assertEquals("壹万零壹元整", Convert.digitUppercase(10001));
        assertEquals("壹拾元整", Convert.digitUppercase(10.0));
        assertEquals("壹拾元伍角", Convert.digitUppercase(10.5));
        assertEquals("壹拾元伍角伍分", Convert.digitUppercase(10.55));
        assertEquals("负壹元整", Convert.digitUppercase(-1));
        assertEquals("负壹拾元整", Convert.digitUppercase(-10));
    }
}