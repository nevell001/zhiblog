package com.ruoyi.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * SPA路由控制器
 * 处理前端SPA应用的路由请求，支持history模式
 * @author ruoyi
 */
@Controller
public class SpaRouterController {

    /**
     * 捕获所有/blog路径下的请求，重定向到index.html
     * 这样前端路由可以处理这些请求
     */
    @RequestMapping(value = "/blog/**")
    public String spaBlogRouter() {
        return "forward:/index.html";
    }
    
    /**
     * 捕获所有其他未匹配的页面路由，重定向到index.html
     * 确保前端可以正确处理所有路由
     */
    @RequestMapping({
        "/index", 
        "/about",
        "/404"
    })
    public String spaIndexRouter() {
        return "forward:/index.html";
    }
}