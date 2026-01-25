package com.ruoyi.framework.web.domain.server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CPU相关信息测试类
 * 
 * @author ruoyi
 */
public class CpuTest
{
    @Test
    public void testGetCpuNum()
    {
        // 测试获取CPU核心数
        Cpu cpu = new Cpu();
        cpu.setCpuNum(4);
        assertEquals(4, cpu.getCpuNum());
    }

    @Test
    public void testSetCpuNum()
    {
        // 测试设置CPU核心数
        Cpu cpu = new Cpu();
        cpu.setCpuNum(8);
        assertEquals(8, cpu.getCpuNum());
    }

    @Test
    public void testGetTotal()
    {
        // 测试获取CPU总使用率
        Cpu cpu = new Cpu();
        cpu.setTotal(0.5);
        // 0.5 * 100 = 50.0
        assertEquals(50.0, cpu.getTotal());
    }

    @Test
    public void testSetTotal()
    {
        // 测试设置CPU总使用率
        Cpu cpu = new Cpu();
        cpu.setTotal(0.75);
        // getTotal() 会返回乘以 100 后的值
        assertEquals(75.0, cpu.getTotal());
    }

    @Test
    public void testGetSys()
    {
        // 测试获取CPU系统使用率
        Cpu cpu = new Cpu();
        cpu.setTotal(1.0);
        cpu.setSys(0.3);
        // 0.3 / 1.0 * 100 = 30.0
        assertEquals(30.0, cpu.getSys());
    }

    @Test
    public void testSetSys()
    {
        // 测试设置CPU系统使用率
        Cpu cpu = new Cpu();
        cpu.setTotal(1.0);
        cpu.setSys(0.25);
        // 0.25 / 1.0 * 100 = 25.0
        assertEquals(25.0, cpu.getSys());
    }

    @Test
    public void testGetUsed()
    {
        // 测试获取CPU用户使用率
        Cpu cpu = new Cpu();
        cpu.setTotal(1.0);
        cpu.setUsed(0.4);
        // 0.4 / 1.0 * 100 = 40.0
        assertEquals(40.0, cpu.getUsed());
    }

    @Test
    public void testSetUsed()
    {
        // 测试设置CPU用户使用率
        Cpu cpu = new Cpu();
        cpu.setTotal(1.0);
        cpu.setUsed(0.35);
        // 0.35 / 1.0 * 100 = 35.0
        assertEquals(35.0, cpu.getUsed());
    }

    @Test
    public void testGetWait()
    {
        // 测试获取CPU等待率
        Cpu cpu = new Cpu();
        cpu.setTotal(1.0);
        cpu.setWait(0.1);
        // 0.1 / 1.0 * 100 = 10.0
        assertEquals(10.0, cpu.getWait());
    }

    @Test
    public void testSetWait()
    {
        // 测试设置CPU等待率
        Cpu cpu = new Cpu();
        cpu.setTotal(1.0);
        cpu.setWait(0.05);
        // 0.05 / 1.0 * 100 = 5.0
        assertEquals(5.0, cpu.getWait());
    }

    @Test
    public void testGetFree()
    {
        // 测试获取CPU空闲率
        Cpu cpu = new Cpu();
        cpu.setTotal(1.0);
        cpu.setFree(0.2);
        // 0.2 / 1.0 * 100 = 20.0
        assertEquals(20.0, cpu.getFree());
    }

    @Test
    public void testSetFree()
    {
        // 测试设置CPU空闲率
        Cpu cpu = new Cpu();
        cpu.setTotal(1.0);
        cpu.setFree(0.15);
        // 0.15 / 1.0 * 100 = 15.0
        assertEquals(15.0, cpu.getFree());
    }

    @Test
    public void testCalculatePercentages()
    {
        // 测试计算各项百分比
        Cpu cpu = new Cpu();
        cpu.setCpuNum(4);
        cpu.setTotal(1.0);
        cpu.setSys(0.2);
        cpu.setUsed(0.3);
        cpu.setWait(0.1);
        cpu.setFree(0.4);

        assertEquals(4, cpu.getCpuNum());
        assertEquals(100.0, cpu.getTotal());
        assertEquals(20.0, cpu.getSys());
        assertEquals(30.0, cpu.getUsed());
        assertEquals(10.0, cpu.getWait());
        assertEquals(40.0, cpu.getFree());
    }
}