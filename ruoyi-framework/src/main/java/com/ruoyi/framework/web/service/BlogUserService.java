package com.ruoyi.framework.web.service;

import com.ruoyi.common.cache.UnifiedCacheManager;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.BlogRegisterBody;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.service.IBlogEmailService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 博客用户服务
 * 用于博客前台用户注册登录
 *
 * @author nevell
 * @date 2025-01-26
 */
@Service
public class BlogUserService
{
    private static final Logger log = LoggerFactory.getLogger(BlogUserService.class);

    /** 博客用户类型标识 */
    private static final String USER_TYPE_BLOG = "01";

    /** 博客用户角色Key */
    private static final String BLOG_USER_ROLE_KEY = "blog_user";

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private UnifiedCacheManager unifiedCacheManager;

    @Autowired
    private IBlogEmailService emailService;

    @Autowired
    private SysRoleMapper roleMapper;

    /**
     * 注册博客用户
     *
     * @param registerBody 注册信息
     * @return 结果
     */
    @Transactional
    public AjaxResult registerBlogUser(BlogRegisterBody registerBody)
    {
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();
        String email = registerBody.getEmail();
        String emailCode = registerBody.getEmailCode();

        // 1. 校验图形验证码
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        // 2. 校验邮箱验证码
        if (!emailService.verifyCode(email, emailCode, IBlogEmailService.CODE_TYPE_REGISTER))
        {
            return AjaxResult.error("邮箱验证码错误或已过期");
        }

        // 3. 校验用户名和邮箱唯一性
        SysUser checkUser = new SysUser();
        checkUser.setUserName(username);
        if (!userService.checkUserNameUnique(checkUser))
        {
            return AjaxResult.error("用户名已存在");
        }

        if (!checkEmailUnique(email))
        {
            return AjaxResult.error("邮箱已被使用");
        }

        // 4. 校验密码一致性
        if (!password.equals(registerBody.getConfirmPassword()))
        {
            return AjaxResult.error("两次输入的密码不一致");
        }

        // 5. 创建博客用户
        SysUser blogUser = new SysUser();
        blogUser.setUserName(username);
        blogUser.setNickName(username);
        blogUser.setEmail(email);
        blogUser.setPassword(SecurityUtils.encryptPassword(password));
        blogUser.setStatus("0"); // 正常状态
        blogUser.setPwdUpdateDate(DateUtils.getNowDate());
        blogUser.setUserType(USER_TYPE_BLOG); // 设置为博客用户类型

        try
        {
            // 6. 保存用户
            boolean regFlag = userService.registerUser(blogUser);
            if (!regFlag)
            {
                return AjaxResult.error("注册失败，请联系系统管理人员");
            }

            // 7. 分配博客用户角色（关键！）
            SysRole blogRole = roleMapper.checkRoleKeyUnique(BLOG_USER_ROLE_KEY);
            if (blogRole == null)
            {
                log.error("博客用户角色不存在：role_key={}", BLOG_USER_ROLE_KEY);
                // 角色不存在时仍然注册成功，但不分配角色
            }
            else
            {
                userService.insertUserAuth(blogUser.getUserId(), new Long[]{blogRole.getRoleId()});
                log.info("博客用户注册成功并分配角色：username={}, roleId={}", username, blogRole.getRoleId());
            }

            // 8. 记录注册日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER,
                    MessageUtils.message("user.register.success")));

            return AjaxResult.success("注册成功");
        }
        catch (Exception e)
        {
            log.error("博客用户注册失败：username={}, error={}", username, e.getMessage());
            return AjaxResult.error("注册失败：" + e.getMessage());
        }
    }

    /**
     * 校验邮箱是否唯一
     *
     * @param email 邮箱地址
     * @return true=唯一, false=不唯一
     */
    public boolean checkEmailUnique(String email)
    {
        if (StringUtils.isEmpty(email))
        {
            return false;
        }

        SysUser user = new SysUser();
        user.setEmail(email);
        return userService.checkEmailUnique(user);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     */
    private void validateCaptcha(String username, String code, String uuid)
    {
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(uuid))
        {
            throw new CaptchaException();
        }

        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        String captcha = unifiedCacheManager.get(verifyKey, String.class);
        unifiedCacheManager.delete(verifyKey);

        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }

        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }
}
