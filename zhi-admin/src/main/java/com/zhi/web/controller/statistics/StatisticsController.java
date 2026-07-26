package com.zhi.web.controller.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhi.common.core.domain.AjaxResult;
import com.zhi.common.annotation.Anonymous;
import com.zhi.system.service.IBlogArticleService;
import com.zhi.system.service.IBlogCategoryService;
import com.zhi.system.service.IBlogTagService;
import com.zhi.system.service.IBlogCommentService;
import com.zhi.system.service.ISysUserService;
import com.zhi.system.domain.BlogArticle;
import com.zhi.system.domain.BlogCategory;
import com.zhi.system.domain.BlogTag;
import com.zhi.system.domain.BlogComment;
import com.zhi.common.core.domain.entity.SysUser;
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

    @Autowired
    private ISysUserService userService;

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

        // 获取注册用户总数（仅统计正常状态、未删除的用户）
        SysUser userQuery = new SysUser();
        userQuery.setStatus("0");
        long userCount = userService.selectUserCount(userQuery);

        data.put("articleCount", articleCount);
        data.put("categoryCount", categoryCount);
        data.put("tagCount", tagCount);
        data.put("commentCount", commentCount);
        data.put("viewCount", totalViews);
        data.put("totalViews", totalViews); // 兼容旧字段名
        data.put("userCount", userCount);

        return AjaxResult.success(data);
    }
}