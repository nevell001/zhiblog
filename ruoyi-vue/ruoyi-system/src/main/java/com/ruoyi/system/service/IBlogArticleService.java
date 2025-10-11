package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BlogArticle;

/**
 * 博客文章Service接口
 * 
 * @author nevell
 * @date 2025-07-18
 */
public interface IBlogArticleService 
{
    /**
     * 查询博客文章
     * 
     * @param id 博客文章主键
     * @return 博客文章
     */
    public BlogArticle selectBlogArticleById(Long id);

    /**
     * 查询博客文章列表
     * 
     * @param blogArticle 博客文章
     * @return 博客文章集合
     */
    public List<BlogArticle> selectBlogArticleList(BlogArticle blogArticle);

    /**
     * 新增博客文章
     * 
     * @param blogArticle 博客文章
     * @return 结果
     */
    public int insertBlogArticle(BlogArticle blogArticle);

    /**
     * 修改博客文章
     * 
     * @param blogArticle 博客文章
     * @return 结果
     */
    public int updateBlogArticle(BlogArticle blogArticle);

    /**
     * 批量删除博客文章
     * 
     * @param ids 需要删除的博客文章主键集合
     * @return 结果
     */
    public int deleteBlogArticleByIds(Long[] ids);

    /**
     * 删除博客文章信息
     * 
     * @param id 博客文章主键
     * @return 结果
     */
    public int deleteBlogArticleById(Long id);

    /**
     * 文章浏览量+1
     * @param id 文章ID
     */
    void addViewCount(Long id);

    /**
     * 根据标签ID查询文章列表
     * 
     * @param tagId 标签ID
     * @return 文章列表
     */
    List<BlogArticle> selectArticlesByTagId(Long tagId);
}
