package com.ruoyi.common.utils.poi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ExcelUtil Excel相关处理工具类测试
 *
 * @author ruoyi
 */
public class ExcelUtilTest {

    @Test
    public void testConstants() {
        // 测试常量
        assertEquals(",", ExcelUtil.SEPARATOR);
        assertEquals("=|-|\\+|@", ExcelUtil.FORMULA_REGEX_STR);
        assertArrayEquals(new String[]{"=", "-", "+", "@"}, ExcelUtil.FORMULA_STR);
        assertEquals(65536, ExcelUtil.sheetSize);
    }

    @Test
    public void testFormulaRegexStr() {
        // 测试公式正则表达式
        String formulaStr = ExcelUtil.FORMULA_REGEX_STR;
        assertNotNull(formulaStr);
        assertTrue(formulaStr.contains("="));
        assertTrue(formulaStr.contains("-"));
        assertTrue(formulaStr.contains("\\+"));
        assertTrue(formulaStr.contains("@"));
    }

    @Test
    public void testFormulaStr() {
        // 测试公式字符串数组
        String[] formulaStr = ExcelUtil.FORMULA_STR;
        assertNotNull(formulaStr);
        assertEquals(4, formulaStr.length);
        assertEquals("=", formulaStr[0]);
        assertEquals("-", formulaStr[1]);
        assertEquals("+", formulaStr[2]);
        assertEquals("@", formulaStr[3]);
    }

    @Test
    public void testSheetSize() {
        // 测试工作表大小
        assertEquals(65536, ExcelUtil.sheetSize);
    }
}