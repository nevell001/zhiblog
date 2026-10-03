package com.zhi.framework.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.zhi.common.exception.SecurityConfigValidationException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * 启动期安全配置校验单元测试。
 *
 * <p>这个类决定了"生产环境缺凭据就直接拒绝启动"还是"只告警放行"，是防止弱配置上线的最后一道闸；
 * 开发环境必须只告警，否则本地无法起服务。</p>
 */
class SecurityConfigValidatorTest
{
    /** 64 个字符的合法 JWT 密钥 */
    private static final String STRONG_SECRET = repeat('k', 64);

    private static String repeat(char c, int n)
    {
        return String.valueOf(c).repeat(n);
    }

    private SecurityConfigValidator validator(String profile, String tokenSecret, String druidPassword,
            String redisPassword, String dbPassword)
    {
        SecurityConfigValidator validator = new SecurityConfigValidator();
        ReflectionTestUtils.setField(validator, "activeProfile", profile);
        ReflectionTestUtils.setField(validator, "tokenSecret", tokenSecret);
        ReflectionTestUtils.setField(validator, "druidUsername", "admin");
        ReflectionTestUtils.setField(validator, "druidPassword", druidPassword);
        ReflectionTestUtils.setField(validator, "redisPassword", redisPassword);
        ReflectionTestUtils.setField(validator, "dbPassword", dbPassword);
        ReflectionTestUtils.setField(validator, "validationEnabled", true);
        return validator;
    }

    private SecurityConfigValidator productionDefaults()
    {
        return validator("prod", STRONG_SECRET, "druid-secret", "redis-secret", "db-secret-strong");
    }

    @Test
    void productionWithCompleteCredentialsStarts()
    {
        assertThatCode(productionDefaults()::validateSecurityConfiguration).doesNotThrowAnyException();
    }

    @Test
    void productionProfileIsMatchedCaseInsensitively()
    {
        SecurityConfigValidator validator = validator("Production", "", "", "", "");

        assertThatExceptionOfType(SecurityConfigValidationException.class)
                .isThrownBy(validator::validateSecurityConfiguration);
    }

    @Test
    void productionWithoutAnyCredentialBlocksStartup()
    {
        SecurityConfigValidator validator = validator("prod", "", "", "", "");

        assertThatExceptionOfType(SecurityConfigValidationException.class)
                .isThrownBy(validator::validateSecurityConfiguration)
                .withMessageContaining("生产环境安全配置验证失败");
    }

    @Test
    void developmentOnlyWarnsSoLocalStartupStillWorks()
    {
        SecurityConfigValidator validator = validator("dev", "", "", "", "");

        assertThatCode(validator::validateSecurityConfiguration).doesNotThrowAnyException();
    }

    @Test
    void blankProfileFallsBackToDevelopmentSemantics()
    {
        SecurityConfigValidator validator = validator("", "", "", "", "");

        assertThatCode(validator::validateSecurityConfiguration).doesNotThrowAnyException();
    }

    @Test
    void whitespaceOnlySecretCountsAsMissing()
    {
        SecurityConfigValidator validator = validator("prod", "   ", "druid-secret", "redis-secret",
                "db-secret-strong");

        assertThatExceptionOfType(SecurityConfigValidationException.class)
                .isThrownBy(validator::validateSecurityConfiguration);
    }

    @Test
    void unresolvedPlaceholderIsTreatedAsMissingSecret()
    {
        SecurityConfigValidator validator = validator("prod", "#{null}", "druid-secret", "redis-secret",
                "db-secret-strong");

        assertThatExceptionOfType(SecurityConfigValidationException.class)
                .isThrownBy(validator::validateSecurityConfiguration);
    }

    @Test
    void shortJwtSecretIsAcceptedInDevButRejectedInProduction()
    {
        String weak = repeat('s', 32);

        assertThatCode(validator("dev", weak, "druid-secret", "redis-secret", "db-secret-strong")
                ::validateSecurityConfiguration).doesNotThrowAnyException();
        assertThatExceptionOfType(SecurityConfigValidationException.class)
                .isThrownBy(validator("prod", weak, "druid-secret", "redis-secret", "db-secret-strong")
                        ::validateSecurityConfiguration);
    }

    @Test
    void missingDruidOrRedisPasswordBlocksProductionIndependently()
    {
        assertThatExceptionOfType(SecurityConfigValidationException.class).isThrownBy(
                validator("prod", STRONG_SECRET, "", "redis-secret", "db-secret-strong")
                        ::validateSecurityConfiguration);
        assertThatExceptionOfType(SecurityConfigValidationException.class).isThrownBy(
                validator("prod", STRONG_SECRET, "druid-secret", "", "db-secret-strong")
                        ::validateSecurityConfiguration);
    }

    @Test
    void weakDatabasePasswordBlocksProductionOnly()
    {
        for (String weak : new String[] { "root", "Password", "123456", "ADMIN", "12345678", "abc" })
        {
            assertThatExceptionOfType(SecurityConfigValidationException.class).as("password=%s", weak)
                    .isThrownBy(validator("prod", STRONG_SECRET, "druid-secret", "redis-secret", weak)
                            ::validateSecurityConfiguration);
        }
        // 同一个弱密码在开发环境不拦启动：本地库常用 root
        assertThatCode(validator("dev", STRONG_SECRET, "druid-secret", "redis-secret", "root")
                ::validateSecurityConfiguration).doesNotThrowAnyException();
    }

    @Test
    void validationCanBeExplicitlyDisabledForEmergencyStartup()
    {
        SecurityConfigValidator validator = validator("prod", "", "", "", "");
        ReflectionTestUtils.setField(validator, "validationEnabled", false);

        assertThatCode(validator::validateSecurityConfiguration).doesNotThrowAnyException();
    }

    @Test
    void failureMessageNeverContainsTheConfiguredSecrets()
    {
        String dbPassword = "Sup3r-Secret-Db-Pw";
        SecurityConfigValidator validator = validator("prod", "", "druid-secret", "redis-secret", dbPassword);

        assertThatExceptionOfType(SecurityConfigValidationException.class)
                .isThrownBy(validator::validateSecurityConfiguration)
                .withMessageNotContaining(dbPassword)
                .withMessageNotContaining("druid-secret")
                .withMessageNotContaining("redis-secret");
    }
}
