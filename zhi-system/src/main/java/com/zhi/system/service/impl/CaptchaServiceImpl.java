package com.zhi.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhi.common.cache.UnifiedCacheManager;
import com.zhi.common.constant.CacheConstants;
import com.zhi.common.constant.Constants;
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
        validateAndGetAgeSeconds(code, uuid);
    }

    /**
     * 校验图形验证码并返回已存在秒数（未启用返回 -1）
     */
    @Override
    public long validateAndGetAgeSeconds(String code, String uuid)
    {
        if (!isCaptchaEnabled())
        {
            return -1L;
        }
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = unifiedCacheManager.get(verifyKey, String.class);
        // 先取剩余有效期，再删除（一次性使用，避免被暴力尝试）
        long remainSeconds = unifiedCacheManager.getExpire(verifyKey);
        unifiedCacheManager.delete(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (StringUtils.isEmpty(code) || !code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
        long totalSeconds = Constants.CAPTCHA_EXPIRATION * 60L;
        if (remainSeconds <= 0 || remainSeconds > totalSeconds)
        {
            // 键存在但读不到有效期（0/-1）或数值异常时不做“填写过快”判定，避免误判为已填写满额时长
            return -1L;
        }
        return totalSeconds - remainSeconds;
    }
}
