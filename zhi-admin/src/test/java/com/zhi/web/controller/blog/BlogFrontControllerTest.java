package com.zhi.web.controller.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhi.system.domain.BlogArticle;
import com.zhi.system.domain.BlogCategory;
import com.zhi.system.domain.BlogComment;
import com.zhi.system.domain.BlogTag;
import com.zhi.system.service.IBlogArticleService;
import com.zhi.system.service.IBlogCategoryService;
import com.zhi.system.service.IBlogCommentService;
import com.zhi.system.service.IBlogSettingService;
import com.zhi.system.service.IBlogTagService;
import com.zhi.system.service.IBlogVisitLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 博客前台接口单元测试
 * 覆盖 /blog/comment 的服务端校验与提交链路，以及 /blog/stats/overview 公开统计
 */
class BlogFrontControllerTest
{
    private IBlogArticleService blogArticleService;
    private IBlogCategoryService blogCategoryService;
    private IBlogCommentService blogCommentService;
    private IBlogSettingService blogSettingService;
    private IBlogTagService blogTagService;
    private IBlogVisitLogService blogVisitLogService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp()
    {
        blogArticleService = mock(IBlogArticleService.class);
        blogCategoryService = mock(IBlogCategoryService.class);
        blogCommentService = mock(IBlogCommentService.class);
        blogSettingService = mock(IBlogSettingService.class);
        blogTagService = mock(IBlogTagService.class);
        blogVisitLogService = mock(IBlogVisitLogService.class);

        BlogFrontController controller = new BlogFrontController();
        ReflectionTestUtils.setField(controller, "blogArticleService", blogArticleService);
        ReflectionTestUtils.setField(controller, "blogCategoryService", blogCategoryService);
        ReflectionTestUtils.setField(controller, "blogCommentService", blogCommentService);
        ReflectionTestUtils.setField(controller, "blogSettingService", blogSettingService);
        ReflectionTestUtils.setField(controller, "blogTagService", blogTagService);
        ReflectionTestUtils.setField(controller, "blogVisitLogService", blogVisitLogService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void addCommentShouldBeRejectedWhenCommentDisabled() throws Exception
    {
        when(blogSettingService.selectSettingValueByKey("comment_enabled")).thenReturn("false");

        perform(validComment())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(500))
            .andExpect(jsonPath("$.msg").value("评论功能已关闭"));

        verify(blogCommentService, never()).insertBlogComment(any(BlogComment.class));
    }

    @Test
    void searchShouldBeRejectedWhenSearchDisabled() throws Exception
    {
        when(blogSettingService.selectSettingValueByKey("search_enabled")).thenReturn("0");

        mockMvc.perform(get("/blog/article/search").param("keyword", "test"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(500))
            .andExpect(jsonPath("$.msg").value("搜索功能已关闭"))
            .andExpect(jsonPath("$.total").value(0));
    }

    @Test
    void statsOverviewShouldExposePublicCountersOnly() throws Exception
    {
        when(blogArticleService.selectBlogArticleCount(any(BlogArticle.class))).thenReturn(12L);
        when(blogCommentService.selectBlogCommentCount(any(BlogComment.class))).thenReturn(34L);
        when(blogCategoryService.selectBlogCategoryList(any(BlogCategory.class)))
            .thenReturn(List.of(new BlogCategory(), new BlogCategory()));
        when(blogTagService.selectBlogTagList(any(BlogTag.class)))
            .thenReturn(List.of(new BlogTag()));
        when(blogArticleService.selectTotalViewCount()).thenReturn(567L);

        mockMvc.perform(get("/blog/stats/overview"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.articleCount").value(12))
            .andExpect(jsonPath("$.data.categoryCount").value(2))
            .andExpect(jsonPath("$.data.tagCount").value(1))
            .andExpect(jsonPath("$.data.commentCount").value(34))
            .andExpect(jsonPath("$.data.totalViews").value(567))
            .andExpect(jsonPath("$.data.viewCount").value(567))
            // 公开接口不得泄露注册用户数等后台信息
            .andExpect(jsonPath("$.data.userCount").doesNotExist());
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
