package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ArticleTagMapper;
import com.ruoyi.system.domain.ArticleTag;
import com.ruoyi.system.service.IArticleTagService;

/**
 * 文章-标签关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-15
 */
@Service
public class ArticleTagServiceImpl implements IArticleTagService 
{
    @Autowired
    private ArticleTagMapper articleTagMapper;

    /**
     * 查询文章-标签关联
     * 
     * @param articleId 文章-标签关联主键
     * @return 文章-标签关联
     */
    @Override
    public ArticleTag selectArticleTagByArticleId(Long articleId)
    {
        return articleTagMapper.selectArticleTagByArticleId(articleId);
    }

    /**
     * 查询文章-标签关联列表
     * 
     * @param articleTag 文章-标签关联
     * @return 文章-标签关联
     */
    @Override
    public List<ArticleTag> selectArticleTagList(ArticleTag articleTag)
    {
        return articleTagMapper.selectArticleTagList(articleTag);
    }

    /**
     * 新增文章-标签关联
     * 
     * @param articleTag 文章-标签关联
     * @return 结果
     */
    @Override
    public int insertArticleTag(ArticleTag articleTag)
    {
        return articleTagMapper.insertArticleTag(articleTag);
    }

    /**
     * 修改文章-标签关联
     * 
     * @param articleTag 文章-标签关联
     * @return 结果
     */
    @Override
    public int updateArticleTag(ArticleTag articleTag)
    {
        return articleTagMapper.updateArticleTag(articleTag);
    }

    /**
     * 批量删除文章-标签关联
     * 
     * @param articleIds 需要删除的文章-标签关联主键
     * @return 结果
     */
    @Override
    public int deleteArticleTagByArticleIds(Long[] articleIds)
    {
        return articleTagMapper.deleteArticleTagByArticleIds(articleIds);
    }

    /**
     * 删除文章-标签关联信息
     * 
     * @param articleId 文章-标签关联主键
     * @return 结果
     */
    @Override
    public int deleteArticleTagByArticleId(Long articleId)
    {
        return articleTagMapper.deleteArticleTagByArticleId(articleId);
    }
}
