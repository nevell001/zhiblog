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
                tagIds.add(tag.getTagId());
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
        return blogArticleMapper.selectBlogArticleList(blogArticle);
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
        // 校验文章内容不能为空
        if (blogArticle.getContent() == null || blogArticle.getContent().trim().isEmpty()) {
            throw new RuntimeException("文章内容不能为空，请填写内容后再发布！");
        }
        // 标题去除前后空格
        String title = blogArticle.getTitle().trim();
        BlogArticle exist = blogArticleMapper.selectBlogArticleByTitle(title);
        if (exist != null) {
            throw new RuntimeException("文章标题已存在，请更换标题！");
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
                for (Long tagId : blogArticle.getTagIds()) {
                    BlogArticleTag articleTag = new BlogArticleTag();
                    articleTag.setArticleId(blogArticle.getId());
                    articleTag.setTagId(tagId);
                    blogArticleTagMapper.insertBlogArticleTag(articleTag);
                }
            }
            
            return result;
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                throw new RuntimeException("文章标题已存在，请更换标题！");
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
        // 标题去除前后空格
        String title = blogArticle.getTitle().trim();
        // 校验除自己外是否有同名文章（保持原大小写）
        BlogArticle exist = blogArticleMapper.selectBlogArticleByTitle(title);
        if (exist != null && !exist.getId().equals(blogArticle.getId())) {
            throw new RuntimeException("文章标题已存在，请更换标题！");
        }
        blogArticle.setTitle(title);
        blogArticle.setUpdateTime(DateUtils.getNowDate());
        // 确保delFlag有默认值
        if (blogArticle.getDelFlag() == null) {
            blogArticle.setDelFlag(0L);
        }
        try {
            // 先删除原有的标签关联
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
}
