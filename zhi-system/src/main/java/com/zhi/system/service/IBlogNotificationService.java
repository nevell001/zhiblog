package com.zhi.system.service;

import java.util.List;
import com.zhi.system.domain.BlogNotification;

/**
 * 站内信通知Service接口
 *
 * @author nevell
 * @date 2026-07-26
 */
public interface IBlogNotificationService
{
    /**
     * 查询站内信通知列表
     *
     * @param blogNotification 站内信通知
     * @return 站内信通知集合
     */
    List<BlogNotification> selectBlogNotificationList(BlogNotification blogNotification);

    /**
     * 创建通知
     *
     * @param blogNotification 通知对象
     * @return 结果
     */
    int createNotification(BlogNotification blogNotification);

    /**
     * 标记为已读（批量）
     *
     * @param ids 通知ID数组
     * @param recipientId 接收用户ID
     * @return 结果
     */
    int markAsRead(Long[] ids, Long recipientId);

    /**
     * 标记所有为已读
     *
     * @param recipientId 接收用户ID
     * @return 结果
     */
    int markAllAsRead(Long recipientId);

    /**
     * 获取未读数量
     *
     * @param recipientId 接收用户ID
     * @return 未读数量
     */
    int selectUnreadCount(Long recipientId);
}
