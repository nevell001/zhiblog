package com.zhi.system.service;

import java.util.List;
import com.zhi.system.domain.BlogUpload;

/**
 * 博客上传记录Service接口
 *
 * @author nevell
 * @date 2026-09-05
 */
public interface IBlogUploadService
{
    /**
     * 记录一次上传
     *
     * @param file 上传记录字段（fileName/url/mimeType/fileSize/originalName/uploadType）
     * @return 结果
     */
    public int recordUpload(BlogUpload file);

    /**
     * 查询上传记录列表
     *
     * @param blogUpload 查询条件
     * @return 上传记录集合
     */
    public List<BlogUpload> selectBlogUploadList(BlogUpload blogUpload);

    /**
     * 根据ID删除上传记录（并尝试删除物理文件）
     *
     * @param id 记录ID
     * @return 结果
     */
    public int deleteBlogUploadById(Long id);
}
