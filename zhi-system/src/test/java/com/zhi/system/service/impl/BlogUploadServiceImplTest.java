package com.zhi.system.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.zhi.system.domain.BlogUpload;
import com.zhi.system.mapper.BlogUploadMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * BlogUploadServiceImpl 单元测试
 *
 * @author nevell
 */
@ExtendWith(MockitoExtension.class)
class BlogUploadServiceImplTest
{
    @Mock
    private BlogUploadMapper blogUploadMapper;

    @InjectMocks
    private BlogUploadServiceImpl blogUploadService;

    @Test
    void testRecordUpload()
    {
        BlogUpload upload = new BlogUpload();
        upload.setFileName("a.png");
        upload.setUrl("/profile/upload/a.png");
        upload.setFileSize(1024L);
        when(blogUploadMapper.insertBlogUpload(upload)).thenReturn(1);

        assertEquals(1, blogUploadService.recordUpload(upload));
        verify(blogUploadMapper).insertBlogUpload(upload);
    }

    @Test
    void testRecordUploadNullFile()
    {
        assertEquals(0, blogUploadService.recordUpload(null));
        BlogUpload empty = new BlogUpload();
        assertEquals(0, blogUploadService.recordUpload(empty));
        verify(blogUploadMapper, never()).insertBlogUpload(any());
    }

    @Test
    void testDeleteBlogUploadRecordNotFound()
    {
        when(blogUploadMapper.selectBlogUploadById(1L)).thenReturn(null);
        assertEquals(0, blogUploadService.deleteBlogUploadById(1L));
        verify(blogUploadMapper, never()).deleteBlogUploadById(1L);
    }

    @Test
    void testDeleteBlogUploadRemovesRecord()
    {
        BlogUpload upload = new BlogUpload();
        upload.setId(1L);
        upload.setUrl("/profile/upload/x.png");
        when(blogUploadMapper.selectBlogUploadById(1L)).thenReturn(upload);
        when(blogUploadMapper.deleteBlogUploadById(1L)).thenReturn(1);

        assertEquals(1, blogUploadService.deleteBlogUploadById(1L));
        verify(blogUploadMapper).deleteBlogUploadById(1L);
    }
}
