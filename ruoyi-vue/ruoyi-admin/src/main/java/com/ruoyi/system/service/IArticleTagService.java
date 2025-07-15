package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.ArticleTag;

/**
 * 文章-标签关联Service接口
 * 
 * @author ruoyi
 * @date 2025-07-15
 */
public interface IArticleTagService 
{
    /**
     * 查询文章-标签关联
     * 
     * @param articleId 文章-标签关联主键
     * @return 文章-标签关联
     */
    public ArticleTag selectArticleTagByArticleId(Long articleId);

    /**
     * 查询文章-标签关联列表
     * 
     * @param articleTag 文章-标签关联
     * @return 文章-标签关联集合
     */
    public List<ArticleTag> selectArticleTagList(ArticleTag articleTag);

    /**
     * 新增文章-标签关联
     * 
     * @param articleTag 文章-标签关联
     * @return 结果
     */
    public int insertArticleTag(ArticleTag articleTag);

    /**
     * 修改文章-标签关联
     * 
     * @param articleTag 文章-标签关联
     * @return 结果
     */
    public int updateArticleTag(ArticleTag articleTag);

    /**
     * 批量删除文章-标签关联
     * 
     * @param articleIds 需要删除的文章-标签关联主键集合
     * @return 结果
     */
    public int deleteArticleTagByArticleIds(Long[] articleIds);

    /**
     * 删除文章-标签关联信息
     * 
     * @param articleId 文章-标签关联主键
     * @return 结果
     */
    public int deleteArticleTagByArticleId(Long articleId);
}
