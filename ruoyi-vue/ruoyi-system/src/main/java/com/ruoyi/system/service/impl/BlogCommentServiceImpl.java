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

    /**
     * 审核通过博客评论
     * 
     * @param ids 需要审核通过的博客评论主键集合
     * @return 结果
     */
    @Override
    public int auditBlogCommentByIds(Long[] ids)
    {
        // 更新评论状态为已审核（通过）
        int count = 0;
        for (Long id : ids) {
            BlogComment blogComment = new BlogComment();
            blogComment.setId(id);
            blogComment.setStatus("1"); // 1表示已审核通过
            count += blogCommentMapper.updateBlogComment(blogComment);
        }
        return count;
    }

    /**
     * 审核拒绝博客评论
     * 
     * @param ids 需要审核拒绝的博客评论主键集合
     * @return 结果
     */
    @Override
    public int rejectBlogCommentByIds(Long[] ids)
    {
        // 更新评论状态为已删除（拒绝）
        int count = 0;
        for (Long id : ids) {
            BlogComment blogComment = new BlogComment();
            blogComment.setId(id);
            blogComment.setStatus("2"); // 2表示已删除（拒绝）
            count += blogCommentMapper.updateBlogComment(blogComment);
        }
        return count;
    }
}