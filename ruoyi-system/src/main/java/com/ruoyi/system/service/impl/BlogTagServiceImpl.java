package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import com.ruoyi.common.cache.annotation.BlogCacheable;
import com.ruoyi.common.cache.annotation.BlogCacheEvict;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BlogTag;
import com.ruoyi.system.mapper.BlogTagMapper;
import com.ruoyi.system.service.IBlogTagService;

/**
 * 博客标签Service业务层处理
 * 
 * @author nevell
 * @date 2025-09-08
 */
@Service
public class BlogTagServiceImpl implements IBlogTagService
{
    @Autowired
    private BlogTagMapper blogTagMapper;

    /**
     * 查询博客标签列表
     * 
     * @param blogTag 博客标签
     * @return 博客标签
     */
    @Override
    @BlogCacheable(key = "blog:tag:list:#blogTag.hashCode()", ttl = 30, timeUnit = TimeUnit.MINUTES)
    public List<BlogTag> selectBlogTagList(BlogTag blogTag)
    {
        return blogTagMapper.selectBlogTagList(blogTag);
    }

    /**
     * 查询所有标签列表
     * 
     * @return 标签列表
     */
    @Override
    @BlogCacheable(key = "blog:tag:all", ttl = 60, timeUnit = TimeUnit.MINUTES)
    public List<BlogTag> selectAllTagList()
    {
        return blogTagMapper.selectAllTagList();
    }

    /**
     * 通过ID查询单条数据
     * 
     * @param tagId 标签ID
     * @return 实例对象
     */
    @Override
    @BlogCacheable(key = "blog:tag:#tagId", ttl = 60, timeUnit = TimeUnit.MINUTES)
    public BlogTag selectBlogTagById(Long tagId)
    {
        return blogTagMapper.selectBlogTagById(tagId);
    }

    /**
     * 新增博客标签
     * 
     * @param blogTag 博客标签
     * @return 结果
     */
    @Override
    @BlogCacheEvict(value = {"blog:tag:*"}, keyPattern = "blog:tag:*")
    public int insertBlogTag(BlogTag blogTag)
    {
        blogTag.setCreateBy(SecurityUtils.getUsername());
        return blogTagMapper.insertBlogTag(blogTag);
    }

    /**
     * 修改博客标签
     * 
     * @param blogTag 博客标签
     * @return 结果
     */
    @Override
    @BlogCacheEvict(value = {"blog:tag:*"}, keyPattern = "blog:tag:*")
    public int updateBlogTag(BlogTag blogTag)
    {
        blogTag.setUpdateBy(SecurityUtils.getUsername());
        return blogTagMapper.updateBlogTag(blogTag);
    }

    /**
     * 通过主键删除数据
     *
     * @param tagId 标签ID
     * @return 影响行数
     */
    @Override
    @BlogCacheEvict(value = {"blog:tag:*"}, keyPattern = "blog:tag:*")
    public int deleteBlogTagById(Long tagId)
    {
        return blogTagMapper.deleteBlogTagById(tagId);
    }

    /**
     * 批量删除博客标签
     *
     * @param tagIds 需要删除的数据ID
     * @return 影响行数
     */
    @Override
    @BlogCacheEvict(value = {"blog:tag:*"}, keyPattern = "blog:tag:*")
    public int deleteBlogTagByIds(Long[] tagIds)
    {
        return blogTagMapper.deleteBlogTagByIds(tagIds);
    }

    @Override
    @BlogCacheable(key = "blog:tag:cloud", ttl = 30, timeUnit = TimeUnit.MINUTES)
    public List<Map<String, Object>> getTagCloud() {
        return blogTagMapper.getTagCloud();
    }

    @Override
    public boolean checkTagNameUnique(BlogTag blogTag)
    {
        Long tagId = blogTag.getTagId() == null ? -1L : blogTag.getTagId();
        BlogTag info = blogTagMapper.checkTagNameUnique(blogTag.getTagName());
        if (info != null && info.getTagId().longValue() != tagId.longValue())
        {
            return false;
        }
        return true;
    }
    
    /**
     * 检查标签是否已被文章使用
     * 
     * @param tagId 标签ID
     * @return 结果
     */
    @Override
    public boolean checkTagExistArticle(Long tagId)
    {
        int count = blogTagMapper.checkTagExistArticle(tagId);
        return count > 0;
    }

    /**
     * 根据文章ID查询标签列表
     *
     * @param articleId 文章ID
     * @return 标签列表
     */
    @Override
    public List<BlogTag> selectTagsByArticleId(Long articleId)
    {
        return blogTagMapper.selectTagsByArticleId(articleId);
    }
}