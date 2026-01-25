package com.ruoyi.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Arith 精确浮点数运算工具类测试
 *
 * @author ruoyi
 */
public class ArithTest {

    @Test
    public void testAdd() {
        // 测试加法运算
        assertEquals(3.0, Arith.add(1.0, 2.0));
        assertEquals(0.0, Arith.add(1.0, -1.0));
        assertEquals(-3.0, Arith.add(-1.0, -2.0));
        assertEquals(0.3, Arith.add(0.1, 0.2), 0.0001); // 解决浮点数精度问题
    }

    @Test
    public void testSub() {
        // 测试减法运算
        assertEquals(1.0, Arith.sub(3.0, 2.0));
        assertEquals(2.0, Arith.sub(1.0, -1.0));
        assertEquals(1.0, Arith.sub(-1.0, -2.0));
        assertEquals(0.1, Arith.sub(0.3, 0.2), 0.0001); // 解决浮点数精度问题
    }

    @Test
    public void testMul() {
        // 测试乘法运算
        assertEquals(6.0, Arith.mul(2.0, 3.0));
        assertEquals(-6.0, Arith.mul(2.0, -3.0));
        assertEquals(6.0, Arith.mul(-2.0, -3.0));
        assertEquals(0.0, Arith.mul(2.0, 0.0));
        assertEquals(0.06, Arith.mul(0.2, 0.3), 0.0001); // 解决浮点数精度问题
    }

    @Test
    public void testDiv() {
        // 测试除法运算（默认精度）
        assertEquals(2.0, Arith.div(6.0, 3.0));
        assertEquals(-2.0, Arith.div(6.0, -3.0));
        assertEquals(0.3333333333, Arith.div(1.0, 3.0), 0.000000001);
        assertEquals(0.0, Arith.div(0.0, 3.0));
    }

    @Test
    public void testDivWithScale() {
        // 测试除法运算（指定精度）
        assertEquals(0.33, Arith.div(1.0, 3.0, 2));
        assertEquals(0.3333, Arith.div(1.0, 3.0, 4));
        assertEquals(0.3333333333, Arith.div(1.0, 3.0, 10));
        assertEquals(2.5, Arith.div(5.0, 2.0, 1));
        assertEquals(0.0, Arith.div(0.0, 3.0, 2));
    }

    @Test
    public void testDivWithInvalidScale() {
        // 测试除法运算（无效精度）
        assertThrows(IllegalArgumentException.class, () -> {
            Arith.div(1.0, 3.0, -1);
        });
    }

    @Test
    public void testRound() {
        // 测试四舍五入
        assertEquals(3.0, Arith.round(3.14159, 0));
        assertEquals(3.1, Arith.round(3.14159, 1));
        assertEquals(3.14, Arith.round(3.14159, 2));
        assertEquals(3.142, Arith.round(3.14159, 3));
        assertEquals(4.0, Arith.round(3.5, 0)); // 四舍五入
        assertEquals(3.0, Arith.round(3.4, 0)); // 四舍五入
    }

    @Test
    public void testRoundWithInvalidScale() {
        // 测试四舍五入（无效精度）
        assertThrows(IllegalArgumentException.class, () -> {
            Arith.round(3.14159, -1);
        });
    }

    @Test
    public void testFloatingPointPrecision() {
        // 测试浮点数精度问题
        // 传统的浮点数运算会有精度问题
        assertFalse(0.1 + 0.2 == 0.3);
        assertFalse(0.3 - 0.2 == 0.1);

        // 使用 Arith 工具类可以解决精度问题
        assertEquals(0.3, Arith.add(0.1, 0.2), 0.0001);
        assertEquals(0.1, Arith.sub(0.3, 0.2), 0.0001);
    }

    @Test
    public void testComplexCalculations() {
        // 测试复杂计算
        double result1 = Arith.add(Arith.mul(0.1, 0.2), Arith.div(0.3, 0.4));
        assertTrue(result1 > 0.7 && result1 < 0.8);

        double result2 = Arith.sub(Arith.round(Arith.div(1.0, 3.0), 2), 0.33);
        assertTrue(Math.abs(result2) < 0.01);
    }
}