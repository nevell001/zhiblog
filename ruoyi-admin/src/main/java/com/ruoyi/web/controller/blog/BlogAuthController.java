package com.ruoyi.web.controller.blog;

import java.util.Map;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.BlogRegisterBody;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.web.service.BlogUserService;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.IBlogEmailService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 博客认证控制器
 * 处理博客前台用户注册、登录、发送验证码等
 *
 * @author nevell
 * @date 2025-01-26
 */
@RestController
@RequestMapping("/blog/auth")
public class BlogAuthController extends BaseController
{
    @Autowired
    private BlogUserService blogUserService;

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private IBlogEmailService emailService;

    @Autowired
    private com.ruoyi.framework.web.service.TokenService tokenService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysUserMapper userMapper;

    /**
     * 博客用户登录
     * 支持用户名或邮箱登录，仅限博客用户（user_type='01'）
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            return AjaxResult.error("用户名和密码不能为空");
        }

        try
        {
            // 调用登录服务，返回token
            String token = loginService.login(username, password, loginBody.getCode(), loginBody.getUuid());
            return AjaxResult.success("登录成功", token);
        }
        catch (Exception e)
        {
            logger.error("博客用户登录失败：username={}, error={}", username, e.getMessage());
            return AjaxResult.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 博客用户注册
     */
    @PostMapping("/register")
    public AjaxResult register(@RequestBody BlogRegisterBody registerBody, HttpServletRequest request)
    {
        String ipAddress = IpUtils.getIpAddr(request);
        logger.info("博客用户注册请求：username={}, email={}, ip={}",
                registerBody.getUsername(), registerBody.getEmail(), ipAddress);

        return blogUserService.registerBlogUser(registerBody);
    }

    /**
     * 发送注册验证码
     */
    @PostMapping("/send-register-code")
    public AjaxResult sendRegisterCode(@RequestBody Map<String, String> params, HttpServletRequest request)
    {
        String email = params.get("email");
        if (StringUtils.isEmpty(email))
        {
            return AjaxResult.error("邮箱不能为空");
        }

        // 检查邮箱是否已被使用
        if (!blogUserService.checkEmailUnique(email))
        {
            return AjaxResult.error("该邮箱已被注册");
        }

        String ipAddress = IpUtils.getIpAddr(request);

        boolean success = emailService.sendRegisterCode(email, ipAddress);

        if (success)
        {
            return AjaxResult.success("验证码发送成功，请查收邮箱");
        }
        else
        {
            return AjaxResult.error("验证码发送失败，请稍后重试");
        }
    }

    /**
     * 发送密码重置验证码
     */
    @PostMapping("/send-reset-code")
    public AjaxResult sendResetCode(@RequestBody Map<String, String> params, HttpServletRequest request)
    {
        String email = params.get("email");
        if (StringUtils.isEmpty(email))
        {
            return AjaxResult.error("邮箱不能为空");
        }

        String ipAddress = IpUtils.getIpAddr(request);

        boolean success = emailService.sendResetCode(email, ipAddress);

        if (success)
        {
            return AjaxResult.success("验证码发送成功，请查收邮箱");
        }
        else
        {
            return AjaxResult.error("验证码发送失败，请稍后重试");
        }
    }

    /**
     * 重置密码
     */
    @PostMapping("/reset-password")
    public AjaxResult resetPassword(@RequestBody Map<String, String> params)
    {
        String email = params.get("email");
        String code = params.get("code");
        String newPassword = params.get("newPassword");
        String confirmPassword = params.get("confirmPassword");

        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(code) ||
                StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(confirmPassword))
        {
            return AjaxResult.error("参数不能为空");
        }

        if (!newPassword.equals(confirmPassword))
        {
            return AjaxResult.error("两次输入的密码不一致");
        }

        // 验证邮箱验证码
        if (!emailService.verifyCode(email, code, IBlogEmailService.CODE_TYPE_RESET))
        {
            return AjaxResult.error("验证码错误或已过期");
        }

        try
        {
            // 根据邮箱查询用户（使用checkEmailUnique方法）
            SysUser existUser = userMapper.checkEmailUnique(email);

            if (existUser == null)
            {
                return AjaxResult.error("该邮箱未注册");
            }

            // 重置密码
            String encryptedPassword = SecurityUtils.encryptPassword(newPassword);
            userService.resetUserPwd(existUser.getUserId(), encryptedPassword);

            logger.info("密码重置成功：email={}, userId={}", email, existUser.getUserId());
            return AjaxResult.success("密码重置成功，请使用新密码登录");
        }
        catch (Exception e)
        {
            logger.error("密码重置失败：email={}, error={}", email, e.getMessage(), e);
            return AjaxResult.error("密码重置失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public AjaxResult getInfo()
    {
        try
        {
            return AjaxResult.success(getLoginUser());
        }
        catch (Exception e)
        {
            return AjaxResult.error("获取用户信息失败");
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public AjaxResult logout()
    {
        try
        {
            // 删除当前用户的token
            tokenService.delLoginUser(getLoginUser().getToken());
            return AjaxResult.success("登出成功");
        }
        catch (Exception e)
        {
            return AjaxResult.error("登出失败：" + e.getMessage());
        }
    }
}
