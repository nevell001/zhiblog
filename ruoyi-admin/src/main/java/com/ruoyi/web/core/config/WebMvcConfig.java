package com.ruoyi.web.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import java.io.IOException;

/**
 * Web MVC配置
 * 用于支持SPA应用的前端路由
 * @author ruoyi
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置资源处理器，支持SPA应用history模式
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 首先配置文件上传路径的静态资源访问，优先级更高
        String profilePath = "/app/uploadPath/";
        registry.addResourceHandler("/profile/**")
                .addResourceLocations("file:" + profilePath);

        // 配置静态资源处理，支持SPA应用history模式
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new SpaResourceResolver());
    }

    /**
     * 自定义资源解析器，用于支持SPA应用的history模式
     * 当请求的资源不存在时，返回index.html，让前端路由处理
     */
    private static class SpaResourceResolver extends PathResourceResolver {
        @Override
        protected Resource getResource(String resourcePath, Resource location) throws IOException {
            Resource resource = super.getResource(resourcePath, location);
            
            // 如果资源不存在，且不是API请求（不包含/api和不以下划线开头），且不是静态资源路径，且不是profile路径，返回index.html
            if (resource == null && !resourcePath.contains("/api") && !resourcePath.startsWith("/")
                && !resourcePath.contains(".") && !resourcePath.startsWith("profile")) {
                return super.getResource("index.html", location);
            }
            
            return resource;
        }
    }
}