package com.ruoyi.system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.BlogTag;
import com.ruoyi.system.service.IBlogTagService;

/**
 * 前台博客标签Controller
 * 
 * @author nevell
 * @date 2025-10-24
 */
@RestController
@RequestMapping("/blog/tags")
public class BlogTagFrontController extends BaseController
{
    @Autowired
    private IBlogTagService blogTagService;

    /**
     * 获取前台标签列表
     */
    @GetMapping("/list")
    public AjaxResult list(BlogTag blogTag)
    {
        List<BlogTag> list = blogTagService.selectAllTagList();
        return success(list);
    }

    /**
     * 获取标签详情
     */
    @GetMapping(value = "/{tagId}")
    public AjaxResult getInfo(@PathVariable("tagId") Long tagId)
    {
        BlogTag tag = blogTagService.selectBlogTagById(tagId);
        return success(tag);
    }

    /**
     * 获取标签云
     */
    @GetMapping("/cloud")
    public AjaxResult getTagCloud()
    {
        List<Map<String, Object>> tagCloud = blogTagService.getTagCloud();
        return success(tagCloud);
    }
}