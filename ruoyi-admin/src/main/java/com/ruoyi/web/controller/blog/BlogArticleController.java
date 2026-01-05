package com.ruoyi.web.controller.blog;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.service.IBlogArticleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 博客文章前台访问控制器
 * 提供前台博客文章匿名访问接口
 *
 * @author nevell
 * @date 2025-12-18
 */
@Api(tags = "博客文章管理（前台）")
@RestController("blogFrontArticleController")
@RequestMapping("/common/blog/article")
public class BlogArticleController extends BaseController {

    @Autowired
    private IBlogArticleService blogArticleService;

    /**
     * 获取文章列表（前台匿名访问）
     */
    @ApiOperation("获取文章列表（前台用）")
    @GetMapping("/list")
    public TableDataInfo list(
            @ApiParam(value = "页码", defaultValue = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页大小", defaultValue = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "分类ID") @RequestParam(required = false) Long categoryId,
            @ApiParam(value = "搜索关键词") @RequestParam(required = false) String keyword,
            @ApiParam(value = "状态：1-已发布，0-草稿") @RequestParam(defaultValue = "1") Integer status) {
        try {
            startPage();

            // 构建查询条件
            BlogArticle blogArticle = new BlogArticle();
            blogArticle.setStatus(Long.valueOf(status)); // 默认只显示已发布的文章

            if (categoryId != null) {
                blogArticle.setCategoryId(categoryId);
            }

            if (StringUtils.isNotEmpty(keyword)) {
                blogArticle.setTitle(keyword);
            }

            // 查询文章列表
            List<BlogArticle> list = blogArticleService.selectBlogArticleList(blogArticle);

            return getDataTable(list);
        } catch (Exception e) {
            logger.error("获取文章列表失败", e);
            return getDataTable(new ArrayList<>());
        }
    }

    /**
     * 获取文章详情（前台匿名访问）
     */
    @ApiOperation("获取文章详情（前台用）")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        try {
            BlogArticle article = blogArticleService.selectBlogArticleById(id);
            if (article == null) {
                return error("文章不存在");
            }

            // 检查文章状态（前台只能查看已发布的文章）
            if (article.getStatus() == null || article.getStatus() != 1) {
                return error("文章未发布");
            }

            return success(article);
        } catch (Exception e) {
            logger.error("获取文章详情失败: {}", id, e);
            return error("获取文章详情失败");
        }
    }

    /**
     * 增加文章浏览量
     */
    @ApiOperation("增加文章浏览量")
    @GetMapping("/view/{id}")
    public AjaxResult addViewCount(@PathVariable("id") Long id) {
        try {
            BlogArticle article = blogArticleService.selectBlogArticleById(id);
            if (article == null) {
                return error("文章不存在");
            }

            // 增加浏览量
            if (article.getViewCount() == null) {
                article.setViewCount(0L);
            }
            article.setViewCount(article.getViewCount() + 1);

            int result = blogArticleService.updateBlogArticle(article);
            if (result > 0) {
                return success("浏览量更新成功");
            } else {
                return error("浏览量更新失败");
            }
        } catch (Exception e) {
            logger.error("增加文章浏览量失败: {}", id, e);
            return error("增加文章浏览量失败");
        }
    }
}