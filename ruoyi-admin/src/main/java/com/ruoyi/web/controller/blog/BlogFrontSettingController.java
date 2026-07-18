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
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.cache.UnifiedCacheManager;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.IBlogSettingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 博客前台设置控制器
 * 提供前台博客设置匿名访问接口
 *
 * @author nevell
 * @date 2025-12-18
 */
@Tag(name = "博客前台设置管理")
@RestController
@RequestMapping("/common/blog/setting")
public class BlogFrontSettingController extends BaseController {

    /**
     * 博客配置键常量
     */
    private static final String[] BLOG_CONFIG_KEYS = {
        "blog_name", "blog_desc", "blog_author", "blog_email",
        "blog_avatar", "blog_copyright", "blog_beian",
        "comment_enabled", "comment_review", "like_enabled", "view_count_enabled",
        "share_enabled", "search_enabled", "sidebar_enabled", "footer_enabled", "copyright_enabled",
        "about_content",
        "author_title", "author_bio", "github_url", "weibo_url", "wechat_qr", "author_location", "personal_website"
    };

    /**
     * 博客设置缓存键
     */
    private static final String BLOG_SETTINGS_CACHE_KEY = "blog:settings:all";

    /**
     * 缓存过期时间（秒）
     */
    private static final int CACHE_EXPIRE_SECONDS = 3600;

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
    @Operation(summary = "获取博客设置（前台用）")
    @GetMapping
    @SuppressWarnings("unchecked")
    public AjaxResult getBlogSettings() {
        try {
            // 先从缓存读取
            Map<String, Object> cached = unifiedCacheManager.get(BLOG_SETTINGS_CACHE_KEY, Map.class);
            if (cached != null) {
                return AjaxResult.success(cached);
            }

            // 缓存不存在，查询数据库
            Map<String, Object> blogSettings = new HashMap<>();

            // 一次性查询所有系统配置，然后过滤
            try {
                List<SysConfig> allConfigs = configService.selectConfigList(new SysConfig());
                Map<String, String> configMap = allConfigs.stream()
                    .filter(c -> java.util.Arrays.asList(BLOG_CONFIG_KEYS).contains(c.getConfigKey()))
                    .collect(java.util.stream.Collectors.toMap(SysConfig::getConfigKey, SysConfig::getConfigValue));

                // 批量查询博客设置
                for (String configKey : BLOG_CONFIG_KEYS) {
                    try {
                        // 优先从 blog_setting 表读取
                        String configValue = blogSettingService.selectSettingValueByKey(configKey);

                        // 如果 blog_setting 表中没有，尝试从 sys_config 表读取
                        if (StringUtils.isEmpty(configValue)) {
                            configValue = configMap.get(configKey);
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
            } catch (Exception e) {
                logger.error("批量查询系统配置失败，回退到逐个查询", e);
                // 回退到逐个查询
                for (String configKey : BLOG_CONFIG_KEYS) {
                    try {
                        String configValue = blogSettingService.selectSettingValueByKey(configKey);
                        if (StringUtils.isEmpty(configValue)) {
                            configValue = configService.selectConfigByKey(configKey);
                        }
                        if (StringUtils.isNotEmpty(configValue)) {
                            blogSettings.put(configKey, configValue);
                        } else {
                            setDefaultValue(blogSettings, configKey);
                        }
                    } catch (Exception ex) {
                        logger.warn("获取博客设置失败: {}, 错误: {}", configKey, ex.getMessage());
                        setDefaultValue(blogSettings, configKey);
                    }
                }
            }

            // 存入缓存，设置过期时间
            unifiedCacheManager.set(BLOG_SETTINGS_CACHE_KEY, blogSettings, CACHE_EXPIRE_SECONDS, java.util.concurrent.TimeUnit.SECONDS);

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
                settings.put(configKey, "基于 Spring Boot 3.3.0 + Vue 3 + Element Plus 构建的现代化博客系统");
                break;
            case "blog_author":
                settings.put(configKey, "nevell");
                break;
            case "blog_avatar":
                settings.put(configKey, "");
                break;
            case "blog_copyright":
                settings.put(configKey, "");
                break;
            case "blog_beian":
                settings.put(configKey, "");
                break;
            case "author_title":
                settings.put(configKey, "全栈开发工程师");
                break;
            case "blog_email":
                settings.put(configKey, "");
                break;
            case "github_url":
                settings.put(configKey, "");
                break;
            case "weibo_url":
                settings.put(configKey, "");
                break;
            case "author_location":
                settings.put(configKey, "");
                break;
            case "personal_website":
                settings.put(configKey, "");
                break;
            case "wechat_qr":
                settings.put(configKey, "");
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
    @Operation(summary = "根据键获取设置值")
    @GetMapping("/value/{configKey}")
    public AjaxResult getSettingValueByKey(@PathVariable("configKey") String configKey) {
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
    @Operation(summary = "批量更新博客设置")
    @PreAuthorize("@ss.hasPermi('system:setting:edit')")
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

            // 清除博客设置缓存
            unifiedCacheManager.delete(BLOG_SETTINGS_CACHE_KEY);

            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("批量更新博客设置失败", e);
            return AjaxResult.error("更新设置失败: " + e.getMessage());
        }
    }

    /**
     * 清除头像缓存（解决头像更新后缓存问题）
     */
    @PreAuthorize("@ss.hasPermi('system:setting:edit')")
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

    @PreAuthorize("@ss.hasPermi('system:setting:edit')")
    @GetMapping("/clear-blog-cache")
    @Operation(summary = "清除博客设置缓存")
    public AjaxResult clearBlogCache() {
        try {
            // 清除博客设置主缓存
            unifiedCacheManager.delete(BLOG_SETTINGS_CACHE_KEY);
            
            // 同时也清除单个配置的缓存（兼容旧逻辑）
            for (String configKey : BLOG_CONFIG_KEYS) {
                unifiedCacheManager.delete("sys_config:" + configKey);
            }

            logger.info("所有博客设置缓存已清除，包括主缓存: " + BLOG_SETTINGS_CACHE_KEY);
            return AjaxResult.success("所有博客设置缓存已清除");
        } catch (Exception e) {
            logger.error("清除博客设置缓存失败", e);
            return AjaxResult.error("清除博客设置缓存失败: " + e.getMessage());
        }
    }

    /**
     * 清除所有标签缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @GetMapping("/clear-tag-cache")
    public AjaxResult clearTagCache() {
        try {
            // 清除所有标签相关缓存
            unifiedCacheManager.deleteByPattern("blog:tag:*");
            logger.info("所有标签缓存已清除");
            return AjaxResult.success("所有标签缓存已清除");
        } catch (Exception e) {
            logger.error("清除标签缓存失败", e);
            return AjaxResult.error("清除标签缓存失败: " + e.getMessage());
        }
    }

    /**
     * 清除所有文章缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @GetMapping("/clear-article-cache")
    public AjaxResult clearArticleCache() {
        try {
            // 清除所有文章相关缓存
            unifiedCacheManager.deleteByPattern("blog:article:*");
            unifiedCacheManager.deleteByPattern("blog:search:*");
            unifiedCacheManager.deleteByPattern("blog:hot:*");
            logger.info("所有文章缓存已清除");
            return AjaxResult.success("所有文章缓存已清除");
        } catch (Exception e) {
            logger.error("清除文章缓存失败", e);
            return AjaxResult.error("清除文章缓存失败: " + e.getMessage());
        }
    }

    /**
     * 清除所有博客缓存（文章、分类、标签等）
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @GetMapping("/clear-all-cache")
    public AjaxResult clearAllBlogCache() {
        try {
            // 清除所有博客相关缓存
            unifiedCacheManager.deleteByPattern("blog:*");
            logger.info("所有博客缓存已清除");
            return AjaxResult.success("所有博客缓存已清除（文章、分类、标签、搜索等）");
        } catch (Exception e) {
            logger.error("清除所有博客缓存失败", e);
            return AjaxResult.error("清除所有博客缓存失败: " + e.getMessage());
        }
    }
}