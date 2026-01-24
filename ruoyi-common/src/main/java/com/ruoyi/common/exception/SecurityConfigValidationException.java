package com.ruoyi.common.exception;

/**
 * 安全配置验证异常
 * 当检测到不安全的生产环境配置时抛出此异常
 *
 * @author ruoyi
 */
public class SecurityConfigValidationException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public SecurityConfigValidationException(String message)
    {
        super(message);
    }

    public SecurityConfigValidationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
