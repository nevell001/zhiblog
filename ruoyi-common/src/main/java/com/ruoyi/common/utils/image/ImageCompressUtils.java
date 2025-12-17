package com.ruoyi.common.utils.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.config.ImageCompressConfig;
import com.ruoyi.common.utils.StringUtils;

/**
 * 图片压缩工具类
 * 基于Thumbnailator实现高质量图片压缩
 *
 * @author nevell
 * @since 2025-12-17
 */
public class ImageCompressUtils {

    private static final Logger log = LoggerFactory.getLogger(ImageCompressUtils.class);

    private static ImageCompressConfig config;

    /**
     * 设置配置（用于Spring依赖注入）
     *
     * @param config 图片压缩配置
     */
    public static void setConfig(ImageCompressConfig config) {
        ImageCompressUtils.config = config;
    }

    /**
     * 获取当前配置
     *
     * @return 图片压缩配置
     */
    public static ImageCompressConfig getConfig() {
        return config;
    }

    /**
     * 默认压缩质量
     */
    private static final double DEFAULT_QUALITY = 0.85;

    /**
     * 默认最大宽度
     */
    private static final int DEFAULT_MAX_WIDTH = 1920;

    /**
     * 默认最大高度
     */
    private static final int DEFAULT_MAX_HEIGHT = 1080;

    /**
     * 压缩阈值大小（2MB）
     */
    private static final long COMPRESS_THRESHOLD = 2 * 1024 * 1024L;

    /**
     * 头像压缩尺寸
     */
    private static final int AVATAR_SIZE = 200;

    /**
     * 缩略图压缩尺寸
     */
    private static final int THUMBNAIL_SIZE = 400;

    /**
     * 智能压缩图片
     * 根据文件大小和图片类型自动选择最佳压缩策略
     *
     * @param file 源图片文件
     * @return 压缩后的图片数据
     * @throws IOException
     */
    public static byte[] smartCompress(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }

        long originalSize = file.getSize();
        String originalName = file.getOriginalFilename();

        log.debug("开始压缩图片: {}，原始大小: {} bytes", originalName, originalSize);

        // 获取压缩阈值，如果配置未初始化则使用默认值
        long threshold = (config != null) ? config.getThresholdSizeBytes() : COMPRESS_THRESHOLD;

        // 检查是否启用压缩
        if (config != null && !config.isEnabled()) {
            log.debug("图片压缩功能已禁用，跳过压缩");
            return file.getBytes();
        }

        // 小于阈值不压缩
        if (originalSize < threshold) {
            log.debug("文件大小 {} 小于压缩阈值 {}，跳过压缩", originalSize, threshold);
            return file.getBytes();
        }

        // 检查图片格式
        String format = getImageFormat(file);
        if (format == null) {
            log.warn("不支持的图片格式: {}", originalName);
            return file.getBytes();
        }

        // 根据文件大小和格式选择压缩策略
        CompressionStrategy strategy = selectCompressionStrategy(originalSize, format);

        return compressWithStrategy(file.getBytes(), strategy, format);
    }

    /**
     * 压缩头像图片
     * 专门用于头像压缩，生成正方形小图
     *
     * @param file 源图片文件
     * @return 压缩后的头像数据
     * @throws IOException
     */
    public static byte[] compressAvatar(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }

        String originalName = file.getOriginalFilename();
        String format = getImageFormat(file);

        log.debug("开始压缩头像: {}", originalName);

        // 获取头像压缩配置
        int avatarSize = (config != null) ? config.getAvatarSize() : AVATAR_SIZE;
        double avatarQuality = (config != null) ? config.getAvatarQuality() : 0.9;

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .size(avatarSize, avatarSize)
                    .crop(Positions.CENTER)
                    .outputQuality(avatarQuality)
                    .outputFormat(format != null ? format : "jpg")
                    .toOutputStream(outputStream);

            byte[] compressed = outputStream.toByteArray();
            log.debug("头像压缩完成: {} -> {} bytes", file.getSize(), compressed.length);

            return compressed;
        }
    }

    /**
     * 压缩缩略图
     * 生成适合列表展示的缩略图
     *
     * @param file 源图片文件
     * @return 压缩后的缩略图数据
     * @throws IOException
     */
    public static byte[] compressThumbnail(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }

        String format = getImageFormat(file);

        // 获取缩略图压缩配置
        int thumbnailSize = (config != null) ? config.getThumbnailSize() : THUMBNAIL_SIZE;
        double thumbnailQuality = (config != null) ? config.getThumbnailQuality() : 0.8;

        log.debug("开始压缩缩略图: {}, 尺寸: {}, 质量: {}",
            file.getOriginalFilename(), thumbnailSize, thumbnailQuality);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .size(thumbnailSize, thumbnailSize)
                    .keepAspectRatio(true)
                    .outputQuality(thumbnailQuality)
                    .outputFormat(format != null ? format : "jpg")
                    .toOutputStream(outputStream);

            byte[] compressed = outputStream.toByteArray();
            log.debug("缩略图压缩完成: {} -> {} bytes", file.getSize(), compressed.length);

            return compressed;
        }
    }

    /**
     * 自定义压缩
     *
     * @param file 源图片文件
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @param quality 压缩质量 (0.0-1.0)
     * @return 压缩后的图片数据
     * @throws IOException
     */
    public static byte[] compress(MultipartFile file, int maxWidth, int maxHeight, double quality) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }

        String format = getImageFormat(file);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .size(maxWidth, maxHeight)
                    .keepAspectRatio(true)
                    .outputQuality(quality)
                    .outputFormat(format != null ? format : "jpg")
                    .toOutputStream(outputStream);

            return outputStream.toByteArray();
        }
    }

    /**
     * 批量压缩
     *
     * @param files 源图片文件数组
     * @return 压缩后的图片数据数组
     */
    public static byte[][] batchCompress(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return new byte[0][];
        }

        byte[][] results = new byte[files.length][];

        for (int i = 0; i < files.length; i++) {
            try {
                results[i] = smartCompress(files[i]);
            } catch (IOException e) {
                log.error("压缩第 {} 张图片失败: {}", i + 1, e.getMessage());
                try {
                    results[i] = files[i].getBytes();
                } catch (IOException ex) {
                    results[i] = new byte[0];
                }
            }
        }

        return results;
    }

    /**
     * 选择压缩策略
     */
    private static CompressionStrategy selectCompressionStrategy(long fileSize, String format) {
        // PNG文件特殊处理
        if ("png".equalsIgnoreCase(format)) {
            if (fileSize > 5 * 1024 * 1024) { // 5MB
                return CompressionStrategy.AGGRESSIVE;
            } else if (fileSize > 3 * 1024 * 1024) { // 3MB
                return CompressionStrategy.MODERATE;
            } else {
                return CompressionStrategy.LIGHT;
            }
        }

        // JPEG文件
        if (fileSize > 8 * 1024 * 1024) { // 8MB
            return CompressionStrategy.AGGRESSIVE;
        } else if (fileSize > 4 * 1024 * 1024) { // 4MB
            return CompressionStrategy.MODERATE;
        } else {
            return CompressionStrategy.LIGHT;
        }
    }

    /**
     * 根据策略压缩图片
     */
    private static byte[] compressWithStrategy(byte[] imageData, CompressionStrategy strategy, String format) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Thumbnails.Builder<?> builder = Thumbnails.of(inputStream);

            switch (strategy) {
                case LIGHT:
                    builder.size(DEFAULT_MAX_WIDTH, DEFAULT_MAX_HEIGHT)
                          .outputQuality(0.85);
                    break;

                case MODERATE:
                    builder.size(1600, 1200)
                          .outputQuality(0.75);
                    break;

                case AGGRESSIVE:
                    builder.size(1200, 900)
                          .outputQuality(0.65);
                    break;
            }

            builder.keepAspectRatio(true)
                   .outputFormat("jpg".equalsIgnoreCase(format) ? "jpg" : "jpg")
                   .toOutputStream(outputStream);

            byte[] compressed = outputStream.toByteArray();
            log.debug("图片压缩完成，策略: {}，原始: {} bytes，压缩后: {} bytes，压缩率: {:.2f}%",
                    strategy, imageData.length, compressed.length,
                    (1.0 - (double)compressed.length / imageData.length) * 100);

            return compressed;
        }
    }

    /**
     * 获取图片格式
     */
    private static String getImageFormat(MultipartFile file) throws IOException {
        try (InputStream input = file.getInputStream();
             ImageInputStream imageInput = ImageIO.createImageInputStream(input)) {

            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInput);
            if (!readers.hasNext()) {
                return null;
            }

            ImageReader reader = readers.next();
            return reader.getFormatName();
        }
    }

    /**
     * 压缩策略枚举
     */
    private enum CompressionStrategy {
        LIGHT,       // 轻度压缩
        MODERATE,    // 中度压缩
        AGGRESSIVE   // 重度压缩
    }

    /**
     * 检查文件是否为图片
     */
    public static boolean isImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    /**
     * 获取压缩后的图片信息
     */
    public static ImageInfo getImageInfo(MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            return null;
        }

        return new ImageInfo(
            image.getWidth(),
            image.getHeight(),
            file.getSize(),
            getImageFormat(file)
        );
    }

    /**
     * 图片信息类
     */
    public static class ImageInfo {
        private final int width;
        private final int height;
        private final long size;
        private final String format;

        public ImageInfo(int width, int height, long size, String format) {
            this.width = width;
            this.height = height;
            this.size = size;
            this.format = format;
        }

        public int getWidth() { return width; }
        public int getHeight() { return height; }
        public long getSize() { return size; }
        public String getFormat() { return format; }

        @Override
        public String toString() {
            return String.format("ImageInfo{width=%d, height=%d, size=%d, format='%s'}",
                    width, height, size, format);
        }
    }
}