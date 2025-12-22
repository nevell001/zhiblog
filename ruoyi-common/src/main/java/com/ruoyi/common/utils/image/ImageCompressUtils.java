package com.ruoyi.common.utils.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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

    // 使用volatile确保多线程环境下的可见性
    private static volatile ImageCompressConfig config;
    
    // 配置变更监听器列表
    private static final List<ConfigChangeListener> listeners = Collections.synchronizedList(new ArrayList<>());

    /**
     * 设置配置（用于Spring依赖注入）
     *
     * @param config 图片压缩配置
     */
    public static void setConfig(ImageCompressConfig config) {
        ImageCompressConfig oldConfig = ImageCompressUtils.config;
        ImageCompressUtils.config = config;
        
        // 触发配置变更事件
        notifyConfigChange(oldConfig, config);
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
     * 添加配置变更监听器
     *
     * @param listener 监听器
     */
    public static void addConfigChangeListener(ConfigChangeListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }
    
    /**
     * 移除配置变更监听器
     *
     * @param listener 监听器
     */
    public static void removeConfigChangeListener(ConfigChangeListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }
    
    /**
     * 通知配置变更
     *
     * @param oldConfig 旧配置
     * @param newConfig 新配置
     */
    private static void notifyConfigChange(ImageCompressConfig oldConfig, ImageCompressConfig newConfig) {
        if (listeners.isEmpty()) {
            return;
        }
        
        // 创建配置变更事件
        ConfigChangeEvent event = new ConfigChangeEvent(oldConfig, newConfig);
        
        // 通知所有监听器
        for (ConfigChangeListener listener : listeners) {
            try {
                listener.onConfigChange(event);
            } catch (Exception e) {
                log.error("配置变更监听器执行失败: {}", e.getMessage(), e);
            }
        }
    }
    
    /**
     * 配置变更事件
     */
    public static class ConfigChangeEvent {
        private final ImageCompressConfig oldConfig;
        private final ImageCompressConfig newConfig;
        
        public ConfigChangeEvent(ImageCompressConfig oldConfig, ImageCompressConfig newConfig) {
            this.oldConfig = oldConfig;
            this.newConfig = newConfig;
        }
        
        public ImageCompressConfig getOldConfig() {
            return oldConfig;
        }
        
        public ImageCompressConfig getNewConfig() {
            return newConfig;
        }
    }
    
    /**
     * 配置变更监听器
     */
    @FunctionalInterface
    public interface ConfigChangeListener {
        /**
         * 配置变更回调
         *
         * @param event 配置变更事件
         */
        void onConfigChange(ConfigChangeEvent event);
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
     * 文章封面图尺寸
     */
    private static final int ARTICLE_COVER_WIDTH = 800;
    private static final int ARTICLE_COVER_HEIGHT = 450;

    /**
     * 移动端适配尺寸
     */
    private static final int MOBILE_MAX_WIDTH = 750;

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
     * 压缩文章封面图
     * 生成适合文章列表和详情页的封面图
     *
     * @param file 源图片文件
     * @return 压缩后的封面图数据
     * @throws IOException
     */
    public static byte[] compressArticleCover(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }

        String format = getImageFormat(file);

        // 获取配置参数
        int coverWidth = (config != null && config.getArticleCoverWidth() > 0) ?
                        config.getArticleCoverWidth() : ARTICLE_COVER_WIDTH;
        int coverHeight = (config != null && config.getArticleCoverHeight() > 0) ?
                         config.getArticleCoverHeight() : ARTICLE_COVER_HEIGHT;
        double coverQuality = (config != null) ? config.getCoverQuality() : 0.85;

        log.debug("开始压缩文章封面: {}, 尺寸: {}x{}, 质量: {}",
                file.getOriginalFilename(), coverWidth, coverHeight, coverQuality);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .size(coverWidth, coverHeight)
                    .keepAspectRatio(true)
                    .outputQuality(coverQuality)
                    .outputFormat(format != null ? format : "jpg")
                    .toOutputStream(outputStream);

            byte[] compressed = outputStream.toByteArray();
            log.debug("文章封面压缩完成: {} -> {} bytes", file.getSize(), compressed.length);

            return compressed;
        }
    }

    /**
     * 移动端适配压缩
     * 为移动端设备优化图片尺寸和质量
     *
     * @param file 源图片文件
     * @return 压缩后的移动端适配图片数据
     * @throws IOException
     */
    public static byte[] compressForMobile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }

        String format = getImageFormat(file);

        // 获取移动端配置参数
        int mobileWidth = (config != null && config.getMobileMaxWidth() > 0) ?
                          config.getMobileMaxWidth() : MOBILE_MAX_WIDTH;
        double mobileQuality = (config != null) ? config.getMobileQuality() : 0.75;

        log.debug("开始移动端适配压缩: {}, 最大宽度: {}, 质量: {}",
                file.getOriginalFilename(), mobileWidth, mobileQuality);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .width(mobileWidth)
                    .keepAspectRatio(true)
                    .outputQuality(mobileQuality)
                    .outputFormat(format != null ? format : "jpg")
                    .toOutputStream(outputStream);

            byte[] compressed = outputStream.toByteArray();
            log.debug("移动端适配压缩完成: {} -> {} bytes", file.getSize(), compressed.length);

            return compressed;
        }
    }

    /**
     * 水印图片处理
     * 在图片上添加水印
     *
     * @param file 源图片文件
     * @param watermarkText 水印文字
     * @return 处理后的图片数据
     * @throws IOException
     */
    public static byte[] addWatermark(MultipartFile file, String watermarkText) throws IOException {
        return addWatermark(file, watermarkText, Positions.BOTTOM_RIGHT, 
                           "Arial", 16, java.awt.Color.WHITE, 
                           new java.awt.Color(0, 0, 0, 128), 0.5f);
    }

    /**
     * 水印图片处理（高级版）
     * 在图片上添加自定义水印
     *
     * @param file 源图片文件
     * @param watermarkText 水印文字
     * @param position 水印位置
     * @param fontName 字体名称
     * @param fontSize 字体大小
     * @param textColor 文字颜色
     * @param backgroundColor 背景颜色
     * @param opacity 透明度 (0.0-1.0)
     * @return 处理后的图片数据
     * @throws IOException
     */
    public static byte[] addWatermark(MultipartFile file, String watermarkText, Positions position, 
                                     String fontName, int fontSize, Color textColor, 
                                     Color backgroundColor, float opacity) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }

        if (StringUtils.isEmpty(watermarkText)) {
            return file.getBytes();
        }

        String format = getImageFormat(file);

        log.debug("开始添加水印: {}, 水印文字: {}", file.getOriginalFilename(), watermarkText);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .size(DEFAULT_MAX_WIDTH, DEFAULT_MAX_HEIGHT)
                    .keepAspectRatio(true)
                    .outputQuality(DEFAULT_QUALITY)
                    .watermark(position,
                               createWatermarkImage(watermarkText, fontName, fontSize, Font.BOLD, textColor, backgroundColor, 0),
                               opacity)
                    .outputFormat(format != null ? format : "jpg")
                    .toOutputStream(outputStream);

            return outputStream.toByteArray();
        }
    }

    /**
     * 创建水印图片
     *
     * @param text 水印文字
     * @return 水印图片
     */
    public static BufferedImage createWatermarkImage(String text) {
        return createWatermarkImage(text, "Arial", 16, Font.BOLD, 
                Color.WHITE, new Color(0, 0, 0, 128), 0);
    }

    /**
     * 创建自定义水印图片
     *
     * @param text 水印文字
     * @param fontName 字体名称
     * @param fontSize 字体大小
     * @param fontStyle 字体样式（Font.PLAIN, Font.BOLD, Font.ITALIC）
     * @param textColor 文字颜色
     * @param backgroundColor 背景颜色
     * @param rotation 旋转角度（0-360度）
     * @return 水印图片
     */
    public static BufferedImage createWatermarkImage(String text, String fontName, 
                                                                   int fontSize, int fontStyle, 
                                                                   Color textColor, 
                                                                   Color backgroundColor, 
                                                                   double rotation) {
        // 支持多行文本
        String[] lines = text.split("\\n");
        int lineCount = lines.length;
        
        // 创建临时图像来获取Graphics2D对象
        BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tempG2d = tempImage.createGraphics();
        
        // 获取FontRenderContext
        java.awt.font.FontRenderContext frc = tempG2d.getFontRenderContext();
        tempG2d.dispose();
        
        // 计算水印宽度和高度
        Font font = new Font(fontName, fontStyle, fontSize);
        TextLayout layout = new TextLayout(text, font, frc);
        
        // 计算最大行宽度
        int maxWidth = 0;
        for (String line : lines) {
            TextLayout lineLayout = new TextLayout(line, font, frc);
            maxWidth = Math.max(maxWidth, (int) lineLayout.getAdvance());
        }
        
        // 计算总高度（行高 + 行间距）
        int lineHeight = (int) layout.getBounds().getHeight();
        int lineSpacing = (int) (lineHeight * 0.5);
        int totalHeight = lineCount * lineHeight + (lineCount - 1) * lineSpacing;
        int width = maxWidth + 20; // 左右边距
        int height = totalHeight + 10; // 上下边距
        
        // 如果有旋转，需要计算旋转后的画布大小
        int canvasWidth = width;
        int canvasHeight = height;
        if (rotation != 0) {
            double radians = Math.toRadians(rotation);
            double sin = Math.abs(Math.sin(radians));
            double cos = Math.abs(Math.cos(radians));
            canvasWidth = (int) Math.ceil(width * cos + height * sin);
            canvasHeight = (int) Math.ceil(width * sin + height * cos);
        }
        
        BufferedImage image = new BufferedImage(canvasWidth, canvasHeight,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);
        
        // 设置旋转
        if (rotation != 0) {
            g2d.translate(canvasWidth / 2, canvasHeight / 2);
            g2d.rotate(Math.toRadians(rotation));
            g2d.translate(-width / 2, -height / 2);
        }

        // 绘制背景
        if (backgroundColor != null) {
            g2d.setColor(backgroundColor);
            g2d.fillRect(0, 0, width, height);
        }

        // 设置文字颜色和字体
        g2d.setColor(textColor);
        g2d.setFont(font);

        // 绘制多行文字
        int y = lineHeight;
        for (String line : lines) {
            TextLayout lineLayout = new TextLayout(line, font, frc);
            float x = (width - (float) lineLayout.getAdvance()) / 2;
            g2d.drawString(line, x, y);
            y += lineHeight + lineSpacing;
        }
        
        g2d.dispose();
        return image;
    }

    /**
     * 图片格式转换
     * 将图片转换为指定格式
     *
     * @param file 源图片文件
     * @param targetFormat 目标格式 (jpg, png, webp)
     * @return 转换后的图片数据
     * @throws IOException
     */
    public static byte[] convertFormat(MultipartFile file, String targetFormat) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }

        if (StringUtils.isEmpty(targetFormat)) {
            return file.getBytes();
        }

        log.debug("开始格式转换: {} -> {}", file.getOriginalFilename(), targetFormat);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .scale(1.0)
                    .outputFormat(targetFormat.toLowerCase())
                    .toOutputStream(outputStream);

            byte[] converted = outputStream.toByteArray();
            log.debug("格式转换完成: {} -> {} bytes", targetFormat, converted.length);

            return converted;
        }
    }

    /**
     * 批量压缩
     *
     * @param files 源图片文件数组
     * @return 压缩后的图片数据数组
     */
    public static byte[][] batchCompress(MultipartFile[] files) {
        return batchCompress(files, Runtime.getRuntime().availableProcessors());
    }

    /**
     * 批量压缩（支持自定义线程数）
     *
     * @param files 源图片文件数组
     * @param threadCount 线程数
     * @return 压缩后的图片数据数组
     */
    public static byte[][] batchCompress(MultipartFile[] files, int threadCount) {
        if (files == null || files.length == 0) {
            return new byte[0][];
        }

        if (threadCount <= 0) {
            threadCount = Runtime.getRuntime().availableProcessors();
        }

        byte[][] results = new byte[files.length][];

        // 单线程处理
        if (files.length <= 1 || threadCount == 1) {
            for (int i = 0; i < files.length; i++) {
                results[i] = processSingleFile(files[i], i);
            }
            return results;
        }

        // 并行处理
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(threadCount, files.length));
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            final int index = i;
            Future<?> future = executor.submit(() -> {
                results[index] = processSingleFile(files[index], index);
            });
            futures.add(future);
        }

        // 等待所有任务完成
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("批量压缩任务执行失败: {}", e.getMessage(), e);
            }
        }

        executor.shutdown();
        return results;
    }

    /**
     * 处理单个文件
     *
     * @param file 源文件
     * @param index 文件索引
     * @return 压缩后的文件数据
     */
    private static byte[] processSingleFile(MultipartFile file, int index) {
        try {
            return smartCompress(file);
        } catch (IOException e) {
            log.error("压缩第 {} 张图片失败: {}", index + 1, e.getMessage());
            try {
                return file.getBytes();
            } catch (IOException ex) {
                return new byte[0];
            }
        }
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
                   .outputFormat(format != null ? format : "png")
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
    public static String getImageFormat(MultipartFile file) throws IOException {
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
     * 图片裁剪功能
     * 
     * @param file 源图片文件
     * @param x 裁剪起始X坐标
     * @param y 裁剪起始Y坐标
     * @param width 裁剪宽度
     * @param height 裁剪高度
     * @return 裁剪后的图片数据
     * @throws IOException
     */
    public static byte[] cropImage(MultipartFile file, int x, int y, int width, int height) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }

        if (width <= 0 || height <= 0) {
            throw new IOException("裁剪宽度和高度必须大于0");
        }

        String format = getImageFormat(file);

        log.debug("开始裁剪图片: {}, 裁剪区域: {}x{}+{}+{}", 
                 file.getOriginalFilename(), width, height, x, y);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .sourceRegion(x, y, width, height)
                    .size(width, height)
                    .keepAspectRatio(false)
                    .outputQuality(DEFAULT_QUALITY)
                    .outputFormat(format != null ? format : "jpg")
                    .toOutputStream(outputStream);

            return outputStream.toByteArray();
        }
    }

    /**
     * 生成缩略图
     * 
     * @param file 源图片文件
     * @param width 缩略图宽度
     * @param height 缩略图高度
     * @return 缩略图数据
     * @throws IOException
     */
    public static byte[] generateThumbnail(MultipartFile file, int width, int height) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }

        if (width <= 0 || height <= 0) {
            throw new IOException("缩略图宽度和高度必须大于0");
        }

        String format = getImageFormat(file);

        log.debug("开始生成缩略图: {}, 尺寸: {}x{}", file.getOriginalFilename(), width, height);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .size(width, height)
                    .keepAspectRatio(true)
                    .outputQuality(0.9f)
                    .outputFormat(format != null ? format : "jpg")
                    .toOutputStream(outputStream);

            return outputStream.toByteArray();
        }
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