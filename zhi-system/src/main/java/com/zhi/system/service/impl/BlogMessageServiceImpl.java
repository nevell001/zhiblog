package com.zhi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhi.common.exception.ServiceException;
import com.zhi.common.utils.SecurityUtils;
import com.zhi.common.utils.StringUtils;
import com.zhi.system.domain.BlogMessage;
import com.zhi.system.mapper.BlogMessageMapper;
import com.zhi.system.service.IBlogMessageService;

/**
 * 留言板Service业务层处理
 * 
 * @author nevell
 * @date 2026-09-10
 */
@Service
public class BlogMessageServiceImpl implements IBlogMessageService
{
    /** 待审核 */
    private static final String STATUS_PENDING = "0";

    /** 已发布 */
    private static final String STATUS_PUBLISHED = "1";

    /** 已拒绝 */
    private static final String STATUS_REJECTED = "2";

    @Autowired
    private BlogMessageMapper blogMessageMapper;

    /**
     * 查询留言列表
     * 
     * @param blogMessage 留言
     * @return 留言集合
     */
    @Override
    public List<BlogMessage> selectBlogMessageList(BlogMessage blogMessage)
    {
        return blogMessageMapper.selectBlogMessageList(blogMessage);
    }

    /**
     * 查询前台展示的留言列表（仅已发布）
     * 
     * @return 留言集合
     */
    @Override
    public List<BlogMessage> selectPublishedMessageList()
    {
        return blogMessageMapper.selectPublishedMessageList();
    }

    /**
     * 查询前台已发布留言总数
     * 
     * @return 留言总数
     */
    @Override
    public Long selectPublishedMessageCount()
    {
        return blogMessageMapper.selectPublishedMessageCount();
    }

    /**
     * 通过ID查询单条数据
     * 
     * @param id 留言ID
     * @return 实例对象
     */
    @Override
    public BlogMessage selectBlogMessageById(Long id)
    {
        return blogMessageMapper.selectBlogMessageById(id);
    }

    /**
     * 新增留言
     * 
     * @param blogMessage 留言
     * @return 结果
     */
    @Override
    public int insertBlogMessage(BlogMessage blogMessage)
    {
        if (StringUtils.isEmpty(blogMessage.getStatus()))
        {
            blogMessage.setStatus(STATUS_PENDING);
        }
        if (StringUtils.isEmpty(blogMessage.getDelFlag()))
        {
            blogMessage.setDelFlag("0");
        }
        blogMessage.setCreateTime(new Date());
        return blogMessageMapper.insertBlogMessage(blogMessage);
    }

    /**
     * 修改留言
     * 
     * @param blogMessage 留言
     * @return 结果
     */
    @Override
    public int updateBlogMessage(BlogMessage blogMessage)
    {
        blogMessage.setUpdateTime(new Date());
        return blogMessageMapper.updateBlogMessage(blogMessage);
    }

    /**
     * 审核留言（status：0待审核 1已发布 2已拒绝）
     * 
     * @param id 留言ID
     * @param status 目标状态
     * @return 结果
     */
    @Override
    public int auditBlogMessage(Long id, String status)
    {
        if (!STATUS_PENDING.equals(status) && !STATUS_PUBLISHED.equals(status) && !STATUS_REJECTED.equals(status))
        {
            throw new ServiceException("留言状态不合法");
        }
        BlogMessage message = new BlogMessage();
        message.setId(id);
        message.setStatus(status);
        message.setUpdateBy(SecurityUtils.getUsername());
        message.setUpdateTime(new Date());
        return blogMessageMapper.updateBlogMessage(message);
    }

    /**
     * 回复留言
     * 
     * @param id 留言ID
     * @param replyContent 回复内容
     * @return 结果
     */
    @Override
    public int replyBlogMessage(Long id, String replyContent)
    {
        if (StringUtils.isEmpty(replyContent))
        {
            throw new ServiceException("回复内容不能为空");
        }
        if (replyContent.length() > 500)
        {
            throw new ServiceException("回复内容长度不能超过500个字符");
        }
        BlogMessage message = new BlogMessage();
        message.setId(id);
        message.setReplyContent(replyContent);
        message.setReplyTime(new Date());
        message.setReplyBy(SecurityUtils.getUsername());
        // 回复即视为审核通过
        message.setStatus(STATUS_PUBLISHED);
        message.setUpdateBy(SecurityUtils.getUsername());
        message.setUpdateTime(new Date());
        return blogMessageMapper.updateBlogMessage(message);
    }

    /**
     * 通过主键删除数据
     * 
     * @param id 留言ID
     * @return 影响行数
     */
    @Override
    public int deleteBlogMessageById(Long id)
    {
        return blogMessageMapper.deleteBlogMessageById(id);
    }

    /**
     * 批量删除留言
     * 
     * @param ids 需要删除的数据ID
     * @return 影响行数
     */
    @Override
    public int deleteBlogMessageByIds(Long[] ids)
    {
        return blogMessageMapper.deleteBlogMessageByIds(ids);
    }
}
