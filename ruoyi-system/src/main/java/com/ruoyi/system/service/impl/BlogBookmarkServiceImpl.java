package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.system.domain.BlogBookmark;
import com.ruoyi.system.mapper.BlogBookmarkMapper;
import com.ruoyi.system.service.IBlogBookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文章收藏Service实现
 *
 * @author nevell
 * @date 2026-01-18
 */
@Service
public class BlogBookmarkServiceImpl implements IBlogBookmarkService {

    @Autowired
    private BlogBookmarkMapper blogBookmarkMapper;

    /**
     * 查询文章收藏
     *
     * @param id 文章收藏主键
     * @return 文章收藏
     */
    @Override
    public BlogBookmark selectBlogBookmarkById(Long id) {
        return blogBookmarkMapper.selectBlogBookmarkById(id);
    }

    /**
     * 查询文章收藏列表
     *
     * @param blogBookmark 文章收藏
     * @return 文章收藏
     */
    @Override
    public List<BlogBookmark> selectBlogBookmarkList(BlogBookmark blogBookmark) {
        return blogBookmarkMapper.selectBlogBookmarkList(blogBookmark);
    }

    /**
     * 新增文章收藏
     *
     * @param blogBookmark 文章收藏
     * @return 结果
     */
    @Override
    public int insertBlogBookmark(BlogBookmark blogBookmark) {
        return blogBookmarkMapper.insertBlogBookmark(blogBookmark);
    }

    /**
     * 删除文章收藏
     *
     * @param id 文章收藏主键
     * @return 结果
     */
    @Override
    public int deleteBlogBookmarkById(Long id) {
        return blogBookmarkMapper.deleteBlogBookmarkById(id);
    }

    /**
     * 根据用户ID和文章ID删除收藏
     *
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 结果
     */
    @Override
    public int deleteByUserIdAndArticleId(Long userId, Long articleId) {
        return blogBookmarkMapper.deleteBlogBookmarkByUserIdAndArticleId(userId, articleId);
    }

    /**
     * 切换收藏状态
     *
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 结果：1=已收藏，0=已取消
     */
    @Override
    public int toggleBookmark(Long userId, Long articleId) {
        // 检查是否已收藏
        int exists = blogBookmarkMapper.existsBookmark(userId, articleId);
        if (exists > 0) {
            // 已收藏，取消收藏
            blogBookmarkMapper.deleteBlogBookmarkByUserIdAndArticleId(userId, articleId);
            return 0;
        } else {
            // 未收藏，添加收藏
            BlogBookmark bookmark = new BlogBookmark();
            bookmark.setUserId(userId);
            bookmark.setArticleId(articleId);
            blogBookmarkMapper.insertBlogBookmark(bookmark);
            return 1;
        }
    }

    /**
     * 检查用户是否已收藏文章
     *
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 结果
     */
    @Override
    public boolean isBookmarked(Long userId, Long articleId) {
        return blogBookmarkMapper.existsBookmark(userId, articleId) > 0;
    }

    /**
     * 查询用户的收藏列表
     *
     * @param userId 用户ID
     * @return 收藏列表
     */
    @Override
    public List<BlogBookmark> selectBookmarksByUserId(Long userId) {
        return blogBookmarkMapper.selectBookmarksByUserId(userId);
    }
}
