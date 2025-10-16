package com.ruoyi.web.controller.blog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 博客测试控制器
 * 
 * @author nevell
 * @date 2025-10-16
 */
@RestController
@RequestMapping("/blog-test")
public class BlogTestController extends BaseController
{
    /**
     * 测试方法
     */
    @GetMapping("/test")
    public AjaxResult test()
    {
        return success("Blog test success");
    }
}