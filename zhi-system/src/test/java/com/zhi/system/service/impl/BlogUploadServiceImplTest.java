package com.zhi.system.service.impl;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.zhi.common.exception.ServiceException;
import com.zhi.system.domain.BlogUpload;
import com.zhi.system.domain.BlogUploadReference;
import com.zhi.system.mapper.BlogUploadMapper;
import com.zhi.system.mapper.BlogUploadRefMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Mock
    private BlogUploadRefMapper blogUploadRefMapper;

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

    @Test
    void testDeleteBlogUploadBlockedWhenStillReferenced()
    {
        BlogUpload upload = new BlogUpload();
        upload.setId(1L);
        upload.setFileName("cover_20261001.jpg");
        upload.setOriginalName("封面.jpg");
        upload.setUrl("/profile/upload/2026/10/cover_20261001.jpg");
        when(blogUploadMapper.selectBlogUploadById(1L)).thenReturn(upload);
        when(blogUploadRefMapper.countFileReferences("/profile/upload/2026/10/cover_20261001.jpg"))
            .thenReturn(List.of(reference("article", 2L), reference("page", 0L), reference("setting", 1L)));

        ServiceException ex = assertThrows(ServiceException.class,
            () -> blogUploadService.deleteBlogUploadById(1L));

        assertTrue(ex.getMessage().contains("封面.jpg"), "提示应带上文件名，便于识别是哪个文件");
        assertTrue(ex.getMessage().contains("2 篇文章"), "应说明文章引用数量");
        assertTrue(ex.getMessage().contains("1 项站点设置"), "应说明设置引用数量");
        assertTrue(ex.getMessage().contains("请先解除引用再删除"), "应给出处理建议");
        verify(blogUploadMapper, never()).deleteBlogUploadById(1L);
    }

    @Test
    void testDeleteBlogUploadAllowsZeroCounts()
    {
        BlogUpload upload = new BlogUpload();
        upload.setId(2L);
        upload.setUrl("/profile/upload/2026/10/free.jpg");
        when(blogUploadMapper.selectBlogUploadById(2L)).thenReturn(upload);
        when(blogUploadRefMapper.countFileReferences("/profile/upload/2026/10/free.jpg"))
            .thenReturn(List.of(reference("article", 0L), reference("page", 0L)));
        when(blogUploadMapper.deleteBlogUploadById(2L)).thenReturn(1);

        assertEquals(1, blogUploadService.deleteBlogUploadById(2L), "全零计数的引用统计不应阻止删除");
        verify(blogUploadMapper).deleteBlogUploadById(2L);
    }

    @Test
    void testDeleteBlogUploadWithoutUrlSkipsReferenceCheck()
    {
        BlogUpload upload = new BlogUpload();
        upload.setId(3L);
        when(blogUploadMapper.selectBlogUploadById(3L)).thenReturn(upload);
        when(blogUploadMapper.deleteBlogUploadById(3L)).thenReturn(1);

        assertEquals(1, blogUploadService.deleteBlogUploadById(3L));
        verify(blogUploadRefMapper, never()).countFileReferences(any(String.class));
    }

    private static BlogUploadReference reference(String type, long count)
    {
        BlogUploadReference ref = new BlogUploadReference();
        ref.setRefType(type);
        ref.setRefCount(count);
        return ref;
    }
}
