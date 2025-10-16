package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;
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
     * @param ids 需要删除的博客文章主键
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
     * 增加文章浏览量
     * 
     * @param id 文章ID
     */
    public void addViewCount(Long id);

    /**
     * 根据标签ID查询文章列表
     * 
     * @param tagId 标签ID
     * @return 文章列表
     */
    public List<BlogArticle> selectArticlesByTagId(Long tagId);

    /**
     * 获取上一篇文章
     * 
     * @param id 当前文章ID
     * @return 上一篇文章
     */
    public BlogArticle getPrevArticle(Long id);

    /**
     * 获取下一篇文章
     * 
     * @param id 当前文章ID
     * @return 下一篇文章
     */
    public BlogArticle getNextArticle(Long id);

    /**
     * 获取文章归档（按年月分组）
     * 
     * @return 归档列表
     */
    public List<Map<String, Object>> getArticleArchive();

    /**
     * 搜索文章（根据标题和内容搜索）
     * 
     * @param keyword 搜索关键词
     * @param blogArticle 查询条件
     * @return 文章列表
     */
    public List<BlogArticle> searchArticles(String keyword, BlogArticle blogArticle);
}