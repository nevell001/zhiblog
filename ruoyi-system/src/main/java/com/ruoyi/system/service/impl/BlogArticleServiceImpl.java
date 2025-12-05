package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.BlogArticleMapper;
import com.ruoyi.system.mapper.BlogArticleTagMapper;
import com.ruoyi.system.mapper.BlogTagMapper;
import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.domain.BlogArticleTag;
import com.ruoyi.system.domain.BlogTag;
import com.ruoyi.system.service.IBlogArticleService;

/**
 * 博客文章Service业务层处理
 * 
 * @author nevell
 * @date 2025-07-18
 */
@Service
public class BlogArticleServiceImpl implements IBlogArticleService 
{
    @Autowired
    private BlogArticleMapper blogArticleMapper;

    @Autowired
    private BlogArticleTagMapper blogArticleTagMapper;

    @Autowired
    private BlogTagMapper blogTagMapper;

    /**
     * 查询博客文章
     * 
     * @param id 博客文章主键
     * @return 博客文章
     */
    @Override
    public BlogArticle selectBlogArticleById(Long id)
    {
        BlogArticle article = blogArticleMapper.selectBlogArticleById(id);
        if (article != null) {
            // 查询文章关联的标签
            List<BlogTag> tags = blogTagMapper.selectTagsByArticleId(id);
            article.setTags(tags);
            
            // 设置标签ID列表
            List<Long> tagIds = new ArrayList<>();
            for (BlogTag tag : tags) {
                if (tag != null && tag.getId() != null) {
                    tagIds.add(tag.getId());
                }
            }
            article.setTagIds(tagIds);
        }
        return article;
    }

    /**
     * 查询博客文章列表
     * 
     * @param blogArticle 博客文章
     * @return 博客文章
     */
    @Override
    public List<BlogArticle> selectBlogArticleList(BlogArticle blogArticle)
    {
        List<BlogArticle> articleList = blogArticleMapper.selectBlogArticleList(blogArticle);
        if (articleList != null && !articleList.isEmpty()) {
            // 为每篇文章加载标签数据
            for (BlogArticle article : articleList) {
                if (article != null && article.getId() != null) {
                    List<BlogTag> tags = blogTagMapper.selectTagsByArticleId(article.getId());
                    article.setTags(tags);

                    // 设置标签ID列表
                    List<Long> tagIds = new ArrayList<>();
                    for (BlogTag tag : tags) {
                        if (tag != null && tag.getId() != null) {
                            tagIds.add(tag.getId());
                        }
                    }
                    article.setTagIds(tagIds);
                }
            }
        }
        return articleList;
    }

    /**
     * 新增博客文章
     * 
     * @param blogArticle 博客文章
     * @return 结果
     */
    @Override
    @Transactional
    public int insertBlogArticle(BlogArticle blogArticle)
    {
        if (blogArticle.getContent() == null || blogArticle.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("文章内容不能为空");
        }
        String title = blogArticle.getTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("文章标题不能为空");
        }
        title = title.trim();
        BlogArticle exist = blogArticleMapper.selectBlogArticleByTitle(title);
        if (exist != null) {
            throw new IllegalArgumentException("文章标题已存在");
        }
        blogArticle.setTitle(title);
        blogArticle.setCreateTime(DateUtils.getNowDate());
        // 新增时自动设置作者ID
        blogArticle.setAuthorId(com.ruoyi.common.utils.SecurityUtils.getLoginUser().getUser().getUserId());
        // 确保delFlag有默认值
        if (blogArticle.getDelFlag() == null) {
            blogArticle.setDelFlag(0L);
        }
        try {
            int result = blogArticleMapper.insertBlogArticle(blogArticle);
            
            // 处理标签关联
            if (blogArticle.getTagIds() != null && !blogArticle.getTagIds().isEmpty()) {
                for (Object tagIdObj : blogArticle.getTagIds()) {
                    Long tagId = null;
                    if (tagIdObj instanceof Long) {
                        tagId = (Long) tagIdObj;
                    } else if (tagIdObj instanceof Integer) {
                        tagId = ((Integer) tagIdObj).longValue();
                    } else if (tagIdObj instanceof String) {
                        tagId = Long.parseLong((String) tagIdObj);
                    }
                    
                    if (tagId != null) {
                        BlogArticleTag articleTag = new BlogArticleTag();
                        articleTag.setArticleId(blogArticle.getId());
                        articleTag.setTagId(tagId);
                        blogArticleTagMapper.insertBlogArticleTag(articleTag);
                    }
                }
            }
            
            return result;
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("文章标题已存在");
            }
            throw e;
        }
    }

    /**
     * 修改博客文章
     * 
     * @param blogArticle 博客文章
     * @return 结果
     */
    @Override
    @Transactional
    public int updateBlogArticle(BlogArticle blogArticle)
    {
        if (blogArticle.getId() == null) {
            throw new IllegalArgumentException("文章ID不能为空");
        }
        String title = blogArticle.getTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("文章标题不能为空");
        }
        title = title.trim();
        blogArticle.setTitle(title);
        BlogArticle exist = blogArticleMapper.selectBlogArticleByTitle(title);
        if (exist != null && !exist.getId().equals(blogArticle.getId())) {
            throw new IllegalArgumentException("文章标题已存在");
        }
        // 如果只更新状态等其他字段，不校验标题和唯一性
        blogArticle.setUpdateTime(DateUtils.getNowDate());
        // 确保delFlag有默认值
        if (blogArticle.getDelFlag() == null) {
            blogArticle.setDelFlag(0L);
        }
        try {
            blogArticleTagMapper.deleteByArticleId(blogArticle.getId());
            
            // 处理新的标签关联
            if (blogArticle.getTagIds() != null && !blogArticle.getTagIds().isEmpty()) {
                for (Long tagId : blogArticle.getTagIds()) {
                    BlogArticleTag articleTag = new BlogArticleTag();
                    articleTag.setArticleId(blogArticle.getId());
                    articleTag.setTagId(tagId);
                    blogArticleTagMapper.insertBlogArticleTag(articleTag);
                }
            }
            
            return blogArticleMapper.updateBlogArticle(blogArticle);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                throw new RuntimeException("文章标题已存在，请更换标题！");
            }
            throw e;
        }
    }

    /**
     * 批量删除博客文章
     * 
     * @param ids 需要删除的博客文章主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteBlogArticleByIds(Long[] ids)
    {
        // 先删除标签关联
        for (Long id : ids) {
            blogArticleTagMapper.deleteByArticleId(id);
        }
        return blogArticleMapper.deleteBlogArticleByIds(ids);
    }

    /**
     * 删除博客文章信息
     * 
     * @param id 博客文章主键
     * @return 结果
     */
    @Override
    public int deleteBlogArticleById(Long id)
    {
        return blogArticleMapper.deleteBlogArticleById(id);
    }

    @Override
    public void addViewCount(Long id) {
        blogArticleMapper.addViewCount(id);
    }

    @Override
    public BlogArticle getPrevArticle(Long id) {
        return blogArticleMapper.getPrevArticle(id);
    }

    @Override
    public BlogArticle getNextArticle(Long id) {
        return blogArticleMapper.getNextArticle(id);
    }

    @Override
    public List<Map<String, Object>> getArticleArchive() {
        return blogArticleMapper.getArticleArchive();
    }

    @Override
    public List<BlogArticle> selectArticlesByTagId(Long tagId) {
        return blogArticleMapper.selectArticlesByTagId(tagId);
    }

    @Override
    public List<BlogArticle> searchArticles(String keyword, BlogArticle blogArticle) {
        return blogArticleMapper.searchArticles(keyword, blogArticle);
    }

    /**
     * 插入文章标签关联关系
     *
     * @param articleId 文章ID
     * @param tagIds 标签ID列表
     * @return 结果
     */
    @Override
    @Transactional
    public int insertArticleTagRelations(Long articleId, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return 0;
        }
        
        int result = 0;
        for (Long tagId : tagIds) {
            BlogArticleTag articleTag = new BlogArticleTag();
            articleTag.setArticleId(articleId);
            articleTag.setTagId(tagId);
            result += blogArticleTagMapper.insertBlogArticleTag(articleTag);
        }
        return result;
    }

    /**
     * 更新文章标签关联关系
     *
     * @param articleId 文章ID
     * @param tagIds 标签ID列表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateArticleTagRelations(Long articleId, List<Long> tagIds) {
        // 先删除原有关联
        deleteArticleTagRelations(articleId);
        
        // 插入新的关联
        return insertArticleTagRelations(articleId, tagIds);
    }

    /**
     * 删除文章标签关联关系
     *
     * @param articleId 文章ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteArticleTagRelations(Long articleId) {
        return blogArticleTagMapper.deleteByArticleId(articleId);
    }

    /**
     * 批量更新文章状态
     *
     * @param ids 文章ID列表
     * @param status 状态
     * @return 结果
     */
    @Override
    @Transactional
    public int updateArticleStatus(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        
        int result = 0;
        for (Long id : ids) {
            // 先查询文章，验证标题是否为空
            BlogArticle existingArticle = blogArticleMapper.selectBlogArticleById(id);
            if (existingArticle != null) {
                // 当状态为发布时，校验标题是否为空
                if (status == 1 && (existingArticle.getTitle() == null || existingArticle.getTitle().trim().isEmpty())) {
                    throw new IllegalArgumentException("文章标题不能为空");
                }
                
                // 创建更新对象，仅设置必要字段
                BlogArticle article = new BlogArticle();
                article.setId(id);
                article.setStatus(status.longValue());
                article.setUpdateTime(DateUtils.getNowDate());
                result += blogArticleMapper.updateBlogArticle(article);
            }
        }
        return result;
    }

    /**
     * 查询博客文章数量
     * 
     * @param blogArticle 查询条件
     * @return 文章数量
     */
    @Override
    public Long selectBlogArticleCount(BlogArticle blogArticle)
    {
        return blogArticleMapper.selectBlogArticleCount(blogArticle);
    }

    /**
     * 获取文章总浏览量
     * 
     * @return 总浏览量
     */
    @Override
    public Long selectTotalViewCount()
    {
        return blogArticleMapper.selectTotalViewCount();
    }

    /**
     * 获取文章平均浏览量
     * 
     * @return 平均浏览量
     */
    @Override
    public Double selectAverageViewCount()
    {
        return blogArticleMapper.selectAverageViewCount();
    }
}
