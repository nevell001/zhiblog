package com.ruoyi.framework.config;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import static com.google.code.kaptcha.Constants.*;

/**
 * 验证码配置
 * 
 * @author ruoyi
 */
@Configuration
public class CaptchaConfig
{
    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean()
    {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否有边框 默认为true 我们可以自己设置yes，no
        properties.setProperty(KAPTCHA_BORDER, "yes");
        // 边框颜色 使用浅灰色边框，不抢眼
        properties.setProperty(KAPTCHA_BORDER_COLOR, "200,200,200");
        // 验证码文本字符颜色 使用深蓝色提高可读性
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "0,51,153");
        // 验证码图片宽度 增加到180px，提供更多空间
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "180");
        // 验证码图片高度 增加到70px，提高可读性
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "70");
        // 验证码文本字符大小 增加到42px，更清晰
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "42");
        // 验证码文本字符间距 增加到5px，字符更清晰
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "5");
        // KAPTCHA_SESSION_KEY
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCode");
        // 验证码文本字符长度 增加到5位，提高安全性
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "5");
        // 验证码文本字体样式 使用更清晰的字体
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier New,Verdana");
        // 验证码背景颜色 使用浅色背景，提高对比度
        properties.setProperty(KAPTCHA_BACKGROUND_CLR_FROM, "245,245,245");
        properties.setProperty(KAPTCHA_BACKGROUND_CLR_TO, "245,245,245");
        // 验证码噪点颜色 使用浅灰色，适度干扰
        properties.setProperty(KAPTCHA_NOISE_COLOR, "220,220,220");
        // 干扰实现类 使用默认噪点，增加OCR识别难度
        properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.DefaultNoise");
        // 图片样式 使用水纹效果，比阴影效果更难被OCR识别
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.WaterRipple");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha getKaptchaBeanMath()
    {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否有边框 默认为true 我们可以自己设置yes，no
        properties.setProperty(KAPTCHA_BORDER, "yes");
        // 边框颜色 使用浅灰色边框，不抢眼
        properties.setProperty(KAPTCHA_BORDER_COLOR, "200,200,200");
        // 验证码文本字符颜色 使用深红色提高可读性，与绿色边框形成对比
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "139,0,0");
        // 验证码图片宽度 增加到200px，提供更多空间
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "200");
        // 验证码图片高度 增加到70px，提高可读性
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "70");
        // 验证码文本字符大小 增加到40px，更清晰
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "40");
        // KAPTCHA_SESSION_KEY
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCodeMath");
        // 验证码文本生成器
        properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, "com.ruoyi.framework.config.KaptchaTextCreator");
        // 验证码文本字符间距 增加到6px，字符更清晰
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "6");
        // 验证码文本字符长度 默认为5
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "6");
        // 验证码文本字体样式 使用更清晰的字体
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier New,Verdana");
        // 验证码背景颜色 使用浅色背景，提高对比度
        properties.setProperty(KAPTCHA_BACKGROUND_CLR_FROM, "245,245,245");
        properties.setProperty(KAPTCHA_BACKGROUND_CLR_TO, "245,245,245");
        // 验证码噪点颜色 使用浅灰色，适度干扰
        properties.setProperty(KAPTCHA_NOISE_COLOR, "220,220,220");
        // 干扰实现类 使用默认噪点，增加OCR识别难度
        properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.DefaultNoise");
        // 图片样式 使用水纹效果，比阴影效果更难被OCR识别
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.WaterRipple");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
