package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.BlogTag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 博客标签Service接口
 * 
 * @author ruoyi
 * @date 2025-06-01
 */
public interface IBlogTagService extends IService<BlogTag>
{
    /**
     * 查询博客标签列表
     * 
     * @param blogTag 博客标签
     * @return 博客标签集合
     */
    public List<BlogTag> selectBlogTagList(BlogTag blogTag);

    /**
     * 导出博客标签列表
     * 
     * @param blogTag 博客标签
     * @return 数组
     */
    public AjaxResult exportBlogTag(BlogTag blogTag);

    /**
     * 获取博客标签详细信息
     * 
     * @param tagId 博客标签ID
     * @return 博客标签
     */
    public BlogTag selectBlogTagById(Long tagId);

    /**
     * 新增博客标签
     * 
     * @param blogTag 博客标签
     * @return 结果
     */
    public int insertBlogTag(BlogTag blogTag);

    /**
     * 修改博客标签
     * 
     * @param blogTag 博客标签
     * @return 结果
     */
    public int updateBlogTag(BlogTag blogTag);

    /**
     * 批量删除博客标签
     * 
     * @param tagIds 需要删除的博客标签ID
     * @return 结果
     */
    public int deleteBlogTagByIds(Long[] tagIds);

    /**
     * 删除博客标签信息
     * 
     * @param tagId 博客标签ID
     * @return 结果
     */
    public int deleteBlogTagById(Long tagId);

    /**
     * 获取所有标签列表（用于文章编辑时选择）
     * 
     * @return 标签列表
     */
    public List<BlogTag> getAllTagList();

    /**
     * 验证标签名称是否唯一
     * 
     * @param blogTag 标签信息
     * @return 结果
     */
    public String checkTagNameUnique(BlogTag blogTag);
}