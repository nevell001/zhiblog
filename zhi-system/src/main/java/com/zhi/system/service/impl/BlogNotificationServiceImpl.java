package com.zhi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhi.system.domain.BlogNotification;
import com.zhi.system.mapper.BlogNotificationMapper;
import com.zhi.system.service.IBlogNotificationService;

/**
 * 站内信通知Service业务层处理
 *
 * @author nevell
 * @date 2026-07-26
 */
@Service
public class BlogNotificationServiceImpl implements IBlogNotificationService
{
    @Autowired
    private BlogNotificationMapper blogNotificationMapper;

    @Override
    public List<BlogNotification> selectBlogNotificationList(BlogNotification blogNotification)
    {
        return blogNotificationMapper.selectBlogNotificationList(blogNotification);
    }

    @Override
    public int createNotification(BlogNotification blogNotification)
    {
        return blogNotificationMapper.insertBlogNotification(blogNotification);
    }

    @Override
    public int markAsRead(Long[] ids, Long recipientId)
    {
        return blogNotificationMapper.markAsRead(ids, recipientId);
    }

    @Override
    public int markAllAsRead(Long recipientId)
    {
        return blogNotificationMapper.markAllAsRead(recipientId);
    }

    @Override
    public int selectUnreadCount(Long recipientId)
    {
        return blogNotificationMapper.selectUnreadCount(recipientId);
    }
}
