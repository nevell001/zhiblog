package com.ruoyi.web.controller.blog;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.cache.UnifiedCacheManager;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.IBlogSettingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 博客前台设置控制器
 * 提供前台博客设置匿名访问接口
 *
 * @author nevell
 * @date 2025-12-18
 */
@Api(tags = "博客前台设置管理")
@RestController
@RequestMapping("/common/blog/setting")
public class BlogFrontSettingController extends BaseController {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IBlogSettingService blogSettingService;

    @Autowired
    private UnifiedCacheManager unifiedCacheManager;

    /**
     * 获取博客设置（前台匿名访问）
     */
    @Anonymous
    @ApiOperation("获取博客设置（前台用）")
    @GetMapping
    public AjaxResult getBlogSettings() {
        try {
            // 定义博客相关的配置键
            String[] blogConfigKeys = {
                "blog_name", "blog_desc", "blog_author", "blog_email", "blog_url", "blog_start_time",
                "blog_avatar", "blog_signature", "blog_keywords", "blog_copyright", "blog_beian",
                "seo_title", "seo_description", "seo_canonical_url", "seo_robots", "seo_favicon",
                "theme_color", "header_background", "sidebar_style",
                "comment_enabled", "comment_review", "like_enabled", "view_count_enabled",
                "share_enabled", "search_enabled", "sidebar_enabled", "footer_enabled", "copyright_enabled",
                "page_size", "hot_article_count", "recent_comment_count", "greeting_message", "about_content",
                "author_title", "author_bio", "github_url", "weibo_url", "wechat_qr", "author_location", "personal_website"
            };

            Map<String, Object> blogSettings = new HashMap<>();

            // 批量查询博客设置
            for (String configKey : blogConfigKeys) {
                try {
                    // 优先从 blog_setting 表读取
                    String configValue = blogSettingService.selectSettingValueByKey(configKey);

                    // 如果 blog_setting 表中没有，尝试从 sys_config 表读取
                    if (StringUtils.isEmpty(configValue)) {
                        configValue = configService.selectConfigByKey(configKey);
                    }

                    if (StringUtils.isNotEmpty(configValue)) {
                        blogSettings.put(configKey, configValue);
                    } else {
                        // 设置默认值
                        setDefaultValue(blogSettings, configKey);
                    }
                } catch (Exception e) {
                    logger.warn("获取博客设置失败: {}, 错误: {}", configKey, e.getMessage());
                    // 设置默认值
                    setDefaultValue(blogSettings, configKey);
                }
            }

            return AjaxResult.success(blogSettings);
        } catch (Exception e) {
            logger.error("获取博客设置失败", e);
            return AjaxResult.error("获取博客设置失败");
        }
    }

    /**
     * 设置默认值
     */
    private void setDefaultValue(Map<String, Object> settings, String configKey) {
        switch (configKey) {
            case "blog_name":
                settings.put(configKey, "我的博客");
                break;
            case "blog_desc":
                settings.put(configKey, "这是一个基于RuoYi-Vue的博客系统");
                break;
            case "blog_author":
                settings.put(configKey, "nevell");
                break;
            case "blog_avatar":
                settings.put(configKey, "");
                break;
            case "author_title":
                settings.put(configKey, "全栈开发工程师");
                break;
            case "blog_email":
                settings.put(configKey, "");
                break;
            case "blog_url":
                settings.put(configKey, "");
                break;
            case "github_url":
                settings.put(configKey, "");
                break;
            case "weibo_url":
                settings.put(configKey, "");
                break;
            case "wechat_qr":
                settings.put(configKey, "");
                break;
            case "theme_color":
                settings.put(configKey, "#409eff");
                break;
            case "page_size":
                settings.put(configKey, "10");
                break;
            case "hot_article_count":
                settings.put(configKey, "5");
                break;
            case "recent_comment_count":
                settings.put(configKey, "5");
                break;
            case "comment_enabled":
                settings.put(configKey, "true");
                break;
            case "like_enabled":
                settings.put(configKey, "true");
                break;
            case "view_count_enabled":
                settings.put(configKey, "true");
                break;
            case "share_enabled":
                settings.put(configKey, "true");
                break;
            case "search_enabled":
                settings.put(configKey, "true");
                break;
            case "sidebar_enabled":
                settings.put(configKey, "true");
                break;
            case "footer_enabled":
                settings.put(configKey, "true");
                break;
            case "copyright_enabled":
                settings.put(configKey, "true");
                break;
            default:
                settings.put(configKey, "");
                break;
        }
    }

    /**
     * 根据键获取单个设置值
     */
    @Anonymous
    @ApiOperation("根据键获取设置值")
    @GetMapping("/value/{configKey}")
    public AjaxResult getSettingValueByKey(@PathVariable String configKey) {
        try {
            String configValue = configService.selectConfigByKey(configKey);
            if (StringUtils.isNotEmpty(configValue)) {
                return AjaxResult.success(configValue);
            } else {
                // 返回默认值
                Map<String, Object> defaultValueMap = new HashMap<>();
                setDefaultValue(defaultValueMap, configKey);
                return AjaxResult.success(defaultValueMap.get(configKey));
            }
        } catch (Exception e) {
            logger.error("获取设置值失败: {}", configKey, e);
            return AjaxResult.error("获取设置值失败");
        }
    }

    /**
     * 批量更新博客设置
     */
    @ApiOperation("批量更新博客设置")
    @PostMapping("/update")
    public AjaxResult updateBlogSettings(@RequestBody Map<String, Object> settings) {
        try {
            for (Map.Entry<String, Object> entry : settings.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                
                // 更新系统配置表
                try {
                    // 先尝试获取现有配置
                    List<SysConfig> configList = configService.selectConfigList(new SysConfig());
                    SysConfig config = null;
                    for (SysConfig c : configList) {
                        if (key.equals(c.getConfigKey())) {
                            config = c;
                            break;
                        }
                    }
                    
                    if (config != null) {
                        config.setConfigValue(value);
                        configService.updateConfig(config);
                    } else {
                        // 如果配置不存在，创建新配置
                        config = new SysConfig();
                        config.setConfigKey(key);
                        config.setConfigValue(value);
                        config.setConfigName("博客设置 - " + key);
                        configService.insertConfig(config);
                    }
                } catch (Exception e) {
                    logger.warn("更新系统配置失败: {}, 错误: {}", key, e.getMessage());
                    // 如果系统配置失败，尝试更新博客设置表
                    try {
                        int result = blogSettingService.updateSettingValueByKey(key, value);
                        if (result == 0) {
                            logger.info("博客设置 {} 不存在，已自动创建", key);
                        }
                    } catch (Exception be) {
                        logger.error("更新博客设置失败: {}", key, be);
                    }
                }
            }
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("批量更新博客设置失败", e);
            return AjaxResult.error("更新设置失败: " + e.getMessage());
        }
    }

    /**
     * 清除头像缓存（解决头像更新后缓存问题）
     */
    @PostMapping("/clear-avatar-cache")
    public AjaxResult clearAvatarCache() {
        try {
            unifiedCacheManager.delete("sys_config:blog_avatar");
            logger.info("头像缓存已清除");
            return AjaxResult.success("头像缓存已清除");
        } catch (Exception e) {
            logger.error("清除头像缓存失败", e);
            return AjaxResult.error("清除头像缓存失败: " + e.getMessage());
        }
    }

    /**
     * 清除所有博客设置缓存
     */
    @GetMapping("/clear-blog-cache")
    public AjaxResult clearBlogCache() {
        try {
            String[] blogConfigKeys = {
                "blog_name", "blog_desc", "blog_author", "blog_email", "blog_url", "blog_start_time",
                "blog_avatar", "blog_signature", "blog_keywords", "blog_copyright", "blog_beian",
                "seo_title", "seo_description", "seo_canonical_url", "seo_robots", "seo_favicon",
                "theme_color", "header_background", "sidebar_style",
                "comment_enabled", "comment_review", "like_enabled", "view_count_enabled",
                "share_enabled", "search_enabled", "sidebar_enabled", "footer_enabled", "copyright_enabled",
                "page_size", "hot_article_count", "recent_comment_count", "greeting_message", "about_content",
                "author_title", "author_bio", "github_url", "weibo_url", "wechat_qr", "author_location", "personal_website"
            };

            for (String configKey : blogConfigKeys) {
                unifiedCacheManager.delete("sys_config:" + configKey);
            }

            logger.info("所有博客设置缓存已清除");
            return AjaxResult.success("所有博客设置缓存已清除");
        } catch (Exception e) {
            logger.error("清除博客设置缓存失败", e);
            return AjaxResult.error("清除博客设置缓存失败: " + e.getMessage());
        }
    }
}