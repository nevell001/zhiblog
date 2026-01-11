package com.ruoyi.framework.config;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
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
     * Druid监控用户名（从环境变量读取）
     */
    @Value("${spring.datasource.druid.statViewServlet.loginUsername:ruoyi}")
    private String druidUsername;

    /**
     * Druid监控密码（从环境变量读取，生产环境必须设置）
     */
    @Value("${spring.datasource.druid.statViewServlet.loginPassword:#{null}}")
    private String druidPassword;

    /**
     * 注册 DruidStatViewServlet
     * 用于启用 Druid 数据库监控页面
     */
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.druid.statViewServlet.enabled", havingValue = "true")
    public ServletRegistrationBean<HttpServlet> druidStatViewServlet() {
        // 如果没有设置密码，使用强随机密码并记录警告
        if (druidPassword == null || druidPassword.isEmpty()) {
            druidPassword = generateSecurePassword();
            System.err.println("========================================");
            System.err.println("警告: Druid监控密码未配置！");
            System.err.println("已自动生成临时密码: " + druidPassword);
            System.err.println("请在环境变量中设置 DRUID_PASSWORD");
            System.err.println("========================================");
        }

        ServletRegistrationBean<HttpServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new DruidStatViewServlet());
        registrationBean.addUrlMappings("/druid/*");
        registrationBean.addInitParameter("loginUsername", druidUsername);
        registrationBean.addInitParameter("loginPassword", druidPassword);
        registrationBean.addInitParameter("resetEnable", "false");
        return registrationBean;
    }

    /**
     * 生成安全的随机密码
     */
    private String generateSecurePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
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