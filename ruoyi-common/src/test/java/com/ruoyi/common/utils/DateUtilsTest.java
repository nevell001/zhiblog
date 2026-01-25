package com.ruoyi.common.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DateUtils 日期工具类测试
 *
 * @author ruoyi
 */
public class DateUtilsTest {

    @Test
    public void testGetNowDate() {
        // 测试获取当前日期
        Date now = DateUtils.getNowDate();
        assertNotNull(now);
        assertTrue(now.getTime() <= System.currentTimeMillis());
    }

    @Test
    public void testGetDate() {
        // 测试获取当前日期字符串
        String date = DateUtils.getDate();
        assertNotNull(date);
        assertTrue(date.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testGetTime() {
        // 测试获取当前时间字符串
        String time = DateUtils.getTime();
        assertNotNull(time);
        assertTrue(time.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    public void testDateTimeNow() {
        // 测试获取当前日期时间字符串
        String dateTime = DateUtils.dateTimeNow();
        assertNotNull(dateTime);
        assertTrue(dateTime.matches("\\d{14}"));

        String dateTimeWithFormat = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
        assertNotNull(dateTimeWithFormat);
        assertTrue(dateTimeWithFormat.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testDateTimeWithDate() {
        // 测试日期转字符串
        Date date = new Date();
        String result = DateUtils.dateTime(date);
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testParseDateToStr() {
        // 测试日期转字符串
        Date date = new Date();
        String result = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    public void testDateTimeWithFormatAndString() {
        // 测试字符串转日期
        String dateStr = "2024-01-01 12:00:00";
        Date result = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, dateStr);
        assertNotNull(result);
    }

    @Test
    public void testDatePath() {
        // 测试日期路径
        String path = DateUtils.datePath();
        assertNotNull(path);
        assertTrue(path.matches("\\d{4}/\\d{2}/\\d{2}"));
    }

    @Test
    public void testDateTimeCompact() {
        // 测试紧凑日期格式
        String compact = DateUtils.dateTime();
        assertNotNull(compact);
        assertTrue(compact.matches("\\d{8}"));
    }

    @Test
    public void testParseDate() {
        // 测试解析日期字符串
        Date result1 = DateUtils.parseDate("2024-01-01");
        assertNotNull(result1);

        Date result2 = DateUtils.parseDate("2024-01-01 12:00:00");
        assertNotNull(result2);

        Date result3 = DateUtils.parseDate("2024/01/01");
        assertNotNull(result3);

        Date result4 = DateUtils.parseDate("2024.01.01");
        assertNotNull(result4);

        Date result5 = DateUtils.parseDate(null);
        assertNull(result5);

        Date result6 = DateUtils.parseDate("invalid");
        assertNull(result6);
    }

    @Test
    public void testGetServerStartDate() {
        // 测试获取服务器启动时间
        Date startDate = DateUtils.getServerStartDate();
        assertNotNull(startDate);
        assertTrue(startDate.getTime() <= System.currentTimeMillis());
    }

    @Test
    public void testDifferentDaysByMillisecond() {
        // 测试计算相差天数
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 24 * 60 * 60 * 1000L); // 1天后

        int days = DateUtils.differentDaysByMillisecond(date1, date2);
        assertEquals(1, days);

        int days2 = DateUtils.differentDaysByMillisecond(date2, date1);
        assertEquals(1, days2);
    }

    @Test
    public void testTimeDistance() {
        // 测试计算时间差
        Date startTime = new Date();
        Date endTime = new Date(startTime.getTime() + 2 * 60 * 60 * 1000L); // 2小时后

        String distance = DateUtils.timeDistance(endTime, startTime);
        assertNotNull(distance);
        assertTrue(distance.contains("小时"));
    }

    @Test
    public void testToDateFromLocalDateTime() {
        // 测试 LocalDateTime 转 Date
        LocalDateTime localDateTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        Date date = DateUtils.toDate(localDateTime);
        assertNotNull(date);
    }

    @Test
    public void testToDateFromLocalDate() {
        // 测试 LocalDate 转 Date
        LocalDate localDate = LocalDate.of(2024, 1, 1);
        Date date = DateUtils.toDate(localDate);
        assertNotNull(date);
    }

    @Test
    public void testConstants() {
        // 测试常量
        assertEquals("yyyy", DateUtils.YYYY);
        assertEquals("yyyy-MM", DateUtils.YYYY_MM);
        assertEquals("yyyy-MM-dd", DateUtils.YYYY_MM_DD);
        assertEquals("yyyyMMddHHmmss", DateUtils.YYYYMMDDHHMMSS);
        assertEquals("yyyy-MM-dd HH:mm:ss", DateUtils.YYYY_MM_DD_HH_MM_SS);
    }
}
