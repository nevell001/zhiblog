package com.ruoyi.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 图片压缩配置类
 *
 * @author nevell
 * @since 2025-12-17
 */
@Component
@ConfigurationProperties(prefix = "image.compress")
public class ImageCompressConfig {

    /**
     * 是否启用图片压缩
     */
    private boolean enabled = true;

    /**
     * 压缩阈值（超过此大小才压缩）
     */
    private String thresholdSize = "2MB";

    /**
     * 默认最大宽度
     */
    private int maxWidth = 1920;

    /**
     * 默认最大高度
     */
    private int maxHeight = 1080;

    /**
     * 默认压缩质量 (0.0-1.0)
     */
    private double defaultQuality = 0.85;

    /**
     * 头像压缩尺寸
     */
    private int avatarSize = 200;

    /**
     * 缩略图压缩尺寸
     */
    private int thumbnailSize = 400;

    /**
     * 头像压缩质量
     */
    private double avatarQuality = 0.9;

    /**
     * 缩略图压缩质量
     */
    private double thumbnailQuality = 0.8;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getThresholdSize() {
        return thresholdSize;
    }

    public void setThresholdSize(String thresholdSize) {
        this.thresholdSize = thresholdSize;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public double getDefaultQuality() {
        return defaultQuality;
    }

    public void setDefaultQuality(double defaultQuality) {
        this.defaultQuality = defaultQuality;
    }

    public int getAvatarSize() {
        return avatarSize;
    }

    public void setAvatarSize(int avatarSize) {
        this.avatarSize = avatarSize;
    }

    public int getThumbnailSize() {
        return thumbnailSize;
    }

    public void setThumbnailSize(int thumbnailSize) {
        this.thumbnailSize = thumbnailSize;
    }

    public double getAvatarQuality() {
        return avatarQuality;
    }

    public void setAvatarQuality(double avatarQuality) {
        this.avatarQuality = avatarQuality;
    }

    public double getThumbnailQuality() {
        return thumbnailQuality;
    }

    public void setThumbnailQuality(double thumbnailQuality) {
        this.thumbnailQuality = thumbnailQuality;
    }

    /**
     * 获取压缩阈值字节数
     */
    public long getThresholdSizeBytes() {
        return parseSize(thresholdSize);
    }

    /**
     * 解析大小字符串为字节数
     * 支持：KB, MB, GB
     */
    private long parseSize(String size) {
        try {
            String upperSize = size.toUpperCase().trim();
            if (upperSize.endsWith("KB")) {
                return Long.parseLong(upperSize.substring(0, upperSize.length() - 2)) * 1024;
            } else if (upperSize.endsWith("MB")) {
                return Long.parseLong(upperSize.substring(0, upperSize.length() - 2)) * 1024 * 1024;
            } else if (upperSize.endsWith("GB")) {
                return Long.parseLong(upperSize.substring(0, upperSize.length() - 2)) * 1024 * 1024 * 1024;
            } else {
                // 默认为字节
                return Long.parseLong(upperSize);
            }
        } catch (NumberFormatException e) {
            // 解析失败，返回默认值2MB
            return 2 * 1024 * 1024L;
        }
    }

    @Override
    public String toString() {
        return "ImageCompressConfig{" +
                "enabled=" + enabled +
                ", thresholdSize='" + thresholdSize + '\'' +
                ", maxWidth=" + maxWidth +
                ", maxHeight=" + maxHeight +
                ", defaultQuality=" + defaultQuality +
                ", avatarSize=" + avatarSize +
                ", thumbnailSize=" + thumbnailSize +
                ", avatarQuality=" + avatarQuality +
                ", thumbnailQuality=" + thumbnailQuality +
                '}';
    }
}