package com.zhi.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhi.common.config.RuoYiConfig;
import com.zhi.common.exception.ServiceException;
import com.zhi.common.utils.SecurityUtils;
import com.zhi.common.utils.StringUtils;
import com.zhi.common.utils.file.FileUtils;
import com.zhi.system.domain.BlogUpload;
import com.zhi.system.domain.BlogUploadReference;
import com.zhi.system.mapper.BlogUploadMapper;
import com.zhi.system.mapper.BlogUploadRefMapper;
import com.zhi.system.service.IBlogUploadService;

/**
 * 博客上传记录Service实现
 *
 * @author nevell
 * @date 2026-09-05
 */
@Service
public class BlogUploadServiceImpl implements IBlogUploadService
{
    private static final Logger log = LoggerFactory.getLogger(BlogUploadServiceImpl.class);

    @Autowired
    private BlogUploadMapper blogUploadMapper;

    @Autowired
    private BlogUploadRefMapper blogUploadRefMapper;

    @Override
    public int recordUpload(BlogUpload file)
    {
        if (file == null || StringUtils.isEmpty(file.getFileName()))
        {
            return 0;
        }
        try
        {
            file.setCreateBy(SecurityUtils.getUsername());
        }
        catch (Exception e)
        {
            // 匿名场景下取不到用户名，忽略
            file.setCreateBy("anonymous");
        }
        if (file.getFileSize() == null)
        {
            file.setFileSize(0L);
        }
        if (StringUtils.isEmpty(file.getUploadType()))
        {
            file.setUploadType("upload");
        }
        return blogUploadMapper.insertBlogUpload(file);
    }

    @Override
    public List<BlogUpload> selectBlogUploadList(BlogUpload blogUpload)
    {
        return blogUploadMapper.selectBlogUploadList(blogUpload);
    }

    @Override
    public int deleteBlogUploadById(Long id)
    {
        BlogUpload record = blogUploadMapper.selectBlogUploadById(id);
        if (record == null)
        {
            return 0;
        }
        // 仍被文章/页面/设置等引用时拒绝删除，避免删掉物理文件造成引用处图片失效
        String references = describeReferences(record.getUrl());
        if (references != null)
        {
            String name = StringUtils.isNotEmpty(record.getOriginalName())
                ? record.getOriginalName() : record.getFileName();
            throw new ServiceException("文件「" + name + "」仍被引用（" + references + "），请先解除引用再删除");
        }
        int result = blogUploadMapper.deleteBlogUploadById(id);
        if (result > 0 && StringUtils.isNotEmpty(record.getUrl()))
        {
            try
            {
                // 尝试删除物理文件：url 形如 /profile/upload/xxx
                String localPath = RuoYiConfig.getProfile();
                String filePath = localPath + FileUtils.stripPrefix(record.getUrl());
                FileUtils.deleteFile(filePath);
            }
            catch (Exception e)
            {
                log.warn("删除上传物理文件失败: url={}, error={}", record.getUrl(), e.getMessage());
            }
        }
        return result;
    }

    /**
     * 描述该文件在各业务表中的引用情况
     *
     * @param url 上传文件的访问路径
     * @return 引用描述（如「2 篇文章、1 个自定义页面」）；未被引用返回 null
     */
    private String describeReferences(String url)
    {
        if (StringUtils.isEmpty(url))
        {
            return null;
        }
        List<BlogUploadReference> references = blogUploadRefMapper.countFileReferences(url);
        if (references == null)
        {
            return null;
        }
        List<String> parts = new ArrayList<>();
        for (BlogUploadReference reference : references)
        {
            Long count = reference.getRefCount();
            if (count != null && count > 0)
            {
                parts.add(count + " " + refTypeLabel(reference.getRefType()));
            }
        }
        return parts.isEmpty() ? null : String.join("、", parts);
    }

    /** 引用来源标识转中文量词描述 */
    private String refTypeLabel(String refType)
    {
        if (refType == null)
        {
            return "处引用";
        }
        switch (refType)
        {
            case "article":
                return "篇文章";
            case "page":
                return "个自定义页面";
            case "setting":
                return "项站点设置";
            case "friendLink":
                return "条友情链接";
            case "userAvatar":
                return "个用户头像";
            default:
                return "处引用";
        }
    }
}
