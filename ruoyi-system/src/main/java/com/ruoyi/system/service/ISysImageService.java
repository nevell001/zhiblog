package com.ruoyi.system.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 图片处理服务接口
 * 提供图片压缩、水印添加、格式转换等功能
 *
 * @author ruoyi
 * @date 2025-12-20
 */
public interface ISysImageService {
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
    Map<String, Object> compressImage(MultipartFile file, float quality, Integer maxWidth, Integer maxHeight) throws IOException;

    /**
     * 添加水印
     *
     * @param file 图片文件
     * @param watermarkText 水印文字
     * @return 添加水印后的图片数据，包含图片URL和处理信息
     * @throws IOException
     */
    Map<String, Object> addWatermark(MultipartFile file, String watermarkText) throws IOException;

    /**
     * 图片格式转换
     *
     * @param file 图片文件
     * @param targetFormat 目标格式 (jpg, png, webp)
     * @return 转换后的图片数据，包含图片URL和处理信息
     * @throws IOException
     */
    Map<String, Object> convertFormat(MultipartFile file, String targetFormat) throws IOException;

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
    Map<String, Object> batchProcessImages(MultipartFile[] files, float quality, Integer maxWidth, Integer maxHeight, String watermarkText, String targetFormat) throws IOException;
}