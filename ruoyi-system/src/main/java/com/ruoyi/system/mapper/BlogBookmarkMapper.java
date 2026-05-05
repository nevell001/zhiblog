package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BlogBookmark;
import org.apache.ibatis.annotations.Param;

/**
 * 文章收藏Mapper接口
 *
 * @author nevell
 * @date 2026-01-18
 */
public interface BlogBookmarkMapper {

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
    public int deleteBlogBookmarkByUserIdAndArticleId(@Param("userId") Long userId, @Param("articleId") Long articleId);

    /**
     * 检查收藏是否已存在
     *
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 结果
     */
    public int existsBookmark(@Param("userId") Long userId, @Param("articleId") Long articleId);

    /**
     * 查询用户的收藏列表
     *
     * @param userId 用户ID
     * @return 收藏列表
     */
    public List<BlogBookmark> selectBookmarksByUserId(Long userId);
}
