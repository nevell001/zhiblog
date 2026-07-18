package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.BlogBookmark;
import com.ruoyi.system.mapper.BlogBookmarkMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 文章收藏服务层单元测试
 */
@ExtendWith(MockitoExtension.class)
class BlogBookmarkServiceImplTest {

    @Mock
    private BlogBookmarkMapper blogBookmarkMapper;

    @InjectMocks
    private BlogBookmarkServiceImpl blogBookmarkService;

    @Test
    void testSelectBlogBookmarkById() {
        BlogBookmark bookmark = new BlogBookmark();
        bookmark.setId(1L);
        when(blogBookmarkMapper.selectBlogBookmarkById(1L)).thenReturn(bookmark);

        assertSame(bookmark, blogBookmarkService.selectBlogBookmarkById(1L));
        verify(blogBookmarkMapper).selectBlogBookmarkById(1L);
    }

    @Test
    void testSelectBlogBookmarkList() {
        BlogBookmark query = new BlogBookmark();
        List<BlogBookmark> bookmarks = Collections.singletonList(new BlogBookmark());
        when(blogBookmarkMapper.selectBlogBookmarkList(query)).thenReturn(bookmarks);

        assertSame(bookmarks, blogBookmarkService.selectBlogBookmarkList(query));
        verify(blogBookmarkMapper).selectBlogBookmarkList(query);
    }

    @Test
    void testInsertAndDeleteDelegateToMapper() {
        BlogBookmark bookmark = new BlogBookmark();
        when(blogBookmarkMapper.insertBlogBookmark(bookmark)).thenReturn(1);
        when(blogBookmarkMapper.deleteBlogBookmarkById(2L)).thenReturn(1);
        when(blogBookmarkMapper.deleteBlogBookmarkByUserIdAndArticleId(3L, 4L)).thenReturn(1);

        assertEquals(1, blogBookmarkService.insertBlogBookmark(bookmark));
        assertEquals(1, blogBookmarkService.deleteBlogBookmarkById(2L));
        assertEquals(1, blogBookmarkService.deleteByUserIdAndArticleId(3L, 4L));
    }

    @Test
    void testToggleBookmarkCancelsExistingBookmark() {
        when(blogBookmarkMapper.existsBookmark(10L, 20L)).thenReturn(1);
        when(blogBookmarkMapper.deleteBlogBookmarkByUserIdAndArticleId(10L, 20L)).thenReturn(1);

        assertEquals(0, blogBookmarkService.toggleBookmark(10L, 20L));
        verify(blogBookmarkMapper).deleteBlogBookmarkByUserIdAndArticleId(10L, 20L);
        verify(blogBookmarkMapper, never()).insertBlogBookmark(any(BlogBookmark.class));
    }

    @Test
    void testToggleBookmarkCreatesMissingBookmark() {
        when(blogBookmarkMapper.existsBookmark(10L, 20L)).thenReturn(0);
        when(blogBookmarkMapper.insertBlogBookmark(any(BlogBookmark.class))).thenReturn(1);

        assertEquals(1, blogBookmarkService.toggleBookmark(10L, 20L));
        verify(blogBookmarkMapper).insertBlogBookmark(any(BlogBookmark.class));
        verify(blogBookmarkMapper, never()).deleteBlogBookmarkByUserIdAndArticleId(10L, 20L);
    }

    @Test
    void testIsBookmarked() {
        when(blogBookmarkMapper.existsBookmark(10L, 20L)).thenReturn(1);
        when(blogBookmarkMapper.existsBookmark(10L, 21L)).thenReturn(0);

        assertTrue(blogBookmarkService.isBookmarked(10L, 20L));
        assertFalse(blogBookmarkService.isBookmarked(10L, 21L));
    }

    @Test
    void testSelectBookmarksByUserId() {
        List<BlogBookmark> bookmarks = Collections.singletonList(new BlogBookmark());
        when(blogBookmarkMapper.selectBookmarksByUserId(10L)).thenReturn(bookmarks);

        assertSame(bookmarks, blogBookmarkService.selectBookmarksByUserId(10L));
        verify(blogBookmarkMapper).selectBookmarksByUserId(10L);
    }
}
