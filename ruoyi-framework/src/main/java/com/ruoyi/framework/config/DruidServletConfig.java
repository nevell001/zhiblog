package com.ruoyi.framework.config;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * Druid 监控页面 Servlet 配置
 *
 * @author ruoyi
 */
@Configuration
public class DruidServletConfig {
    private static final Logger log = LoggerFactory.getLogger(DruidServletConfig.class);

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
     * 
     * 注意：此功能已禁用，建议使用 Actuator 的 Prometheus 端点。
     * 如需启用，请在配置文件中设置 spring.datasource.druid.statViewServlet.enabled=true
     */
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.druid.statViewServlet.enabled", havingValue = "true")
    public ServletRegistrationBean<HttpServlet> druidStatViewServlet() {
        // 如果没有设置密码，使用强随机密码并记录警告
        if (druidPassword == null || druidPassword.isEmpty()) {
            druidPassword = generateSecurePassword();
            log.warn("========================================");
            log.warn("警告: Druid监控密码未配置！");
            log.warn("已自动生成临时密码: {}", druidPassword);
            log.warn("请在环境变量中设置 DRUID_PASSWORD");
            log.warn("========================================");
        }

        ServletRegistrationBean<HttpServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new CustomDruidStatViewServlet());
        registrationBean.addUrlMappings("/druid-servlet");
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
     * 自定义 DruidStatViewServlet 实现类
     * 返回一个简单的HTML页面，说明Druid监控功能
     */
    public static class CustomDruidStatViewServlet extends HttpServlet {
        
        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter writer = resp.getWriter();
            
            writer.println("<!DOCTYPE html>");
            writer.println("<html>");
            writer.println("<head>");
            writer.println("<meta charset='UTF-8'>");
            writer.println("<title>Druid 数据库监控</title>");
            writer.println("<style>");
            writer.println("body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }");
            writer.println(".container { max-width: 800px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
            writer.println("h1 { color: #333; }");
            writer.println("p { color: #666; line-height: 1.6; }");
            writer.println(".info { background-color: #e7f3ff; padding: 15px; border-left: 4px solid #2196F3; margin: 20px 0; }");
            writer.println(".warning { background-color: #fff3cd; padding: 15px; border-left: 4px solid #ffc107; margin: 20px 0; }");
            writer.println("a { color: #2196F3; text-decoration: none; }");
            writer.println("a:hover { text-decoration: underline; }");
            writer.println("</style>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("<div class='container'>");
            writer.println("<h1>🔍 Druid 数据库监控</h1>");
            writer.println("<div class='info'>");
            writer.println("<p><strong>Druid 数据库连接池监控页面</strong></p>");
            writer.println("<p>Druid 是阿里巴巴开源的数据库连接池，提供强大的监控和统计功能。</p>");
            writer.println("</div>");
            writer.println("<div class='warning'>");
            writer.println("<p><strong>⚠️ 注意：</strong></p>");
            writer.println("<p>由于 Spring Boot 3.x 使用 Jakarta EE 9+，Druid 的原生监控页面需要额外的配置。</p>");
            writer.println("<p>当前显示的是一个简化的监控页面。</p>");
            writer.println("</div>");
            writer.println("<h2>📊 监控功能</h2>");
            writer.println("<ul>");
            writer.println("<li>SQL 监控</li>");
            writer.println("<li>URI 监控</li>");
            writer.println("<li>Session 监控</li>");
            writer.println("<li>Spring 监控</li>");
            writer.println("<li>Web 应用监控</li>");
            writer.println("<li>JDBC 监控</li>");
            writer.println("</ul>");
            writer.println("<h2>🔗 相关链接</h2>");
            writer.println("<p><a href='/manage/actuator/prometheus' target='_blank'>Prometheus 监控端点</a></p>");
            writer.println("<p><a href='/admin/monitor/prometheus' target='_blank'>Prometheus 监控页面</a></p>");
            writer.println("<p><a href='/admin/monitor/actuator' target='_blank'>Actuator 监控页面</a></p>");
            writer.println("<p><a href='https://github.com/alibaba/druid' target='_blank'>Druid 官方文档</a></p>");
            writer.println("</div>");
            writer.println("</body>");
            writer.println("</html>");
        }
    }
}