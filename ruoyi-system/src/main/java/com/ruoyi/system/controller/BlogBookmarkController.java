package com.ruoyi.system.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BlogBookmark;
import com.ruoyi.system.service.IBlogBookmarkService;

/**
 * 文章收藏Controller
 *
 * @author nevell
 * @date 2026-01-18
 */
@RestController
@RequestMapping("/blog/bookmark")
public class BlogBookmarkController extends BaseController {

    @Autowired
    private IBlogBookmarkService blogBookmarkService;

    /**
     * 切换收藏状态
     */
    @PostMapping("/toggle/{articleId}")
    public AjaxResult toggleBookmark(@PathVariable("articleId") Long articleId) {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            return error("请先登录");
        }

        int result = blogBookmarkService.toggleBookmark(userId, articleId);
        Map<String, Object> data = new HashMap<>();
        data.put("bookmarked", result == 1);
        return success(data);
    }

    /**
     * 检查收藏状态
     */
    @GetMapping("/check/{articleId}")
    public AjaxResult checkBookmark(@PathVariable("articleId") Long articleId) {
        Long userId = SecurityUtils.getUserId();
        boolean isBookmarked = false;
        if (userId != null) {
            isBookmarked = blogBookmarkService.isBookmarked(userId, articleId);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("bookmarked", isBookmarked);
        return success(data);
    }

    /**
     * 获取用户收藏列表
     */
    @GetMapping("/list")
    public AjaxResult listBookmarks() {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            return error("请先登录");
        }

        List<BlogBookmark> list = blogBookmarkService.selectBookmarksByUserId(userId);
        return success(list);
    }
}
