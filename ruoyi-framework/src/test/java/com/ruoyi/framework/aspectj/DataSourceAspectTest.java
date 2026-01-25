package com.ruoyi.framework.aspectj;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.framework.datasource.DynamicDataSourceContextHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 多数据源处理切面测试类
 * 
 * @author ruoyi
 */
public class DataSourceAspectTest
{
    private DataSourceAspect dataSourceAspect;

    @BeforeEach
    public void setUp()
    {
        dataSourceAspect = new DataSourceAspect();
    }

    @AfterEach
    public void tearDown()
    {
        // 清理数据源上下文
        DynamicDataSourceContextHolder.clearDataSourceType();
    }

    @Test
    public void testDataSourceAspectExists()
    {
        // 测试数据源切面是否存在
        assertNotNull(dataSourceAspect);
    }

    @Test
    public void testDataSourceSwitching()
    {
        // 测试数据源切换
        String dsType = "MASTER";
        DynamicDataSourceContextHolder.setDataSourceType(dsType);
        assertEquals(dsType, DynamicDataSourceContextHolder.getDataSourceType());
        
        String dsType2 = "SLAVE";
        DynamicDataSourceContextHolder.setDataSourceType(dsType2);
        assertEquals(dsType2, DynamicDataSourceContextHolder.getDataSourceType());
        
        // 清理
        DynamicDataSourceContextHolder.clearDataSourceType();
        assertNull(DynamicDataSourceContextHolder.getDataSourceType());
    }

    @Test
    public void testDataSourceTypeEnum()
    {
        // 测试数据源类型枚举
        assertNotNull(DataSourceType.MASTER);
        assertNotNull(DataSourceType.SLAVE);
        assertNotEquals(DataSourceType.MASTER, DataSourceType.SLAVE);
    }

    @Test
    public void testDataSourceAnnotation()
    {
        // 测试数据源注解
        DataSource annotation = TestClass.class.getAnnotation(DataSource.class);
        assertNotNull(annotation);
        assertEquals(DataSourceType.MASTER, annotation.value());
    }

    /**
     * 测试用的类
     */
    @DataSource(DataSourceType.MASTER)
    public static class TestClass
    {
    }
}