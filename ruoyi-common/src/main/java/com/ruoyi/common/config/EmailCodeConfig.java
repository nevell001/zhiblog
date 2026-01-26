package com.ruoyi.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邮件验证码配置类
 *
 * @author nevell
 * @date 2025-01-26
 */
@Component
@ConfigurationProperties(prefix = "email-code")
public class EmailCodeConfig
{
    /** 验证码有效期（分钟） */
    private int expireMinutes = 5;

    /** 验证码长度 */
    private int codeLength = 6;

    /** 邮箱发送频率限制（秒） */
    private int rateLimitSeconds = 60;

    /** IP频率限制（每小时最大发送次数） */
    private int ipRateLimit = 10;

    /** 开发环境是否在控制台打印验证码 */
    private boolean devPrintCode = true;

    public int getExpireMinutes()
    {
        return expireMinutes;
    }

    public void setExpireMinutes(int expireMinutes)
    {
        this.expireMinutes = expireMinutes;
    }

    public int getCodeLength()
    {
        return codeLength;
    }

    public void setCodeLength(int codeLength)
    {
        this.codeLength = codeLength;
    }

    public int getRateLimitSeconds()
    {
        return rateLimitSeconds;
    }

    public void setRateLimitSeconds(int rateLimitSeconds)
    {
        this.rateLimitSeconds = rateLimitSeconds;
    }

    public int getIpRateLimit()
    {
        return ipRateLimit;
    }

    public void setIpRateLimit(int ipRateLimit)
    {
        this.ipRateLimit = ipRateLimit;
    }

    public boolean isDevPrintCode()
    {
        return devPrintCode;
    }

    public void setDevPrintCode(boolean devPrintCode)
    {
        this.devPrintCode = devPrintCode;
    }
}
