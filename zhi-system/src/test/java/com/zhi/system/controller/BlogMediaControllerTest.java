package com.zhi.system.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.zhi.common.exception.ServiceException;
import com.zhi.system.service.IBlogUploadService;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 媒体库控制器单元测试
 *
 * @author nevell
 */
@WebMvcTest(controllers = BlogMediaController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class BlogMediaControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBlogUploadService blogUploadService;

    /**
     * 未被引用的文件正常删除
     */
    @Test
    void testRemoveSuccess() throws Exception
    {
        when(blogUploadService.deleteBlogUploadById(1L)).thenReturn(1);

        mockMvc.perform(delete("/system/media/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogUploadService).deleteBlogUploadById(1L);
    }

    /**
     * 文件仍被引用时不删除，并以警告码返回原因（前端拦截器按 601 弹 warning 提示）
     */
    @Test
    void testRemoveBlockedWhenReferenced() throws Exception
    {
        when(blogUploadService.deleteBlogUploadById(1L))
            .thenThrow(new ServiceException("文件「封面.jpg」仍被引用（2 篇文章），请先解除引用再删除"));

        mockMvc.perform(delete("/system/media/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(601))
                .andExpect(jsonPath("$.msg").value("文件「封面.jpg」仍被引用（2 篇文章），请先解除引用再删除"));
    }

    /**
     * 批量删除：可删的照常删除，被引用的跳过并在提示里带上已删除数量与原因
     */
    @Test
    void testRemovePartialBatchKeepsDeletedAndReportsBlocked() throws Exception
    {
        when(blogUploadService.deleteBlogUploadById(1L)).thenReturn(1);
        when(blogUploadService.deleteBlogUploadById(2L))
            .thenThrow(new ServiceException("文件「封面.jpg」仍被引用（2 篇文章），请先解除引用再删除"));

        mockMvc.perform(delete("/system/media/1,2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(601))
                .andExpect(jsonPath("$.msg").value(
                    "已删除 1 个文件；文件「封面.jpg」仍被引用（2 篇文章），请先解除引用再删除"));

        verify(blogUploadService).deleteBlogUploadById(1L);
        verify(blogUploadService).deleteBlogUploadById(2L);
    }

    /**
     * 删除失败（数据库异常等）由全局异常处理返回错误码
     */
    @Test
    void testRemoveFailureReturnsErrorCode() throws Exception
    {
        when(blogUploadService.deleteBlogUploadById(anyLong())).thenReturn(0);

        mockMvc.perform(delete("/system/media/9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));

        verify(blogUploadService).deleteBlogUploadById(9L);
    }
}
