package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BlogBookmark;

/**
 * 文章收藏Service接口
 *
 * @author nevell
 * @date 2026-01-18
 */
public interface IBlogBookmarkService {

    /**
     * 查询文章收藏
     *
     * @param id 文章收藏主键
     * @return 文章收藏
     */
    public BlogBookmark selectBlogBookmarkById(Long id);

    /**
     * 查询文章收藏列表
     *
     * @param blogBookmark 文章收藏
     * @return 文章收藏集合
     */
    public List<BlogBookmark> selectBlogBookmarkList(BlogBookmark blogBookmark);

    /**
     * 新增文章收藏
     *
     * @param blogBookmark 文章收藏
     * @return 结果
     */
    public int insertBlogBookmark(BlogBookmark blogBookmark);

    /**
     * 删除文章收藏
     *
     * @param id 文章收藏主键
     * @return 结果
     */
    public int deleteBlogBookmarkById(Long id);

    /**
     * 根据用户ID和文章ID删除收藏
     *
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 结果
     */
    public int deleteByUserIdAndArticleId(Long userId, Long articleId);

    /**
     * 切换收藏状态
     *
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 结果：1=已收藏，0=已取消
     */
    public int toggleBookmark(Long userId, Long articleId);

    /**
     * 检查用户是否已收藏文章
     *
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 结果
     */
    public boolean isBookmarked(Long userId, Long articleId);

    /**
     * 查询用户的收藏列表
     *
     * @param userId 用户ID
     * @return 收藏列表
     */
    public List<BlogBookmark> selectBookmarksByUserId(Long userId);
}
