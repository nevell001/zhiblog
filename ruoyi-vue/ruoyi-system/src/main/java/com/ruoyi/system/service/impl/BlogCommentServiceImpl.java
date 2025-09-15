package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BlogCommentMapper;
import com.ruoyi.system.domain.BlogComment;
import com.ruoyi.system.service.IBlogCommentService;

/**
 * 博客评论Service业务层处理
 * 
 * @author nevell
 */
@Service
public class BlogCommentServiceImpl implements IBlogCommentService
{
    @Autowired
    private BlogCommentMapper blogCommentMapper;

    /**
     * 查询博客评论
     * 
     * @param id 博客评论主键
     * @return 博客评论
     */
    @Override
    public BlogComment selectBlogCommentById(Long id)
    {
        return blogCommentMapper.selectBlogCommentById(id);
    }

    /**
     * 查询博客评论列表
     * 
     * @param blogComment 博客评论
     * @return 博客评论集合
     */
    @Override
    public List<BlogComment> selectBlogCommentList(BlogComment blogComment)
    {
        return blogCommentMapper.selectBlogCommentList(blogComment);
    }

    /**
     * 新增博客评论
     * 
     * @param blogComment 博客评论
     * @return 结果
     */
    @Override
    public int insertBlogComment(BlogComment blogComment)
    {
        return blogCommentMapper.insertBlogComment(blogComment);
    }

    /**
     * 修改博客评论
     * 
     * @param blogComment 博客评论
     * @return 结果
     */
    @Override
    public int updateBlogComment(BlogComment blogComment)
    {
        return blogCommentMapper.updateBlogComment(blogComment);
    }

    /**
     * 批量删除博客评论
     * 
     * @param ids 需要删除的博客评论主键集合
     * @return 结果
     */
    @Override
    public int deleteBlogCommentByIds(Long[] ids)
    {
        return blogCommentMapper.deleteBlogCommentByIds(ids);
    }

    /**
     * 删除博客评论信息
     * 
     * @param id 博客评论主键
     * @return 结果
     */
    @Override
    public int deleteBlogCommentById(Long id)
    {
        return blogCommentMapper.deleteBlogCommentById(id);
    }
}