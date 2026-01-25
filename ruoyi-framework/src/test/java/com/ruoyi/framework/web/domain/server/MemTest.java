package com.ruoyi.framework.web.domain.server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 内存相关信息测试类
 * 
 * @author ruoyi
 */
public class MemTest
{
    @Test
    public void testGetTotal()
    {
        // 测试获取内存总量（转换为GB）
        Mem mem = new Mem();
        // 8GB = 8 * 1024 * 1024 * 1024 = 8589934592 bytes
        long totalBytes = 8L * 1024 * 1024 * 1024;
        mem.setTotal(totalBytes);
        assertEquals(8.0, mem.getTotal());
    }

    @Test
    public void testSetTotal()
    {
        // 测试设置内存总量
        Mem mem = new Mem();
        long totalBytes = 16L * 1024 * 1024 * 1024;
        mem.setTotal(totalBytes);
        assertEquals(16.0, mem.getTotal());
    }

    @Test
    public void testGetUsed()
    {
        // 测试获取已用内存（转换为GB）
        Mem mem = new Mem();
        long usedBytes = 4L * 1024 * 1024 * 1024;
        mem.setUsed(usedBytes);
        assertEquals(4.0, mem.getUsed());
    }

    @Test
    public void testSetUsed()
    {
        // 测试设置已用内存
        Mem mem = new Mem();
        long usedBytes = 6L * 1024 * 1024 * 1024;
        mem.setUsed(usedBytes);
        assertEquals(6.0, mem.getUsed());
    }

    @Test
    public void testGetFree()
    {
        // 测试获取剩余内存（转换为GB）
        Mem mem = new Mem();
        long freeBytes = 2L * 1024 * 1024 * 1024;
        mem.setFree(freeBytes);
        assertEquals(2.0, mem.getFree());
    }

    @Test
    public void testSetFree()
    {
        // 测试设置剩余内存
        Mem mem = new Mem();
        long freeBytes = 3L * 1024 * 1024 * 1024;
        mem.setFree(freeBytes);
        assertEquals(3.0, mem.getFree());
    }

    @Test
    public void testGetUsage()
    {
        // 测试获取内存使用率
        Mem mem = new Mem();
        long totalBytes = 8L * 1024 * 1024 * 1024;
        long usedBytes = 4L * 1024 * 1024 * 1024;
        mem.setTotal(totalBytes);
        mem.setUsed(usedBytes);
        // 4GB / 8GB * 100 = 50.0%
        assertEquals(50.0, mem.getUsage());
    }

    @Test
    public void testGetUsageWithHighUsage()
    {
        // 测试高内存使用率
        Mem mem = new Mem();
        long totalBytes = 8L * 1024 * 1024 * 1024;
        long usedBytes = 7L * 1024 * 1024 * 1024;
        mem.setTotal(totalBytes);
        mem.setUsed(usedBytes);
        // 7GB / 8GB * 100 = 87.5%
        assertEquals(87.5, mem.getUsage());
    }

    @Test
    public void testGetUsageWithLowUsage()
    {
        // 测试低内存使用率
        Mem mem = new Mem();
        long totalBytes = 8L * 1024 * 1024 * 1024;
        long usedBytes = 1L * 1024 * 1024 * 1024;
        mem.setTotal(totalBytes);
        mem.setUsed(usedBytes);
        // 1GB / 8GB * 100 = 12.5%
        assertEquals(12.5, mem.getUsage());
    }

    @Test
    public void testCalculateMemoryInfo()
    {
        // 测试计算内存信息
        Mem mem = new Mem();
        long totalBytes = 16L * 1024 * 1024 * 1024;
        long usedBytes = 8L * 1024 * 1024 * 1024;
        long freeBytes = 8L * 1024 * 1024 * 1024;
        
        mem.setTotal(totalBytes);
        mem.setUsed(usedBytes);
        mem.setFree(freeBytes);

        assertEquals(16.0, mem.getTotal());
        assertEquals(8.0, mem.getUsed());
        assertEquals(8.0, mem.getFree());
        assertEquals(50.0, mem.getUsage());
    }

    @Test
    public void testGetTotalWithNonPowerOfTwo()
    {
        // 测试非2的幂次方的内存值
        Mem mem = new Mem();
        long totalBytes = 12L * 1024 * 1024 * 1024;
        mem.setTotal(totalBytes);
        assertEquals(12.0, mem.getTotal());
    }
}