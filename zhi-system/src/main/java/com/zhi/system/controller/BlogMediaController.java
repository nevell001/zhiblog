package com.zhi.system.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhi.common.core.controller.BaseController;
import com.zhi.common.core.domain.AjaxResult;
import com.zhi.common.core.page.TableDataInfo;
import com.zhi.common.exception.ServiceException;
import com.zhi.system.domain.BlogUpload;
import com.zhi.system.service.IBlogUploadService;

/**
 * 博客媒体库（上传记录管理）Controller
 *
 * @author nevell
 * @date 2026-09-05
 */
@RestController
@RequestMapping("/system/media")
public class BlogMediaController extends BaseController
{
    @Autowired
    private IBlogUploadService blogUploadService;

    /**
     * 查询上传记录列表
     */
    @PreAuthorize("@ss.hasPermi('blog:media:list')")
    @GetMapping("/list")
    public TableDataInfo list(BlogUpload blogUpload)
    {
        startPage();
        List<BlogUpload> list = blogUploadService.selectBlogUploadList(blogUpload);
        return getDataTable(list);
    }

    /**
     * 删除上传记录（同时尝试删除物理文件）；
     * 仍被文章/页面/设置等引用的文件会跳过并说明原因
     */
    @PreAuthorize("@ss.hasPermi('blog:media:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") Long[] ids)
    {
        if (ids == null || ids.length == 0)
        {
            return error("参数不合法");
        }
        int total = 0;
        List<String> blocked = new ArrayList<>();
        for (Long id : ids)
        {
            try
            {
                total += blogUploadService.deleteBlogUploadById(id);
            }
            catch (ServiceException e)
            {
                blocked.add(e.getMessage());
            }
        }
        if (!blocked.isEmpty())
        {
            String prefix = total > 0 ? "已删除 " + total + " 个文件；" : "";
            return warn(prefix + String.join("；", blocked));
        }
        return toAjax(total);
    }
}
