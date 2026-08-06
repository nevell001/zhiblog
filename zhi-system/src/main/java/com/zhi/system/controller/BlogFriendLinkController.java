package com.zhi.system.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;
import com.zhi.common.annotation.Log;
import com.zhi.common.annotation.Anonymous;
import com.zhi.common.core.controller.BaseController;
import com.zhi.common.core.domain.AjaxResult;
import com.zhi.common.enums.BusinessType;
import com.zhi.common.cache.annotation.BlogCacheEvict;
import com.zhi.common.constant.CacheConstants;
import com.zhi.system.domain.BlogFriendLink;
import com.zhi.system.service.IBlogFriendLinkService;
import com.zhi.common.utils.poi.ExcelUtil;
import com.zhi.common.core.page.TableDataInfo;

/**
 * 友情链接Controller
 * 
 * @author nevell
 * @date 2025-09-08
 */
@RestController
@RequestMapping("/system/friendLink")
public class BlogFriendLinkController extends BaseController
{
    @Autowired
    private IBlogFriendLinkService blogFriendLinkService;

    /**
     * 查询友情链接列表
     */
    @PreAuthorize("@ss.hasPermi('blog:friendLink:list')")
    @GetMapping("/list")
    public TableDataInfo list(BlogFriendLink blogFriendLink)
    {
        startPage();
        List<BlogFriendLink> list = blogFriendLinkService.selectBlogFriendLinkList(blogFriendLink);
        return getDataTable(list);
    }

    /**
     * 查询前台展示的友情链接列表
     */
    @Anonymous
    @GetMapping("/front/list")
    public AjaxResult frontList()
    {
        List<BlogFriendLink> list = blogFriendLinkService.selectFrontFriendLinkList();
        return success(list);
    }

    /**
     * 导出友情链接列表
     */
    @PreAuthorize("@ss.hasPermi('blog:friendLink:export')")
    @Log(title = "友情链接", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BlogFriendLink blogFriendLink)
    {
        List<BlogFriendLink> list = blogFriendLinkService.selectBlogFriendLinkList(blogFriendLink);
        ExcelUtil<BlogFriendLink> util = new ExcelUtil<BlogFriendLink>(BlogFriendLink.class);
        util.exportExcel(response, list, "友情链接数据");
    }

    /**
     * 获取友情链接详细信息
     */
    @PreAuthorize("@ss.hasPermi('blog:friendLink:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(blogFriendLinkService.selectBlogFriendLinkById(id));
    }

    /**
     * 新增友情链接
     */
    @PreAuthorize("@ss.hasPermi('blog:friendLink:add')")
    @Log(title = "友情链接", businessType = BusinessType.INSERT)
    @BlogCacheEvict(value = CacheConstants.BLOG_FRIEND_LINK_LIST)
    @PostMapping
    public AjaxResult add(@RequestBody BlogFriendLink blogFriendLink)
    {
        return toAjax(blogFriendLinkService.insertBlogFriendLink(blogFriendLink));
    }

    /**
     * 修改友情链接
     */
    @PreAuthorize("@ss.hasPermi('blog:friendLink:edit')")
    @Log(title = "友情链接", businessType = BusinessType.UPDATE)
    @BlogCacheEvict(value = CacheConstants.BLOG_FRIEND_LINK_LIST)
    @PutMapping
    public AjaxResult edit(@RequestBody BlogFriendLink blogFriendLink)
    {
        return toAjax(blogFriendLinkService.updateBlogFriendLink(blogFriendLink));
    }

    /**
     * 删除友情链接
     */
    @PreAuthorize("@ss.hasPermi('blog:friendLink:remove')")
    @Log(title = "友情链接", businessType = BusinessType.DELETE)
    @BlogCacheEvict(value = CacheConstants.BLOG_FRIEND_LINK_LIST)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") Long[] ids)
    {
        return toAjax(blogFriendLinkService.deleteBlogFriendLinkByIds(ids));
    }
}
