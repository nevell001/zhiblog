package com.ruoyi.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.domain.BlogComment;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.IBlogArticleService;
import com.ruoyi.system.service.IBlogCategoryService;
import com.ruoyi.system.service.IBlogTagService;
import com.ruoyi.system.service.IBlogCommentService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.ISysLogininforService;

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

    @Autowired
    private ISysLogininforService logininforService;

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
            
            // 获取在线用户数（最近5分钟有登录记录的用户）
            Long onlineUsers = userService.selectOnlineUserCount(5);
            result.put("onlineUsers", onlineUsers != null ? onlineUsers : 0);
            
            // 获取今日访问次数（今日登录成功次数）
            Long todayVisits = logininforService.selectTodayLoginCount();
            result.put("todayVisits", todayVisits != null ? todayVisits : 0);
            
            // 获取系统运行时间（天数）
            long systemUptime = getSystemUptimeDays();
            result.put("systemUptime", systemUptime + "天");
            
            // 获取内存使用率（百分比）
            Runtime runtime = Runtime.getRuntime();
            long maxMemory = runtime.maxMemory();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            double memoryUsage = (double) usedMemory / maxMemory * 100;
            result.put("memoryUsage", String.format("%.1f%%", memoryUsage));
            
            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取数据概览统计失败:", e);
            // 返回默认数据，确保前端不会因为后端错误而显示空白
            Map<String, Object> defaultResult = new HashMap<>();
            defaultResult.put("articleCount", 0);
            defaultResult.put("userCount", 0);
            defaultResult.put("commentCount", 0);
            defaultResult.put("viewCount", 0);
            defaultResult.put("onlineUsers", 0);
            defaultResult.put("todayVisits", 0);
            defaultResult.put("systemUptime", "0天");
            defaultResult.put("memoryUsage", "0%");
            return AjaxResult.success(defaultResult);
        }
    }
    
    /**
     * 获取系统运行时间（天数）
     */
    private long getSystemUptimeDays()
    {
        try {
            // 使用 ManagementFactory 获取 JVM 启动时间
            java.lang.management.RuntimeMXBean runtimeMXBean = java.lang.management.ManagementFactory.getRuntimeMXBean();
            long startTime = runtimeMXBean.getStartTime();
            long currentTime = System.currentTimeMillis();
            
            // 计算运行天数
            long uptimeMillis = currentTime - startTime;
            long uptimeDays = uptimeMillis / (1000 * 60 * 60 * 24);
            
            return uptimeDays;
        } catch (Exception e) {
            logger.error("获取系统运行时间失败:", e);
            // 如果获取失败，返回0天
            return 0;
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
            
            // 统计已发布文章数（status = 1）
            BlogArticle publishedArticle = new BlogArticle();
            publishedArticle.setStatus(1L);
            Long publishedCount = blogArticleService.selectBlogArticleCount(publishedArticle);
            result.put("publishedCount", publishedCount != null ? publishedCount : 0);
            
            // 统计草稿文章数（status = 0）
            BlogArticle draftArticle = new BlogArticle();
            draftArticle.setStatus(0L);
            Long draftCount = blogArticleService.selectBlogArticleCount(draftArticle);
            result.put("draftCount", draftCount != null ? draftCount : 0);
            
            // 获取平均浏览量
            Double avgViews = blogArticleService.selectAverageViewCount();
            result.put("avgViews", avgViews != null ? Math.round(avgViews * 100) / 100.0 : 0);
            
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
            Map<String, Object> result = new HashMap<>();

            // 从数据库查询文章发布趋势
            List<Map<String, Object>> trendData = blogArticleService.selectArticleTrend();

            if (trendData != null && !trendData.isEmpty()) {
                List<String> labels = new ArrayList<>();
                List<Integer> data = new ArrayList<>();

                // 数据是按月份降序排列的，需要反转成升序
                for (int i = trendData.size() - 1; i >= 0; i--) {
                    Map<String, Object> monthData = trendData.get(i);
                    String month = (String) monthData.get("month");
                    Long count = monthData.get("count") != null ? Long.parseLong(monthData.get("count").toString()) : 0;

                    // 格式化月份标签（例如：2025-12 -> 12月）
                    String[] parts = month.split("-");
                    if (parts.length == 2) {
                        labels.add(parts[1] + "月");
                    } else {
                        labels.add(month);
                    }

                    data.add(count.intValue());
                }

                result.put("labels", labels);
                result.put("data", data);
            } else {
                // 返回空数据
                result.put("labels", new ArrayList<>());
                result.put("data", new ArrayList<>());
            }

            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取文章发布趋势失败:", e);
            // 返回默认数据
            Map<String, Object> defaultResult = new HashMap<>();
            defaultResult.put("labels", new ArrayList<>());
            defaultResult.put("data", new ArrayList<>());
            return AjaxResult.success(defaultResult);
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
            Map<String, Object> result = new HashMap<>();

            // 从数据库查询用户活跃度
            List<Map<String, Object>> activityData = logininforService.selectUserActivity();

            if (activityData != null && !activityData.isEmpty()) {
                List<String> labels = new ArrayList<>();
                List<Integer> data = new ArrayList<>();

                // 数据是按月份升序排列的
                for (Map<String, Object> monthData : activityData) {
                    String month = (String) monthData.get("month");
                    Long count = monthData.get("count") != null ? Long.parseLong(monthData.get("count").toString()) : 0;

                    // 格式化月份标签（例如：2025-12 -> 12月）
                    String[] parts = month.split("-");
                    if (parts.length == 2) {
                        labels.add(parts[1] + "月");
                    } else {
                        labels.add(month);
                    }

                    data.add(count.intValue());
                }

                result.put("labels", labels);
                result.put("data", data);
            } else {
                // 返回空数据
                result.put("labels", new ArrayList<>());
                result.put("data", new ArrayList<>());
            }

            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取用户活跃度失败:", e);
            // 返回默认数据
            Map<String, Object> defaultResult = new HashMap<>();
            defaultResult.put("labels", new ArrayList<>());
            defaultResult.put("data", new ArrayList<>());
            return AjaxResult.success(defaultResult);
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
            Map<String, Object> result = new HashMap<>();

            // 从数据库查询用户注册趋势（最近12个月）
            List<Map<String, Object>> trendData = userService.selectUserRegisterTrend(12);

            if (trendData != null && !trendData.isEmpty()) {
                List<String> labels = new ArrayList<>();
                List<Integer> data = new ArrayList<>();

                // 数据是按月份升序排列的
                for (Map<String, Object> monthData : trendData) {
                    String month = (String) monthData.get("month");
                    Long count = monthData.get("count") != null ? Long.parseLong(monthData.get("count").toString()) : 0;

                    // 格式化月份标签（例如：2025-12 -> 12月）
                    String[] parts = month.split("-");
                    if (parts.length == 2) {
                        labels.add(parts[1] + "月");
                    } else {
                        labels.add(month);
                    }

                    data.add(count.intValue());
                }

                result.put("labels", labels);
                result.put("data", data);
            } else {
                // 返回空数据
                result.put("labels", new ArrayList<>());
                result.put("data", new ArrayList<>());
            }

            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取用户注册趋势失败:", e);
            // 返回默认数据
            Map<String, Object> defaultResult = new HashMap<>();
            defaultResult.put("labels", new ArrayList<>());
            defaultResult.put("data", new ArrayList<>());
            return AjaxResult.success(defaultResult);
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
            Map<String, Object> result = new HashMap<>();

            // 从数据库查询用户角色分布
            List<Map<String, Object>> roleData = userService.selectUserRoleDistribution();

            if (roleData != null && !roleData.isEmpty()) {
                List<String> labels = new ArrayList<>();
                List<Integer> data = new ArrayList<>();

                for (Map<String, Object> roleItem : roleData) {
                    String label = (String) roleItem.get("label");
                    Long count = roleItem.get("count") != null ? Long.parseLong(roleItem.get("count").toString()) : 0;

                    // 只统计有用户的角色
                    if (count > 0) {
                        labels.add(label);
                        data.add(count.intValue());
                    }
                }

                result.put("labels", labels);
                result.put("data", data);
            } else {
                // 返回空数据
                result.put("labels", new ArrayList<>());
                result.put("data", new ArrayList<>());
            }

            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取用户角色分布失败:", e);
            // 返回默认数据
            Map<String, Object> defaultResult = new HashMap<>();
            defaultResult.put("labels", new ArrayList<>());
            defaultResult.put("data", new ArrayList<>());
            return AjaxResult.success(defaultResult);
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
            Map<String, Object> result = new HashMap<>();
            
            // 查询分类列表（包含文章数量）
            List<com.ruoyi.system.domain.BlogCategory> categoryList = blogCategoryService.selectCategoryListForFront(new com.ruoyi.system.domain.BlogCategory());
            
            if (categoryList != null && !categoryList.isEmpty()) {
                List<String> labels = new ArrayList<>();
                List<Integer> data = new ArrayList<>();
                
                for (com.ruoyi.system.domain.BlogCategory category : categoryList) {
                    String name = category.getName();
                    Integer articleCount = category.getArticleCount() != null ? category.getArticleCount().intValue() : 0;
                    
                    // 只统计有文章的分类
                    if (articleCount > 0) {
                        labels.add(name);
                        data.add(articleCount);
                    }
                }
                
                result.put("labels", labels);
                result.put("data", data);
            } else {
                // 返回空数据
                result.put("labels", new ArrayList<>());
                result.put("data", new ArrayList<>());
            }
            
            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取文章分类分布失败:", e);
            // 返回默认数据
            Map<String, Object> defaultResult = new HashMap<>();
            defaultResult.put("labels", new ArrayList<>());
            defaultResult.put("data", new ArrayList<>());
            return AjaxResult.success(defaultResult);
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
            Map<String, Object> result = new HashMap<>();
            
            // 查询标签云（包含文章数量）
            List<Map<String, Object>> tagList = blogTagService.getTagCloud();
            
            if (tagList != null && !tagList.isEmpty()) {
                List<String> labels = new ArrayList<>();
                List<Integer> data = new ArrayList<>();
                
                // 取前10个热门标签
                int count = 0;
                for (Map<String, Object> tag : tagList) {
                    if (count >= 10) {
                        break;
                    }
                    
                    String name = (String) tag.get("name");
                    Integer articleCount = tag.get("article_count") != null ? 
                        Integer.parseInt(tag.get("article_count").toString()) : 0;
                    
                    // 只统计有文章的标签
                    if (articleCount > 0) {
                        labels.add(name);
                        data.add(articleCount);
                        count++;
                    }
                }
                
                result.put("labels", labels);
                result.put("data", data);
            } else {
                // 返回空数据
                result.put("labels", new ArrayList<>());
                result.put("data", new ArrayList<>());
            }
            
            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取热门标签失败:", e);
            // 返回默认数据
            Map<String, Object> defaultResult = new HashMap<>();
            defaultResult.put("labels", new ArrayList<>());
            defaultResult.put("data", new ArrayList<>());
            return AjaxResult.success(defaultResult);
        }
    }
}