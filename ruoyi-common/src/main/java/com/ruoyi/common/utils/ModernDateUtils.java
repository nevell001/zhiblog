package com.ruoyi.common.utils;

import java.lang.management.ManagementFactory;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 现代化时间工具类 - 使用Java 8+ DateTime API
 * 替代原有的SimpleDateFormat，提供线程安全的时间操作
 * 
 * @author nevell
 * @version 2.0
 */
public class ModernDateUtils
{
    public static String YYYY = "yyyy";
    public static String YYYY_MM = "yyyy-MM";
    public static String YYYY_MM_DD = "yyyy-MM-dd";
    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    // 常用格式化器 - 线程安全且高效
    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD);
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
    private static final DateTimeFormatter COMPACT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS);
    private static final DateTimeFormatter PATH_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    // 缓存常用格式化器，提高性能
    private static final DateTimeFormatter[] CACHED_FORMATTERS = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
        DateTimeFormatter.ofPattern("yyyy-MM"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"),
        DateTimeFormatter.ofPattern("yyyy/MM"),
        DateTimeFormatter.ofPattern("yyyy.MM.dd"),
        DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"),
        DateTimeFormatter.ofPattern("yyyy.MM")
    };

    /**
     * 获取当前日期
     */
    public static LocalDate getNowDate()
    {
        return LocalDate.now();
    }

    /**
     * 获取当前日期时间
     */
    public static LocalDateTime getNowDateTime()
    {
        return LocalDateTime.now();
    }

    /**
     * 获取当前日期，默认格式为yyyy-MM-dd
     */
    public static String getDate()
    {
        return getNowDate().format(DEFAULT_DATE_FORMATTER);
    }

    /**
     * 获取当前时间，默认格式为yyyy-MM-dd HH:mm:ss
     */
    public static String getTime()
    {
        return getNowDateTime().format(DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * 获取当前日期时间，紧凑格式yyyyMMddHHmmss
     */
    public static String getCompactDateTime()
    {
        return getNowDateTime().format(COMPACT_DATETIME_FORMATTER);
    }

    /**
     * 格式化当前日期时间为指定格式
     */
    public static String formatNow(String pattern)
    {
        return getNowDateTime().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化指定日期为字符串
     */
    public static String format(LocalDateTime dateTime, String pattern)
    {
        if (dateTime == null)
        {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化指定日期为字符串
     */
    public static String format(LocalDate date, String pattern)
    {
        if (date == null)
        {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析字符串为LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeStr)
    {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty())
        {
            return null;
        }
        
        return parseDateTime(dateTimeStr, CACHED_FORMATTERS);
    }

    /**
     * 解析字符串为LocalDateTime（尝试多种格式）
     */
    private static LocalDateTime parseDateTime(String dateTimeStr, DateTimeFormatter[] formatters)
    {
        for (DateTimeFormatter formatter : formatters)
        {
            try
            {
                return LocalDateTime.parse(dateTimeStr, formatter);
            }
            catch (Exception ignored)
            {
                // 尝试下一个格式
            }
        }
        
        // 如果都不匹配，尝试解析为日期
        try
        {
            return LocalDate.parse(dateTimeStr, DEFAULT_DATE_FORMATTER).atStartOfDay();
        }
        catch (Exception ignored)
        {
            return null;
        }
    }

    /**
     * 解析字符串为LocalDate
     */
    public static LocalDate parseDate(String dateStr)
    {
        if (dateStr == null || dateStr.trim().isEmpty())
        {
            return null;
        }
        
        for (DateTimeFormatter formatter : CACHED_FORMATTERS)
        {
            try
            {
                return LocalDate.parse(dateStr, formatter);
            }
            catch (Exception ignored)
            {
                // 尝试下一个格式
            }
        }
        
        return null;
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static String datePath()
    {
        return getNowDate().format(PATH_DATE_FORMATTER);
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static String compactDate()
    {
        return getNowDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * 计算两个日期之间的天数差
     */
    public static long differentDays(LocalDate date1, LocalDate date2)
    {
        if (date1 == null || date2 == null)
        {
            return 0;
        }
        return Math.abs(ChronoUnit.DAYS.between(date1, date2));
    }

    /**
     * 计算两个时间之间的时间差
     */
    public static String timeDistance(LocalDateTime endTime, LocalDateTime startTime)
    {
        if (endTime == null || startTime == null)
        {
            return "未知";
        }
        
        Duration duration = Duration.between(startTime, endTime);
        
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        
        if (days > 0)
        {
            return days + "天" + hours + "小时" + minutes + "分钟";
        }
        else if (hours > 0)
        {
            return hours + "小时" + minutes + "分钟";
        }
        else
        {
            return minutes + "分钟";
        }
    }

    /**
     * LocalDateTime转换为Date（兼容现有API）
     */
    public static Date toDate(LocalDateTime localDateTime)
    {
        if (localDateTime == null)
        {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDate转换为Date（兼容现有API）
     */
    public static Date toDate(LocalDate localDate)
    {
        if (localDate == null)
        {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date转换为LocalDateTime（兼容现有API）
     */
    public static LocalDateTime toLocalDateTime(Date date)
    {
        if (date == null)
        {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Date转换为LocalDate（兼容现有API）
     */
    public static LocalDate toLocalDate(Date date)
    {
        if (date == null)
        {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 获取服务器启动时间
     */
    public static LocalDateTime getServerStartDate()
    {
        long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        return Instant.ofEpochMilli(startTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 检查日期是否在指定范围内
     */
    public static boolean isDateInRange(LocalDateTime date, LocalDateTime start, LocalDateTime end)
    {
        if (date == null || start == null || end == null)
        {
            return false;
        }
        return !date.isBefore(start) && !date.isAfter(end);
    }

    /**
     * 获取当天的开始时间
     */
    public static LocalDateTime getStartOfDay()
    {
        return LocalDate.now().atStartOfDay();
    }

    /**
     * 获取当天的结束时间
     */
    public static LocalDateTime getEndOfDay()
    {
        return LocalDate.now().atTime(23, 59, 59, 999999999);
    }

    /**
     * 获取本周的第一天（周一）
     */
    public static LocalDate getFirstDayOfWeek()
    {
        return LocalDate.now().with(DayOfWeek.MONDAY);
    }

    /**
     * 获取本周的最后一天（周日）
     */
    public static LocalDate getLastDayOfWeek()
    {
        return LocalDate.now().with(DayOfWeek.SUNDAY);
    }

    /**
     * 获取本月的第一天
     */
    public static LocalDate getFirstDayOfMonth()
    {
        return LocalDate.now().withDayOfMonth(1);
    }

    /**
     * 获取本月的最后一天
     */
    public static LocalDate getLastDayOfMonth()
    {
        return LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
    }
}