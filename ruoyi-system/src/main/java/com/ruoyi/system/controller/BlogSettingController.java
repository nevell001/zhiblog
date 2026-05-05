package com.ruoyi.system.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.common.cache.UnifiedCacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BlogSetting;
import com.ruoyi.system.service.IBlogSettingService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 博客设置Controller
 * 
 * @author nevell
 * @date 2025-09-08
 */
@RestController
@RequestMapping("/system/setting")
public class BlogSettingController extends BaseController
{
    @Autowired
    private IBlogSettingService blogSettingService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private UnifiedCacheManager unifiedCacheManager;

    /**
     * 查询博客设置列表
     */
    @PreAuthorize("@ss.hasPermi('system:setting:list')")
    @GetMapping("/list")
    public TableDataInfo list(BlogSetting blogSetting)
    {
        startPage();
        List<BlogSetting> list = blogSettingService.selectBlogSettingList(blogSetting);
        return getDataTable(list);
    }

    /**
     * 通过设置键查询设置值
     */
    @GetMapping("/value/{settingKey}")
    public AjaxResult getSettingValueByKey(@PathVariable("settingKey") String settingKey)
    {
        String value = blogSettingService.selectSettingValueByKey(settingKey);
        return success(value);
    }

    /**
     * 获取博客设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:setting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(blogSettingService.selectBlogSettingById(id));
    }

    /**
     * 新增博客设置
     */
    @PreAuthorize("@ss.hasPermi('system:setting:add')")
    @Log(title = "博客设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BlogSetting blogSetting)
    {
        return toAjax(blogSettingService.insertBlogSetting(blogSetting));
    }

    /**
     * 修改博客设置
     */
    @PreAuthorize("@ss.hasPermi('system:setting:edit')")
    @Log(title = "博客设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BlogSetting blogSetting)
    {
        return toAjax(blogSettingService.updateBlogSetting(blogSetting));
    }

    /**
     * 通过设置键修改设置值
     */
    @PreAuthorize("@ss.hasPermi('system:setting:edit')")
    @Log(title = "博客设置", businessType = BusinessType.UPDATE)
    @PutMapping("/updateByKey")
    public AjaxResult updateByKey(@RequestBody BlogSetting blogSetting)
    {
        int result = blogSettingService.updateSettingValueByKey(
            blogSetting.getSettingKey(), blogSetting.getSettingValue());

        // 同时更新 sys_config 表，保持数据同步
        if (result > 0) {
            try {
                String settingKey = blogSetting.getSettingKey();
                String settingValue = blogSetting.getSettingValue();

                // 先尝试获取现有配置
                SysConfig config = new SysConfig();
                config.setConfigKey(settingKey);
                SysConfig existingConfig = configService.selectConfigList(config).stream()
                    .filter(c -> settingKey.equals(c.getConfigKey()))
                    .findFirst()
                    .orElse(null);

                if (existingConfig != null) {
                    // 更新现有配置
                    existingConfig.setConfigValue(settingValue);
                    configService.updateConfig(existingConfig);
                    logger.info("已同步更新 sys_config 表中的 {}: {}", settingKey, settingValue);
                } else {
                    // 创建新配置
                    config.setConfigName("博客设置 - " + settingKey);
                    config.setConfigValue(settingValue);
                    config.setConfigType("Y");
                    configService.insertConfig(config);
                    logger.info("已在 sys_config 表中创建 {}: {}", settingKey, settingValue);
                }

                // 清除缓存
                unifiedCacheManager.delete("sys_config:" + settingKey);
                logger.info("已清除缓存: sys_config:{}", settingKey);
            } catch (Exception e) {
                logger.error("同步更新 sys_config 表失败", e);
            }
        }

        return toAjax(result);
    }

    /**
     * 通过设置键修改设置值 (POST方法支持)
     */
    @PreAuthorize("@ss.hasPermi('system:setting:edit')")
    @Log(title = "博客设置", businessType = BusinessType.UPDATE)
    @PostMapping("/updateByKey")
    public AjaxResult updateByKeyPost(@RequestBody BlogSetting blogSetting)
    {
        int result = blogSettingService.updateSettingValueByKey(
            blogSetting.getSettingKey(), blogSetting.getSettingValue());

        // 同时更新 sys_config 表，保持数据同步
        if (result > 0) {
            try {
                String settingKey = blogSetting.getSettingKey();
                String settingValue = blogSetting.getSettingValue();

                // 先尝试获取现有配置
                SysConfig config = new SysConfig();
                config.setConfigKey(settingKey);
                SysConfig existingConfig = configService.selectConfigList(config).stream()
                    .filter(c -> settingKey.equals(c.getConfigKey()))
                    .findFirst()
                    .orElse(null);

                if (existingConfig != null) {
                    // 更新现有配置
                    existingConfig.setConfigValue(settingValue);
                    configService.updateConfig(existingConfig);
                    logger.info("已同步更新 sys_config 表中的 {}: {}", settingKey, settingValue);
                } else {
                    // 创建新配置
                    config.setConfigName("博客设置 - " + settingKey);
                    config.setConfigValue(settingValue);
                    config.setConfigType("Y");
                    configService.insertConfig(config);
                    logger.info("已在 sys_config 表中创建 {}: {}", settingKey, settingValue);
                }

                // 清除缓存
                unifiedCacheManager.delete("sys_config:" + settingKey);
                logger.info("已清除缓存: sys_config:{}", settingKey);
            } catch (Exception e) {
                logger.error("同步更新 sys_config 表失败", e);
            }
        }

        return toAjax(result);
    }

    /**
     * 删除博客设置
     */
    @PreAuthorize("@ss.hasPermi('system:setting:remove')")
    @Log(title = "博客设置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") Long[] ids)
    {
        return toAjax(blogSettingService.deleteBlogSettingByIds(ids));
    }
}