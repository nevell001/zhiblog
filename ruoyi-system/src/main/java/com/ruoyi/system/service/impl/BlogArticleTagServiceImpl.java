package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.BlogArticleTag;
import com.ruoyi.system.mapper.BlogArticleTagMapper;
import com.ruoyi.system.service.IBlogArticleTagService;

/**
 * 文章标签关联Service业务层处理
 * 
 * @author nevell
 * @date 2025-09-08
 */
@Service
public class BlogArticleTagServiceImpl  implements IBlogArticleTagService
{
    @Autowired
    private BlogArticleTagMapper blogArticleTagMapper;

    /**
     * 查询文章标签关联列表
     * 
     * @param blogArticleTag 文章标签关联
     * @return 文章标签关联集合
     */
    @Override
    public List<BlogArticleTag> selectBlogArticleTagList(BlogArticleTag blogArticleTag)
    {
        return blogArticleTagMapper.selectBlogArticleTagList(blogArticleTag);
    }

    /**
     * 通过文章ID查询标签ID列表
     * 
     * @param articleId 文章ID
     * @return 标签ID列表
     */
    @Override
    public List<Long> selectTagIdsByArticleId(Long articleId)
    {
        return blogArticleTagMapper.selectTagIdsByArticleId(articleId);
    }

    /**
     * 通过标签ID查询文章ID列表
     * 
     * @param tagId 标签ID
     * @return 文章ID列表
     */
    @Override
    public List<Long> selectArticleIdsByTagId(Long tagId)
    {
        return blogArticleTagMapper.selectArticleIdsByTagId(tagId);
    }

    /**
     * 新增文章标签关联
     * 
     * @param blogArticleTag 文章标签关联
     * @return 结果
     */
    @Override
    public int insertBlogArticleTag(BlogArticleTag blogArticleTag)
    {
        return blogArticleTagMapper.insertBlogArticleTag(blogArticleTag);
    }

    /**
     * 批量新增文章标签关联
     * 
     * @param articleTagList 文章标签关联列表
     * @return 结果
     */
    @Override
    public int batchInsertArticleTag(List<BlogArticleTag> articleTagList)
    {
        return blogArticleTagMapper.batchInsertArticleTag(articleTagList);
    }

    /**
     * 修改文章标签关联
     * 
     * @param blogArticleTag 文章标签关联
     * @return 结果
     */
    @Override
    public int updateBlogArticleTag(BlogArticleTag blogArticleTag)
    {
        return blogArticleTagMapper.updateBlogArticleTag(blogArticleTag);
    }

    /**
     * 通过主键删除数据
     * 
     * @param id 主键ID
     * @return 影响行数
     */
    @Override
    public int deleteBlogArticleTagById(Long id)
    {
        return blogArticleTagMapper.deleteBlogArticleTagById(id);
    }

    /**
     * 批量删除文章标签关联
     * 
     * @param ids 需要删除的数据ID
     * @return 影响行数
     */
    @Override
    public int deleteBlogArticleTagByIds(Long[] ids)
    {
        return blogArticleTagMapper.deleteBlogArticleTagByIds(ids);
    }

    /**
     * 通过文章ID删除文章和标签关联
     * 
     * @param articleId 文章ID
     * @return 影响行数
     */
    @Override
    public int deleteByArticleId(Long articleId)
    {
        return blogArticleTagMapper.deleteByArticleId(articleId);
    }

    /**
     * 通过ID查询文章标签关联
     * 
     * @param id 主键ID
     * @return 文章标签关联
     */
    @Override
    public BlogArticleTag selectBlogArticleTagById(Long id)
    {
        return blogArticleTagMapper.selectBlogArticleTagById(id);
    }
}