package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.BlogComment;
import com.ruoyi.system.mapper.BlogCommentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 博客评论服务层单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class BlogCommentServiceImplTest {

    @Mock
    private BlogCommentMapper blogCommentMapper;

    @InjectMocks
    private BlogCommentServiceImpl blogCommentService;

    private BlogComment testComment;

    @BeforeEach
    void setUp() {
        testComment = new BlogComment();
        testComment.setId(1L);
        testComment.setContent("测试评论");
        testComment.setArticleId(1L);
        testComment.setUserId(1L);
        testComment.setStatus("0"); // 待审核
    }

    /**
     * 测试查询评论列表
     */
    @Test
    void testSelectBlogCommentList() {
        // 准备数据
        List<BlogComment> commentList = Arrays.asList(testComment);
        when(blogCommentMapper.selectBlogCommentList(any(BlogComment.class)))
            .thenReturn(commentList);

        // 执行测试
        List<BlogComment> result = blogCommentService.selectBlogCommentList(new BlogComment());

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试评论", result.get(0).getContent());
        verify(blogCommentMapper).selectBlogCommentList(any(BlogComment.class));
    }

    /**
     * 测试查询评论列表 - 空结果
     */
    @Test
    void testSelectBlogCommentList_Empty() {
        // 模拟空结果
        when(blogCommentMapper.selectBlogCommentList(any(BlogComment.class)))
            .thenReturn(Collections.emptyList());

        // 执行测试
        List<BlogComment> result = blogCommentService.selectBlogCommentList(new BlogComment());

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(blogCommentMapper).selectBlogCommentList(any(BlogComment.class));
    }

    /**
     * 测试根据ID查询评论
     */
    @Test
    void testSelectBlogCommentById() {
        // 准备数据
        when(blogCommentMapper.selectBlogCommentById(1L)).thenReturn(testComment);

        // 执行测试
        BlogComment result = blogCommentService.selectBlogCommentById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("测试评论", result.getContent());
        verify(blogCommentMapper).selectBlogCommentById(1L);
    }

    /**
     * 测试根据ID查询评论 - 不存在
     */
    @Test
    void testSelectBlogCommentById_NotFound() {
        // 模拟评论不存在
        when(blogCommentMapper.selectBlogCommentById(999L)).thenReturn(null);

        // 执行测试
        BlogComment result = blogCommentService.selectBlogCommentById(999L);

        // 验证结果
        assertNull(result);
        verify(blogCommentMapper).selectBlogCommentById(999L);
    }

    /**
     * 测试新增评论 - 成功
     */
    @Test
    void testInsertBlogComment_Success() {
        // 准备数据
        when(blogCommentMapper.insertBlogComment(any(BlogComment.class))).thenReturn(1);

        // 执行测试
        int result = blogCommentService.insertBlogComment(testComment);

        // 验证结果
        assertEquals(1, result);
        verify(blogCommentMapper).insertBlogComment(any(BlogComment.class));
    }

    /**
     * 测试更新评论 - 成功
     */
    @Test
    void testUpdateBlogComment_Success() {
        // 准备数据
        when(blogCommentMapper.updateBlogComment(any(BlogComment.class))).thenReturn(1);

        // 执行测试
        int result = blogCommentService.updateBlogComment(testComment);

        // 验证结果
        assertEquals(1, result);
        verify(blogCommentMapper).updateBlogComment(any(BlogComment.class));
    }

    /**
     * 测试删除评论 - 成功
     */
    @Test
    void testDeleteBlogCommentById_Success() {
        // 准备数据
        when(blogCommentMapper.deleteBlogCommentById(1L)).thenReturn(1);

        // 执行测试
        int result = blogCommentService.deleteBlogCommentById(1L);

        // 验证结果
        assertEquals(1, result);
        verify(blogCommentMapper).deleteBlogCommentById(1L);
    }

    /**
     * 测试删除评论 - 不存在
     */
    @Test
    void testDeleteBlogCommentById_NotFound() {
        // 准备数据
        when(blogCommentMapper.deleteBlogCommentById(999L)).thenReturn(0);

        // 执行测试
        int result = blogCommentService.deleteBlogCommentById(999L);

        // 验证结果
        assertEquals(0, result);
        verify(blogCommentMapper).deleteBlogCommentById(999L);
    }

    /**
     * 测试批量删除评论 - 成功
     */
    @Test
    void testDeleteBlogCommentByIds_Success() {
        // 准备数据
        when(blogCommentMapper.deleteBlogCommentByIds(any(Long[].class))).thenReturn(2);

        // 执行测试
        int result = blogCommentService.deleteBlogCommentByIds(new Long[]{1L, 2L});

        // 验证结果
        assertEquals(2, result);
        verify(blogCommentMapper).deleteBlogCommentByIds(any(Long[].class));
    }

    /**
     * 测试批量删除评论 - 空列表
     */
    @Test
    void testDeleteBlogCommentByIds_EmptyList() {
        // 执行测试
        int result = blogCommentService.deleteBlogCommentByIds(new Long[]{});

        // 验证结果
        assertEquals(0, result);
        verify(blogCommentMapper).deleteBlogCommentByIds(any(Long[].class));
    }

    /**
     * 测试查询评论数量
     */
    @Test
    void testSelectBlogCommentCount() {
        // 准备数据
        when(blogCommentMapper.selectBlogCommentCount(any(BlogComment.class))).thenReturn(10L);

        // 执行测试
        Long result = blogCommentService.selectBlogCommentCount(new BlogComment());

        // 验证结果
        assertNotNull(result);
        assertEquals(10L, result);
        verify(blogCommentMapper).selectBlogCommentCount(any(BlogComment.class));
    }

    /**
     * 测试查询评论数量 - 空结果
     */
    @Test
    void testSelectBlogCommentCount_Empty() {
        // 模拟空结果
        when(blogCommentMapper.selectBlogCommentCount(any(BlogComment.class))).thenReturn(0L);

        // 执行测试
        Long result = blogCommentService.selectBlogCommentCount(new BlogComment());

        // 验证结果
        assertNotNull(result);
        assertEquals(0L, result);
        verify(blogCommentMapper).selectBlogCommentCount(any(BlogComment.class));
    }

    /**
     * 测试评论内容长度限制
     */
    @Test
    void testInsertBlogComment_LongContent() {
        // 设置超长内容
        StringBuilder longContent = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longContent.append("测试内容");
        }
        testComment.setContent(longContent.toString());

        // 准备数据
        when(blogCommentMapper.insertBlogComment(any(BlogComment.class))).thenReturn(1);

        // 执行测试
        int result = blogCommentService.insertBlogComment(testComment);

        // 验证结果
        assertEquals(1, result);
        verify(blogCommentMapper).insertBlogComment(any(BlogComment.class));
    }

    /**
     * 测试评论状态更新
     */
    @Test
    void testUpdateBlogComment_Status() {
        // 更新状态为已审核
        testComment.setStatus("1");
        when(blogCommentMapper.updateBlogComment(any(BlogComment.class))).thenReturn(1);

        // 执行测试
        int result = blogCommentService.updateBlogComment(testComment);

        // 验证结果
        assertEquals(1, result);
        verify(blogCommentMapper).updateBlogComment(any(BlogComment.class));
    }

    /**
     * 测试评论内容包含特殊字符
     */
    @Test
    void testInsertBlogComment_SpecialCharacters() {
        // 设置包含特殊字符的内容
        testComment.setContent("测试评论@#$%^&*()_+-={}[]|\\:\";'<>?,./~`");

        // 准备数据
        when(blogCommentMapper.insertBlogComment(any(BlogComment.class))).thenReturn(1);

        // 执行测试
        int result = blogCommentService.insertBlogComment(testComment);

        // 验证结果
        assertEquals(1, result);
        verify(blogCommentMapper).insertBlogComment(any(BlogComment.class));
    }

    /**
     * 测试审核通过博客评论 - 成功
     */
    @Test
    void testAuditBlogCommentByIds_Success() {
        // 准备数据
        when(blogCommentMapper.updateBlogComment(any(BlogComment.class))).thenReturn(1);

        // 执行测试
        int result = blogCommentService.auditBlogCommentByIds(new Long[]{1L, 2L, 3L});

        // 验证结果
        assertEquals(3, result);
        verify(blogCommentMapper, times(3)).updateBlogComment(any(BlogComment.class));
    }

    /**
     * 测试审核通过博客评论 - 单个评论
     */
    @Test
    void testAuditBlogCommentByIds_Single() {
        // 准备数据
        when(blogCommentMapper.updateBlogComment(any(BlogComment.class))).thenReturn(1);

        // 执行测试
        int result = blogCommentService.auditBlogCommentByIds(new Long[]{1L});

        // 验证结果
        assertEquals(1, result);
        verify(blogCommentMapper, times(1)).updateBlogComment(any(BlogComment.class));
    }

    /**
     * 测试审核通过博客评论 - 空列表
     */
    @Test
    void testAuditBlogCommentByIds_Empty() {
        // 执行测试
        int result = blogCommentService.auditBlogCommentByIds(new Long[]{});

        // 验证结果
        assertEquals(0, result);
        verify(blogCommentMapper, never()).updateBlogComment(any(BlogComment.class));
    }

    /**
     * 测试审核通过博客评论 - 验证状态
     */
    @Test
    void testAuditBlogCommentByIds_VerifyStatus() {
        // 准备数据
        when(blogCommentMapper.updateBlogComment(any(BlogComment.class))).thenAnswer(invocation -> {
            BlogComment comment = invocation.getArgument(0);
            assertEquals("1", comment.getStatus()); // 验证状态为已审核通过
            return 1;
        });

        // 执行测试
        int result = blogCommentService.auditBlogCommentByIds(new Long[]{1L});

        // 验证结果
        assertEquals(1, result);
        verify(blogCommentMapper).updateBlogComment(any(BlogComment.class));
    }

    /**
     * 测试审核拒绝博客评论 - 成功
     */
    @Test
    void testRejectBlogCommentByIds_Success() {
        // 准备数据
        when(blogCommentMapper.updateBlogComment(any(BlogComment.class))).thenReturn(1);

        // 执行测试
        int result = blogCommentService.rejectBlogCommentByIds(new Long[]{1L, 2L, 3L});

        // 验证结果
        assertEquals(3, result);
        verify(blogCommentMapper, times(3)).updateBlogComment(any(BlogComment.class));
    }

    /**
     * 测试审核拒绝博客评论 - 单个评论
     */
    @Test
    void testRejectBlogCommentByIds_Single() {
        // 准备数据
        when(blogCommentMapper.updateBlogComment(any(BlogComment.class))).thenReturn(1);

        // 执行测试
        int result = blogCommentService.rejectBlogCommentByIds(new Long[]{1L});

        // 验证结果
        assertEquals(1, result);
        verify(blogCommentMapper, times(1)).updateBlogComment(any(BlogComment.class));
    }

    /**
     * 测试审核拒绝博客评论 - 空列表
     */
    @Test
    void testRejectBlogCommentByIds_Empty() {
        // 执行测试
        int result = blogCommentService.rejectBlogCommentByIds(new Long[]{});

        // 验证结果
        assertEquals(0, result);
        verify(blogCommentMapper, never()).updateBlogComment(any(BlogComment.class));
    }

    /**
     * 测试审核拒绝博客评论 - 验证状态
     */
    @Test
    void testRejectBlogCommentByIds_VerifyStatus() {
        // 准备数据
        when(blogCommentMapper.updateBlogComment(any(BlogComment.class))).thenAnswer(invocation -> {
            BlogComment comment = invocation.getArgument(0);
            assertEquals("2", comment.getStatus()); // 验证状态为已删除（拒绝）
            return 1;
        });

        // 执行测试
        int result = blogCommentService.rejectBlogCommentByIds(new Long[]{1L});

        // 验证结果
        assertEquals(1, result);
        verify(blogCommentMapper).updateBlogComment(any(BlogComment.class));
    }

    /**
     * 测试审核通过和拒绝博客评论 - 批量操作
     */
    @Test
    void testAuditAndRejectBlogComment_Batch() {
        // 准备数据
        when(blogCommentMapper.updateBlogComment(any(BlogComment.class))).thenReturn(1);

        // 执行测试 - 审核通过
        int auditCount = blogCommentService.auditBlogCommentByIds(new Long[]{1L, 2L});
        assertEquals(2, auditCount);

        // 执行测试 - 审核拒绝
        int rejectCount = blogCommentService.rejectBlogCommentByIds(new Long[]{3L, 4L, 5L});
        assertEquals(3, rejectCount);

        // 验证结果
        verify(blogCommentMapper, times(5)).updateBlogComment(any(BlogComment.class));
    }

    /**
     * 测试审核通过博客评论 - 包含失败的更新
     */
    @Test
    void testAuditBlogCommentByIds_WithFailures() {
        // 准备数据 - 部分更新失败
        when(blogCommentMapper.updateBlogComment(any(BlogComment.class)))
            .thenReturn(1)
            .thenReturn(0)
            .thenReturn(1);

        // 执行测试
        int result = blogCommentService.auditBlogCommentByIds(new Long[]{1L, 2L, 3L});

        // 验证结果 - 应该返回实际更新的数量
        assertEquals(2, result);
        verify(blogCommentMapper, times(3)).updateBlogComment(any(BlogComment.class));
    }

    /**
     * 测试审核拒绝博客评论 - 包含失败的更新
     */
    @Test
    void testRejectBlogCommentByIds_WithFailures() {
        // 准备数据 - 部分更新失败
        when(blogCommentMapper.updateBlogComment(any(BlogComment.class)))
            .thenReturn(1)
            .thenReturn(0)
            .thenReturn(0);

        // 执行测试
        int result = blogCommentService.rejectBlogCommentByIds(new Long[]{1L, 2L, 3L});

        // 验证结果 - 应该返回实际更新的数量
        assertEquals(1, result);
        verify(blogCommentMapper, times(3)).updateBlogComment(any(BlogComment.class));
    }
}
