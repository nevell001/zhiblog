package com.ruoyi.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Actuator 端点配置
 * 确保静态资源不拦截 Actuator 端点
 * 
 * @author ruoyi
 */
@Configuration
@Order(1)
public class ActuatorConfig implements WebMvcConfigurer
{
    // 不添加任何配置，让 Actuator 框架自己处理
    // 这个配置类的存在是为了确保 Actuator 端点优先级正确
}