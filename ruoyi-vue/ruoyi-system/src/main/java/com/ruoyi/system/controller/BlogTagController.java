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

/**
 * 博客标签Controller
 * 
 * @author nevell
 * @date 2025-09-08
 */
@RestController
@RequestMapping("/system/tag")
public class BlogTagController extends BaseController
{
    @Autowired
    private IBlogTagService blogTagService;

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
    @GetMapping(value = "/{tagId}")
    public AjaxResult getInfo(@PathVariable("tagId") Long tagId)
    {
        return success(blogTagService.selectBlogTagById(tagId));
    }

    /**
     * 新增博客标签
     */
    @PreAuthorize("@ss.hasPermi('system:tag:add')")
    @Log(title = "博客标签", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BlogTag blogTag)
    {
        if (!blogTagService.checkTagNameUnique(blogTag))
        {
            return error("新增标签'" + blogTag.getTagName() + "'失败，标签名称已存在");
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
        if (!blogTagService.checkTagNameUnique(blogTag))
        {
            return error("修改标签'" + blogTag.getTagName() + "'失败，标签名称已存在");
        }
        return toAjax(blogTagService.updateBlogTag(blogTag));
    }

    /**
     * 删除博客标签
     */
    @PreAuthorize("@ss.hasPermi('system:tag:remove')")
    @Log(title = "博客标签", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tagIds}")
    public AjaxResult remove(@PathVariable Long[] tagIds)
    {
        for (Long tagId : tagIds)
        {
            if (blogTagService.checkTagExistArticle(tagId))
            {
                return error("删除标签失败，标签已关联文章");
            }
        }
        return toAjax(blogTagService.deleteBlogTagByIds(tagIds));
    }

    /**
     * 获取所有标签（供前端选择）
     */
    @GetMapping("/all")
    public AjaxResult getAllTags()
    {
        List<BlogTag> tags = blogTagService.selectAllTagList();
        return success(tags);
    }
}