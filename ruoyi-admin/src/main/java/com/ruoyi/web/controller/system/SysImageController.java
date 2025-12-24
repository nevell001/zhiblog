package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.service.ISysImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 图片处理Controller
 * 提供图片压缩、水印添加、格式转换等功能
 *
 * @author ruoyi
 * @date 2025-12-20
 */
@RestController
@RequestMapping("/system/image")
public class SysImageController
{
    @Autowired
    private ISysImageService sysImageService;

    /**
     * 图片压缩
     *
     * @param file 图片文件
     * @param quality 压缩质量 (0.1-1.0)
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @return 压缩后的图片数据
     */
    @PostMapping("/compress")
    public R<?> compressImage(@RequestParam("file") MultipartFile file,
                            @RequestParam(required = false, defaultValue = "0.8") float quality,
                            @RequestParam(required = false) Integer maxWidth,
                            @RequestParam(required = false) Integer maxHeight)
    {
        try {
            Map<String, Object> result = sysImageService.compressImage(file, quality, maxWidth, maxHeight);
            return R.ok(result);
        } catch (IOException e) {
            return R.fail("图片压缩失败: " + e.getMessage());
        }
    }

    /**
     * 添加水印
     *
     * @param file 图片文件
     * @param watermarkText 水印文字
     * @return 添加水印后的图片数据
     */
    @PostMapping("/watermark")
    public R<?> addWatermark(@RequestParam("file") MultipartFile file,
                           @RequestParam(required = false) String watermarkText)
    {
        try {
            Map<String, Object> result = sysImageService.addWatermark(file, watermarkText);
            return R.ok(result);
        } catch (IOException e) {
            return R.fail("添加水印失败: " + e.getMessage());
        }
    }

    /**
     * 图片格式转换
     *
     * @param file 图片文件
     * @param targetFormat 目标格式 (jpg, png, webp)
     * @return 转换后的图片数据
     */
    @PostMapping("/convert")
    public R<?> convertFormat(@RequestParam("file") MultipartFile file,
                            @RequestParam(defaultValue = "jpg") String targetFormat)
    {
        try {
            Map<String, Object> result = sysImageService.convertFormat(file, targetFormat);
            return R.ok(result);
        } catch (IOException e) {
            return R.fail("格式转换失败: " + e.getMessage());
        }
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
     * @return 处理后的图片数据列表
     */
    @PostMapping("/batch-process")
    public R<?> batchProcessImages(@RequestParam("files") MultipartFile[] files,
                                @RequestParam(required = false, defaultValue = "0.8") float quality,
                                @RequestParam(required = false) Integer maxWidth,
                                @RequestParam(required = false) Integer maxHeight,
                                @RequestParam(required = false) String watermarkText,
                                @RequestParam(required = false, defaultValue = "jpg") String targetFormat)
    {
        try {
            Map<String, Object> result = sysImageService.batchProcessImages(files, quality, maxWidth, maxHeight, watermarkText, targetFormat);
            return R.ok(result);
        } catch (IOException e) {
            return R.fail("批量处理失败: " + e.getMessage());
        }
    }
}