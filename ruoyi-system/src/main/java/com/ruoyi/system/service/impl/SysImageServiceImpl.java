package com.ruoyi.system.service.impl;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.image.ImageCompressUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 图片处理服务实现类
 * 提供图片压缩、水印添加、格式转换等功能的具体实现
 *
 * @author ruoyi
 * @date 2025-12-20
 */
@Service
public class SysImageServiceImpl implements ISysImageService {
    /**
     * 图片存储路径前缀
     */
    private static final String IMAGE_PATH_PREFIX = "/image/processed";

    /**
     * 图片压缩
     *
     * @param file 图片文件
     * @param quality 压缩质量 (0.1-1.0)
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @return 压缩后的图片数据，包含图片URL和处理信息
     * @throws IOException
     */
    @Override
    public Map<String, Object> compressImage(MultipartFile file, float quality, Integer maxWidth, Integer maxHeight) throws IOException {
        // 处理质量参数限制
        quality = Math.max(0.1f, Math.min(1.0f, quality));

        // 执行压缩
        byte[] compressed;
        if (maxWidth != null && maxHeight != null) {
            compressed = ImageCompressUtils.compress(file, maxWidth, maxHeight, quality);
        } else {
            compressed = ImageCompressUtils.smartCompress(file);
        }

        // 保存压缩后的图片
        String imageUrl = saveProcessedImage(compressed, file);

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("imageUrl", imageUrl);
        result.put("originalSize", file.getSize());
        result.put("compressedSize", compressed.length);
        result.put("compressionRatio", String.format("%.2f%%", (1 - (float) compressed.length / file.getSize()) * 100));
        result.put("quality", quality);
        result.put("width", maxWidth);
        result.put("height", maxHeight);
        result.put("format", ImageCompressUtils.getImageFormat(file));

        return result;
    }

    /**
     * 添加水印
     *
     * @param file 图片文件
     * @param watermarkText 水印文字
     * @return 添加水印后的图片数据，包含图片URL和处理信息
     * @throws IOException
     */
    @Override
    public Map<String, Object> addWatermark(MultipartFile file, String watermarkText) throws IOException {
        // 执行添加水印
        byte[] watermarked = ImageCompressUtils.addWatermark(file, StringUtils.isEmpty(watermarkText) ? "" : watermarkText);

        // 保存添加水印后的图片
        String imageUrl = saveProcessedImage(watermarked, file);

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("imageUrl", imageUrl);
        result.put("originalSize", file.getSize());
        result.put("watermarkedSize", watermarked.length);
        result.put("watermarkText", watermarkText);
        result.put("format", ImageCompressUtils.getImageFormat(file));

        return result;
    }

    /**
     * 图片格式转换
     *
     * @param file 图片文件
     * @param targetFormat 目标格式 (jpg, png, webp)
     * @return 转换后的图片数据，包含图片URL和处理信息
     * @throws IOException
     */
    @Override
    public Map<String, Object> convertFormat(MultipartFile file, String targetFormat) throws IOException {
        // 执行格式转换
        byte[] converted = ImageCompressUtils.convertFormat(file, targetFormat);

        // 保存转换后的图片
        String imageUrl = saveProcessedImage(converted, file, targetFormat);

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("imageUrl", imageUrl);
        result.put("originalSize", file.getSize());
        result.put("convertedSize", converted.length);
        result.put("originalFormat", ImageCompressUtils.getImageFormat(file));
        result.put("targetFormat", targetFormat);

        return result;
    }

    /**
     * 批量图片处理
     *
     * @param files 图片文件列表
     * @param quality 压缩质量
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @param watermarkText 水印文字
     * @param targetFormat 目标格式
     * @return 处理后的图片数据列表，包含每张图片的URL和处理信息
     * @throws IOException
     */
    @Override
    public Map<String, Object> batchProcessImages(MultipartFile[] files, float quality, Integer maxWidth, Integer maxHeight, String watermarkText, String targetFormat) throws IOException {
        List<Map<String, Object>> results = new ArrayList<>();
        long totalOriginalSize = 0;
        long totalProcessedSize = 0;

        // 处理每张图片
        for (MultipartFile file : files) {
            try {
                // 压缩图片
                byte[] processed = ImageCompressUtils.compress(file, maxWidth != null ? maxWidth : 1920, maxHeight != null ? maxHeight : 1080, quality);

                // 如需要，添加水印
                if (StringUtils.isNotEmpty(watermarkText)) {
                    processed = ImageCompressUtils.addWatermark(file, watermarkText);
                }

                // 如需要，转换格式
                if (StringUtils.isNotEmpty(targetFormat)) {
                    processed = ImageCompressUtils.convertFormat(file, targetFormat);
                }

                // 保存处理后的图片
                String imageUrl = saveProcessedImage(processed, file, targetFormat);

                // 计算统计信息
                totalOriginalSize += file.getSize();
                totalProcessedSize += processed.length;

                // 保存单张图片的结果
                Map<String, Object> result = new HashMap<>();
                result.put("imageUrl", imageUrl);
                result.put("originalSize", file.getSize());
                result.put("processedSize", processed.length);
                result.put("compressionRatio", String.format("%.2f%%", (1 - (float) processed.length / file.getSize()) * 100));
                result.put("quality", quality);
                result.put("width", maxWidth);
                result.put("height", maxHeight);
                result.put("watermarkText", watermarkText);
                result.put("originalFormat", ImageCompressUtils.getImageFormat(file));
                result.put("targetFormat", targetFormat);

                results.add(result);
            } catch (Exception e) {
                // 处理单张图片失败，记录错误信息但继续处理其他图片
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("filename", file.getOriginalFilename());
                errorResult.put("error", e.getMessage());
                results.add(errorResult);
            }
        }

        // 返回总体结果
        Map<String, Object> overallResult = new HashMap<>();
        overallResult.put("totalImages", files.length);
        overallResult.put("totalOriginalSize", totalOriginalSize);
        overallResult.put("totalProcessedSize", totalProcessedSize);
        if (totalOriginalSize > 0) {
            overallResult.put("overallCompressionRatio", String.format("%.2f%%", (1 - (float) totalProcessedSize / totalOriginalSize) * 100));
        }
        overallResult.put("results", results);

        return overallResult;
    }

    /**
     * 保存处理后的图片
     *
     * @param imageData 图片数据
     * @param originalFile 原始文件
     * @return 图片URL
     * @throws IOException
     */
    private String saveProcessedImage(byte[] imageData, MultipartFile originalFile) throws IOException {
        return saveProcessedImage(imageData, originalFile, null);
    }

    /**
     * 保存处理后的图片
     *
     * @param imageData 图片数据
     * @param originalFile 原始文件
     * @param targetFormat 目标格式（可选）
     * @return 图片URL
     * @throws IOException
     */
    private String saveProcessedImage(byte[] imageData, MultipartFile originalFile, String targetFormat) throws IOException {
        // 生成文件名
        String fileName = FileUploadUtils.extractFilename(originalFile);
        
        // 确保目录存在
        String uploadDir = RuoYiConfig.getProfile() + IMAGE_PATH_PREFIX;
        File desc = FileUploadUtils.getAbsoluteFile(uploadDir, fileName);
        
        // 写入文件
        java.nio.file.Files.write(desc.toPath(), imageData);
        
        // 返回相对路径
        return FileUploadUtils.getPathFileName(uploadDir, fileName);
    }
}