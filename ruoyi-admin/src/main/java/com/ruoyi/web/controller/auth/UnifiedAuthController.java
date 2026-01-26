package com.ruoyi.web.controller.auth;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.SysLoginService;
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

    /**
     * 统一登录接口
     * 支持管理员和博客用户登录
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

            logger.info("用户登录成功：username={}", username);

            // 返回token，前端根据获取的用户信息判断用户类型
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);

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
            SysUser user = getLoginUser().getUser();
            logger.info("📋 获取用户信息 - userId: {}, userName: {}, userType: {}", 
                user.getUserId(), user.getUserName(), user.getUserType());

            // 提取角色标识列表
            java.util.List<String> roles = new java.util.ArrayList<>();
            if (user.getRoles() != null && !user.getRoles().isEmpty())
            {
                for (com.ruoyi.common.core.domain.entity.SysRole role : user.getRoles())
                {
                    roles.add(role.getRoleKey());
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("user", user);
            result.put("roles", roles);
            result.put("permissions", getLoginUser().getPermissions());
            result.put("userId", user.getUserId());
            result.put("userName", user.getUserName());
            result.put("nickName", user.getNickName());
            result.put("avatar", user.getAvatar());
            result.put("email", user.getEmail());
            result.put("userType", user.getUserType());

            logger.info("📊 返回用户信息 - userType: {}", result.get("userType"));
            return AjaxResult.success(result);
        }
        catch (Exception e)
        {
            logger.error("获取用户信息失败：error={}", e.getMessage(), e);
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
            logger.info("用户登出：username={}", getLoginUser().getUser().getUserName());
            return AjaxResult.success("登出成功");
        }
        catch (Exception e)
        {
            logger.error("登出失败：error={}", e.getMessage());
            return AjaxResult.error("登出失败：" + e.getMessage());
        }
    }
}
