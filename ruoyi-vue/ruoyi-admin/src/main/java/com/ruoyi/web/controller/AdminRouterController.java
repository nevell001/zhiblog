package com.ruoyi.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台管理路由控制器
 * 处理前端SPA应用的后台管理路由请求，支持history模式
 * @author ruoyi
 */
@Controller
public class AdminRouterController {

    /**
     * 捕获所有/admin路径下的请求，重定向到index.html
     * 这样前端路由可以处理这些请求
     */
    @RequestMapping(value = "/admin/**")
    public String spaAdminRouter() {
        return "forward:/index.html";
    }
}
