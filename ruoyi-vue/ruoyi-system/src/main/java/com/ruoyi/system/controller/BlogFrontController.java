package com.ruoyi.system.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.domain.BlogCategory;
import com.ruoyi.system.domain.BlogComment;
import com.ruoyi.system.domain.BlogSetting;
import com.ruoyi.system.domain.BlogTag;
import com.ruoyi.system.service.IBlogArticleService;
import com.ruoyi.system.service.IBlogCategoryService;
import com.ruoyi.system.service.IBlogCommentService;
import com.ruoyi.system.service.IBlogSettingService;
import com.ruoyi.system.service.IBlogTagService;

/**
 * 上下篇文章信息DTO
 */
class ArticleNavigationDTO {
    private Long id;
    private String title;
    
    public ArticleNavigationDTO() {}
    
    public ArticleNavigationDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}

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

    public BlogFrontController() {
        System.out.println("BlogFrontController initialized");
    }
    
    @Autowired
    private IBlogArticleService blogArticleService;

    @Autowired
    private IBlogCategoryService blogCategoryService;

    @Autowired
    private IBlogCommentService blogCommentService;

    @Autowired
    private IBlogSettingService blogSettingService;

    @Autowired
    private IBlogTagService blogTagService;

    /**
     * 获取博客设置（前台用）
     */
    @GetMapping("/setting")
    public AjaxResult getBlogSettings()
    {
        // 创建一个Map来存储所有设置项，键为settingKey，值为settingValue
        Map<String, String> settingsMap = new HashMap<>();
        
        // 查询所有设置项
        BlogSetting blogSetting = new BlogSetting();
        blogSetting.setDelFlag("0"); // 只查询未删除的设置
        List<BlogSetting> list = blogSettingService.selectBlogSettingList(blogSetting);
        
        if (list != null && !list.isEmpty()) {
            // 将所有设置项转换为Map格式
            for (BlogSetting setting : list) {
                if (setting.getSettingKey() != null) {
                    settingsMap.put(setting.getSettingKey(), setting.getSettingValue());
                }
            }
        }
        
        return success(settingsMap);
    }

    /**
     * 简单测试方法
     */
    @Anonymous
    @GetMapping("/front-test")
    public AjaxResult frontTest()
    {
        return success("BlogFrontController is working!");
    }

    /**
     * 测试方法
     */
    @GetMapping("/test")
    public AjaxResult test()
    {
        System.out.println("test method called");
        return success("test success");
    }

    /**
     * 获取文章详情（前台用）
     */
    @Anonymous
    @GetMapping("/article/{id}")
    public AjaxResult getArticle(@PathVariable("id") Long id)
    {
        BlogArticle blogArticle = blogArticleService.selectBlogArticleById(id);
        if (blogArticle == null) {
            return error("文章不存在");
        }
        
        // 确保所有字段都有值，避免null
        if (blogArticle.getStatus() == null) {
            blogArticle.setStatus(0L); // 默认草稿状态
        }
        // 使用安全的null检查方式
        Long status = blogArticle.getStatus();
        if (status == null || !Long.valueOf(1L).equals(status)) {
            return error("文章未发布");
        }
        
        // 使用安全的null检查方式，避免直接调用getDelFlag().longValue()
        Long delFlag = blogArticle.getDelFlag();
        if (delFlag == null) {
            blogArticle.setDelFlag(0L); // 使用默认值
            delFlag = 0L;
        }
        // 然后安全地检查delFlag的值
        if (!Long.valueOf(0L).equals(delFlag)) {
            return error("文章已删除");
        }
        
        // 确保其他字段都有默认值
        if (blogArticle.getViewCount() == null) blogArticle.setViewCount(0L);
        if (blogArticle.getLikeCount() == null) blogArticle.setLikeCount(0L);
        if (blogArticle.getCommentCount() == null) blogArticle.setCommentCount(0L);
        if (blogArticle.getIsTop() == null) blogArticle.setIsTop(0L);
        if (blogArticle.getIsRecommend() == null) blogArticle.setIsRecommend(0L);
        
        // 获取上一篇和下一篇文章，使用专门的DTO对象
        Map<String, Object> extraInfo = new HashMap<>();
        
        BlogArticle prevArticle = blogArticleService.getPrevArticle(id);
        BlogArticle nextArticle = blogArticleService.getNextArticle(id);
        
        // 确保prevArticle和nextArticle的delFlag字段有默认值
        // 在调用getDelFlag()之前先检查对象是否为null并设置默认值
        if (prevArticle != null) {
            Long prevDelFlag = prevArticle.getDelFlag();
            if (prevDelFlag == null) {
                prevArticle.setDelFlag(0L);
            }
        }
        if (nextArticle != null) {
            Long nextDelFlag = nextArticle.getDelFlag();
            if (nextDelFlag == null) {
                nextArticle.setDelFlag(0L);
            }
        }
        
        // 使用DTO对象避免序列化问题，添加更严格的null检查
        if (prevArticle != null && prevArticle.getId() != null) {
            ArticleNavigationDTO prevArticleDTO = new ArticleNavigationDTO(
                prevArticle.getId(), 
                prevArticle.getTitle() != null ? prevArticle.getTitle() : "上一篇"
            );
            extraInfo.put("prevArticle", prevArticleDTO);
        } else {
            extraInfo.put("prevArticle", null);
        }
        
        if (nextArticle != null && nextArticle.getId() != null) {
            ArticleNavigationDTO nextArticleDTO = new ArticleNavigationDTO(
                nextArticle.getId(), 
                nextArticle.getTitle() != null ? nextArticle.getTitle() : "下一篇"
            );
            extraInfo.put("nextArticle", nextArticleDTO);
        } else {
            extraInfo.put("nextArticle", null);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("article", blogArticle);
        result.put("extraInfo", extraInfo);
        
        return success(result);
    }

    /**
     * 获取文章归档（按年月分组）
     */
    @Anonymous
    @GetMapping("/article-archive")
    public AjaxResult getArticleArchive()
    {
        // 添加日志以便调试
        System.out.println("getArticleArchive method called");
        List<Map<String, Object>> archiveList = blogArticleService.getArticleArchive();
        System.out.println("archiveList size: " + (archiveList != null ? archiveList.size() : "null"));
        return success(archiveList);
    }

    /**
     * 获取文章列表（前台用，支持分页）
     */
    @Anonymous
    @GetMapping("/article/list")
    public TableDataInfo articleList(BlogArticle blogArticle)
    {
        // 设置查询条件
        blogArticle.setStatus(1L); // 只查询已发布的文章
        blogArticle.setDelFlag(0L); // 只查询未删除的文章

        startPage(); // 启用分页
        List<BlogArticle> list = blogArticleService.selectBlogArticleList(blogArticle);
        return getDataTable(list);
    }

    /**
     * 搜索文章（前台用，支持分页）
     */
    @GetMapping("/article/search")
    public TableDataInfo searchArticles(BlogArticle blogArticle, @RequestParam(value = "keyword", required = false) String keyword)
    {
        // 设置查询条件
        blogArticle.setStatus(1L); // 只查询已发布的文章
        blogArticle.setDelFlag(0L); // 只查询未删除的文章
        
        startPage(); // 启用分页
        
        List<BlogArticle> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            // 如果有搜索关键词，则进行模糊搜索
            list = blogArticleService.searchArticles(keyword, blogArticle);
        } else {
            // 否则按常规条件查询
            list = blogArticleService.selectBlogArticleList(blogArticle);
        }
        
        return getDataTable(list);
    }

    /**
     * 根据分类获取文章列表（前台用，支持分页）
     */
    @GetMapping("/article/category/{categoryId}")
    public TableDataInfo getArticlesByCategory(@PathVariable("categoryId") Long categoryId, BlogArticle blogArticle)
    {
        // 设置查询条件
        blogArticle.setCategoryId(categoryId);
        blogArticle.setStatus(1L); // 只查询已发布的文章
        blogArticle.setDelFlag(0L); // 只查询未删除的文章
        
        startPage(); // 启用分页
        List<BlogArticle> list = blogArticleService.selectBlogArticleList(blogArticle);
        return getDataTable(list);
    }

    /**
     * 根据标签获取文章列表（前台用，支持分页）
     */
    @GetMapping("/article/tag/{tagId}")
    public TableDataInfo getArticlesByTag(@PathVariable("tagId") Long tagId, BlogArticle blogArticle)
    {
        startPage(); // 启用分页
        List<BlogArticle> list = blogArticleService.selectArticlesByTagId(tagId);
        return getDataTable(list);
    }

    /**
     * 获取热门文章（前台用，支持分页）
     */
    @GetMapping("/article/hot")
    public TableDataInfo getHotArticles(BlogArticle blogArticle)
    {
        // 设置查询条件
        blogArticle.setStatus(1L); // 只查询已发布的文章
        blogArticle.setDelFlag(0L); // 只查询未删除的文章
        blogArticle.setIsRecommend(1L); // 推荐的文章
        
        startPage(); // 启用分页
        List<BlogArticle> list = blogArticleService.selectBlogArticleList(blogArticle);
        return getDataTable(list);
    }

    /**
     * 更新文章浏览量（前台用）
     */
    @GetMapping("/article/view/{id}")
    public AjaxResult addArticleView(@PathVariable("id") Long id)
    {
        blogArticleService.addViewCount(id);
        return success();
    }

    /**
     * 获取文章评论列表（前台用）
     */
    @GetMapping("/comment/article/{articleId}")
    public AjaxResult getArticleComments(@PathVariable("articleId") Long articleId)
    {
        BlogComment blogComment = new BlogComment();
        blogComment.setArticleId(articleId);
        blogComment.setStatus("1"); // 只查询已发布的评论（status=1）
        List<BlogComment> list = blogCommentService.selectBlogCommentList(blogComment);
        return success(list);
    }

    /**
     * 添加评论（前台用）
     */
    @PostMapping("/comment")
    public AjaxResult addComment(@RequestBody BlogComment blogComment)
    {
        blogComment.setStatus("1"); // 设置评论状态为已发布
        return toAjax(blogCommentService.insertBlogComment(blogComment));
    }

    /**
     * 获取分类列表（前台用）
     */
    @GetMapping("/category/list")
    public AjaxResult categoryList()
    {
        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setDelFlag("0"); // 只查询未删除的分类
        List<BlogCategory> list = blogCategoryService.selectBlogCategoryList(blogCategory);
        return success(list);
    }

    /**
     * 获取分类详情（前台用）
     */
    @GetMapping("/category/{id}")
    public AjaxResult getCategory(@PathVariable(value = "id", required = false) String idStr)
    {
        if (idStr == null || "undefined".equals(idStr) || idStr.trim().isEmpty()) {
            return error("分类ID不能为空");
        }
        
        Long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            return error("分类ID格式不正确");
        }
        
        BlogCategory category = blogCategoryService.selectBlogCategoryById(id);
        if (category == null) {
            return error("分类不存在或已删除");
        }
        // 安全检查delFlag，避免null值
        String categoryDelFlag = category.getDelFlag();
        if (categoryDelFlag == null) {
            categoryDelFlag = "0";
        }
        if (!"0".equals(categoryDelFlag)) {
            return error("分类不存在或已删除");
        }
        return success(category);
    }

    /**
     * 获取标签列表（前台用）
     */
    @Anonymous
    @GetMapping("/tag/list")
    public AjaxResult tagList()
    {
        BlogTag blogTag = new BlogTag();
        blogTag.setDelFlag(0); // 只查询未删除的标签
        List<BlogTag> list = blogTagService.selectBlogTagList(blogTag);
        return success(list);
    }

    /**
     * 获取标签详情（前台用）
     */
    @GetMapping("/tag/{id}")
    public AjaxResult getTag(@PathVariable("id") Long id)
    {
        return success(blogTagService.selectBlogTagById(id));
    }

    /**
     * 获取标签云（包含文章数量）
     */
    @Anonymous
    @GetMapping("/tag/cloud")
    public AjaxResult getTagCloud()
    {
        System.out.println("getTagCloud method called");
        List<Map<String, Object>> tagCloud = blogTagService.getTagCloud();
        System.out.println("tagCloud size: " + (tagCloud != null ? tagCloud.size() : "null"));
        return success(tagCloud);
    }

    /**
     * 获取RSS订阅
     */
    @GetMapping(value = "/rss", produces = "application/xml;charset=UTF-8")
    public void getRssFeed(HttpServletResponse response) throws IOException {
        response.setContentType("application/xml;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<rss version=\"2.0\">");
        out.println("<channel>");
        out.println("<title>我的博客</title>");
        out.println("<link>http://localhost:3000</link>");
        out.println("<description>这是一个基于RuoYi-Vue的博客系统</description>");
        out.println("<language>zh-CN</language>");
        
        // 获取最新的10篇文章
        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setStatus(1L);
        blogArticle.setDelFlag(0L);
        
        List<BlogArticle> articles = blogArticleService.selectBlogArticleList(blogArticle);
        // 只取前10篇文章
        int count = 0;
        for (BlogArticle article : articles) {
            if (count >= 10) break;
            
            out.println("<item>");
            out.println("<title><![CDATA[" + (article.getTitle() != null ? article.getTitle() : "") + "]]></title>");
            out.println("<link>http://localhost:3000/blog/article/" + article.getId() + "</link>");
            out.println("<description><![CDATA[" + (article.getSummary() != null ? article.getSummary() : "") + "]]></description>");
            out.println("<pubDate>" + (article.getCreateTime() != null ? article.getCreateTime().toString() : "") + "</pubDate>");
            out.println("<guid>http://localhost:3000/blog/article/" + article.getId() + "</guid>");
            out.println("</item>");
            
            count++;
        }
        
        out.println("</channel>");
        out.println("</rss>");
        out.flush();
    }
}