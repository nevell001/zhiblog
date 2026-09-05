package com.zhi.system.mapper;

import java.util.List;
import com.zhi.system.domain.BlogUpload;

/**
 * 博客上传记录Mapper接口
 *
 * @author nevell
 * @date 2026-09-05
 */
public interface BlogUploadMapper
{
    public List<BlogUpload> selectBlogUploadList(BlogUpload blogUpload);

    public BlogUpload selectBlogUploadById(Long id);

    public int insertBlogUpload(BlogUpload blogUpload);

    public int deleteBlogUploadById(Long id);
}
