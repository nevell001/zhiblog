package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BlogTag;

/**
 * 博客标签Mapper接口
 * 
 * @author nevell
 * @date 2025-09-08
 */
public interface BlogTagMapper 
{
    /**
     * 查询博客标签列表
     * 
     * @param blogTag 博客标签
     * @return 博客标签集合
     */
    public List<BlogTag> selectBlogTagList(BlogTag blogTag);

    /**
     * 查询所有标签列表
     * 
     * @return 标签列表
     */
    public List<BlogTag> selectAllTagList();

    /**
     * 通过ID查询单条数据
     * 
     * @param tagId 标签ID
     * @return 实例对象
     */
    public BlogTag selectBlogTagById(Long tagId);

    /**
     * 新增博客标签
     * 
     * @param blogTag 博客标签
     * @return 影响行数
     */
    public int insertBlogTag(BlogTag blogTag);

    /**
     * 修改博客标签
     * 
     * @param blogTag 博客标签
     * @return 影响行数
     */
    public int updateBlogTag(BlogTag blogTag);

    /**
     * 通过主键删除数据
     * 
     * @param tagId 标签ID
     * @return 影响行数
     */
    public int deleteBlogTagById(Long tagId);

    /**
     * 批量删除博客标签
     * 
     * @param tagIds 需要删除的数据ID
     * @return 影响行数
     */
    public int deleteBlogTagByIds(Long[] tagIds);

    /**
     * 校验标签名称是否唯一
     * 
     * @param tagName 标签名称
     * @return 标签信息
     */
    public BlogTag checkTagNameUnique(String tagName);
    
    /**
     * 检查标签是否已被文章使用
     * 
     * @param tagId 标签ID
     * @return 结果
     */
    public int checkTagExistArticle(Long tagId);
}