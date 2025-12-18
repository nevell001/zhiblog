package com.ruoyi.web.controller.common;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.domain.BlogCategory;
import com.ruoyi.system.domain.BlogTag;
import com.ruoyi.system.domain.BlogSetting;
import com.ruoyi.system.service.IBlogArticleService;
import com.ruoyi.system.service.IBlogCategoryService;
import com.ruoyi.system.service.IBlogTagService;
import com.ruoyi.system.service.IBlogSettingService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 通用请求处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/common")
public class CommonController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private IBlogArticleService blogArticleService;

    @Autowired
    private IBlogCategoryService blogCategoryService;

    @Autowired
    private IBlogTagService blogTagService;

    @Autowired
    private IBlogSettingService blogSettingService;

    private static final String FILE_DELIMETER = ",";

    /**
     * 通用下载请求
     * 
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 通用上传请求（带图片压缩）
     */
    @PostMapping("/upload/compressed")
    public AjaxResult uploadFileCompressed(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称（带图片压缩）
            String fileName = FileUploadUtils.uploadWithCompression(filePath, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);

            // 生成可访问的URL，使用相对路径避免域名问题
            String url = fileName; // 直接返回文件路径，前端会自动拼接域名

            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            ajax.put("compressed", true); // 标记是否进行了压缩
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 头像上传接口（专门压缩为头像尺寸）
     */
    @PostMapping("/upload/avatar")
    public AjaxResult uploadAvatar(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称（头像压缩）
            String fileName = FileUploadUtils.uploadAvatar(filePath, file, true);

            // 生成可访问的URL，使用相对路径避免域名问题
            String url = fileName; // 直接返回文件路径

            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            ajax.put("type", "avatar"); // 标记为头像类型
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 缩略图上传接口
     */
    @PostMapping("/upload/thumbnail")
    public AjaxResult uploadThumbnail(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称（缩略图压缩）
            String fileName = FileUploadUtils.uploadThumbnail(filePath, file, true);

            // 生成可访问的URL，使用相对路径避免域名问题
            String url = fileName; // 直接返回文件路径

            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            ajax.put("type", "thumbnail"); // 标记为缩略图类型
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 文章封面图上传接口
     */
    @PostMapping("/upload/article-cover")
    public AjaxResult uploadArticleCover(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称（文章封面压缩）
            String fileName = FileUploadUtils.uploadArticleCover(filePath, file, true);

            // 生成可访问的URL，使用相对路径避免域名问题
            String url = fileName; // 直接返回文件路径

            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            ajax.put("type", "article-cover"); // 标记为文章封面类型
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 移动端图片上传接口
     */
    @PostMapping("/upload/mobile")
    public AjaxResult uploadMobileImage(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称（移动端适配压缩）
            String fileName = FileUploadUtils.uploadMobileImage(filePath, file, true);

            // 生成可访问的URL，使用相对路径避免域名问题
            String url = fileName; // 直接返回文件路径

            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            ajax.put("type", "mobile"); // 标记为移动端适配类型
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 带水印图片上传接口
     */
    @PostMapping("/upload/watermark")
    public AjaxResult uploadWatermarkImage(MultipartFile file, String watermarkText) throws Exception
    {
        try
        {
            if (StringUtils.isEmpty(watermarkText)) {
                watermarkText = "版权所有";
            }

            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称（添加水印）
            String fileName = FileUploadUtils.uploadWatermarkImage(filePath, file, watermarkText, true);

            // 生成可访问的URL，使用相对路径避免域名问题
            String url = fileName; // 直接返回文件路径

            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            ajax.put("watermarkText", watermarkText);
            ajax.put("type", "watermark"); // 标记为水印类型
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files)
            {
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = serverConfig.getUrl() + fileName;
                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", StringUtils.join(urls, FILE_DELIMETER));
            ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMETER));
            ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMETER));
            ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMETER));
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = RuoYiConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + FileUtils.stripPrefix(resource);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    // ==================== 博客前台接口转发 ====================

    /**
     * 获取博客设置（前台用）- 转发接口
     */
    @Anonymous
    @GetMapping("/blog/settings")
    public AjaxResult getBlogSettings()
    {
        try {
            BlogSetting blogSetting = new BlogSetting();
            blogSetting.setDelFlag("0");
            List<BlogSetting> blogSettingList = blogSettingService.selectBlogSettingList(blogSetting);
            return AjaxResult.success(blogSettingList);
        } catch (Exception e) {
            log.error("获取博客设置失败", e);
            return AjaxResult.error("获取博客设置失败");
        }
    }

    /**
     * 获取文章列表（前台用，支持分页）- 转发接口
     */
    @Anonymous
    @GetMapping("/blog/articles/list")
    public TableDataInfo articleList(BlogArticle blogArticle)
    {
        try {
            // 设置查询条件
            blogArticle.setStatus(1L); // 只查询已发布的文章
            blogArticle.setDelFlag(0L); // 只查询未删除的文章

            startPage(); // 启用分页
            List<BlogArticle> list = blogArticleService.selectBlogArticleList(blogArticle);
            return getDataTable(list);
        } catch (Exception e) {
            log.error("获取文章列表失败", e);
            return getDataTable(null);
        }
    }

    /**
     * 获取文章详情（前台用）- 转发接口
     */
    @Anonymous
    @GetMapping("/blog/articles/{id}")
    public AjaxResult getArticleDetail(@PathVariable("id") Long id)
    {
        try {
            BlogArticle blogArticle = blogArticleService.selectBlogArticleById(id);
            if (blogArticle == null) {
                return AjaxResult.error("文章不存在");
            }
            
            // 检查文章状态
            if (blogArticle.getStatus() == null || !blogArticle.getStatus().equals(1L)) {
                return AjaxResult.error("文章未发布");
            }
            
            if (blogArticle.getDelFlag() == null || !blogArticle.getDelFlag().equals(0L)) {
                return AjaxResult.error("文章已删除");
            }
            
            return AjaxResult.success(blogArticle);
        } catch (Exception e) {
            log.error("获取文章详情失败", e);
            return AjaxResult.error("获取文章详情失败");
        }
    }

    /**
     * 获取分类列表（前台用）- 转发接口
     */
    @Anonymous
    @GetMapping("/blog/categories/list")
    public AjaxResult categoryList()
    {
        try {
            BlogCategory blogCategory = new BlogCategory();
            blogCategory.setDelFlag("0");
            blogCategory.setStatus(1);
            List<BlogCategory> list = blogCategoryService.selectBlogCategoryList(blogCategory);
            return AjaxResult.success(list);
        } catch (Exception e) {
            log.error("获取分类列表失败", e);
            return AjaxResult.error("获取分类列表失败");
        }
    }

    /**
     * 获取标签云（前台用）- 转发接口
     */
    @Anonymous
    @GetMapping("/blog/tags/cloud")
    public AjaxResult getTagCloud()
    {
        try {
            BlogTag blogTag = new BlogTag();
            blogTag.setDelFlag(0);
            List<BlogTag> list = blogTagService.selectBlogTagList(blogTag);
            return AjaxResult.success(list);
        } catch (Exception e) {
            log.error("获取标签云失败", e);
            return AjaxResult.error("获取标签云失败");
        }
    }
}
