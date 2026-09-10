package com.zhi.web.controller.blog;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhi.common.annotation.Anonymous;
import com.zhi.common.core.controller.BaseController;
import com.zhi.common.core.domain.AjaxResult;
import com.zhi.system.domain.BlogPage;
import com.zhi.system.service.IBlogPageService;

/**
 * 自定义页面前台接口
 * 
 * @author nevell
 * @date 2026-09-10
 */
@RestController
@RequestMapping("/blog/page")
public class BlogFrontPageController extends BaseController
{
    @Autowired
    private IBlogPageService blogPageService;

    /**
     * 查询已发布页面列表（前台导航/页面索引使用）
     */
    @Anonymous
    @GetMapping("/list")
    public AjaxResult list()
    {
        List<BlogPage> list = blogPageService.selectPublishedPageList();
        return success(list);
    }

    /**
     * 按别名查询已发布页面详情（并累加浏览次数）
     */
    @Anonymous
    @GetMapping("/{slug}")
    public AjaxResult getBySlug(@PathVariable("slug") String slug)
    {
        BlogPage page = blogPageService.selectBlogPageBySlug(slug);
        if (page == null)
        {
            return error("页面不存在或未发布");
        }
        blogPageService.increaseViewCount(page.getId());
        page.setViewCount(page.getViewCount() == null ? 1L : page.getViewCount() + 1);
        return success(page);
    }
}
