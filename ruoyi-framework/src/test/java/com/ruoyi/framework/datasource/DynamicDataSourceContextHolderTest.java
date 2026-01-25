package com.ruoyi.framework.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 动态数据源上下文测试类
 * 
 * @author ruoyi
 */
public class DynamicDataSourceContextHolderTest
{
    private static final String MASTER_DATA_SOURCE = "master";
    private static final String SLAVE_DATA_SOURCE = "slave";

    @AfterEach
    public void tearDown()
    {
        // 每个测试后清理数据源
        DynamicDataSourceContextHolder.clearDataSourceType();
    }

    @Test
    public void testSetDataSourceType()
    {
        // 测试设置数据源
        DynamicDataSourceContextHolder.setDataSourceType(MASTER_DATA_SOURCE);
        assertEquals(MASTER_DATA_SOURCE, DynamicDataSourceContextHolder.getDataSourceType());
    }

    @Test
    public void testGetDataSourceType()
    {
        // 测试获取数据源
        DynamicDataSourceContextHolder.setDataSourceType(SLAVE_DATA_SOURCE);
        String dsType = DynamicDataSourceContextHolder.getDataSourceType();
        assertEquals(SLAVE_DATA_SOURCE, dsType);
    }

    @Test
    public void testClearDataSourceType()
    {
        // 测试清空数据源
        DynamicDataSourceContextHolder.setDataSourceType(MASTER_DATA_SOURCE);
        DynamicDataSourceContextHolder.clearDataSourceType();
        assertNull(DynamicDataSourceContextHolder.getDataSourceType());
    }

    @Test
    public void testGetDataSourceTypeWhenNotSet()
    {
        // 测试未设置数据源时获取
        String dsType = DynamicDataSourceContextHolder.getDataSourceType();
        assertNull(dsType);
    }

    @Test
    public void testMultipleSetAndClear()
    {
        // 测试多次设置和清空
        DynamicDataSourceContextHolder.setDataSourceType(MASTER_DATA_SOURCE);
        assertEquals(MASTER_DATA_SOURCE, DynamicDataSourceContextHolder.getDataSourceType());

        DynamicDataSourceContextHolder.setDataSourceType(SLAVE_DATA_SOURCE);
        assertEquals(SLAVE_DATA_SOURCE, DynamicDataSourceContextHolder.getDataSourceType());

        DynamicDataSourceContextHolder.clearDataSourceType();
        assertNull(DynamicDataSourceContextHolder.getDataSourceType());
    }
}