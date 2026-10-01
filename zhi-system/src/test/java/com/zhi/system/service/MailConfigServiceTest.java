package com.zhi.system.service;

import com.zhi.system.domain.MailConfigForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 邮件配置服务单元测试
 * 覆盖三类真实回归：mail_* 无种子行时写入必须是 upsert；SSL 与 STARTTLS 冲突时的生效口径；密码不回显。
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MailConfigServiceTest {

    @Mock
    private IBlogSettingService blogSettingService;

    private MailConfigService mailConfigService;

    private JavaMailSenderImpl sender;

    @BeforeEach
    void setUp() {
        mailConfigService = new MailConfigService();
        ReflectionTestUtils.setField(mailConfigService, "blogSettingService", blogSettingService);
        sender = new JavaMailSenderImpl();
        ReflectionTestUtils.setField(mailConfigService, "mailSender", sender);
    }

    private MailConfigForm form(Boolean ssl, Boolean starttls) {
        MailConfigForm form = new MailConfigForm();
        form.setHost("smtp.126.com");
        form.setPort(465);
        form.setUsername("someone@126.com");
        form.setPassword("auth-code");
        form.setSsl(ssl);
        form.setStarttls(starttls);
        form.setEnabled(true);
        return form;
    }

    @Test
    void savePersistsEveryFieldEvenWhenRowDoesNotExistYet() {
        when(blogSettingService.selectSettingValueByKey(anyString())).thenReturn(null);
        when(blogSettingService.updateSettingValueByKey(anyString(), anyString())).thenReturn(1);

        mailConfigService.save(form(true, false));

        verify(blogSettingService).updateSettingValueByKey(MailConfigService.KEY_HOST, "smtp.126.com");
        verify(blogSettingService).updateSettingValueByKey(MailConfigService.KEY_PORT, "465");
        verify(blogSettingService).updateSettingValueByKey(MailConfigService.KEY_USERNAME, "someone@126.com");
        verify(blogSettingService).updateSettingValueByKey(MailConfigService.KEY_PASSWORD, "auth-code");
        verify(blogSettingService).updateSettingValueByKey(MailConfigService.KEY_SSL, "true");
        verify(blogSettingService).updateSettingValueByKey(MailConfigService.KEY_STARTTLS, "false");
        verify(blogSettingService).updateSettingValueByKey(MailConfigService.KEY_ENABLED, "true");
    }

    @Test
    void saveKeepsStoredPasswordWhenFormSubmitsBlank() {
        MailConfigForm form = form(true, false);
        form.setPassword("");

        mailConfigService.save(form);

        verify(blogSettingService, org.mockito.Mockito.never())
            .updateSettingValueByKey(eq(MailConfigService.KEY_PASSWORD), anyString());
    }

    @Test
    void saveResolvesConflictingSwitchesInFavorOfSsl() {
        mailConfigService.save(form(true, true));

        verify(blogSettingService).updateSettingValueByKey(MailConfigService.KEY_SSL, "true");
        verify(blogSettingService).updateSettingValueByKey(MailConfigService.KEY_STARTTLS, "false");
    }

    @Test
    void appliedSenderNeverEnablesStarttlsTogetherWithSsl() {
        // 环境变量基线可能两者皆开（MAIL_SSL=true + 默认 MAIL_STARTTLS=true）
        sender.getJavaMailProperties().setProperty("mail.smtp.ssl.enable", "true");
        sender.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", "true");
        sender.setHost("smtp.126.com");
        sender.setPort(465);
        when(blogSettingService.selectSettingValueByKey(anyString())).thenReturn(null);

        mailConfigService.init();

        assertTrue(Boolean.parseBoolean(sender.getJavaMailProperties().getProperty("mail.smtp.ssl.enable")));
        assertFalse(Boolean.parseBoolean(sender.getJavaMailProperties().getProperty("mail.smtp.starttls.enable")));
    }

    @Test
    void maskedViewReportsResolvedStarttlsAndHidesPassword() {
        when(blogSettingService.selectSettingValueByKey(MailConfigService.KEY_PASSWORD)).thenReturn("auth-code");
        when(blogSettingService.selectSettingValueByKey(MailConfigService.KEY_SSL)).thenReturn("true");
        when(blogSettingService.selectSettingValueByKey(MailConfigService.KEY_STARTTLS)).thenReturn("true");
        when(blogSettingService.selectSettingValueByKey(MailConfigService.KEY_HOST)).thenReturn("smtp.126.com");
        when(blogSettingService.selectSettingValueByKey(MailConfigService.KEY_USERNAME)).thenReturn("someone@126.com");

        Map<String, Object> view = mailConfigService.getMaskedView();

        assertEquals(true, view.get("ssl"));
        assertEquals(false, view.get("starttls"));
        assertEquals(true, view.get("hasPassword"));
        assertNull(view.get("password"));
        assertFalse(view.containsValue("auth-code"));
    }
}
