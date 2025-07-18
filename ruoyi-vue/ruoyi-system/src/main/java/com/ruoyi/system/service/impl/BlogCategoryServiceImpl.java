package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BlogCategoryMapper;
import com.ruoyi.system.domain.BlogCategory;
import com.ruoyi.system.service.IBlogCategoryService;

/**
 * 文章分类Service业务层处理
 * 
 * @author nevell
 * @date 2025-07-18
 */
@Service
public class BlogCategoryServiceImpl implements IBlogCategoryService 
{
    @Autowired
    private BlogCategoryMapper blogCategoryMapper;

    /**
     * 查询文章分类
     * 
     * @param id 文章分类主键
     * @return 文章分类
     */
    @Override
    public BlogCategory selectBlogCategoryById(Long id)
    {
        return blogCategoryMapper.selectBlogCategoryById(id);
    }

    /**
     * 查询文章分类列表
     * 
     * @param blogCategory 文章分类
     * @return 文章分类
     */
    @Override
    public List<BlogCategory> selectBlogCategoryList(BlogCategory blogCategory)
    {
        return blogCategoryMapper.selectBlogCategoryList(blogCategory);
    }

    /**
     * 新增文章分类
     * 
     * @param blogCategory 文章分类
     * @return 结果
     */
    @Override
    public int insertBlogCategory(BlogCategory blogCategory)
    {
        blogCategory.setCreateTime(DateUtils.getNowDate());
        return blogCategoryMapper.insertBlogCategory(blogCategory);
    }

    /**
     * 修改文章分类
     * 
     * @param blogCategory 文章分类
     * @return 结果
     */
    @Override
    public int updateBlogCategory(BlogCategory blogCategory)
    {
        blogCategory.setUpdateTime(DateUtils.getNowDate());
        return blogCategoryMapper.updateBlogCategory(blogCategory);
    }

    /**
     * 批量删除文章分类
     * 
     * @param ids 需要删除的文章分类主键
     * @return 结果
     */
    @Override
    public int deleteBlogCategoryByIds(Long[] ids)
    {
        return blogCategoryMapper.deleteBlogCategoryByIds(ids);
    }

    /**
     * 删除文章分类信息
     * 
     * @param id 文章分类主键
     * @return 结果
     */
    @Override
    public int deleteBlogCategoryById(Long id)
    {
        return blogCategoryMapper.deleteBlogCategoryById(id);
    }
}
