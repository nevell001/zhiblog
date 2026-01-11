package com.ruoyi.common.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.image.ImageCompressUtils;

/**
 * 图片压缩配置初始化器
 * 用于将配置注入到工具类中
 *
 * @author nevell
 * @since 2025-12-17
 */
@Component
public class ImageCompressInitializer {

    private static final Logger log = LoggerFactory.getLogger(ImageCompressInitializer.class);

    @Autowired
    private ImageCompressConfig imageCompressConfig;

    @PostConstruct
    public void init() {
        try {
            log.info("开始初始化图片压缩配置...");
            log.info("ImageCompressConfig实例: {}", imageCompressConfig);

            if (imageCompressConfig == null) {
                log.error("ImageCompressConfig 注入失败，为null");
                return;
            }

            ImageCompressUtils.setConfig(imageCompressConfig);

            // 验证设置是否成功
            ImageCompressConfig config = ImageCompressUtils.getConfig();
            log.info("图片压缩配置初始化完成，设置后的配置: {}", config);
            log.info("压缩启用状态: {}, 阈值: {}, 头像尺寸: {}",
                config.isEnabled(), config.getThresholdSize(), config.getAvatarSize());

        } catch (Exception e) {
            log.error("图片压缩配置初始化失败", e);
        }
    }
}