package com.ruoyi.framework.web.domain.server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JVM相关信息测试类
 * 
 * @author ruoyi
 */
public class JvmTest
{
    @Test
    public void testGetTotal()
    {
        // 测试获取JVM占用的内存总数（转换为MB）
        Jvm jvm = new Jvm();
        jvm.setTotal(512 * 1024 * 1024); // 512MB
        assertEquals(512.0, jvm.getTotal());
    }

    @Test
    public void testSetTotal()
    {
        // 测试设置JVM占用的内存总数
        Jvm jvm = new Jvm();
        jvm.setTotal(1024 * 1024 * 1024); // 1024MB
        assertEquals(1024.0, jvm.getTotal());
    }

    @Test
    public void testGetMax()
    {
        // 测试获取JVM最大可用内存总数（转换为MB）
        Jvm jvm = new Jvm();
        double maxBytes = 2048.0 * 1024 * 1024; // 2048MB
        jvm.setMax(maxBytes);
        assertEquals(2048.0, jvm.getMax());
    }

    @Test
    public void testSetMax()
    {
        // 测试设置JVM最大可用内存总数
        Jvm jvm = new Jvm();
        double maxBytes = 4096.0 * 1024 * 1024; // 4096MB
        jvm.setMax(maxBytes);
        assertEquals(4096.0, jvm.getMax());
    }

    @Test
    public void testGetFree()
    {
        // 测试获取JVM空闲内存（转换为MB）
        Jvm jvm = new Jvm();
        jvm.setFree(256 * 1024 * 1024); // 256MB
        assertEquals(256.0, jvm.getFree());
    }

    @Test
    public void testSetFree()
    {
        // 测试设置JVM空闲内存
        Jvm jvm = new Jvm();
        jvm.setFree(128 * 1024 * 1024); // 128MB
        assertEquals(128.0, jvm.getFree());
    }

    @Test
    public void testGetUsed()
    {
        // 测试获取JVM已用内存（转换为MB）
        Jvm jvm = new Jvm();
        jvm.setTotal(512 * 1024 * 1024); // 512MB
        jvm.setFree(256 * 1024 * 1024); // 256MB
        // 512MB - 256MB = 256MB
        assertEquals(256.0, jvm.getUsed());
    }

    @Test
    public void testGetUsage()
    {
        // 测试获取JVM内存使用率
        Jvm jvm = new Jvm();
        jvm.setTotal(512 * 1024 * 1024); // 512MB
        jvm.setFree(256 * 1024 * 1024); // 256MB
        // (512MB - 256MB) / 512MB * 100 = 50.0%
        assertEquals(50.0, jvm.getUsage());
    }

    @Test
    public void testGetUsageWithHighUsage()
    {
        // 测试高内存使用率
        Jvm jvm = new Jvm();
        jvm.setTotal(1024 * 1024 * 1024); // 1024MB
        jvm.setFree(128 * 1024 * 1024); // 128MB
        // (1024MB - 128MB) / 1024MB * 100 = 87.5%
        assertEquals(87.5, jvm.getUsage());
    }

    @Test
    public void testGetUsageWithLowUsage()
    {
        // 测试低内存使用率
        Jvm jvm = new Jvm();
        jvm.setTotal(1024 * 1024 * 1024); // 1024MB
        jvm.setFree(768 * 1024 * 1024); // 768MB
        // (1024MB - 768MB) / 1024MB * 100 = 25.0%
        assertEquals(25.0, jvm.getUsage());
    }

    @Test
    public void testGetName()
    {
        // 测试获取JDK名称
        Jvm jvm = new Jvm();
        String name = jvm.getName();
        assertNotNull(name);
        assertFalse(name.isEmpty());
    }

    @Test
    public void testGetVersion()
    {
        // 测试获取JDK版本
        Jvm jvm = new Jvm();
        jvm.setVersion("17.0.1");
        assertEquals("17.0.1", jvm.getVersion());
    }

    @Test
    public void testSetVersion()
    {
        // 测试设置JDK版本
        Jvm jvm = new Jvm();
        jvm.setVersion("11.0.12");
        assertEquals("11.0.12", jvm.getVersion());
    }

    @Test
    public void testGetHome()
    {
        // 测试获取JDK路径
        Jvm jvm = new Jvm();
        jvm.setHome("/usr/lib/jvm/java-17-openjdk");
        assertEquals("/usr/lib/jvm/java-17-openjdk", jvm.getHome());
    }

    @Test
    public void testSetHome()
    {
        // 测试设置JDK路径
        Jvm jvm = new Jvm();
        jvm.setHome("/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home");
        assertEquals("/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home", jvm.getHome());
    }

    @Test
    public void testGetStartTime()
    {
        // 测试获取JDK启动时间
        Jvm jvm = new Jvm();
        String startTime = jvm.getStartTime();
        assertNotNull(startTime);
        assertFalse(startTime.isEmpty());
    }

    @Test
    public void testGetRunTime()
    {
        // 测试获取JDK运行时间
        Jvm jvm = new Jvm();
        String runTime = jvm.getRunTime();
        assertNotNull(runTime);
        assertFalse(runTime.isEmpty());
    }

    @Test
    public void testGetInputArgs()
    {
        // 测试获取运行参数
        Jvm jvm = new Jvm();
        String inputArgs = jvm.getInputArgs();
        assertNotNull(inputArgs);
        assertFalse(inputArgs.isEmpty());
    }

    @Test
    public void testCalculateJvmInfo()
    {
        // 测试计算JVM信息
        Jvm jvm = new Jvm();
        jvm.setTotal(1024.0 * 1024 * 1024); // 1024MB
        jvm.setMax(2048.0 * 1024 * 1024); // 2048MB
        jvm.setFree(512.0 * 1024 * 1024); // 512MB
        jvm.setVersion("17.0.1");
        jvm.setHome("/usr/lib/jvm/java-17-openjdk");

        assertEquals(1024.0, jvm.getTotal());
        assertEquals(2048.0, jvm.getMax());
        assertEquals(512.0, jvm.getFree());
        assertEquals(512.0, jvm.getUsed());
        assertEquals(50.0, jvm.getUsage());
        assertEquals("17.0.1", jvm.getVersion());
        assertEquals("/usr/lib/jvm/java-17-openjdk", jvm.getHome());
    }
}