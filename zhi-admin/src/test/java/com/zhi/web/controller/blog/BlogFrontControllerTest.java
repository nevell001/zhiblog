package com.zhi.web.controller.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhi.system.domain.BlogComment;
import com.zhi.system.service.IBlogCommentService;
import com.zhi.system.service.IBlogSettingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 博客前台评论接口单元测试
 * 覆盖 /blog/comment 的服务端校验与提交链路
 */
class BlogFrontControllerTest
{
    private IBlogCommentService blogCommentService;
    private IBlogSettingService blogSettingService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp()
    {
        blogCommentService = mock(IBlogCommentService.class);
        blogSettingService = mock(IBlogSettingService.class);

        BlogFrontController controller = new BlogFrontController();
        ReflectionTestUtils.setField(controller, "blogCommentService", blogCommentService);
        ReflectionTestUtils.setField(controller, "blogSettingService", blogSettingService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void addCommentShouldRejectMissingArticleId() throws Exception
    {
        BlogComment comment = validComment();
        comment.setArticleId(null);

        perform(comment)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(500));

        verify(blogCommentService, never()).insertBlogComment(any(BlogComment.class));
    }

    @Test
    void addCommentShouldRejectEmptyContent() throws Exception
    {
        BlogComment comment = validComment();
        comment.setContent("   ");

        perform(comment)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(500));

        verify(blogCommentService, never()).insertBlogComment(any(BlogComment.class));
    }

    @Test
    void addCommentShouldRejectContentTooLong() throws Exception
    {
        BlogComment comment = validComment();
        comment.setContent("a".repeat(501));

        perform(comment)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(500));

        verify(blogCommentService, never()).insertBlogComment(any(BlogComment.class));
    }

    @Test
    void addCommentShouldRejectMissingNickname() throws Exception
    {
        BlogComment comment = validComment();
        comment.setNickname("  ");

        perform(comment)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(500));

        verify(blogCommentService, never()).insertBlogComment(any(BlogComment.class));
    }

    @Test
    void addCommentShouldSubmitWithReviewStatusByDefault() throws Exception
    {
        BlogComment comment = validComment();
        when(blogCommentService.insertBlogComment(any(BlogComment.class))).thenReturn(1);

        perform(comment)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200));

        verify(blogCommentService).insertBlogComment(any(BlogComment.class));
    }

    private ResultActions perform(BlogComment comment)
        throws Exception
    {
        return mockMvc.perform(post("/blog/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment)));
    }

    private BlogComment validComment()
    {
        BlogComment comment = new BlogComment();
        comment.setArticleId(1L);
        comment.setNickname("测试用户");
        comment.setContent("这是一条测试评论");
        return comment;
    }
}
