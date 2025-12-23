package com.ruoyi.web.controller.blog;

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
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.domain.SysConfig;

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

    @Autowired
    private ISysConfigService sysConfigService;

    /**
     * 获取博客设置（前台用）
     */
    @Anonymous
    @GetMapping("/setting")
    public AjaxResult getBlogSettings()
    {
        // 创建一个Map来存储所有设置项，键为configKey，值为configValue
        Map<String, Object> settingsMap = new HashMap<>();

        try {
            System.out.println("=== 博客设置调试信息 ===");

            // 首先尝试从sys_config表获取（后台管理页面配置的数据）
            List<SysConfig> configList = null;
            try {
                configList = sysConfigService.selectConfigList(new SysConfig());
                System.out.println("从sys_config表获取到 " + (configList != null ? configList.size() : 0) + " 个配置项");
            } catch (Exception e) {
                System.out.println("sys_config表查询失败: " + e.getMessage());
            }

            // 如果sys_config表为空，则回退到blog_setting表
            if (configList == null || configList.isEmpty()) {
                System.out.println("sys_config表为空，回退到blog_setting表");
                BlogSetting blogSetting = new BlogSetting();
                blogSetting.setDelFlag("0"); // 只查询未删除的设置
                List<BlogSetting> blogSettingList = blogSettingService.selectBlogSettingList(blogSetting);
                System.out.println("从blog_setting表获取到 " + (blogSettingList != null ? blogSettingList.size() : 0) + " 个配置项");

                if (blogSettingList != null && !blogSettingList.isEmpty()) {
                    for (BlogSetting setting : blogSettingList) {
                        if (setting.getSettingKey() != null) {
                            String key = setting.getSettingKey();
                            String value = setting.getSettingValue();

                            // 输出关键配置项
                            if (key.startsWith("blog_") || key.equals("github_url") || key.equals("weibo_url") || key.equals("wechat_qr")) {
                                System.out.println("配置项: " + key + " = " + value);
                            }

                            // 处理特殊类型转换
                            Object convertedValue = value;

                            // 布尔值转换
                            if ("true".equals(value)) {
                                convertedValue = true;
                            } else if ("false".equals(value)) {
                                convertedValue = false;
                            }

                            // 头像URL特殊处理
                            if ("blog_avatar".equals(key) && value != null && !value.isEmpty()) {
                                // 如果是完整的HTTP URL，转换为相对路径
                                if (value.startsWith("http")) {
                                    try {
                                        java.net.URL url = new java.net.URL(value);
                                        String path = url.getPath();
                                        if (path.startsWith("/profile/")) {
                                            convertedValue = path; // 保留/profile/开头的相对路径
                                            System.out.println("头像URL转换: " + value + " -> " + convertedValue);
                                        } else {
                                            convertedValue = value; // 其他情况保持原样
                                        }
                                    } catch (Exception e) {
                                        convertedValue = value; // 转换失败保持原样
                                    }
                                } else if (!value.startsWith("data:") && !value.startsWith("/")) {
                                    convertedValue = "/" + value; // 添加前导斜杠
                                }
                            }

                            settingsMap.put(key, convertedValue);
                        }
                    }
                }
            } else {
                // 使用sys_config表的数据
                for (SysConfig config : configList) {
                    if (config.getConfigKey() != null) {
                        String key = config.getConfigKey();
                        String value = config.getConfigValue();

                        // 输出关键配置项
                        if (key.startsWith("blog_") || key.equals("github_url") || key.equals("weibo_url") || key.equals("wechat_qr")) {
                            System.out.println("配置项: " + key + " = " + value);
                        }

                        // 处理特殊类型转换
                        Object convertedValue = value;

                        // 布尔值转换
                        if ("true".equals(value)) {
                            convertedValue = true;
                        } else if ("false".equals(value)) {
                            convertedValue = false;
                        }

                        // 头像URL特殊处理
                        if ("blog_avatar".equals(key) && value != null && !value.isEmpty()) {
                            // 如果是完整的HTTP URL，转换为相对路径
                            if (value.startsWith("http")) {
                                try {
                                    java.net.URL url = new java.net.URL(value);
                                    String path = url.getPath();
                                    if (path.startsWith("/profile/")) {
                                        convertedValue = path; // 保留/profile/开头的相对路径
                                        System.out.println("头像URL转换: " + value + " -> " + convertedValue);
                                    } else {
                                        convertedValue = value; // 其他情况保持原样
                                    }
                                } catch (Exception e) {
                                    convertedValue = value; // 转换失败保持原样
                                }
                            } else if (!value.startsWith("data:") && !value.startsWith("/")) {
                                convertedValue = "/" + value; // 添加前导斜杠
                            }
                        }

                        settingsMap.put(key, convertedValue);
                    }
                }
            }

            // 确保必要的设置项都有默认值
            setDefaultSetting(settingsMap, "blog_avatar", "");
            setDefaultSetting(settingsMap, "github_url", "");
            setDefaultSetting(settingsMap, "weibo_url", "");
            setDefaultSetting(settingsMap, "wechat_qr", "");
            setDefaultSetting(settingsMap, "blog_author", "nevell");
            setDefaultSetting(settingsMap, "blog_name", "我的博客");
            setDefaultSetting(settingsMap, "blog_desc", "这是一个基于RuoYi-Vue的博客系统");
            setDefaultSetting(settingsMap, "author_title", "全栈开发工程师");
            setDefaultSetting(settingsMap, "blog_email", "");

        } catch (Exception e) {
            System.err.println("获取博客设置出错: " + e.getMessage());
            e.printStackTrace();

            // 如果出错，返回默认设置
            setDefaultSetting(settingsMap, "blog_avatar", "");
            setDefaultSetting(settingsMap, "github_url", "");
            setDefaultSetting(settingsMap, "weibo_url", "");
            setDefaultSetting(settingsMap, "wechat_qr", "");
            setDefaultSetting(settingsMap, "blog_author", "nevell");
            setDefaultSetting(settingsMap, "blog_name", "我的博客");
            setDefaultSetting(settingsMap, "blog_desc", "这是一个基于RuoYi-Vue的博客系统");
            setDefaultSetting(settingsMap, "author_title", "全栈开发工程师");
            setDefaultSetting(settingsMap, "blog_email", "");
        }

        return success(settingsMap);
    }

    /**
     * 设置默认值
     */
    private void setDefaultSetting(Map<String, Object> settingsMap, String key, String defaultValue) {
        if (!settingsMap.containsKey(key) || settingsMap.get(key) == null || "".equals(settingsMap.get(key))) {
            settingsMap.put(key, defaultValue);
        }
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
    public AjaxResult getArticleDetail(@PathVariable("id") Long id)
    {
        logger.info("请求文章详情，ID: {}", id);

        BlogArticle blogArticle = blogArticleService.selectBlogArticleById(id);
        if (blogArticle == null) {
            logger.warn("文章不存在，ID: {}", id);
            return error("文章不存在");
        }

        logger.info("找到文章，ID: {}, 状态: {}, 删除标记: {}",
            id, blogArticle.getStatus(), blogArticle.getDelFlag());
        
        // 确保所有字段都有值，避免null
        if (blogArticle.getStatus() == null) {
            blogArticle.setStatus(0L); // 默认草稿状态
        }
        // 使用安全的null检查方式
        Long status = blogArticle.getStatus();
        if (status == null) {
            logger.warn("文章状态为空，ID: {}", id);
            return error("文章状态无效");
        }

        if (!Long.valueOf(1L).equals(status)) {
            logger.warn("文章未发布，ID: {}, 状态: {}", id, status);
            return error("文章未发布");
        }

        // 使用安全的null检查方式，避免直接调用getDelFlag().longValue()
        Long delFlag = blogArticle.getDelFlag();
        if (delFlag == null) {
            logger.warn("文章删除标记为空，ID: {}, 设置为默认值0", id);
            blogArticle.setDelFlag(0L); // 使用默认值
            delFlag = 0L;
        }
        // 然后安全地检查delFlag的值
        if (!Long.valueOf(0L).equals(delFlag)) {
            logger.warn("文章已删除，ID: {}, 删除标记: {}", id, delFlag);
            return error("文章已删除");
        }

        logger.info("文章状态检查通过，ID: {}", id);
        
        // 确保所有必要字段都有默认值，防止前端显示问题
        if (blogArticle.getViewCount() == null) blogArticle.setViewCount(0L);
        if (blogArticle.getLikeCount() == null) blogArticle.setLikeCount(0L);
        if (blogArticle.getCommentCount() == null) blogArticle.setCommentCount(0L);
        if (blogArticle.getIsTop() == null) blogArticle.setIsTop(0L);
        if (blogArticle.getIsRecommend() == null) blogArticle.setIsRecommend(0L);
        if (blogArticle.getTitle() == null) blogArticle.setTitle("无标题文章");
        if (blogArticle.getContent() == null) blogArticle.setContent("暂无内容");
        if (blogArticle.getSummary() == null) blogArticle.setSummary("");
        if (blogArticle.getAuthor() == null) blogArticle.setAuthor("未知作者");
        if (blogArticle.getAuthorName() == null) blogArticle.setAuthorName(blogArticle.getAuthor());
        
        // 获取上一篇和下一篇文章，使用专门的DTO对象
        Map<String, Object> extraInfo = new HashMap<>();
        
        try {
            BlogArticle prevArticle = blogArticleService.getPrevArticle(id);
            BlogArticle nextArticle = blogArticleService.getNextArticle(id);
            
            // 使用DTO对象避免序列化问题，添加更严格的null检查
            if (prevArticle != null && prevArticle.getId() != null && prevArticle.getStatus() != null && prevArticle.getStatus().equals(1L)) {
                ArticleNavigationDTO prevArticleDTO = new ArticleNavigationDTO(
                    prevArticle.getId(), 
                    prevArticle.getTitle() != null ? prevArticle.getTitle() : "上一篇"
                );
                extraInfo.put("prevArticle", prevArticleDTO);
            } else {
                extraInfo.put("prevArticle", null);
            }
            
            if (nextArticle != null && nextArticle.getId() != null && nextArticle.getStatus() != null && nextArticle.getStatus().equals(1L)) {
                ArticleNavigationDTO nextArticleDTO = new ArticleNavigationDTO(
                    nextArticle.getId(), 
                    nextArticle.getTitle() != null ? nextArticle.getTitle() : "下一篇"
                );
                extraInfo.put("nextArticle", nextArticleDTO);
            } else {
                extraInfo.put("nextArticle", null);
            }
        } catch (Exception e) {
            // 处理获取上下篇文章时的异常，确保即使出错也能返回文章详情
            extraInfo.put("prevArticle", null);
            extraInfo.put("nextArticle", null);
            System.err.println("获取上下篇文章出错: " + e.getMessage());
        }
        
        // 添加分类信息
        try {
            if (blogArticle.getCategoryId() != null) {
                BlogCategory category = blogCategoryService.selectBlogCategoryById(blogArticle.getCategoryId());
                if (category != null && "0".equals(category.getDelFlag())) {
                    extraInfo.put("category", category);
                }
            }
        } catch (Exception e) {
            System.err.println("获取分类信息出错: " + e.getMessage());
        }

        // 直接返回文章对象（已包含tags字段）
        return success(blogArticle);
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
    @Anonymous
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
    @Anonymous
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
    @Anonymous
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
    @Anonymous
    @GetMapping("/article/hot")
    public TableDataInfo getHotArticles(BlogArticle blogArticle)
    {
        // 设置查询条件
        blogArticle.setStatus(1L); // 只查询已发布的文章
        blogArticle.setDelFlag(0L); // 只查询未删除的文章
        blogArticle.setIsRecommend(1L); // 推荐的文章
        blogArticle.setIsTop(1L); // 置顶文章优先
        
        startPage(); // 启用分页
        List<BlogArticle> list = blogArticleService.selectBlogArticleListWithCache(blogArticle);
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
     * 获取分类列表（前台用，包含文章数量）
     */
    @Anonymous
    @GetMapping("/category/list")
    public AjaxResult categoryList()
    {
        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setDelFlag("0"); // 只查询未删除的分类
        blogCategory.setStatus(1); // 只查询启用的分类（1=启用，0=禁用）
        List<BlogCategory> list = blogCategoryService.selectCategoryListForFront(blogCategory);
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
    @Anonymous
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
