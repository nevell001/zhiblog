package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.mapper.BlogArticleMapper;
import com.ruoyi.system.service.IBlogArticleTagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 博客文章服务层单元测试
 * 
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class BlogArticleServiceImplTest {

    @Mock
    private BlogArticleMapper blogArticleMapper;

    @Mock
    private IBlogArticleTagService blogArticleTagService;

    @InjectMocks
    private BlogArticleServiceImpl blogArticleService;

    private BlogArticle testArticle;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testArticle = new BlogArticle();
        testArticle.setId(1L);
        testArticle.setTitle("测试文章标题");
        testArticle.setContent("测试文章内容");
        testArticle.setStatus(0L);
    }

    /**
     * 测试更新文章状态 - 标题非空校验
     */
    @Test
    void testUpdateArticleStatus_TitleNotNull() {
        // 模拟数据库操作
        when(blogArticleMapper.selectBlogArticleById(1L)).thenReturn(testArticle);
        when(blogArticleMapper.updateBlogArticle(any(BlogArticle.class))).thenReturn(1);

        // 执行测试方法
        Integer result = blogArticleService.updateArticleStatus(Collections.singletonList(1L), 1);

        // 验证结果
        assertNotNull(result);
        assertTrue(result > 0);
        verify(blogArticleMapper).updateBlogArticle(any(BlogArticle.class));
    }

    /**
     * 测试更新文章状态 - 标题为空时抛出异常
     */
    @Test
    void testUpdateArticleStatus_TitleNull() {
        // 设置标题为空
        testArticle.setTitle(null);
        when(blogArticleMapper.selectBlogArticleById(1L)).thenReturn(testArticle);

        // 验证抛出异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> blogArticleService.updateArticleStatus(Collections.singletonList(1L), 1));
        
        assertEquals("文章标题不能为空", exception.getMessage());
        verify(blogArticleMapper, never()).updateBlogArticle(any(BlogArticle.class));
    }

    /**
     * 测试新增文章 - 标题唯一性校验
     */
    @Test
    void testInsertArticle_TitleDuplicate() {
        // 模拟数据库中已存在相同标题的文章
        when(blogArticleMapper.selectBlogArticleByTitle("测试文章标题")).thenReturn(testArticle);

        // 验证抛出异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> blogArticleService.insertBlogArticle(testArticle));
        
        assertEquals("文章标题已存在", exception.getMessage());
        verify(blogArticleMapper, never()).insertBlogArticle(any(BlogArticle.class));
    }

    /**
     * 测试新增文章 - 内容非空校验
     */
    @Test
    void testInsertArticle_ContentNull() {
        // 设置内容为空
        testArticle.setContent(null);

        // 验证抛出异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> blogArticleService.insertBlogArticle(testArticle));
        
        assertEquals("文章内容不能为空", exception.getMessage());
    }

    /**
     * 测试更新文章 - 标题非空校验
     */
    @Test
    void testUpdateArticle_TitleNull() {
        // 设置标题为空
        testArticle.setTitle(null);

        // 验证抛出异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> blogArticleService.updateBlogArticle(testArticle));
        
        assertEquals("文章标题不能为空", exception.getMessage());
    }

    /**
     * 测试批量更新文章状态 - 成功场景
     */
    @Test
    void testUpdateArticleStatusBatch_Success() {
        // 模拟数据库操作
        when(blogArticleMapper.selectBlogArticleById(1L)).thenReturn(testArticle);
        when(blogArticleMapper.updateBlogArticle(any(BlogArticle.class))).thenReturn(1);

        // 执行测试方法
        Integer result = blogArticleService.updateArticleStatus(Arrays.asList(1L), 1);

        // 验证结果
        assertNotNull(result);
        assertTrue(result > 0);
        verify(blogArticleMapper).updateBlogArticle(any(BlogArticle.class));
    }
}