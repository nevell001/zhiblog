package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.service.IBlogArticleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 博客文章Controller
 * 
 * @author nevell
 * @date 2025-07-18
 */
@RestController
@RequestMapping("/system/article")
public class BlogArticleController extends BaseController
{
    @Autowired
    private IBlogArticleService blogArticleService;

    private static final Logger log = LoggerFactory.getLogger(BlogArticleController.class);

    /**
     * 查询博客文章列表
     */
    @PreAuthorize("@ss.hasPermi('system:article:list')")
    @GetMapping("/list")
    public TableDataInfo list(BlogArticle blogArticle)
    {
        startPage();
        List<BlogArticle> list = blogArticleService.selectBlogArticleList(blogArticle);
        return getDataTable(list);
    }

    /**
     * 导出博客文章列表
     */
    @PreAuthorize("@ss.hasPermi('system:article:export')")
    @Log(title = "博客文章", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BlogArticle blogArticle)
    {
        List<BlogArticle> list = blogArticleService.selectBlogArticleList(blogArticle);
        ExcelUtil<BlogArticle> util = new ExcelUtil<BlogArticle>(BlogArticle.class);
        util.exportExcel(response, list, "博客文章数据");
    }

    /**
     * 获取博客文章详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:article:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(blogArticleService.selectBlogArticleById(id));
    }

    /**
     * 新增博客文章
     */
    @PreAuthorize("@ss.hasPermi('system:article:add')")
    @Log(title = "博客文章", businessType = BusinessType.INSERT, isSaveRequestData = false)
    @PostMapping
    public AjaxResult add(@RequestBody BlogArticle blogArticle)
    {
        log.info("前端传入authorId: {}", blogArticle.getAuthorId());
        log.info("前端传入title: {}", blogArticle.getTitle());
        log.info("前端传入content长度: {}", blogArticle.getContent() != null ? blogArticle.getContent().length() : 0);
        
        // 设置默认值
        if (blogArticle.getViewCount() == null) {
            blogArticle.setViewCount(0L);
        }
        if (blogArticle.getLikeCount() == null) {
            blogArticle.setLikeCount(0L);
        }
        if (blogArticle.getCommentCount() == null) {
            blogArticle.setCommentCount(0L);
        }
        if (blogArticle.getDelFlag() == null) {
            blogArticle.setDelFlag(0L);
        }
        
        return toAjax(blogArticleService.insertBlogArticle(blogArticle));
    }

    /**
     * 修改博客文章
     */
    @PreAuthorize("@ss.hasPermi('system:article:edit')")
    @Log(title = "博客文章", businessType = BusinessType.UPDATE, isSaveRequestData = false)
    @PutMapping
    public AjaxResult edit(@RequestBody BlogArticle blogArticle)
    {
        // 设置默认值
        if (blogArticle.getDelFlag() == null) {
            blogArticle.setDelFlag(0L);
        }
        return toAjax(blogArticleService.updateBlogArticle(blogArticle));
    }

    /**
     * 删除博客文章
     */
    @PreAuthorize("@ss.hasPermi('system:article:remove')")
    @Log(title = "博客文章", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(blogArticleService.deleteBlogArticleByIds(ids));
    }

    /**
     * 文章浏览量+1
     */
    @PostMapping("/view/{id}")
    public AjaxResult addView(@PathVariable Long id) {
        blogArticleService.addViewCount(id);
        return AjaxResult.success();
    }
}
