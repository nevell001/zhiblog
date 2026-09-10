package com.zhi.system.service;

/**
 * 图形验证码校验Service接口
 * 
 * <p>验证码是否启用遵循与登录/注册一致的规则：优先取环境变量 CAPTCHA_ENABLED，
 * 否则取 sys_config 中的开关。未启用时校验方法直接放行，便于本地开发。</p>
 * 
 * @author nevell
 * @date 2026-09-10
 */
public interface ICaptchaService 
{
    /**
     * 当前是否启用图形验证码
     * 
     * @return true 启用
     */
    public boolean isCaptchaEnabled();

    /**
     * 校验图形验证码（启用时校验，未启用直接通过）
     * 
     * @param code 用户输入的验证码
     * @param uuid 验证码唯一标识
     */
    public void validate(String code, String uuid);
}
