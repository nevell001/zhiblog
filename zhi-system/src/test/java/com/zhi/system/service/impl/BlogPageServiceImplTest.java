package com.zhi.system.service.impl;

import com.zhi.common.exception.ServiceException;
import com.zhi.common.utils.SecurityUtils;
import com.zhi.system.domain.BlogPage;
import com.zhi.system.mapper.BlogPageMapper;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 自定义页面服务层单元测试
 *
 * @author test
 * @date 2026-09-10
 */
@ExtendWith(MockitoExtension.class)
class BlogPageServiceImplTest {

    @Mock
    private BlogPageMapper blogPageMapper;

    @InjectMocks
    private BlogPageServiceImpl blogPageService;

    private BlogPage testPage;

    @BeforeEach
    void setUp() {
        testPage = new BlogPage();
        testPage.setId(1L);
        testPage.setTitle("关于本站");
        testPage.setSlug("about-site");
        testPage.setContent("# 关于");
        testPage.setStatus("1");
    }

    @Test
    void testSelectBlogPageList() {
        when(blogPageMapper.selectBlogPageList(any(BlogPage.class))).thenReturn(Arrays.asList(testPage));

        List<BlogPage> result = blogPageService.selectBlogPageList(new BlogPage());

        assertEquals(1, result.size());
        assertEquals("about-site", result.get(0).getSlug());
    }

    @Test
    void testSelectPublishedPageList() {
        when(blogPageMapper.selectPublishedPageList()).thenReturn(Collections.singletonList(testPage));

        assertEquals(1, blogPageService.selectPublishedPageList().size());
    }

    @Test
    void testSelectBlogPageById() {
        when(blogPageMapper.selectBlogPageById(1L)).thenReturn(testPage);

        assertEquals("关于本站", blogPageService.selectBlogPageById(1L).getTitle());
    }

    @Test
    void testSelectBlogPageBySlug() {
        when(blogPageMapper.selectBlogPageBySlug("about-site")).thenReturn(testPage);

        BlogPage result = blogPageService.selectBlogPageBySlug(" about-site ");

        assertNotNull(result);
        // 入参会做 trim
        verify(blogPageMapper).selectBlogPageBySlug("about-site");
    }

    @Test
    void testSelectBlogPageBySlugWithEmptySlugReturnsNull() {
        assertNull(blogPageService.selectBlogPageBySlug("  "));
        verify(blogPageMapper, never()).selectBlogPageBySlug(anyString());
    }

    @Test
    void testInsertBlogPageAppliesDefaults() {
        BlogPage page = new BlogPage();
        page.setTitle("新页面");
        page.setSlug("new-page");

        when(blogPageMapper.checkSlugUnique("new-page")).thenReturn(null);
        when(blogPageMapper.insertBlogPage(any(BlogPage.class))).thenReturn(1);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("admin");

            int result = blogPageService.insertBlogPage(page);

            assertEquals(1, result);
            assertEquals("0", page.getStatus());
            assertEquals("1", page.getShowInNav());
            assertEquals(0, page.getSort());
            assertEquals(0L, page.getViewCount());
            assertEquals("0", page.getDelFlag());
            assertEquals("admin", page.getCreateBy());
            assertNotNull(page.getCreateTime());
        }
    }

    @Test
    void testInsertBlogPageRejectsEmptyTitle() {
        BlogPage page = new BlogPage();
        page.setSlug("slug-only");

        ServiceException exception = assertThrows(ServiceException.class, () -> blogPageService.insertBlogPage(page));

        assertEquals("页面标题不能为空", exception.getMessage());
    }

    @Test
    void testInsertBlogPageRejectsTooLongTitle() {
        BlogPage page = new BlogPage();
        page.setTitle("a".repeat(101));
        page.setSlug("slug-only");

        ServiceException exception = assertThrows(ServiceException.class, () -> blogPageService.insertBlogPage(page));

        assertEquals("页面标题长度不能超过100个字符", exception.getMessage());
    }

    @Test
    void testInsertBlogPageRejectsEmptySlug() {
        BlogPage page = new BlogPage();
        page.setTitle("标题");

        ServiceException exception = assertThrows(ServiceException.class, () -> blogPageService.insertBlogPage(page));

        assertEquals("页面别名不能为空", exception.getMessage());
    }

    @Test
    void testInsertBlogPageRejectsIllegalSlug() {
        BlogPage page = new BlogPage();
        page.setTitle("标题");
        page.setSlug("非法 slug!");

        ServiceException exception = assertThrows(ServiceException.class, () -> blogPageService.insertBlogPage(page));

        assertTrue(exception.getMessage().contains("页面别名只能包含字母"));
    }

    @Test
    void testInsertBlogPageRejectsDuplicateSlug() {
        BlogPage exist = new BlogPage();
        exist.setId(9L);
        exist.setSlug("about-site");

        BlogPage page = new BlogPage();
        page.setTitle("标题");
        page.setSlug("about-site");

        when(blogPageMapper.checkSlugUnique("about-site")).thenReturn(exist);

        ServiceException exception = assertThrows(ServiceException.class, () -> blogPageService.insertBlogPage(page));

        assertEquals("页面别名已存在：about-site", exception.getMessage());
        verify(blogPageMapper, never()).insertBlogPage(any(BlogPage.class));
    }

    @Test
    void testUpdateBlogPageRequiresId() {
        BlogPage page = new BlogPage();
        page.setTitle("标题");

        ServiceException exception = assertThrows(ServiceException.class, () -> blogPageService.updateBlogPage(page));

        assertEquals("页面ID不能为空", exception.getMessage());
    }

    @Test
    void testUpdateBlogPageWithoutSlugSkipsUniqueCheck() {
        BlogPage page = new BlogPage();
        page.setId(1L);
        page.setTitle("只改标题");

        when(blogPageMapper.updateBlogPage(any(BlogPage.class))).thenReturn(1);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("admin");

            assertEquals(1, blogPageService.updateBlogPage(page));
            verify(blogPageMapper, never()).checkSlugUnique(anyString());
            assertNotNull(page.getUpdateTime());
        }
    }

    @Test
    void testUpdateBlogPageAllowsSameSlugOnItself() {
        BlogPage page = new BlogPage();
        page.setId(1L);
        page.setSlug("about-site");

        BlogPage exist = new BlogPage();
        exist.setId(1L);

        when(blogPageMapper.checkSlugUnique("about-site")).thenReturn(exist);
        when(blogPageMapper.updateBlogPage(any(BlogPage.class))).thenReturn(1);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("admin");

            assertEquals(1, blogPageService.updateBlogPage(page));
        }
    }

    @Test
    void testUpdateBlogPageRejectsSlugTakenByOther() {
        BlogPage page = new BlogPage();
        page.setId(1L);
        page.setSlug("taken");

        BlogPage exist = new BlogPage();
        exist.setId(2L);

        when(blogPageMapper.checkSlugUnique("taken")).thenReturn(exist);

        ServiceException exception = assertThrows(ServiceException.class, () -> blogPageService.updateBlogPage(page));

        assertEquals("页面别名已存在：taken", exception.getMessage());
    }

    @Test
    void testChangePageStatus() {
        when(blogPageMapper.updateBlogPage(any(BlogPage.class))).thenReturn(1);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUsername).thenReturn("admin");

            assertEquals(1, blogPageService.changePageStatus(1L, "0"));
            verify(blogPageMapper).updateBlogPage(argThat(p -> "0".equals(p.getStatus())));
        }
    }

    @Test
    void testChangePageStatusRejectsIllegalStatus() {
        ServiceException exception = assertThrows(ServiceException.class,
            () -> blogPageService.changePageStatus(1L, "5"));

        assertEquals("页面状态不合法", exception.getMessage());
    }

    @Test
    void testIncreaseViewCount() {
        when(blogPageMapper.increaseViewCount(1L)).thenReturn(1);

        assertEquals(1, blogPageService.increaseViewCount(1L));
    }

    @Test
    void testIncreaseViewCountWithNullId() {
        assertEquals(0, blogPageService.increaseViewCount(null));
        verify(blogPageMapper, never()).increaseViewCount(anyLong());
    }

    @Test
    void testDeleteBlogPageById() {
        when(blogPageMapper.deleteBlogPageById(1L)).thenReturn(1);

        assertEquals(1, blogPageService.deleteBlogPageById(1L));
    }

    @Test
    void testDeleteBlogPageByIds() {
        Long[] ids = {1L, 2L};
        when(blogPageMapper.deleteBlogPageByIds(ids)).thenReturn(2);

        assertEquals(2, blogPageService.deleteBlogPageByIds(ids));
    }
}
