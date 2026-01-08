package com.ruoyi.framework.config;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Druid 监控页面 Servlet 配置
 *
 * @author ruoyi
 */
@Configuration
public class DruidServletConfig {

    /**
     * 注册 DruidStatViewServlet
     * 用于启用 Druid 数据库监控页面
     */
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.druid.statViewServlet.enabled", havingValue = "true")
    public ServletRegistrationBean<HttpServlet> druidStatViewServlet() {
        ServletRegistrationBean<HttpServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new DruidStatViewServlet());
        registrationBean.addUrlMappings("/druid/*");
        registrationBean.addInitParameter("loginUsername", "ruoyi");
        registrationBean.addInitParameter("loginPassword", "123456");
        registrationBean.addInitParameter("resetEnable", "false");
        return registrationBean;
    }

    /**
     * DruidStatViewServlet 实现类
     * 使用反射调用 Druid 内部的 Servlet
     */
    public static class DruidStatViewServlet extends HttpServlet {
        private Object druidStatServlet;

        public DruidStatViewServlet() {
            try {
                // 尝试加载 Druid 的 StatViewServlet
                Class<?> clazz = Class.forName("com.alibaba.druid.support.http.StatViewServlet");
                druidStatServlet = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create Druid StatViewServlet", e);
            }
        }

        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            try {
                // 使用反射调用 Druid Servlet 的 service 方法
                druidStatServlet.getClass()
                    .getMethod("service", HttpServletRequest.class, HttpServletResponse.class)
                    .invoke(druidStatServlet, req, resp);
            } catch (Exception e) {
                throw new IOException("Failed to invoke Druid StatViewServlet service", e);
            }
        }
    }
}