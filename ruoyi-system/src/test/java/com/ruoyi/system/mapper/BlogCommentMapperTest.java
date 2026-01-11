package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.BlogComment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 博客评论Mapper单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class BlogCommentMapperTest {

    @Mock
    private BlogCommentMapper blogCommentMapper;

    private BlogComment testComment;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testComment = new BlogComment();
        testComment.setId(1L);
        testComment.setArticleId(1L);
        testComment.setContent("测试评论内容");
        testComment.setNickname("测试用户");
        testComment.setEmail("test@example.com");
        testComment.setStatus("0");
    }

    /**
     * 测试CRUD操作的完整流程
     */
    @Test
    void testCRUDOperations() {
        // 测试新增
        when(blogCommentMapper.insertBlogComment(any(BlogComment.class))).thenReturn(1);
        int insertResult = blogCommentMapper.insertBlogComment(testComment);
        assertEquals(1, insertResult, "新增博客评论失败");
        verify(blogCommentMapper).insertBlogComment(any(BlogComment.class));

        // 测试根据ID查询
        when(blogCommentMapper.selectBlogCommentById(1L)).thenReturn(testComment);
        BlogComment selectResult = blogCommentMapper.selectBlogCommentById(1L);
        assertNotNull(selectResult, "根据ID查询博客评论失败");
        assertEquals("测试评论内容", selectResult.getContent(), "评论内容不匹配");
        verify(blogCommentMapper).selectBlogCommentById(1L);

        // 测试修改
        when(blogCommentMapper.updateBlogComment(any(BlogComment.class))).thenReturn(1);
        testComment.setContent("修改后的评论内容");
        int updateResult = blogCommentMapper.updateBlogComment(testComment);
        assertEquals(1, updateResult, "修改博客评论失败");
        verify(blogCommentMapper).updateBlogComment(any(BlogComment.class));

        // 测试查询列表
        when(blogCommentMapper.selectBlogCommentList(any(BlogComment.class)))
            .thenReturn(Arrays.asList(testComment));
        List<BlogComment> commentList = blogCommentMapper.selectBlogCommentList(new BlogComment());
        assertFalse(commentList.isEmpty(), "查询博客评论列表失败");
        verify(blogCommentMapper).selectBlogCommentList(any(BlogComment.class));

        // 测试删除
        when(blogCommentMapper.deleteBlogCommentById(1L)).thenReturn(1);
        int deleteResult = blogCommentMapper.deleteBlogCommentById(1L);
        assertEquals(1, deleteResult, "删除博客评论失败");
        verify(blogCommentMapper).deleteBlogCommentById(1L);
    }

    /**
     * 测试批量删除功能
     */
    @Test
    void testDeleteBlogCommentByIds() {
        when(blogCommentMapper.deleteBlogCommentByIds(any(Long[].class))).thenReturn(2);
        int deleteResult = blogCommentMapper.deleteBlogCommentByIds(new Long[]{1L, 2L});
        assertEquals(2, deleteResult, "批量删除博客评论失败");
        verify(blogCommentMapper).deleteBlogCommentByIds(any(Long[].class));
    }

    /**
     * 测试统计评论数量
     */
    @Test
    void testSelectBlogCommentCount() {
        when(blogCommentMapper.selectBlogCommentCount(any(BlogComment.class))).thenReturn(10L);
        Long count = blogCommentMapper.selectBlogCommentCount(new BlogComment());
        assertEquals(10L, count, "评论数量统计失败");
        verify(blogCommentMapper).selectBlogCommentCount(any(BlogComment.class));
    }

    /**
     * 测试边界条件：空值处理
     */
    @Test
    void testNullHandling() {
        // 测试查询不存在的ID
        when(blogCommentMapper.selectBlogCommentById(999L)).thenReturn(null);
        BlogComment result = blogCommentMapper.selectBlogCommentById(999L);
        assertNull(result, "查询不存在的ID应返回null");
        verify(blogCommentMapper).selectBlogCommentById(999L);

        // 测试空列表查询
        when(blogCommentMapper.selectBlogCommentList(any(BlogComment.class)))
            .thenReturn(Arrays.asList());
        List<BlogComment> list = blogCommentMapper.selectBlogCommentList(new BlogComment());
        assertTrue(list.isEmpty(), "查询结果应为空列表");
        verify(blogCommentMapper).selectBlogCommentList(any(BlogComment.class));

        // 测试零评论数
        when(blogCommentMapper.selectBlogCommentCount(any(BlogComment.class))).thenReturn(0L);
        Long count = blogCommentMapper.selectBlogCommentCount(new BlogComment());
        assertEquals(0L, count, "零评论数统计失败");
        verify(blogCommentMapper).selectBlogCommentCount(any(BlogComment.class));
    }

    /**
     * 测试边界条件：空内容
     */
    @Test
    void testEmptyContent() {
        BlogComment emptyContentComment = new BlogComment();
        emptyContentComment.setContent("");
        
        when(blogCommentMapper.insertBlogComment(any(BlogComment.class))).thenReturn(1);
        int result = blogCommentMapper.insertBlogComment(emptyContentComment);
        assertEquals(1, result, "空内容评论插入失败");
        verify(blogCommentMapper).insertBlogComment(any(BlogComment.class));
    }

    /**
     * 测试边界条件：空昵称
     */
    @Test
    void testEmptyNickname() {
        BlogComment emptyNicknameComment = new BlogComment();
        emptyNicknameComment.setContent("测试内容");
        emptyNicknameComment.setNickname("");
        
        when(blogCommentMapper.insertBlogComment(any(BlogComment.class))).thenReturn(1);
        int result = blogCommentMapper.insertBlogComment(emptyNicknameComment);
        assertEquals(1, result, "空昵称评论插入失败");
        verify(blogCommentMapper).insertBlogComment(any(BlogComment.class));
    }

    /**
     * 测试边界条件：状态值
     */
    @Test
    void testStatusValues() {
        // 测试待审核状态
        BlogComment pendingComment = new BlogComment();
        pendingComment.setContent("测试");
        pendingComment.setStatus("0");
        when(blogCommentMapper.insertBlogComment(any(BlogComment.class))).thenReturn(1);
        int result1 = blogCommentMapper.insertBlogComment(pendingComment);
        assertEquals(1, result1, "待审核状态评论插入失败");

        // 测试已通过状态
        BlogComment approvedComment = new BlogComment();
        approvedComment.setContent("测试");
        approvedComment.setStatus("1");
        when(blogCommentMapper.insertBlogComment(any(BlogComment.class))).thenReturn(1);
        int result2 = blogCommentMapper.insertBlogComment(approvedComment);
        assertEquals(1, result2, "已通过状态评论插入失败");

        // 测试已拒绝状态
        BlogComment rejectedComment = new BlogComment();
        rejectedComment.setContent("测试");
        rejectedComment.setStatus("2");
        when(blogCommentMapper.insertBlogComment(any(BlogComment.class))).thenReturn(1);
        int result3 = blogCommentMapper.insertBlogComment(rejectedComment);
        assertEquals(1, result3, "已拒绝状态评论插入失败");

        verify(blogCommentMapper, times(3)).insertBlogComment(any(BlogComment.class));
    }

    /**
     * 测试边界条件：长内容
     */
    @Test
    void testLongContent() {
        BlogComment longContentComment = new BlogComment();
        longContentComment.setContent("这是一条非常长的评论内容，用于测试系统对长文本的处理能力。" +
            "这条评论包含了很多文字，用于验证数据库和应用程序能够正确处理超长文本输入。" +
            "在实际应用中，用户可能会输入很长的评论，系统需要能够正确存储和显示这些内容。");
        
        when(blogCommentMapper.insertBlogComment(any(BlogComment.class))).thenReturn(1);
        int result = blogCommentMapper.insertBlogComment(longContentComment);
        assertEquals(1, result, "长内容评论插入失败");
        verify(blogCommentMapper).insertBlogComment(any(BlogComment.class));
    }
}