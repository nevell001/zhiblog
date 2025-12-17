package com.ruoyi.web.controller.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.image.ImageCompressUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.config.ImageCompressConfig;

/**
 * 图片压缩测试控制器
 *
 * @author nevell
 * @since 2025-12-17
 */
@RestController
@RequestMapping("/test/image")
public class ImageCompressTestController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ImageCompressTestController.class);

    /**
     * 测试图片压缩功能
     */
    @PostMapping("/compress")
    public AjaxResult testCompress(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return error("请选择要上传的图片文件");
            }

            // 检查是否为图片文件
            if (!ImageCompressUtils.isImageFile(file)) {
                return error("只支持图片文件格式");
            }

            long originalSize = file.getSize();
            String originalName = file.getOriginalFilename();

            log.info("开始测试图片压缩: 文件名={}, 原始大小={} bytes", originalName, originalSize);

            // 测试智能压缩
            byte[] compressedData = ImageCompressUtils.smartCompress(file);
            long compressedSize = compressedData.length;
            double compressionRatio = (1.0 - (double)compressedSize / originalSize) * 100;

            // 测试头像压缩
            byte[] avatarData = ImageCompressUtils.compressAvatar(file);
            long avatarSize = avatarData.length;
            double avatarCompressionRatio = (1.0 - (double)avatarSize / originalSize) * 100;

            // 获取图片信息
            ImageCompressUtils.ImageInfo imageInfo = ImageCompressUtils.getImageInfo(file);

            AjaxResult ajax = success();
            ajax.put("originalName", originalName);
            ajax.put("originalSize", originalSize);
            ajax.put("compressedSize", compressedSize);
            ajax.put("compressionRatio", String.format("%.2f%%", compressionRatio));
            ajax.put("avatarSize", avatarSize);
            ajax.put("avatarCompressionRatio", String.format("%.2f%%", avatarCompressionRatio));
            ajax.put("imageInfo", imageInfo);
            ajax.put("message", "图片压缩测试成功");

            log.info("图片压缩测试完成: 原始={} bytes -> 智能压缩={} bytes (压缩率: {:.2f}%), 头像压缩={} bytes (压缩率: {:.2f}%)",
                    originalSize, compressedSize, compressionRatio, avatarSize, avatarCompressionRatio);

            return ajax;

        } catch (Exception e) {
            log.error("图片压缩测试失败", e);
            return error("压缩测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试文件上传压缩
     */
    @PostMapping("/upload/compressed")
    public AjaxResult testUploadCompressed(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return error("请选择要上传的文件");
            }

            long originalSize = file.getSize();
            String originalName = file.getOriginalFilename();

            log.info("开始测试文件上传压缩: 文件名={}, 原始大小={} bytes", originalName, originalSize);

            // 使用文件上传工具类进行压缩上传
            String filePath = RuoYiConfig.getUploadPath();
            String fileName = FileUploadUtils.uploadWithCompression(filePath, file,
                    com.ruoyi.common.utils.file.MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);

            AjaxResult ajax = success();
            ajax.put("originalName", originalName);
            ajax.put("originalSize", originalSize);
            ajax.put("fileName", fileName);
            ajax.put("message", "文件上传压缩测试成功");

            log.info("文件上传压缩测试完成: {}", fileName);

            return ajax;

        } catch (Exception e) {
            log.error("文件上传压缩测试失败", e);
            return error("上传压缩测试失败: " + e.getMessage());
        }
    }

    /**
     * 获取图片压缩配置信息
     */
    @GetMapping("/config")
    public AjaxResult getCompressConfig() {
        try {
            AjaxResult ajax = success();

            try {
                // 使用一个简单的方法来检查配置是否初始化
                boolean configAvailable = ImageCompressUtils.getConfig() != null;

                if (configAvailable) {
                    ImageCompressConfig compressConfig = ImageCompressUtils.getConfig();

                    // 直接在data中返回配置信息
                    ajax.put("configAvailable", true);
                    ajax.put("enabled", compressConfig.isEnabled());
                    ajax.put("thresholdSize", compressConfig.getThresholdSize());
                    ajax.put("avatarSize", compressConfig.getAvatarSize());
                    ajax.put("thumbnailSize", compressConfig.getThumbnailSize());
                    ajax.put("avatarQuality", compressConfig.getAvatarQuality());
                    ajax.put("thumbnailQuality", compressConfig.getThumbnailQuality());
                    ajax.put("defaultQuality", compressConfig.getDefaultQuality());
                    ajax.put("maxWidth", compressConfig.getMaxWidth());
                    ajax.put("maxHeight", compressConfig.getMaxHeight());
                    ajax.put("message", "配置加载成功");

                    log.info("图片压缩配置获取成功: {}", compressConfig);
                } else {
                    // 返回默认配置
                    ajax.put("configAvailable", false);
                    ajax.put("enabled", true);
                    ajax.put("thresholdSize", "2MB");
                    ajax.put("avatarSize", 200);
                    ajax.put("thumbnailSize", 400);
                    ajax.put("avatarQuality", 0.9);
                    ajax.put("thumbnailQuality", 0.8);
                    ajax.put("defaultQuality", 0.85);
                    ajax.put("maxWidth", 1920);
                    ajax.put("maxHeight", 1080);
                    ajax.put("message", "使用默认配置");

                    log.warn("图片压缩配置未初始化，使用默认值");
                }
            } catch (Exception configException) {
                log.warn("获取图片压缩配置时出错，将使用默认配置: {}", configException.getMessage());

                // 异常时返回默认配置
                ajax.put("configAvailable", false);
                ajax.put("enabled", true);
                ajax.put("thresholdSize", "2MB");
                ajax.put("avatarSize", 200);
                ajax.put("thumbnailSize", 400);
                ajax.put("avatarQuality", 0.9);
                ajax.put("thumbnailQuality", 0.8);
                ajax.put("defaultQuality", 0.85);
                ajax.put("maxWidth", 1920);
                ajax.put("maxHeight", 1080);
                ajax.put("message", "配置异常，使用默认值: " + configException.getMessage());
            }

            return ajax;
        } catch (Exception e) {
            log.error("获取图片压缩配置失败", e);
            return error("获取配置失败: " + e.getMessage());
        }
    }
}