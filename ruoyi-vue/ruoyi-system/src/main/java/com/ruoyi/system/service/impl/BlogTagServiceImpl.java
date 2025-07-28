package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BlogTagMapper;
import com.ruoyi.system.domain.BlogTag;
import com.ruoyi.system.service.IBlogTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 博客标签Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-01
 */
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements IBlogTagService
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
    public List<BlogTag> selectBlogTagList(BlogTag blogTag)
    {
        return blogTagMapper.selectBlogTagList(blogTag);
    }

    /**
     * 导出博客标签列表
     * 
     * @param blogTag 博客标签
     * @return 结果
     */
    @Override
    public AjaxResult exportBlogTag(BlogTag blogTag)
    {
        List<BlogTag> list = blogTagMapper.selectBlogTagList(blogTag);
        ExcelUtil<BlogTag> util = new ExcelUtil<BlogTag>(BlogTag.class);
        return util.exportExcel(list, "博客标签数据");
    }

    /**
     * 获取博客标签详细信息
     * 
     * @param tagId 博客标签ID
     * @return 博客标签
     */
    @Override
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
    public int insertBlogTag(BlogTag blogTag)
    {
        blogTag.setCreateTime(DateUtils.getNowDate());
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
    public int updateBlogTag(BlogTag blogTag)
    {
        blogTag.setUpdateTime(DateUtils.getNowDate());
        blogTag.setUpdateBy(SecurityUtils.getUsername());
        return blogTagMapper.updateBlogTag(blogTag);
    }

    /**
     * 批量删除博客标签
     * 
     * @param tagIds 需要删除的博客标签ID
     * @return 结果
     */
    @Override
    public int deleteBlogTagByIds(Long[] tagIds)
    {
        return blogTagMapper.deleteBlogTagByIds(tagIds);
    }

    /**
     * 删除博客标签信息
     * 
     * @param tagId 博客标签ID
     * @return 结果
     */
    @Override
    public int deleteBlogTagById(Long tagId)
    {
        return blogTagMapper.deleteBlogTagById(tagId);
    }

    /**
     * 获取所有标签列表
     * 
     * @return 标签列表
     */
    @Override
    public List<BlogTag> getAllTagList()
    {
        return blogTagMapper.selectAllTagList();
    }

    /**
     * 验证标签名称是否唯一
     * 
     * @param blogTag 标签信息
     * @return 结果
     */
    @Override
    public String checkTagNameUnique(BlogTag blogTag)
    {
        Long tagId = blogTag.getTagId() == null ? -1L : blogTag.getTagId();
        BlogTag info = blogTagMapper.checkTagNameUnique(blogTag.getTagName());
        if (info != null && info.getTagId().longValue() != tagId.longValue())
        {
            return "1";
        }
        return "0";
    }
}