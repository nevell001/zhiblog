package com.ruoyi.framework.config.properties;

import org.springframework.context.annotation.Configuration;
import com.alibaba.druid.pool.DruidDataSource;

/**
 * druid 配置属性
 * 
 * @author ruoyi
 */
@Configuration
public class DruidProperties
{
    public DruidDataSource dataSource(DruidDataSource datasource)
    {
        // 使用默认配置，不设置任何属性
        // DruidDataSourceBuilder会自动绑定application-druid.yml中的属性
        return datasource;
    }
}