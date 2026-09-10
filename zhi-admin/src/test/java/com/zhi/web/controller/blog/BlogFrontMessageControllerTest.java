package com.zhi.web.controller.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhi.system.domain.BlogMessage;
import com.zhi.system.service.IBlogMessageService;
import com.zhi.system.service.IBlogSettingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 留言板前台接口单元测试
 * 覆盖 /blog/message 的服务端校验、审核开关与公开列表
 */
class BlogFrontMessageControllerTest
{
    private IBlogMessageService blogMessageService;
    private IBlogSettingService blogSettingService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp()
    {
        blogMessageService = mock(IBlogMessageService.class);
        blogSettingService = mock(IBlogSettingService.class);

        BlogFrontMessageController controller = new BlogFrontMessageController();
        ReflectionTestUtils.setField(controller, "blogMessageService", blogMessageService);
        ReflectionTestUtils.setField(controller, "blogSettingService", blogSettingService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void listShouldExposePublishedMessages() throws Exception
    {
        BlogMessage message = new BlogMessage();
        message.setId(1L);
        message.setNickname("访客");
        message.setContent("你好");
        when(blogMessageService.selectPublishedMessageList()).thenReturn(Collections.singletonList(message));

        mockMvc.perform(get("/blog/message/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.rows[0].nickname").value("访客"))
            // 公开接口不应返回邮箱等隐私字段
            .andExpect(jsonPath("$.rows[0].email").doesNotExist());
    }

    @Test
    void countShouldReturnPublishedCount() throws Exception
    {
        when(blogMessageService.selectPublishedMessageCount()).thenReturn(7L);

        mockMvc.perform(get("/blog/message/count"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").value(7));
    }

    @Test
    void addShouldRejectMissingNickname() throws Exception
    {
        BlogMessage message = validMessage();
        message.setNickname("   ");

        perform(message)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(500));

        verify(blogMessageService, never()).insertBlogMessage(any(BlogMessage.class));
    }

    @Test
    void addShouldRejectTooLongNickname() throws Exception
    {
        BlogMessage message = validMessage();
        message.setNickname("a".repeat(51));

        perform(message).andExpect(jsonPath("$.code").value(500));

        verify(blogMessageService, never()).insertBlogMessage(any(BlogMessage.class));
    }

    @Test
    void addShouldRejectEmptyContent() throws Exception
    {
        BlogMessage message = validMessage();
        message.setContent("  ");

        perform(message).andExpect(jsonPath("$.code").value(500));

        verify(blogMessageService, never()).insertBlogMessage(any(BlogMessage.class));
    }

    @Test
    void addShouldRejectTooLongContent() throws Exception
    {
        BlogMessage message = validMessage();
        message.setContent("a".repeat(501));

        perform(message).andExpect(jsonPath("$.code").value(500));

        verify(blogMessageService, never()).insertBlogMessage(any(BlogMessage.class));
    }

    @Test
    void addShouldRejectTooLongEmail() throws Exception
    {
        BlogMessage message = validMessage();
        message.setEmail("a".repeat(101));

        perform(message).andExpect(jsonPath("$.code").value(500));

        verify(blogMessageService, never()).insertBlogMessage(any(BlogMessage.class));
    }

    @Test
    void addShouldRejectTooLongWebsite() throws Exception
    {
        BlogMessage message = validMessage();
        message.setWebsite("https://" + "a".repeat(250) + ".com");

        perform(message).andExpect(jsonPath("$.code").value(500));

        verify(blogMessageService, never()).insertBlogMessage(any(BlogMessage.class));
    }

    @Test
    void addShouldSetPendingStatusWhenReviewEnabled() throws Exception
    {
        when(blogSettingService.selectSettingValueByKey("comment_review")).thenReturn("true");
        when(blogMessageService.insertBlogMessage(any(BlogMessage.class))).thenReturn(1);

        perform(validMessage())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200));

        verify(blogMessageService).insertBlogMessage(argThatMessage("0"));
    }

    @Test
    void addShouldPublishDirectlyWhenReviewDisabled() throws Exception
    {
        when(blogSettingService.selectSettingValueByKey("comment_review")).thenReturn("false");
        when(blogMessageService.insertBlogMessage(any(BlogMessage.class))).thenReturn(1);

        perform(validMessage())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200));

        verify(blogMessageService).insertBlogMessage(argThatMessage("1"));
    }

    @Test
    void addShouldDefaultToReviewWhenSettingMissing() throws Exception
    {
        when(blogSettingService.selectSettingValueByKey("comment_review")).thenReturn(null);
        when(blogMessageService.insertBlogMessage(any(BlogMessage.class))).thenReturn(1);

        perform(validMessage()).andExpect(jsonPath("$.code").value(200));

        verify(blogMessageService).insertBlogMessage(argThatMessage("0"));
    }

    @Test
    void addShouldCaptureClientInfoAndResetServerFields() throws Exception
    {
        when(blogSettingService.selectSettingValueByKey(anyString())).thenReturn("false");
        when(blogMessageService.insertBlogMessage(any(BlogMessage.class))).thenReturn(1);

        BlogMessage message = validMessage();
        message.setId(99L);
        message.setDelFlag("1");
        message.setReplyContent("伪造回复");
        message.setReplyBy("伪造人");

        mockMvc.perform(post("/blog/message")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", "JUnit-Agent")
                .content(objectMapper.writeValueAsString(message)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200));

        verify(blogMessageService).insertBlogMessage(argThat(m ->
            m.getId() == null
                && "0".equals(m.getDelFlag())
                && m.getReplyContent() == null
                && m.getReplyBy() == null
                && "JUnit-Agent".equals(m.getUserAgent())
                && m.getIp() != null));
    }

    private ResultActions perform(BlogMessage message) throws Exception
    {
        return mockMvc.perform(post("/blog/message")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(message)));
    }

    private BlogMessage argThatMessage(String expectedStatus)
    {
        return argThat(m -> expectedStatus.equals(m.getStatus()) && "0".equals(m.getDelFlag()));
    }

    private BlogMessage validMessage()
    {
        BlogMessage message = new BlogMessage();
        message.setNickname("测试访客");
        message.setContent("这是一条测试留言");
        return message;
    }
}
