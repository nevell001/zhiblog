package com.ruoyi.web.controller.auth;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一认证控制器
 * 处理管理员和博客用户的统一登录
 *
 * @author nevell
 * @date 2025-01-26
 */
@RestController
@RequestMapping("/auth")
public class UnifiedAuthController extends BaseController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private TokenService tokenService;

    /**
     * 统一登录接口
     * 自动识别用户类型（管理员/博客用户）并返回相应的token和用户类型信息
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
            // 调用登录服务进行验证
            String token = loginService.login(username, password, loginBody.getCode(), loginBody.getUuid());

            // 从缓存中获取登录用户信息
            LoginUser loginUser = tokenService.getLoginUser(token);

            if (loginUser == null || loginUser.getUser() == null)
            {
                return AjaxResult.error("登录失败：无法获取用户信息");
            }

            SysUser user = loginUser.getUser();
            String userType = user.getUserType(); // '00' = 管理员, '01' = 博客用户

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("userType", userType);
            result.put("userId", user.getUserId());
            result.put("userName", user.getUserName());
            result.put("nickName", user.getNickName());
            result.put("avatar", user.getAvatar());
            result.put("email", user.getEmail());

            logger.info("用户登录成功：username={}, userType={}, userId={}",
                username, userType, user.getUserId());

            return AjaxResult.success("登录成功", result);
        }
        catch (Exception e)
        {
            logger.error("登录失败：username={}, error={}", username, e.getMessage());
            return AjaxResult.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前登录用户信息（统一接口）
     */
    @GetMapping("/user/info")
    public AjaxResult getUserInfo()
    {
        try
        {
            LoginUser loginUser = getLoginUser();
            SysUser user = loginUser.getUser();

            Map<String, Object> result = new HashMap<>();
            result.put("user", user);
            result.put("permissions", loginUser.getPermissions());
            result.put("userType", user.getUserType());
            result.put("userId", user.getUserId());
            result.put("userName", user.getUserName());
            result.put("nickName", user.getNickName());
            result.put("avatar", user.getAvatar());
            result.put("email", user.getEmail());

            return AjaxResult.success(result);
        }
        catch (Exception e)
        {
            logger.error("获取用户信息失败：error={}", e.getMessage());
            return AjaxResult.error("获取用户信息失败");
        }
    }

    /**
     * 统一登出接口
     */
    @PostMapping("/logout")
    public AjaxResult logout()
    {
        try
        {
            LoginUser loginUser = getLoginUser();
            if (loginUser != null)
            {
                logger.info("用户登出：username={}, userType={}",
                    loginUser.getUser().getUserName(),
                    loginUser.getUser().getUserType());
            }

            tokenService.delLoginUser(getLoginUser().getToken());
            return AjaxResult.success("登出成功");
        }
        catch (Exception e)
        {
            logger.error("登出失败：error={}", e.getMessage());
            return AjaxResult.error("登出失败：" + e.getMessage());
        }
    }
}
