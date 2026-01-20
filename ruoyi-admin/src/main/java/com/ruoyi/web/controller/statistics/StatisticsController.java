package com.ruoyi.web.controller.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.system.service.IBlogArticleService;
import com.ruoyi.system.service.IBlogCategoryService;
import com.ruoyi.system.service.IBlogTagService;
import com.ruoyi.system.service.IBlogCommentService;
import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.domain.BlogCategory;
import com.ruoyi.system.domain.BlogTag;
import com.ruoyi.system.domain.BlogComment;
import java.util.HashMap;
import java.util.Map;

/**
 * 统计分析控制器
 */
@RestController
@RequestMapping("/system-stats")
public class StatisticsController {

    @Autowired
    private IBlogArticleService articleService;

    @Autowired
    private IBlogCategoryService categoryService;

    @Autowired
    private IBlogTagService tagService;

    @Autowired
    private IBlogCommentService commentService;

    /**
     * 获取系统概览统计信息
     */
    @GetMapping("/overview")
    @Anonymous
    public AjaxResult getOverview() {
        Map<String, Object> data = new HashMap<>();

        // 获取文章总数
        long articleCount = articleService.selectBlogArticleCount(new BlogArticle());

        // 获取分类总数（使用列表大小）
        long categoryCount = categoryService.selectBlogCategoryList(new BlogCategory()).size();

        // 获取标签总数（使用列表大小）
        long tagCount = tagService.selectBlogTagList(new BlogTag()).size();

        // 获取评论总数
        long commentCount = commentService.selectBlogCommentCount(new BlogComment());

        // 获取总浏览量
        long totalViews = articleService.selectTotalViewCount();

        data.put("articleCount", articleCount);
        data.put("categoryCount", categoryCount);
        data.put("tagCount", tagCount);
        data.put("commentCount", commentCount);
        data.put("totalViews", totalViews);

        return AjaxResult.success(data);
    }
}