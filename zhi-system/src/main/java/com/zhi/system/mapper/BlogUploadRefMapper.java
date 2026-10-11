package com.zhi.system.mapper;

import java.util.List;
import com.zhi.system.domain.BlogUploadReference;

/**
 * 上传文件引用检查Mapper（跨表只读统计）
 *
 * @author nevell
 */
public interface BlogUploadRefMapper
{
    /**
     * 统计该文件访问路径在各业务表中的引用数量
     *
     * @param url 上传文件的访问路径（blog_upload.url，相对路径）
     * @return 各来源的引用统计（无引用的来源计数为 0）
     */
    public List<BlogUploadReference> countFileReferences(String url);
}
