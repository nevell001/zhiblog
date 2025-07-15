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
import com.ruoyi.system.domain.ArticleTag;
import com.ruoyi.system.service.IArticleTagService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 文章-标签关联Controller
 * 
 * @author ruoyi
 * @date 2025-07-15
 */
@RestController
@RequestMapping("/system/tag")
public class ArticleTagController extends BaseController
{
    @Autowired
    private IArticleTagService articleTagService;

    /**
     * 查询文章-标签关联列表
     */
    @PreAuthorize("@ss.hasPermi('system:tag:list')")
    @GetMapping("/list")
    public TableDataInfo list(ArticleTag articleTag)
    {
        startPage();
        List<ArticleTag> list = articleTagService.selectArticleTagList(articleTag);
        return getDataTable(list);
    }

    /**
     * 导出文章-标签关联列表
     */
    @PreAuthorize("@ss.hasPermi('system:tag:export')")
    @Log(title = "文章-标签关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ArticleTag articleTag)
    {
        List<ArticleTag> list = articleTagService.selectArticleTagList(articleTag);
        ExcelUtil<ArticleTag> util = new ExcelUtil<ArticleTag>(ArticleTag.class);
        util.exportExcel(response, list, "文章-标签关联数据");
    }

    /**
     * 获取文章-标签关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tag:query')")
    @GetMapping(value = "/{articleId}")
    public AjaxResult getInfo(@PathVariable("articleId") Long articleId)
    {
        return success(articleTagService.selectArticleTagByArticleId(articleId));
    }

    /**
     * 新增文章-标签关联
     */
    @PreAuthorize("@ss.hasPermi('system:tag:add')")
    @Log(title = "文章-标签关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ArticleTag articleTag)
    {
        return toAjax(articleTagService.insertArticleTag(articleTag));
    }

    /**
     * 修改文章-标签关联
     */
    @PreAuthorize("@ss.hasPermi('system:tag:edit')")
    @Log(title = "文章-标签关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ArticleTag articleTag)
    {
        return toAjax(articleTagService.updateArticleTag(articleTag));
    }

    /**
     * 删除文章-标签关联
     */
    @PreAuthorize("@ss.hasPermi('system:tag:remove')")
    @Log(title = "文章-标签关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{articleIds}")
    public AjaxResult remove(@PathVariable Long[] articleIds)
    {
        return toAjax(articleTagService.deleteArticleTagByArticleIds(articleIds));
    }
}
