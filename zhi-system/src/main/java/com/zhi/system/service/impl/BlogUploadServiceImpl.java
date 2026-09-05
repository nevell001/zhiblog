package com.zhi.system.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhi.common.config.RuoYiConfig;
import com.zhi.common.utils.SecurityUtils;
import com.zhi.common.utils.StringUtils;
import com.zhi.common.utils.file.FileUtils;
import com.zhi.system.domain.BlogUpload;
import com.zhi.system.mapper.BlogUploadMapper;
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
}
