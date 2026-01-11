package com.ruoyi.system.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.domain.BlogCategory;
import com.ruoyi.system.domain.BlogTag;
import com.ruoyi.system.service.IBlogArticleService;
import com.ruoyi.system.service.IBlogCategoryService;
import com.ruoyi.system.service.IBlogTagService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 文章管理Controller
 * 
 * @author nevell
 * @date 2025-11-14
 */
@RestController
@RequestMapping("/system/article")
public class BlogArticleController extends BaseController
{
    @Autowired
    private IBlogArticleService blogArticleService;

    @Autowired
    private IBlogCategoryService blogCategoryService;

    @Autowired
    private IBlogTagService blogTagService;

    /**
     * 查询文章列表
     */
    @PreAuthorize("@ss.hasPermi('blog:article:list')")
    @GetMapping("/list")
    public TableDataInfo list(BlogArticle blogArticle)
    {
        startPage();
        List<BlogArticle> list = blogArticleService.selectBlogArticleList(blogArticle);
        return getDataTable(list);
    }

    /**
     * 导出文章列表
     */
    @PreAuthorize("@ss.hasPermi('blog:article:export')")
    @Log(title = "文章管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BlogArticle blogArticle)
    {
        List<BlogArticle> list = blogArticleService.selectBlogArticleList(blogArticle);
        ExcelUtil<BlogArticle> util = new ExcelUtil<BlogArticle>(BlogArticle.class);
        util.exportExcel(response, list, "文章数据");
    }

    /**
     * 获取文章详细信息
     */
    @PreAuthorize("@ss.hasPermi('blog:article:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        BlogArticle article = blogArticleService.selectBlogArticleById(id);
        if (article == null) {
            return error("文章不存在");
        }

        return success(article);
    }

    /**
     * 新增文章
     */
    @PreAuthorize("@ss.hasPermi('blog:article:add')")
    @Log(title = "文章管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Map<String, Object> params)
    {
        try {
            // 解析文章信息
            BlogArticle blogArticle = parseArticleFromParams(params);
            
            // 解析标签ID列表
            List<Long> tagIds = parseTagIds(params.get("tagIds"));

            // 插入文章
            int result = blogArticleService.insertBlogArticle(blogArticle);
            if (result > 0) {
                // 关联标签
                if (tagIds != null && !tagIds.isEmpty()) {
                    blogArticleService.insertArticleTagRelations(blogArticle.getId(), tagIds);
                }
                return success("文章创建成功");
            } else {
                return error("文章创建失败");
            }
        } catch (Exception e) {
            logger.error("创建文章失败", e);
            return error("创建文章失败：" + e.getMessage());
        }
    }

    /**
     * 修改文章
     */
    @PreAuthorize("@ss.hasPermi('blog:article:edit')")
    @Log(title = "文章管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Map<String, Object> params)
    {
        try {
            // 解析文章信息
            BlogArticle blogArticle = parseArticleFromParams(params);
            
            // 解析标签ID列表
            List<Long> tagIds = parseTagIds(params.get("tagIds"));

            // 更新文章
            int result = blogArticleService.updateBlogArticle(blogArticle);
            if (result > 0) {
                // 更新标签关联
                blogArticleService.updateArticleTagRelations(blogArticle.getId(), tagIds);
                return success("文章更新成功");
            } else {
                return error("文章更新失败");
            }
        } catch (Exception e) {
            logger.error("更新文章失败", e);
            return error("更新文章失败：" + e.getMessage());
        }
    }

    /**
     * 删除文章
     */
    @PreAuthorize("@ss.hasPermi('blog:article:remove')")
    @Log(title = "文章管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") Long[] ids)
    {
        return toAjax(blogArticleService.deleteBlogArticleByIds(ids));
    }

    /**
     * 获取分类和标签选项
     */
    @PreAuthorize("@ss.hasPermi('blog:article:query')")
    @GetMapping("/options")
    public AjaxResult getOptions()
    {
        Map<String, Object> result = new HashMap<>();

        // 获取分类列表
        BlogCategory categoryQuery = new BlogCategory();
        categoryQuery.setDelFlag("0");
        categoryQuery.setStatus(1);
        List<BlogCategory> categories = blogCategoryService.selectBlogCategoryList(categoryQuery);
        result.put("categories", categories);

        // 获取标签列表
        BlogTag tagQuery = new BlogTag();
        tagQuery.setDelFlag(0);
        List<BlogTag> tags = blogTagService.selectBlogTagList(tagQuery);
        result.put("tags", tags);

        return success(result);
    }

    /**
     * 批量更新文章状态
     */
    @PreAuthorize("@ss.hasPermi('blog:article:edit')")
    @Log(title = "文章管理", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult updateStatus(@RequestBody Map<String, Object> params)
    {
        try {
            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) params.get("ids");
            Integer status = (Integer) params.get("status");

            if (ids == null || ids.isEmpty()) {
                return error("请选择要更新的文章");
            }

            int result = blogArticleService.updateArticleStatus(ids, status);
            return toAjax(result);
        } catch (Exception e) {
            logger.error("更新文章状态失败", e);
            return error("更新文章状态失败：" + e.getMessage());
        }
    }

    /**
     * 搜索文章
     */
    @PreAuthorize("@ss.hasPermi('blog:article:list')")
    @GetMapping("/search")
    public TableDataInfo search(@RequestParam("keyword") String keyword, BlogArticle blogArticle)
    {
        startPage();
        List<BlogArticle> list = blogArticleService.searchArticles(keyword, blogArticle);
        return getDataTable(list);
    }

    /**
     * 根据分类获取文章
     */
    @PreAuthorize("@ss.hasPermi('blog:article:list')")
    @GetMapping("/category/{categoryId}")
    public TableDataInfo getByCategory(@PathVariable("categoryId") Long categoryId, BlogArticle blogArticle)
    {
        blogArticle.setCategoryId(categoryId);
        startPage();
        List<BlogArticle> list = blogArticleService.selectBlogArticleList(blogArticle);
        return getDataTable(list);
    }

    /**
     * 根据标签获取文章
     */
    @PreAuthorize("@ss.hasPermi('blog:article:list')")
    @GetMapping("/tag/{tagId}")
    public TableDataInfo getByTag(@PathVariable("tagId") Long tagId)
    {
        startPage();
        List<BlogArticle> list = blogArticleService.selectArticlesByTagId(tagId);
        return getDataTable(list);
    }

    /**
     * 从参数中解析文章对象
     */
    private BlogArticle parseArticleFromParams(Map<String, Object> params) {
        BlogArticle article = new BlogArticle();

        if (params.get("id") != null) {
            article.setId(Long.valueOf(params.get("id").toString()));
        }
        if (params.get("title") != null) {
            article.setTitle(params.get("title").toString());
        }
        if (params.get("summary") != null) {
            article.setSummary(params.get("summary").toString());
        }
        if (params.get("content") != null) {
            article.setContent(params.get("content").toString());
        }
        if (params.get("coverUrl") != null) {
            article.setCoverUrl(params.get("coverUrl").toString());
        }
        if (params.get("categoryId") != null) {
            article.setCategoryId(Long.valueOf(params.get("categoryId").toString()));
        }
        if (params.get("status") != null) {
            article.setStatus(Long.valueOf(params.get("status").toString()));
        }
        if (params.get("isTop") != null) {
            article.setIsTop(Long.valueOf(params.get("isTop").toString()));
        }
        if (params.get("isRecommend") != null) {
            article.setIsRecommend(Long.valueOf(params.get("isRecommend").toString()));
        }
        if (params.get("authorId") != null) {
            article.setAuthorId(Long.valueOf(params.get("authorId").toString()));
        }
        if (params.get("authorName") != null) {
            article.setAuthorName(params.get("authorName").toString());
        }

        return article;
    }
    
    /**
     * 解析标签ID列表
     */
    @SuppressWarnings("unchecked")
    private List<Long> parseTagIds(Object tagIdsObj) {
        List<Long> tagIds = new java.util.ArrayList<>();
        if (tagIdsObj != null) {
            if (tagIdsObj instanceof List) {
                List<Object> tagIdObjs = (List<Object>) tagIdsObj;
                for (Object tagIdObj : tagIdObjs) {
                    if (tagIdObj instanceof Long) {
                        tagIds.add((Long) tagIdObj);
                    } else if (tagIdObj instanceof Integer) {
                        tagIds.add(((Integer) tagIdObj).longValue());
                    } else if (tagIdObj instanceof String) {
                        try {
                            tagIds.add(Long.parseLong((String) tagIdObj));
                        } catch (NumberFormatException e) {
                            // 忽略无效的数字
                        }
                    }
                }
            }
        }
        return tagIds;
    }
}
