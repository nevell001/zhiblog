package com.ruoyi.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.BlogCategory;
import com.ruoyi.system.domain.BlogComment;
import com.ruoyi.system.service.IBlogCategoryService;
import com.ruoyi.system.service.IBlogCommentService;

/**
 * 博客前台控制器
 * 
 * @author nevell
 * @date 2025-10-11
 */
@RestController
@RequestMapping("/blog")
public class BlogFrontController extends BaseController
{
    @Autowired
    private IBlogCategoryService blogCategoryService;

    @Autowired
    private IBlogCommentService blogCommentService;

    /**
     * 获取分类列表（前台用）
     */
    @GetMapping("/category/list")
    public AjaxResult categoryList()
    {
        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setDelFlag(0L); // 只查询未删除的分类
        List<BlogCategory> list = blogCategoryService.selectBlogCategoryList(blogCategory);
        return success(list);
    }

    /**
     * 获取分类详情（前台用）
     */
    @GetMapping("/category/{id}")
    public AjaxResult getCategory(@PathVariable("id") Long id)
    {
        return success(blogCategoryService.selectBlogCategoryById(id));
    }

    /**
     * 获取文章评论列表（前台用）
     */
    @GetMapping("/comment/article/{articleId}")
    public AjaxResult getArticleComments(@PathVariable("articleId") Long articleId)
    {
        BlogComment blogComment = new BlogComment();
        blogComment.setArticleId(articleId);
        blogComment.setDelFlag(0L); // 只查询未删除的评论
        List<BlogComment> list = blogCommentService.selectBlogCommentList(blogComment);
        return success(list);
    }
}