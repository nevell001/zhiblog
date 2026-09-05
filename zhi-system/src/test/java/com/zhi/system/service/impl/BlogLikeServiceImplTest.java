package com.zhi.system.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.zhi.system.mapper.BlogLikeMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * BlogLikeServiceImpl 单元测试
 *
 * @author nevell
 */
@ExtendWith(MockitoExtension.class)
class BlogLikeServiceImplTest
{
    @Mock
    private BlogLikeMapper blogLikeMapper;

    @InjectMocks
    private BlogLikeServiceImpl blogLikeService;

    private static final Long USER_ID = 1L;

    private static final Long ARTICLE_ID = 100L;

    private static final Long COMMENT_ID = 200L;

    @Test
    void testToggleArticleLikeAddsWhenNotLiked()
    {
        when(blogLikeMapper.selectArticleLikeCount(ARTICLE_ID)).thenReturn(5L, 6L);
        when(blogLikeMapper.existsArticleLike(ARTICLE_ID, USER_ID)).thenReturn(0);
        when(blogLikeMapper.insertArticleLike(ARTICLE_ID, USER_ID)).thenReturn(1);

        Map<String, Object> result = blogLikeService.toggleArticleLike(USER_ID, ARTICLE_ID);

        assertEquals(Boolean.TRUE, result.get("liked"));
        assertEquals(6L, result.get("likeCount"));
        verify(blogLikeMapper).insertArticleLike(ARTICLE_ID, USER_ID);
        verify(blogLikeMapper).changeArticleLikeCount(ARTICLE_ID, 1);
        verify(blogLikeMapper, never()).deleteArticleLike(anyLong(), anyLong());
    }

    @Test
    void testToggleArticleLikeRemovesWhenAlreadyLiked()
    {
        when(blogLikeMapper.selectArticleLikeCount(ARTICLE_ID)).thenReturn(5L, 4L);
        when(blogLikeMapper.existsArticleLike(ARTICLE_ID, USER_ID)).thenReturn(1);
        when(blogLikeMapper.deleteArticleLike(ARTICLE_ID, USER_ID)).thenReturn(1);

        Map<String, Object> result = blogLikeService.toggleArticleLike(USER_ID, ARTICLE_ID);

        assertEquals(Boolean.FALSE, result.get("liked"));
        assertEquals(4L, result.get("likeCount"));
        verify(blogLikeMapper).deleteArticleLike(ARTICLE_ID, USER_ID);
        verify(blogLikeMapper).changeArticleLikeCount(ARTICLE_ID, -1);
        verify(blogLikeMapper, never()).insertArticleLike(anyLong(), anyLong());
    }

    @Test
    void testToggleArticleLikeSkipsWhenArticleMissing()
    {
        when(blogLikeMapper.selectArticleLikeCount(999L)).thenReturn(null);

        Map<String, Object> result = blogLikeService.toggleArticleLike(USER_ID, 999L);

        assertEquals(Boolean.FALSE, result.get("liked"));
        assertEquals(0L, result.get("likeCount"));
        verify(blogLikeMapper, never()).insertArticleLike(anyLong(), anyLong());
        verify(blogLikeMapper, never()).deleteArticleLike(anyLong(), anyLong());
    }

    @Test
    void testToggleArticleLikeHandlesNullArgs()
    {
        Map<String, Object> result = blogLikeService.toggleArticleLike(null, ARTICLE_ID);
        assertEquals(Boolean.FALSE, result.get("liked"));
        assertEquals(0L, result.get("likeCount"));

        Map<String, Object> result2 = blogLikeService.toggleArticleLike(USER_ID, null);
        assertEquals(Boolean.FALSE, result2.get("liked"));
        assertEquals(0L, result2.get("likeCount"));
    }

    @Test
    void testGetArticleLikeStatus()
    {
        when(blogLikeMapper.selectArticleLikeCount(ARTICLE_ID)).thenReturn(5L);
        when(blogLikeMapper.existsArticleLike(ARTICLE_ID, USER_ID)).thenReturn(1);

        Map<String, Object> liked = blogLikeService.getArticleLikeStatus(USER_ID, ARTICLE_ID);
        assertTrue((Boolean) liked.get("liked"));
        assertEquals(5L, liked.get("likeCount"));

        when(blogLikeMapper.existsArticleLike(ARTICLE_ID, USER_ID)).thenReturn(0);

        Map<String, Object> unliked = blogLikeService.getArticleLikeStatus(USER_ID, ARTICLE_ID);
        assertFalse((Boolean) unliked.get("liked"));
        assertEquals(5L, unliked.get("likeCount"));
    }

    @Test
    void testToggleCommentLikeAddsWhenNotLiked()
    {
        when(blogLikeMapper.selectCommentLikeCount(COMMENT_ID)).thenReturn(3L, 4L);
        when(blogLikeMapper.existsCommentLike(COMMENT_ID, USER_ID)).thenReturn(0);
        when(blogLikeMapper.insertCommentLike(COMMENT_ID, USER_ID)).thenReturn(1);

        Map<String, Object> result = blogLikeService.toggleCommentLike(USER_ID, COMMENT_ID);

        assertEquals(Boolean.TRUE, result.get("liked"));
        assertEquals(4L, result.get("likeCount"));
        verify(blogLikeMapper).insertCommentLike(COMMENT_ID, USER_ID);
        verify(blogLikeMapper).changeCommentLikeCount(COMMENT_ID, 1);
    }

    @Test
    void testToggleCommentLikeRemovesWhenAlreadyLiked()
    {
        when(blogLikeMapper.selectCommentLikeCount(COMMENT_ID)).thenReturn(3L, 2L);
        when(blogLikeMapper.existsCommentLike(COMMENT_ID, USER_ID)).thenReturn(1);
        when(blogLikeMapper.deleteCommentLike(COMMENT_ID, USER_ID)).thenReturn(1);

        Map<String, Object> result = blogLikeService.toggleCommentLike(USER_ID, COMMENT_ID);

        assertEquals(Boolean.FALSE, result.get("liked"));
        assertEquals(2L, result.get("likeCount"));
        verify(blogLikeMapper).deleteCommentLike(COMMENT_ID, USER_ID);
        verify(blogLikeMapper).changeCommentLikeCount(COMMENT_ID, -1);
    }

    @Test
    void testToggleCommentLikeSkipsWhenCommentMissing()
    {
        when(blogLikeMapper.selectCommentLikeCount(999L)).thenReturn(null);

        Map<String, Object> result = blogLikeService.toggleCommentLike(USER_ID, 999L);

        assertEquals(Boolean.FALSE, result.get("liked"));
        assertEquals(0L, result.get("likeCount"));
        verify(blogLikeMapper, never()).insertCommentLike(anyLong(), anyLong());
    }

    @Test
    void testGetLikedCommentIds()
    {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(blogLikeMapper.selectLikedCommentIds(USER_ID, ids)).thenReturn(Arrays.asList(2L));

        List<Long> liked = blogLikeService.getLikedCommentIds(USER_ID, ids);
        assertEquals(Collections.singletonList(2L), liked);

        // 空参数直接返回空列表，不查询
        List<Long> empty = blogLikeService.getLikedCommentIds(USER_ID, Collections.emptyList());
        assertTrue(empty.isEmpty());
        List<Long> nullUser = blogLikeService.getLikedCommentIds(null, ids);
        assertTrue(nullUser.isEmpty());
        verify(blogLikeMapper, times(1)).selectLikedCommentIds(any(), any());
    }
}
