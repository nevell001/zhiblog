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
import com.ruoyi.system.domain.BlogTag;
import com.ruoyi.system.service.IBlogTagService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 博客标签Controller
 * 
 * @author nevell
 * @date 2025-07-28
 */
@RestController
@RequestMapping("/system/tag")
public class BlogTagController extends BaseController
{
    @Autowired
    private IBlogTagService blogTagService;

    private static final Logger log = LoggerFactory.getLogger(BlogTagController.class);

    /**
     * 查询博客标签列表
     */
    @PreAuthorize("@ss.hasPermi('system:tag:list')")
    @GetMapping("/list")
    public TableDataInfo list(BlogTag blogTag)
    {
        startPage();
        List<BlogTag> list = blogTagService.selectBlogTagList(blogTag);
        return getDataTable(list);
    }

    /**
     * 导出博客标签列表
     */
    @PreAuthorize("@ss.hasPermi('system:tag:export')")
    @Log(title = "博客标签", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BlogTag blogTag)
    {
        List<BlogTag> list = blogTagService.selectBlogTagList(blogTag);
        ExcelUtil<BlogTag> util = new ExcelUtil<BlogTag>(BlogTag.class);
        util.exportExcel(response, list, "博客标签数据");
    }

    /**
     * 获取博客标签详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tag:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(blogTagService.selectBlogTagById(id));
    }

    /**
     * 新增博客标签
     */
    @PreAuthorize("@ss.hasPermi('system:tag:add')")
    @Log(title = "博客标签", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BlogTag blogTag)
    {
        // 检查标签名称是否已存在
        if (blogTagService.checkTagNameUnique(blogTag))
        {
            return error("标签名称已存在");
        }
        return toAjax(blogTagService.insertBlogTag(blogTag));
    }

    /**
     * 修改博客标签
     */
    @PreAuthorize("@ss.hasPermi('system:tag:edit')")
    @Log(title = "博客标签", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BlogTag blogTag)
    {
        // 检查标签名称是否已存在（排除当前标签）
        if (blogTagService.checkTagNameUnique(blogTag))
        {
            return error("标签名称已存在");
        }
        return toAjax(blogTagService.updateBlogTag(blogTag));
    }

    /**
     * 删除博客标签
     */
    @PreAuthorize("@ss.hasPermi('system:tag:remove')")
    @Log(title = "博客标签", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        // 检查标签是否关联文章
        for (Long id : ids)
        {
            if (blogTagService.checkTagExistArticle(id))
            {
                return error("标签已关联文章，无法删除");
            }
        }
        return toAjax(blogTagService.deleteBlogTagByIds(ids));
    }

    /**
     * 获取所有标签（供前端选择）
     */
    @GetMapping("/all")
    public AjaxResult getAllTags()
    {
        List<BlogTag> tags = blogTagService.selectBlogTagAll();
        return success(tags);
    }
}