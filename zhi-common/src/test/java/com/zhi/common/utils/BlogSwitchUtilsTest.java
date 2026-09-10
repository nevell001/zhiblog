package com.zhi.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 博客功能开关判定工具测试
 * 
 * <p>口径必须与前端 store.isFeatureEnabled 一致，否则会出现“开关不生效”。</p>
 *
 * @author test
 * @date 2026-09-10
 */
class BlogSwitchUtilsTest
{
    @Test
    void testBlankTreatsAsOn()
    {
        assertTrue(BlogSwitchUtils.isOn(null));
        assertTrue(BlogSwitchUtils.isOn(""));
        assertTrue(BlogSwitchUtils.isOn("   "));
        assertFalse(BlogSwitchUtils.isOff(null));
    }

    @Test
    void testTrueVariantsAreOn()
    {
        assertTrue(BlogSwitchUtils.isOn("true"));
        assertTrue(BlogSwitchUtils.isOn("TRUE"));
        assertTrue(BlogSwitchUtils.isOn("1"));
        assertTrue(BlogSwitchUtils.isOn(" true "));
    }

    @Test
    void testFalseVariantsAreOff()
    {
        assertTrue(BlogSwitchUtils.isOff("false"));
        assertTrue(BlogSwitchUtils.isOff("FALSE"));
        assertTrue(BlogSwitchUtils.isOff("0"));
        assertTrue(BlogSwitchUtils.isOff(" false "));
        assertFalse(BlogSwitchUtils.isOn("false"));
        assertFalse(BlogSwitchUtils.isOn("0"));
    }

    @Test
    void testUnknownValueTreatsAsOn()
    {
        // 非预期的值不应当导致功能被意外关闭
        assertTrue(BlogSwitchUtils.isOn("yes"));
        assertTrue(BlogSwitchUtils.isOn("2"));
    }
}
