package com.zhi.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhi.common.cache.UnifiedCacheManager;
import com.zhi.common.constant.CacheConstants;
import com.zhi.common.exception.user.CaptchaException;
import com.zhi.common.exception.user.CaptchaExpireException;
import com.zhi.common.utils.StringUtils;
import com.zhi.system.service.ICaptchaService;
import com.zhi.system.service.ISysConfigService;

/**
 * 图形验证码校验Service业务层处理
 * 
 * @author nevell
 * @date 2026-09-10
 */
@Service
public class CaptchaServiceImpl implements ICaptchaService
{
    @Autowired
    private UnifiedCacheManager unifiedCacheManager;

    @Autowired
    private ISysConfigService configService;

    /**
     * 当前是否启用图形验证码（环境变量优先）
     */
    @Override
    public boolean isCaptchaEnabled()
    {
        String envCaptcha = System.getenv("CAPTCHA_ENABLED");
        if (envCaptcha != null)
        {
            return Boolean.parseBoolean(envCaptcha);
        }
        return configService.selectCaptchaEnabled();
    }

    /**
     * 校验图形验证码（启用时校验，未启用直接通过）
     */
    @Override
    public void validate(String code, String uuid)
    {
        if (!isCaptchaEnabled())
        {
            return;
        }
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = unifiedCacheManager.get(verifyKey, String.class);
        // 一次性使用：无论成功与否都删除，避免被暴力尝试
        unifiedCacheManager.delete(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (StringUtils.isEmpty(code) || !code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }
}
