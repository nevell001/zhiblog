package com.ruoyi.quartz.util;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CronUtils Cron表达式工具类测试
 *
 * @author ruoyi
 */
public class CronUtilsTest {

    @Test
    public void testIsValid() {
        // 测试有效的 Cron 表达式
        assertTrue(CronUtils.isValid("0 0 12 * * ?")); // 每天中午12点
        assertTrue(CronUtils.isValid("0 0/5 * * * ?")); // 每5分钟
        assertTrue(CronUtils.isValid("0 0 12 ? * MON-FRI")); // 周一到周五中午12点
        assertTrue(CronUtils.isValid("0 0 12 1 * ?")); // 每月1号中午12点
        assertTrue(CronUtils.isValid("0 0 12 ? * 6L")); // 每月最后一个周五中午12点

        // 测试无效的 Cron 表达式
        assertFalse(CronUtils.isValid(""));
        assertFalse(CronUtils.isValid("invalid cron"));
        assertFalse(CronUtils.isValid("0 0 25 * * ?")); // 无效的小时
    }

    @Test
    public void testGetInvalidMessage() {
        // 测试有效的 Cron 表达式
        assertNull(CronUtils.getInvalidMessage("0 0 12 * * ?"));
        assertNull(CronUtils.getInvalidMessage("0 0/5 * * * ?"));
        assertNull(CronUtils.getInvalidMessage("0 0 12 ? * MON-FRI"));

        // 测试无效的 Cron 表达式
        assertNotNull(CronUtils.getInvalidMessage(""));
        assertNotNull(CronUtils.getInvalidMessage("invalid cron"));
        assertNotNull(CronUtils.getInvalidMessage("0 0 25 * * ?"));
    }

    @Test
    public void testGetNextExecution() {
        // 测试获取下一次执行时间
        Date nextExecution1 = CronUtils.getNextExecution("0 0 12 * * ?");
        assertNotNull(nextExecution1);
        assertTrue(nextExecution1.after(new Date()));

        Date nextExecution2 = CronUtils.getNextExecution("0 0/5 * * * ?");
        assertNotNull(nextExecution2);
        assertTrue(nextExecution2.after(new Date()));

        // 测试无效的 Cron 表达式
        assertThrows(IllegalArgumentException.class, () -> {
            CronUtils.getNextExecution("invalid cron");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            CronUtils.getNextExecution(null);
        });
    }

    @Test
    public void testGetNextExecutionWithDifferentCron() {
        // 测试不同的 Cron 表达式
        // 每分钟执行
        Date next1 = CronUtils.getNextExecution("0 * * * * ?");
        assertNotNull(next1);

        // 每小时执行
        Date next2 = CronUtils.getNextExecution("0 0 * * * ?");
        assertNotNull(next2);

        // 每天执行
        Date next3 = CronUtils.getNextExecution("0 0 0 * * ?");
        assertNotNull(next3);

        // 每周执行
        Date next4 = CronUtils.getNextExecution("0 0 0 ? * MON");
        assertNotNull(next4);

        // 每月执行
        Date next5 = CronUtils.getNextExecution("0 0 0 1 * ?");
        assertNotNull(next5);
    }
}