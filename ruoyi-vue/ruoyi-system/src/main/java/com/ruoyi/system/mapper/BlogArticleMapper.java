package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.BlogArticle;

/**
 * 博客文章Mapper接口
 * 
 * @author nevell
 * @date 2025-07-18
 */
public interface BlogArticleMapper 
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
     * 删除博客文章
     * 
     * @param id 博客文章主键
     * @return 结果
     */
    public int deleteBlogArticleById(Long id);

    /**
     * 批量删除博客文章
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogArticleByIds(Long[] ids);

    /**
     * 根据标题查询博客文章
     * @param title 文章标题
     * @return 博客文章
     */
    public BlogArticle selectBlogArticleByTitle(String title);

    /**
     * 文章浏览量+1
     * @param id 文章ID
     */
    void addViewCount(Long id);

    /**
     * 根据标签ID查询文章列表
     * @param tagId 标签ID
     * @return 文章列表
     */
    List<BlogArticle> selectArticlesByTagId(Long tagId);

    /**
     * 搜索文章（根据标题和内容搜索）
     * @param keyword 搜索关键词
     * @param blogArticle 查询条件
     * @return 文章列表
     */
    List<BlogArticle> searchArticles(@Param("keyword") String keyword, @Param("blogArticle") BlogArticle blogArticle);

    /**
     * 获取文章归档（按年月分组）
     * @return 归档列表
     */
    List<Map<String, Object>> getArticleArchive();

    /**
     * 获取上一篇文章
     * @param id 当前文章ID
     * @return 上一篇文章
     */
    BlogArticle getPrevArticle(Long id);

    /**
     * 获取下一篇文章
     * @param id 当前文章ID
     * @return 下一篇文章
     */
    BlogArticle getNextArticle(Long id);

    /**
     * 统计分类被文章引用数量（仅统计未删除文章）
     * @param ids 分类ID集合
     * @return 被引用的文章数量
     */
    int countByCategoryIds(Long[] ids);
}
