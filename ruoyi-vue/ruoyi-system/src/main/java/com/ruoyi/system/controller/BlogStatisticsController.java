package com.ruoyi.system.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.domain.BlogComment;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.IBlogArticleService;
import com.ruoyi.system.service.IBlogCategoryService;
import com.ruoyi.system.service.IBlogTagService;
import com.ruoyi.system.service.IBlogCommentService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 博客统计Controller
 * 
 * @author nevell
 * @date 2025-11-15
 */
@RestController
@RequestMapping("/statistics")
public class BlogStatisticsController extends BaseController
{
    @Autowired
    private IBlogArticleService blogArticleService;

    @Autowired
    private IBlogCategoryService blogCategoryService;

    @Autowired
    private IBlogTagService blogTagService;

    @Autowired
    private IBlogCommentService blogCommentService;

    @Autowired
    private ISysUserService userService;

    /**
     * 获取数据概览统计
     */
    @PreAuthorize("@ss.hasPermi('statistics:overview:list')")
    @GetMapping("/overview")
    public AjaxResult getStatisticsOverview()
    {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 获取文章总数
            Long articleCount = blogArticleService.selectBlogArticleCount(new BlogArticle());
            result.put("articleCount", articleCount != null ? articleCount : 0);
            
            // 获取用户总数
            Long userCount = userService.selectUserCount(new SysUser());
            result.put("userCount", userCount != null ? userCount : 0);
            
            // 获取评论总数
            Long commentCount = blogCommentService.selectBlogCommentCount(new BlogComment());
            result.put("commentCount", commentCount != null ? commentCount : 0);
            
            // 获取总浏览量（需要从文章表中统计）
            Long viewCount = blogArticleService.selectTotalViewCount();
            result.put("viewCount", viewCount != null ? viewCount : 0);
            
            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取数据概览统计失败:", e);
            // 返回默认数据，确保前端不会因为后端错误而显示空白
            Map<String, Object> defaultResult = new HashMap<>();
            defaultResult.put("articleCount", 0);
            defaultResult.put("userCount", 0);
            defaultResult.put("commentCount", 0);
            defaultResult.put("viewCount", 0);
            return AjaxResult.success(defaultResult);
        }
    }

    /**
     * 获取文章统计
     */
    @PreAuthorize("@ss.hasPermi('statistics:article:list')")
    @GetMapping("/article")
    public AjaxResult getArticleStatistics()
    {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 获取发布文章数
            BlogArticle publishedQuery = new BlogArticle();
            publishedQuery.setStatus("1"); // 已发布状态
            Long publishedCount = blogArticleService.selectBlogArticleCount(publishedQuery);
            result.put("publishedCount", publishedCount != null ? publishedCount : 0);
            
            // 获取草稿文章数
            BlogArticle draftQuery = new BlogArticle();
            draftQuery.setStatus("0"); // 草稿状态
            Long draftCount = blogArticleService.selectBlogArticleCount(draftQuery);
            result.put("draftCount", draftCount != null ? draftCount : 0);
            
            // 获取平均浏览量
            Double avgViews = blogArticleService.selectAverageViewCount();
            result.put("avgViews", avgViews != null ? avgViews : 0);
            
            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取文章统计失败:", e);
            // 返回默认数据
            Map<String, Object> defaultResult = new HashMap<>();
            defaultResult.put("publishedCount", 0);
            defaultResult.put("draftCount", 0);
            defaultResult.put("avgViews", 0);
            return AjaxResult.success(defaultResult);
        }
    }

    /**
     * 获取用户统计
     */
    @PreAuthorize("@ss.hasPermi('statistics:user:list')")
    @GetMapping("/user")
    public AjaxResult getUserStatistics()
    {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 获取总用户数
            Long totalCount = userService.selectUserCount(new SysUser());
            result.put("totalCount", totalCount != null ? totalCount : 0);
            
            // 获取活跃用户数（最近30天登录的用户）
            Long activeCount = userService.selectActiveUserCount(30);
            result.put("activeCount", activeCount != null ? activeCount : 0);
            
            // 获取新增用户数（最近30天注册的用户）
            Long newCount = userService.selectNewUserCount(30);
            result.put("newCount", newCount != null ? newCount : 0);
            
            // 获取管理员数
            Long adminCount = userService.selectAdminUserCount();
            result.put("adminCount", adminCount != null ? adminCount : 0);
            
            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取用户统计失败:", e);
            // 返回默认数据
            Map<String, Object> defaultResult = new HashMap<>();
            defaultResult.put("totalCount", 0);
            defaultResult.put("activeCount", 0);
            defaultResult.put("newCount", 0);
            defaultResult.put("adminCount", 0);
            return AjaxResult.success(defaultResult);
        }
    }

    /**
     * 获取文章发布趋势数据
     */
    @PreAuthorize("@ss.hasPermi('statistics:article:trend')")
    @GetMapping("/article/trend")
    public AjaxResult getArticleTrend()
    {
        try {
            // 返回模拟数据，实际项目中需要从数据库查询
            Map<String, Object> result = new HashMap<>();
            result.put("labels", new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"});
            result.put("data", new int[]{12, 19, 3, 5, 2, 3, 15, 8, 12, 6, 9, 11});
            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取文章发布趋势失败:", e);
            return AjaxResult.success();
        }
    }

    /**
     * 获取用户活跃度数据
     */
    @PreAuthorize("@ss.hasPermi('statistics:user:activity')")
    @GetMapping("/user/activity")
    public AjaxResult getUserActivity()
    {
        try {
            // 返回模拟数据
            Map<String, Object> result = new HashMap<>();
            result.put("labels", new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"});
            result.put("data", new int[]{45, 52, 38, 24, 33, 52, 35, 48, 42, 55, 60, 48});
            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取用户活跃度失败:", e);
            return AjaxResult.success();
        }
    }

    /**
     * 获取用户注册趋势数据
     */
    @PreAuthorize("@ss.hasPermi('statistics:user:register-trend')")
    @GetMapping("/user/register-trend")
    public AjaxResult getUserRegisterTrend()
    {
        try {
            // 返回模拟数据
            Map<String, Object> result = new HashMap<>();
            result.put("labels", new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"});
            result.put("data", new int[]{5, 8, 3, 2, 6, 4, 7, 9, 12, 15, 8, 6});
            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取用户注册趋势失败:", e);
            return AjaxResult.success();
        }
    }

    /**
     * 获取用户角色分布数据
     */
    @PreAuthorize("@ss.hasPermi('statistics:user:role-distribution')")
    @GetMapping("/user/role-distribution")
    public AjaxResult getUserRoleDistribution()
    {
        try {
            // 返回模拟数据
            Map<String, Object> result = new HashMap<>();
            result.put("labels", new String[]{"管理员", "编辑", "普通用户"});
            result.put("data", new int[]{3, 5, 42});
            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取用户角色分布失败:", e);
            return AjaxResult.success();
        }
    }

    /**
     * 获取文章分类分布数据
     */
    @PreAuthorize("@ss.hasPermi('statistics:article:category-distribution')")
    @GetMapping("/article/category-distribution")
    public AjaxResult getArticleCategoryDistribution()
    {
        try {
            // 返回模拟数据
            Map<String, Object> result = new HashMap<>();
            result.put("labels", new String[]{"技术", "生活", "学习", "其他"});
            result.put("data", new int[]{25, 18, 12, 8});
            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取文章分类分布失败:", e);
            return AjaxResult.success();
        }
    }

    /**
     * 获取热门标签数据
     */
    @PreAuthorize("@ss.hasPermi('statistics:article:hot-tags')")
    @GetMapping("/article/hot-tags")
    public AjaxResult getHotTags()
    {
        try {
            // 返回模拟数据
            Map<String, Object> result = new HashMap<>();
            result.put("labels", new String[]{"Java", "Spring", "Vue", "React", "数据库", "Linux"});
            result.put("data", new int[]{15, 12, 8, 6, 9, 7});
            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取热门标签失败:", e);
            return AjaxResult.success();
        }
    }
}