package com.ruoyi.framework.config;

import com.ruoyi.common.exception.SecurityConfigValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * 安全配置验证器
 * 在应用启动时验证关键安全配置，防止使用不安全的默认值
 *
 * @author ruoyi
 */
@Configuration
public class SecurityConfigValidator
{
    private static final Logger log = LoggerFactory.getLogger(SecurityConfigValidator.class);

    /**
     * 生成密钥命令常量
     */
    private static final String GENERATE_CMD_64 = "   生成命令：openssl rand -base64 64";
    private static final String GENERATE_CMD_32 = "   生成命令：openssl rand -base64 32";

    /**
     * JWT密钥
     */
    @Value("${token.secret:}")
    private String tokenSecret;

    /**
     * Druid监控用户名
     */
    @Value("${spring.datasource.druid.statViewServlet.login-username:}")
    private String druidUsername;

    /**
     * Druid监控密码
     */
    @Value("${spring.datasource.druid.statViewServlet.login-password:}")
    private String druidPassword;

    /**
     * Redis密码
     */
    @Value("${spring.redis.password:}")
    private String redisPassword;

    /**
     * 数据库密码
     */
    @Value("${spring.datasource.druid.master.password:}")
    private String dbPassword;

    /**
     * 当前激活的配置环境
     */
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    /**
     * 是否启用安全验证（可通过环境变量 SECURITY_VALIDATION_ENABLED=false 禁用）
     */
    @Value("${security.validation.enabled:true}")
    private boolean validationEnabled;

    /**
     * 在Bean初始化后执行安全配置验证
     */
    @PostConstruct
    public void validateSecurityConfiguration()
    {
        if (!validationEnabled)
        {
            log.warn("⚠️  安全配置验证已禁用。不建议在生产环境中禁用此功能！");
            return;
        }

        boolean isProduction = isProductionEnvironment();
        log.info("🔒 开始验证安全配置...");
        log.info("📍 当前环境: {}", activeProfile);

        boolean hasErrors = false;
        hasErrors |= validateTokenSecret(isProduction);
        hasErrors |= validateDruidPassword(isProduction);
        hasErrors |= validateRedisPassword(isProduction);
        hasErrors |= validateDatabasePassword(isProduction);

        log.info("🔒 安全配置验证完成");

        if (isProduction && hasErrors)
        {
            String errorMsg = "生产环境安全配置验证失败！应用启动已阻止。请检查上述错误并设置正确的环境变量。";
            log.error("🛑 {}", errorMsg);
            throw new SecurityConfigValidationException(errorMsg);
        }
    }

    /**
     * 验证JWT密钥
     */
    private boolean validateTokenSecret(boolean isProduction)
    {
        if (tokenSecret == null || tokenSecret.trim().isEmpty() || "#{null}".equals(tokenSecret))
        {
            if (isProduction)
            {
                log.error("❌ 安全错误：JWT密钥未设置！");
                log.error("   解决方法：设置环境变量 R_TOKEN_SECRET");
                log.error(GENERATE_CMD_64);
                return true;
            }
            else
            {
                log.warn("⚠️  JWT密钥未设置（开发环境）");
                log.warn("   建议设置环境变量 R_TOKEN_SECRET");
                log.warn(GENERATE_CMD_64);
                return false;
            }
        }
        else if (tokenSecret.length() < 64)
        {
            log.warn("⚠️  JWT密钥长度不足（当前：{} 字符，建议至少 64 字符）", tokenSecret.length());
            return isProduction;
        }
        else
        {
            log.info("✅ JWT密钥：已设置且长度符合要求（{} 字符）", tokenSecret.length());
            return false;
        }
    }

    /**
     * 验证Druid监控密码
     */
    private boolean validateDruidPassword(boolean isProduction)
    {
        if (druidPassword == null || druidPassword.trim().isEmpty())
        {
            if (isProduction)
            {
                log.error("❌ 安全错误：Druid监控密码未设置！");
                log.error("   解决方法：设置环境变量 DRUID_PASSWORD");
                log.error(GENERATE_CMD_32);
                return true;
            }
            else
            {
                log.warn("⚠️  Druid监控密码未设置（开发环境）");
                log.warn("   建议设置环境变量 DRUID_PASSWORD");
                log.warn(GENERATE_CMD_32);
                return false;
            }
        }
        else
        {
            log.info("✅ Druid监控密码：已设置");
            return false;
        }
    }

    /**
     * 验证Redis密码
     */
    private boolean validateRedisPassword(boolean isProduction)
    {
        if (redisPassword == null || redisPassword.trim().isEmpty())
        {
            if (isProduction)
            {
                log.error("❌ 安全错误：Redis密码未设置！");
                log.error("   解决方法：设置环境变量 REDIS_PASSWORD");
                log.error(GENERATE_CMD_32);
                return true;
            }
            else
            {
                log.warn("⚠️  Redis密码未设置（开发环境）");
                log.warn("   建议设置环境变量 REDIS_PASSWORD");
                log.warn(GENERATE_CMD_32);
                return false;
            }
        }
        else
        {
            log.info("✅ Redis密码：已设置");
            return false;
        }
    }

    /**
     * 验证数据库密码
     */
    private boolean validateDatabasePassword(boolean isProduction)
    {
        if (dbPassword == null || dbPassword.trim().isEmpty())
        {
            log.error("❌ 数据库密码未设置！");
            log.error("   解决方法：设置环境变量 DB_PASSWORD");
            return true;
        }
        else if (isProduction && isInsecurePassword(dbPassword))
        {
            log.error("❌ 安全错误：数据库密码使用了不安全的默认值！");
            log.error("   检测到的密码：{}", maskPassword(dbPassword));
            log.error("   解决方法：设置强密码环境变量 DB_PASSWORD");
            return true;
        }
        else
        {
            log.info("✅ 数据库密码：已设置");
            return false;
        }
    }

    /**
     * 判断是否为生产环境
     */
    private boolean isProductionEnvironment()
    {
        return "prod".equalsIgnoreCase(activeProfile) || "production".equalsIgnoreCase(activeProfile);
    }

    /**
     * 检查是否为不安全的密码
     */
    private boolean isInsecurePassword(String password)
    {
        if (password == null || password.isEmpty())
        {
            return true;
        }

        // 检查常见的弱密码
        String[] weakPasswords = {"root", "password", "123456", "admin", "12345678"};
        for (String weak : weakPasswords)
        {
            if (weak.equalsIgnoreCase(password))
            {
                return true;
            }
        }

        // 检查密码长度
        return password.length() < 8;
    }

    /**
     * 掩码密码（用于日志输出）
     */
    private String maskPassword(String password)
    {
        if (password == null || password.isEmpty())
        {
            return "[empty]";
        }
        int length = password.length();
        if (length <= 2)
        {
            return "**";
        }
        return password.substring(0, 2) + "****" + password.substring(length - 1);
    }
}
