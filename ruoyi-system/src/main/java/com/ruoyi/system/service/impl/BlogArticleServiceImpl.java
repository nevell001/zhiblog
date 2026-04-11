package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.ruoyi.common.cache.annotation.BlogCacheable;
import com.ruoyi.common.cache.annotation.BlogCacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.BlogArticleMapper;
import com.ruoyi.system.mapper.BlogArticleTagMapper;
import com.ruoyi.system.mapper.BlogTagMapper;
import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.domain.BlogArticleTag;
import com.ruoyi.system.domain.BlogTag;
import com.ruoyi.system.service.IBlogArticleService;
import com.ruoyi.common.exception.DuplicateArticleTitleException;

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
            if (tags != null) {
                for (BlogTag tag : tags) {
                    if (tag != null && tag.getId() != null) {
                        tagIds.add(tag.getId());
                    }
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
    // 移除缓存以确保前台始终显示最新数据
    public List<BlogArticle> selectBlogArticleList(BlogArticle blogArticle)
    {
        List<BlogArticle> articleList = blogArticleMapper.selectBlogArticleList(blogArticle);
        loadTagsForArticles(articleList);
        return articleList;
    }

    /**
     * 批量加载文章的标签数据（解决N+1查询问题）
     *
     * @param articleList 文章列表
     */
    private void loadTagsForArticles(List<BlogArticle> articleList) {
        if (articleList == null || articleList.isEmpty()) {
            return;
        }

        // 提取所有文章ID
        List<Long> articleIds = new ArrayList<>();
        for (BlogArticle article : articleList) {
            if (article != null && article.getId() != null) {
                articleIds.add(article.getId());
            }
        }

        if (articleIds.isEmpty()) {
            return;
        }

        // 批量查询所有标签映射（每行包含 articleId 和标签信息）
        List<Map<String, Object>> tagMappings = blogTagMapper.selectTagsByArticleIds(articleIds);

        // 将标签映射转换为 Map<Long, List<BlogTag>>
        Map<Long, List<BlogTag>> tagsMap = new HashMap<>();
        for (Map<String, Object> mapping : tagMappings) {
            Long articleId = (Long) mapping.get("articleId");
            if (articleId != null) {
                tagsMap.computeIfAbsent(articleId, k -> new ArrayList<>());
                // 创建 BlogTag 对象并添加到列表
                BlogTag tag = new BlogTag();
                tag.setId((Long) mapping.get("id"));
                tag.setName((String) mapping.get("name"));
                tag.setDescription((String) mapping.get("description"));
                tag.setColor((String) mapping.get("color"));
                tag.setIcon((String) mapping.get("icon"));
                tag.setDelFlag((Integer) mapping.get("delFlag"));
                tagsMap.get(articleId).add(tag);
            }
        }

        // 为每篇文章设置标签
        for (BlogArticle article : articleList) {
            if (article != null && article.getId() != null) {
                List<BlogTag> tags = tagsMap.getOrDefault(article.getId(), new ArrayList<>());
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


    /**
     * 新增博客文章
     *
     * @param blogArticle 博客文章
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBlogArticle(BlogArticle blogArticle)
    {
        validateArticleContent(blogArticle);
        String title = validateArticleTitle(blogArticle.getTitle());
        blogArticle.setTitle(title);
        blogArticle.setCreateTime(DateUtils.getNowDate());
        blogArticle.setAuthorId(com.ruoyi.common.utils.SecurityUtils.getLoginUser().getUser().getUserId());
        setDefaultValues(blogArticle);
        
        try {
            int result = blogArticleMapper.insertBlogArticle(blogArticle);
            processTagAssociations(blogArticle);
            return result;
        } catch (Exception e) {
            handleInsertException(e);
            throw e;
        }
    }

    /**
     * 验证文章内容
     */
    private void validateArticleContent(BlogArticle blogArticle)
    {
        if (blogArticle.getContent() == null || blogArticle.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("文章内容不能为空");
        }
    }

    /**
     * 验证文章标题
     */
    private String validateArticleTitle(String title)
    {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("文章标题不能为空");
        }
        title = title.trim();
        BlogArticle exist = blogArticleMapper.selectBlogArticleByTitle(title);
        if (exist != null) {
            throw new IllegalArgumentException("文章标题已存在");
        }
        return title;
    }

    /**
     * 设置默认值
     */
    private void setDefaultValues(BlogArticle blogArticle)
    {
        if (blogArticle.getDelFlag() == null) {
            blogArticle.setDelFlag(0L);
        }
        if (blogArticle.getViewCount() == null) {
            blogArticle.setViewCount(0L);
        }
        if (blogArticle.getLikeCount() == null) {
            blogArticle.setLikeCount(0L);
        }
        if (blogArticle.getCommentCount() == null) {
            blogArticle.setCommentCount(0L);
        }
    }

    /**
     * 处理标签关联
     */
    private void processTagAssociations(BlogArticle blogArticle)
    {
        if (blogArticle.getTagIds() == null || blogArticle.getTagIds().isEmpty()) {
            return;
        }
        
        for (Object tagIdObj : blogArticle.getTagIds()) {
            Long tagId = convertToLong(tagIdObj);
            if (tagId != null) {
                BlogArticleTag articleTag = new BlogArticleTag();
                articleTag.setArticleId(blogArticle.getId());
                articleTag.setTagId(tagId);
                blogArticleTagMapper.insertBlogArticleTag(articleTag);
            }
        }
    }

    /**
     * 将对象转换为 Long 类型
     */
    private Long convertToLong(Object obj)
    {
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        }
        if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 处理插入异常
     */
    private void handleInsertException(Exception e)
    {
        if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
            throw new DuplicateArticleTitleException("文章标题已存在");
        }
    }

    /**
     * 修改博客文章
     * 
     * @param blogArticle 博客文章
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BlogCacheEvict(value = {"blog:article:*", "blog:article:list:*", "blog:hot:*"}, keyPattern = "blog:*")
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
            // 检查是否为数据库唯一索引冲突
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                throw new DuplicateArticleTitleException("文章标题已存在，请更换标题！");
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
    @Transactional(rollbackFor = Exception.class)
    @BlogCacheEvict(value = {"blog:article:*", "blog:article:list:*", "blog:hot:*"}, keyPattern = "blog:*")
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
        List<BlogArticle> articleList = blogArticleMapper.selectArticlesByTagId(tagId);
        loadTagsForArticles(articleList);
        return articleList;
    }

    @Override
    @BlogCacheable(key = "blog:search:#keyword + '_' + (#blogArticle != null ? #blogArticle.hashCode() : 'null')", ttl = 10, timeUnit = TimeUnit.MINUTES)
    public List<BlogArticle> searchArticles(String keyword, BlogArticle blogArticle) {
        List<BlogArticle> articleList = blogArticleMapper.searchArticles(keyword, blogArticle);
        loadTagsForArticles(articleList);
        return articleList;
    }

    /**
     * 插入文章标签关联关系
     *
     * @param articleId 文章ID
     * @param tagIds 标签ID列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
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

    /**
     * 获取文章发布趋势（按月统计）
     *
     * @return 文章发布趋势数据
     */
    @Override
    public List<Map<String, Object>> selectArticleTrend()
    {
        return blogArticleMapper.selectArticleTrend();
    }

    /**
     * 查询博客文章列表（带缓存，用于热门文章）
     *
     * @param blogArticle 博客文章
     * @return 博客文章集合
     */
    @Override
    @BlogCacheable(key = "blog:hot:#blogArticle.hashCode()", ttl = 60, timeUnit = TimeUnit.MINUTES)
    public List<BlogArticle> selectBlogArticleListWithCache(BlogArticle blogArticle)
    {
        return selectBlogArticleList(blogArticle);
    }

    /**
     * 获取热门文章（按浏览量降序排序）
     *
     * @param blogArticle 查询条件
     * @return 热门文章列表
     */
    @Override
    public List<BlogArticle> selectHotArticles(BlogArticle blogArticle)
    {
        return blogArticleMapper.selectHotArticles(blogArticle);
    }

    /**
     * 根据归档月份获取文章列表
     *
     * @param blogArticle 查询条件（包含archiveDate）
     * @return 文章列表
     */
    @Override
    public List<BlogArticle> selectArticlesByArchive(BlogArticle blogArticle)
    {
        return blogArticleMapper.selectArticlesByArchive(blogArticle);
    }

    /**
     * 批量更新文章置顶状态
     *
     * @param ids 文章ID列表
     * @param isTop 置顶状态（0-不置顶，1-置顶）
     * @return 结果
     */
    @Override
    @BlogCacheEvict(keyPattern = "blog:*")
    public int updateArticleTopStatus(List<Long> ids, Integer isTop)
    {
        if (ids == null || ids.isEmpty())
        {
            return 0;
        }
        return blogArticleMapper.updateArticleTopStatus(ids, isTop);
    }

    /**
     * 批量更新文章推荐状态
     *
     * @param ids 文章ID列表
     * @param isRecommend 推荐状态（0-不推荐，1-推荐）
     * @return 结果
     */
    @Override
    @BlogCacheEvict(keyPattern = "blog:*")
    public int updateArticleRecommendStatus(List<Long> ids, Integer isRecommend)
    {
        if (ids == null || ids.isEmpty())
        {
            return 0;
        }
        return blogArticleMapper.updateArticleRecommendStatus(ids, isRecommend);
    }
}
