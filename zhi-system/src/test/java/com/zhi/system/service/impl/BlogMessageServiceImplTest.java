package com.zhi.system.service.impl;

import com.zhi.common.exception.ServiceException;
import com.zhi.common.utils.SecurityUtils;
import com.zhi.system.domain.BlogMessage;
import com.zhi.system.mapper.BlogMessageMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 留言板服务层单元测试
 *
 * @author test
 * @date 2026-09-10
 */
@ExtendWith(MockitoExtension.class)
class BlogMessageServiceImplTest {

    @Mock
    private BlogMessageMapper blogMessageMapper;

    @InjectMocks
    private BlogMessageServiceImpl blogMessageService;

    private BlogMessage testMessage;

    @BeforeEach
    void setUp() {
        testMessage = new BlogMessage();
        testMessage.setId(1L);
        testMessage.setNickname("测试访客");
        testMessage.setContent("这是一条测试留言");
        testMessage.setStatus("1");
    }

    @Test
    void testSelectBlogMessageList() {
        when(blogMessageMapper.selectBlogMessageList(any(BlogMessage.class)))
            .thenReturn(Arrays.asList(testMessage));

        List<BlogMessage> result = blogMessageService.selectBlogMessageList(new BlogMessage());

        assertEquals(1, result.size());
        assertEquals("测试访客", result.get(0).getNickname());
    }

    @Test
    void testSelectPublishedMessageList() {
        when(blogMessageMapper.selectPublishedMessageList()).thenReturn(Collections.singletonList(testMessage));

        List<BlogMessage> result = blogMessageService.selectPublishedMessageList();

        assertEquals(1, result.size());
        verify(blogMessageMapper).selectPublishedMessageList();
    }

    @Test
    void testSelectPublishedMessageCount() {
        when(blogMessageMapper.selectPublishedMessageCount()).thenReturn(5L);

        assertEquals(5L, blogMessageService.selectPublishedMessageCount());
    }

    @Test
    void testSelectBlogMessageById() {
        when(blogMessageMapper.selectBlogMessageById(1L)).thenReturn(testMessage);

        BlogMessage result = blogMessageService.selectBlogMessageById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testInsertBlogMessageAppliesDefaults() {
        BlogMessage message = new BlogMessage();
        message.setNickname("访客");
        message.setContent("内容");

        when(blogMessageMapper.insertBlogMessage(any(BlogMessage.class))).thenReturn(1);

        int result = blogMessageService.insertBlogMessage(message);

        assertEquals(1, result);
        // 未显式指定状态时默认待审核，并补齐 delFlag 与创建时间
        assertEquals("0", message.getStatus());
        assertEquals("0", message.getDelFlag());
        assertNotNull(message.getCreateTime());
    }

    @Test
    void testInsertBlogMessageKeepsGivenStatus() {
        BlogMessage message = new BlogMessage();
        message.setNickname("访客");
        message.setContent("内容");
        message.setStatus("1");

        when(blogMessageMapper.insertBlogMessage(any(BlogMessage.class))).thenReturn(1);

        blogMessageService.insertBlogMessage(message);

        assertEquals("1", message.getStatus());
    }

    @Test
    void testUpdateBlogMessageSetsUpdateTime() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("testUser");
            when(blogMessageMapper.updateBlogMessage(any(BlogMessage.class))).thenReturn(1);

            int result = blogMessageService.updateBlogMessage(testMessage);

            assertEquals(1, result);
            assertNotNull(testMessage.getUpdateTime());
        }
    }

    @Test
    void testAuditBlogMessageAcceptsPublished() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("admin");
            when(blogMessageMapper.updateBlogMessage(any(BlogMessage.class))).thenReturn(1);

            int result = blogMessageService.auditBlogMessage(1L, "1");

            assertEquals(1, result);
            verify(blogMessageMapper).updateBlogMessage(argThat(m -> "1".equals(m.getStatus()) && "admin".equals(m.getUpdateBy())));
        }
    }

    @Test
    void testAuditBlogMessageAcceptsRejected() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("admin");
            when(blogMessageMapper.updateBlogMessage(any(BlogMessage.class))).thenReturn(1);

            assertEquals(1, blogMessageService.auditBlogMessage(1L, "2"));
        }
    }

    @Test
    void testAuditBlogMessageRejectsIllegalStatus() {
        ServiceException exception = assertThrows(ServiceException.class,
            () -> blogMessageService.auditBlogMessage(1L, "9"));

        assertEquals("留言状态不合法", exception.getMessage());
        verify(blogMessageMapper, never()).updateBlogMessage(any(BlogMessage.class));
    }

    @Test
    void testReplyBlogMessage() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("admin");
            when(blogMessageMapper.updateBlogMessage(any(BlogMessage.class))).thenReturn(1);

            int result = blogMessageService.replyBlogMessage(1L, "感谢留言");

            assertEquals(1, result);
            verify(blogMessageMapper).updateBlogMessage(argThat(m ->
                "感谢留言".equals(m.getReplyContent())
                    && "admin".equals(m.getReplyBy())
                    && "1".equals(m.getStatus())
                    && m.getReplyTime() != null));
        }
    }

    @Test
    void testReplyBlogMessageRejectsEmptyContent() {
        ServiceException exception = assertThrows(ServiceException.class,
            () -> blogMessageService.replyBlogMessage(1L, "  "));

        assertEquals("回复内容不能为空", exception.getMessage());
        verify(blogMessageMapper, never()).updateBlogMessage(any(BlogMessage.class));
    }

    @Test
    void testReplyBlogMessageRejectsTooLongContent() {
        String longContent = "a".repeat(501);

        ServiceException exception = assertThrows(ServiceException.class,
            () -> blogMessageService.replyBlogMessage(1L, longContent));

        assertEquals("回复内容长度不能超过500个字符", exception.getMessage());
        verify(blogMessageMapper, never()).updateBlogMessage(any(BlogMessage.class));
    }

    @Test
    void testDeleteBlogMessageById() {
        when(blogMessageMapper.deleteBlogMessageById(1L)).thenReturn(1);

        assertEquals(1, blogMessageService.deleteBlogMessageById(1L));
    }

    @Test
    void testDeleteBlogMessageByIds() {
        Long[] ids = {1L, 2L};
        when(blogMessageMapper.deleteBlogMessageByIds(ids)).thenReturn(2);

        assertEquals(2, blogMessageService.deleteBlogMessageByIds(ids));
    }
}
