package com.zhi.system.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhi.system.mapper.BlogCommentMapper;
import com.zhi.system.domain.BlogArticle;
import com.zhi.system.domain.BlogComment;
import com.zhi.system.domain.BlogNotification;
import com.zhi.system.service.IBlogArticleService;
import com.zhi.system.service.IBlogCommentService;
import com.zhi.system.service.IBlogNotificationService;

/**
 * 博客评论Service业务层处理
 * 
 * @author nevell
 */
@Service
public class BlogCommentServiceImpl implements IBlogCommentService
{
    private static final Logger logger = LoggerFactory.getLogger(BlogCommentServiceImpl.class);

    @Autowired
    private BlogCommentMapper blogCommentMapper;

    @Autowired
    private IBlogArticleService blogArticleService;

    @Autowired
    private IBlogNotificationService blogNotificationService;

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
        int result = blogCommentMapper.insertBlogComment(blogComment);
        if (result > 0)
        {
            sendCommentNotification(blogComment);
        }
        return result;
    }

    /**
     * 发送评论通知（站内信）
     */
    private void sendCommentNotification(BlogComment blogComment)
    {
        try
        {
            // 获取文章信息
            BlogArticle article = blogArticleService.selectBlogArticleById(blogComment.getArticleId());
            if (article == null)
            {
                logger.debug("评论通知跳过：文章不存在, articleId={}", blogComment.getArticleId());
                return;
            }

            // 确定接收者和通知类型
            Long recipientId = null;
            String type = "comment";
            String title = "你的文章《" + article.getTitle() + "》收到了新评论";

            if (blogComment.getParentId() != null && blogComment.getParentId() > 0)
            {
                // 回复他人的评论
                BlogComment parentComment = blogCommentMapper.selectBlogCommentById(blogComment.getParentId());
                if (parentComment != null && parentComment.getUserId() != null)
                {
                    recipientId = parentComment.getUserId();
                    type = "reply";
                    title = "你的评论收到了新回复";
                }
            }

            // 如果没有父评论或父评论无作者，通知文章作者
            if (recipientId == null)
            {
                recipientId = article.getAuthorId();
            }

            // 不给自己发通知
            Long commenterId = blogComment.getUserId();
            if (recipientId == null || (commenterId != null && recipientId.equals(commenterId)))
            {
                logger.debug("评论通知跳过：无需通知 (self or no recipient), articleId={}", blogComment.getArticleId());
                return;
            }

            // 构建通知
            String contentPreview = blogComment.getContent();
            if (contentPreview != null && contentPreview.length() > 200)
            {
                contentPreview = contentPreview.substring(0, 200) + "...";
            }

            // 发送者昵称：优先用评论提交的昵称，为空则用"匿名用户"
            String senderName = blogComment.getNickname();
            if (senderName == null || senderName.isEmpty()) {
                senderName = "匿名用户";
            }

            BlogNotification notification = new BlogNotification();
            notification.setRecipientId(recipientId);
            notification.setSenderName(senderName);
            notification.setType(type);
            notification.setTitle(title);
            notification.setContent(contentPreview);
            notification.setArticleId(blogComment.getArticleId());
            notification.setArticleTitle(article.getTitle());
            notification.setCommentId(blogComment.getId());
            notification.setIsRead(0);

            blogNotificationService.createNotification(notification);
            logger.info("评论通知已发送: recipientId={}, type={}, articleId={}", recipientId, type, blogComment.getArticleId());
        }
        catch (Exception e)
        {
            logger.error("评论通知发送失败: articleId={}", blogComment.getArticleId(), e);
        }
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

    /**
     * 统计博客评论数量
     *
     * @param blogComment 查询条件
     * @return 评论数量
     */
    @Override
    public Long selectBlogCommentCount(BlogComment blogComment)
    {
        return blogCommentMapper.selectBlogCommentCount(blogComment);
    }
}