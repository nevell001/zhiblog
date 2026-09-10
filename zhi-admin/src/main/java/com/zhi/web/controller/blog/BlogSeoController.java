package com.zhi.web.controller.blog;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhi.common.annotation.Anonymous;
import com.zhi.system.domain.BlogArticle;
import com.zhi.system.domain.BlogPage;
import com.zhi.system.service.IBlogArticleService;
import com.zhi.system.service.IBlogPageService;
import com.zhi.system.service.IBlogSettingService;

/**
 * 博客 SEO 控制器：/sitemap.xml 与 /robots.txt
 *
 * @author nevell
 * @date 2026-09-05
 */
@RestController
public class BlogSeoController
{
    /** 页面地址集合 */
    private static final String[] STATIC_PATHS = { "/blog", "/blog/about", "/blog/category", "/blog/tag", "/blog/archive", "/blog/guestbook" };

    @Autowired
    private IBlogSettingService blogSettingService;

    @Autowired
    private IBlogArticleService blogArticleService;

    @Autowired
    private IBlogPageService blogPageService;

    /**
     * 站点地图
     */
    @Anonymous
    @GetMapping(value = "/sitemap.xml", produces = "application/xml;charset=UTF-8")
    public void sitemap(HttpServletResponse response) throws IOException
    {
        String baseUrl = resolveBaseUrl();
        response.setContentType("application/xml;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        // 首页
        writeUrl(out, baseUrl + "/", null);
        // 静态页面
        for (String path : STATIC_PATHS)
        {
            writeUrl(out, baseUrl + path, null);
        }
        // 已发布文章
        try
        {
            BlogArticle query = new BlogArticle();
            query.setStatus(1L);
            query.setDelFlag(0L);
            List<BlogArticle> articles = blogArticleService.selectBlogArticleList(query);
            if (articles != null)
            {
                for (BlogArticle article : articles)
                {
                    String lastmod = null;
                    if (article.getUpdateTime() != null)
                    {
                        lastmod = new java.text.SimpleDateFormat("yyyy-MM-dd")
                                .format(article.getUpdateTime());
                    }
                    writeUrl(out, baseUrl + "/blog/article/" + article.getId(), lastmod);
                }
            }
        }
        catch (Exception e)
        {
            // 单篇文章失败不阻塞整体输出
        }
        // 已发布的自定义页面
        try
        {
            List<BlogPage> pages = blogPageService.selectPublishedPageList();
            if (pages != null)
            {
                for (BlogPage page : pages)
                {
                    if (page.getSlug() == null || page.getSlug().isEmpty())
                    {
                        continue;
                    }
                    String lastmod = null;
                    if (page.getUpdateTime() != null)
                    {
                        lastmod = new java.text.SimpleDateFormat("yyyy-MM-dd")
                                .format(page.getUpdateTime());
                    }
                    writeUrl(out, baseUrl + "/blog/page/" + page.getSlug(), lastmod);
                }
            }
        }
        catch (Exception e)
        {
            // 单个页面失败不阻塞整体输出
        }
        out.println("</urlset>");
        out.flush();
    }

    /**
     * robots.txt
     */
    @Anonymous
    @GetMapping(value = "/robots.txt", produces = "text/plain;charset=UTF-8")
    public void robots(HttpServletResponse response) throws IOException
    {
        String baseUrl = resolveBaseUrl();
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("User-agent: *");
        out.println("Allow: /");
        out.println("Disallow: /admin");
        out.println("Sitemap: " + baseUrl + "/sitemap.xml");
        out.flush();
    }

    private void writeUrl(PrintWriter out, String loc, String lastmod)
    {
        out.println("  <url>");
        out.println("    <loc>" + escapeXml(loc) + "</loc>");
        if (lastmod != null && !lastmod.isEmpty())
        {
            out.println("    <lastmod>" + escapeXml(lastmod) + "</lastmod>");
        }
        out.println("  </url>");
    }

    private String resolveBaseUrl()
    {
        String blogUrl = null;
        try
        {
            blogUrl = blogSettingService.selectSettingValueByKey("blog_url");
        }
        catch (Exception e)
        {
            // 忽略，使用默认值
        }
        if (blogUrl == null || blogUrl.trim().isEmpty())
        {
            blogUrl = "http://localhost:3000";
        }
        blogUrl = blogUrl.trim();
        while (blogUrl.endsWith("/"))
        {
            blogUrl = blogUrl.substring(0, blogUrl.length() - 1);
        }
        return blogUrl;
    }

    private String escapeXml(String value)
    {
        if (value == null)
        {
            return "";
        }
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&apos;");
    }
}
