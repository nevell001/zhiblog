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

    /**
     * 校验图形验证码并返回该验证码已存在的时间（秒）
     * 
     * <p>用于匿名写入接口的“填写过快”判定：验证码由服务端签发并带固定有效期，
     * 剩余有效期与总有效期之差即用户真实的填写耗时，无法被前端伪造。
     * 校验失败时抛出与 {@link #validate} 相同的异常。</p>
     * 
     * @param code 用户输入的验证码
     * @param uuid 验证码唯一标识
     * @return 已存在秒数；未启用图形验证码时返回 {@code -1}（表示无法判定）
     */
    public long validateAndGetAgeSeconds(String code, String uuid);
}
