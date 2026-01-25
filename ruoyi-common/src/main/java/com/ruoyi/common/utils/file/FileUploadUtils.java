package com.ruoyi.common.utils.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.file.FileNameLengthLimitExceededException;
import com.ruoyi.common.exception.file.FileSizeLimitExceededException;
import com.ruoyi.common.exception.file.InvalidExtensionException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.common.utils.uuid.Seq;
import com.ruoyi.common.utils.image.ImageCompressUtils;

/**
 * 文件上传工具类
 *
 * @author ruoyi
 */
public class FileUploadUtils
{
    private static final Logger log = LoggerFactory.getLogger(FileUploadUtils.class);
    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024L;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * 默认上传的地址
     */
    private static String defaultBaseDir = RuoYiConfig.getProfile();

    public static void setDefaultBaseDir(String defaultBaseDir)
    {
        FileUploadUtils.defaultBaseDir = defaultBaseDir;
    }

    public static String getDefaultBaseDir()
    {
        return defaultBaseDir;
    }

    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件名称
     * @throws Exception
     */
    public static final String upload(MultipartFile file) throws IOException
    {
        try
        {
            return upload(getDefaultBaseDir(), file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file) throws IOException
    {
        try
        {
            return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException 比如读写文件出错时
     * @throws InvalidExtensionException 文件校验异常
     */
    public static final String upload(String baseDir, MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException
    {
        return upload(baseDir, file, allowedExtension, false);
    }
    
    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @param useCustomNaming 系统自定义文件名
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException 比如读写文件出错时
     * @throws InvalidExtensionException 文件校验异常
     */
    public static final String upload(String baseDir, MultipartFile file, String[] allowedExtension, boolean useCustomNaming)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException
    {
        int fileNameLength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH)
        {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }

        assertAllowed(file, allowedExtension);

        String fileName = useCustomNaming ? uuidFilename(file) : extractFilename(file);

        String absPath = getAbsoluteFile(baseDir, fileName).getAbsolutePath();
        file.transferTo(Paths.get(absPath));
        return getPathFileName(baseDir, fileName);
    }

    /**
     * 编码文件名(日期格式目录 + 原文件名 + 序列值 + 后缀)
     */
    public static final String extractFilename(MultipartFile file)
    {
        return StringUtils.format("{}/{}_{}.{}", DateUtils.datePath(), FilenameUtils.getBaseName(file.getOriginalFilename()), Seq.getId(Seq.uploadSeqType), getExtension(file));
    }

    /**
     * 编编码文件名(日期格式目录 + UUID + 后缀)
     */
    public static final String uuidFilename(MultipartFile file)
    {
        return StringUtils.format("{}/{}.{}", DateUtils.datePath(), IdUtils.fastSimpleUUID(), getExtension(file));
    }

    public static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException
    {
        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.exists())
        {
            if (!desc.getParentFile().exists())
            {
                desc.getParentFile().mkdirs();
            }
        }
        return desc;
    }

    public static final String getPathFileName(String uploadDir, String fileName) throws IOException
    {
        int dirLastIndex = RuoYiConfig.getProfile().length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        return Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     * @return
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws InvalidExtensionException
     */
    public static final void assertAllowed(MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, InvalidExtensionException
    {
        long size = file.getSize();
        if (size > DEFAULT_MAX_SIZE)
        {
            throw new FileSizeLimitExceededException(DEFAULT_MAX_SIZE / 1024 / 1024);
        }

        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension))
        {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION)
            {
                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension,
                        fileName);
            }
            else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION)
            {
                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension,
                        fileName);
            }
            else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION)
            {
                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension,
                        fileName);
            }
            else if (allowedExtension == MimeTypeUtils.VIDEO_EXTENSION)
            {
                throw new InvalidExtensionException.InvalidVideoExtensionException(allowedExtension, extension,
                        fileName);
            }
            else
            {
                throw new InvalidExtensionException(allowedExtension, extension, fileName);
            }
        }
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public static final boolean isAllowedExtension(String extension, String[] allowedExtension)
    {
        for (String str : allowedExtension)
        {
            if (str.equalsIgnoreCase(extension))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件名的后缀
     * 
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getExtension(MultipartFile file)
    {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension))
        {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }

    /**
     * 上传文件并自动压缩图片
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException 比如读写文件出错时
     * @throws InvalidExtensionException 文件校验异常
     */
    public static final String uploadWithCompression(String baseDir, MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException
    {
        return uploadWithCompression(baseDir, file, allowedExtension, false);
    }

    /**
     * 上传文件并自动压缩图片
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @param allowedExtension 上传文件类型
     * @param useCustomNaming 系统自定义文件名
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException 比如读写文件出错时
     * @throws InvalidExtensionException 文件校验异常
     */
    public static final String uploadWithCompression(String baseDir, MultipartFile file, String[] allowedExtension, boolean useCustomNaming)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException
    {
        int fileNameLength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH)
        {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }

        assertAllowed(file, allowedExtension);

        String fileName = useCustomNaming ? uuidFilename(file) : extractFilename(file);

        String absPath = getAbsoluteFile(baseDir, fileName).getAbsolutePath();

        // 判断是否为图片文件且需要压缩
        if (ImageCompressUtils.isImageFile(file)) {
            try {
                // 压缩图片
                byte[] compressedData = ImageCompressUtils.smartCompress(file);
                Files.write(Paths.get(absPath), compressedData);
                log.debug("图片已压缩并保存: {}, 原始大小: {} bytes, 压缩后: {} bytes",
                    fileName, file.getSize(), compressedData.length);
            } catch (Exception e) {
                log.warn("图片压缩失败，使用原始文件: {}, 错误: {}", fileName, e.getMessage());
                // 压缩失败时使用原始文件
                file.transferTo(Paths.get(absPath));
            }
        } else {
            // 非图片文件直接保存
            file.transferTo(Paths.get(absPath));
        }

        return getPathFileName(baseDir, fileName);
    }

    /**
     * 上传头像文件（专门压缩为头像尺寸）
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException 比如读写文件出错时
     * @throws InvalidExtensionException 文件校验异常
     */
    public static final String uploadAvatar(String baseDir, MultipartFile file)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException
    {
        return uploadAvatar(baseDir, file, false);
    }

    /**
     * 上传头像文件（专门压缩为头像尺寸）
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @param useCustomNaming 系统自定义文件名
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException 比如读写文件出错时
     * @throws InvalidExtensionException 文件校验异常
     */
    public static final String uploadAvatar(String baseDir, MultipartFile file, boolean useCustomNaming)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException
    {
        int fileNameLength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH)
        {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }

        assertAllowed(file, MimeTypeUtils.IMAGE_EXTENSION);

        String fileName = useCustomNaming ? uuidFilename(file) : extractFilename(file);

        String absPath = getAbsoluteFile(baseDir, fileName).getAbsolutePath();

        try {
            // 专门压缩头像
            byte[] avatarData = ImageCompressUtils.compressAvatar(file);
            Files.write(Paths.get(absPath), avatarData);
            log.debug("头像已压缩并保存: {}, 原始大小: {} bytes, 压缩后: {} bytes",
                fileName, file.getSize(), avatarData.length);
        } catch (Exception e) {
            log.warn("头像压缩失败，使用原始文件: {}, 错误: {}", fileName, e.getMessage());
            // 压缩失败时使用原始文件
            file.transferTo(Paths.get(absPath));
        }

        return getPathFileName(baseDir, fileName);
    }

    /**
     * 上传缩略图文件
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException 比如读写文件出错时
     * @throws InvalidExtensionException 文件校验异常
     */
    public static final String uploadThumbnail(String baseDir, MultipartFile file)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException
    {
        return uploadThumbnail(baseDir, file, false);
    }

    /**
     * 上传缩略图文件
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @param useCustomNaming 系统自定义文件名
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException 比如读写文件出错时
     * @throws InvalidExtensionException 文件校验异常
     */
    public static final String uploadThumbnail(String baseDir, MultipartFile file, boolean useCustomNaming)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException
    {
        int fileNameLength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH)
        {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }

        assertAllowed(file, MimeTypeUtils.IMAGE_EXTENSION);

        String fileName = useCustomNaming ? uuidFilename(file) : extractFilename(file);

        String absPath = getAbsoluteFile(baseDir, fileName).getAbsolutePath();

        try {
            // 压缩缩略图
            byte[] thumbnailData = ImageCompressUtils.compressThumbnail(file);
            Files.write(Paths.get(absPath), thumbnailData);
            log.debug("缩略图已压缩并保存: {}, 原始大小: {} bytes, 压缩后: {} bytes",
                fileName, file.getSize(), thumbnailData.length);
        } catch (Exception e) {
            log.warn("缩略图压缩失败，使用原始文件: {}, 错误: {}", fileName, e.getMessage());
            // 压缩失败时使用原始文件
            file.transferTo(Paths.get(absPath));
        }

        return getPathFileName(baseDir, fileName);
    }

    /**
     * 上传文章封面图（带压缩）
     *
     * @param baseDir 基础路径
     * @param file 上传的文件
     * @param useCustomNaming 是否使用自定义命名
     * @return 文件路径
     * @throws IOException
     */
    public static final String uploadArticleCover(String baseDir, MultipartFile file, boolean useCustomNaming) throws IOException
    {
        try
        {
            return uploadWithSpecialCompression(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, useCustomNaming, "article-cover");
        }
        catch (Exception e)
        {
            throw new IOException("文章封面图上传失败", e);
        }
    }

    /**
     * 上传移动端适配图片
     *
     * @param baseDir 基础路径
     * @param file 上传的文件
     * @param useCustomNaming 是否使用自定义命名
     * @return 文件路径
     * @throws IOException
     */
    public static final String uploadMobileImage(String baseDir, MultipartFile file, boolean useCustomNaming) throws IOException
    {
        try
        {
            return uploadWithSpecialCompression(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, useCustomNaming, "mobile");
        }
        catch (Exception e)
        {
            throw new IOException("移动端图片上传失败", e);
        }
    }

    /**
     * 上传带水印的图片
     *
     * @param baseDir 基础路径
     * @param file 上传的文件
     * @param watermarkText 水印文字
     * @param useCustomNaming 是否使用自定义命名
     * @return 文件路径
     * @throws IOException
     */
    public static final String uploadWatermarkImage(String baseDir, MultipartFile file, String watermarkText, boolean useCustomNaming) throws IOException, InvalidExtensionException
    {
        try
        {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                throw new IOException("文件名为空");
            }
            int fileNamelength = originalFilename.length();
            if (fileNamelength > DEFAULT_FILE_NAME_LENGTH)
            {
                throw new FileNameLengthLimitExceededException(DEFAULT_FILE_NAME_LENGTH);
            }

            assertAllowed(file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);

            String fileName = useCustomNaming ? uuidFilename(file) : extractFilename(file);
            String absPath = getAbsoluteFile(baseDir, fileName).getAbsolutePath();

            // 添加水印
            byte[] watermarkedData = ImageCompressUtils.addWatermark(file, watermarkText);
            Files.write(Paths.get(absPath), watermarkedData);

            log.debug("带水印图片已保存: {}, 水印文字: {}", fileName, watermarkText);

            return getPathFileName(baseDir, fileName);
        }
        catch (Exception e)
        {
            throw new IOException("带水印图片上传失败", e);
        }
    }

    /**
     * 专用压缩上传方法
     */
    private static final String uploadWithSpecialCompression(String baseDir, MultipartFile file, String[] allowedExtension, boolean useCustomNaming, String compressType) throws IOException, InvalidExtensionException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IOException("文件名为空");
        }
        int fileNamelength = originalFilename.length();
        if (fileNamelength > DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException(DEFAULT_FILE_NAME_LENGTH);
        }

        assertAllowed(file, allowedExtension);

        String fileName = useCustomNaming ? uuidFilename(file) : extractFilename(file);
        String absPath = getAbsoluteFile(baseDir, fileName).getAbsolutePath();

        try {
            byte[] compressedData;
            switch (compressType) {
                case "article-cover":
                    compressedData = ImageCompressUtils.compressArticleCover(file);
                    break;
                case "mobile":
                    compressedData = ImageCompressUtils.compressForMobile(file);
                    break;
                default:
                    compressedData = ImageCompressUtils.smartCompress(file);
                    break;
            }

            Files.write(Paths.get(absPath), compressedData);
            log.debug("专用压缩图片已保存: {}, 压缩类型: {}, 原始大小: {} bytes, 压缩后: {} bytes",
                    fileName, compressType, file.getSize(), compressedData.length);
        } catch (Exception e) {
            log.warn("专用压缩失败，使用原始文件: {}, 压缩类型: {}, 错误: {}", fileName, compressType, e.getMessage());
            // 压缩失败时使用原始文件
            file.transferTo(Paths.get(absPath));
        }

        return getPathFileName(baseDir, fileName);
    }
}
