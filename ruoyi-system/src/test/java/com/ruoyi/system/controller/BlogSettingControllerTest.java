package com.ruoyi.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 博客设置控制器单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@WebMvcTest(controllers = BlogSettingController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class BlogSettingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private com.ruoyi.system.service.IBlogSettingService blogSettingService;

    @MockBean
    private com.ruoyi.system.service.ISysConfigService configService;

    @MockBean
    private com.ruoyi.common.cache.UnifiedCacheManager unifiedCacheManager;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    /**
     * 测试获取设置列表接口
     */
    @Test
    void testGetSettingList() throws Exception {
        // 模拟数据
        List<com.ruoyi.system.domain.BlogSetting> settingList = new ArrayList<>();
        com.ruoyi.system.domain.BlogSetting setting = new com.ruoyi.system.domain.BlogSetting();
        setting.setId(1L);
        setting.setSettingKey("blog_name");
        setting.setSettingValue("我的博客");
        settingList.add(setting);

        when(blogSettingService.selectBlogSettingList(any(com.ruoyi.system.domain.BlogSetting.class)))
            .thenReturn(settingList);

        // 执行测试
        mockMvc.perform(get("/system/setting/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows[0].settingKey").value("blog_name"));

        verify(blogSettingService).selectBlogSettingList(any(com.ruoyi.system.domain.BlogSetting.class));
    }

    /**
     * 测试通过设置键获取设置值接口
     */
    @Test
    void testGetSettingValueByKey() throws Exception {
        // 模拟数据
        when(blogSettingService.selectSettingValueByKey("blog_name")).thenReturn("我的博客");

        // 执行测试
        mockMvc.perform(get("/system/setting/value/blog_name")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogSettingService).selectSettingValueByKey("blog_name");
    }

    /**
     * 测试获取设置详情接口
     */
    @Test
    void testGetSettingDetail() throws Exception {
        // 模拟数据
        com.ruoyi.system.domain.BlogSetting setting = new com.ruoyi.system.domain.BlogSetting();
        setting.setId(1L);
        setting.setSettingKey("blog_name");
        setting.setSettingValue("我的博客");

        when(blogSettingService.selectBlogSettingById(1L)).thenReturn(setting);

        // 执行测试
        mockMvc.perform(get("/system/setting/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.settingKey").value("blog_name"));

        verify(blogSettingService).selectBlogSettingById(1L);
    }

    /**
     * 测试新增设置接口
     */
    @Test
    void testAddSetting() throws Exception {
        // 模拟成功添加
        when(blogSettingService.insertBlogSetting(any(com.ruoyi.system.domain.BlogSetting.class)))
            .thenReturn(1);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("settingKey", "test_key");
        params.put("settingValue", "test_value");

        // 执行测试
        mockMvc.perform(post("/system/setting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogSettingService).insertBlogSetting(any(com.ruoyi.system.domain.BlogSetting.class));
    }

    /**
     * 测试更新设置接口
     */
    @Test
    void testEditSetting() throws Exception {
        // 模拟成功更新
        when(blogSettingService.updateBlogSetting(any(com.ruoyi.system.domain.BlogSetting.class)))
            .thenReturn(1);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("settingKey", "blog_name");
        params.put("settingValue", "修改后的博客名称");

        // 执行测试
        mockMvc.perform(put("/system/setting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogSettingService).updateBlogSetting(any(com.ruoyi.system.domain.BlogSetting.class));
    }

    /**
     * 测试通过设置键更新设置值接口 (PUT)
     */
    @Test
    void testUpdateSettingByKey_Put() throws Exception {
        // 模拟成功更新
        when(blogSettingService.updateSettingValueByKey("blog_name", "新博客名称")).thenReturn(1);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("settingKey", "blog_name");
        params.put("settingValue", "新博客名称");

        // 执行测试
        mockMvc.perform(put("/system/setting/updateByKey")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogSettingService).updateSettingValueByKey("blog_name", "新博客名称");
    }

    /**
     * 测试通过设置键更新设置值接口 (POST)
     */
    @Test
    void testUpdateSettingByKey_Post() throws Exception {
        // 模拟成功更新
        when(blogSettingService.updateSettingValueByKey("blog_name", "新博客名称")).thenReturn(1);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("settingKey", "blog_name");
        params.put("settingValue", "新博客名称");

        // 执行测试
        mockMvc.perform(post("/system/setting/updateByKey")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogSettingService).updateSettingValueByKey("blog_name", "新博客名称");
    }

    /**
     * 测试删除设置接口
     */
    @Test
    void testRemoveSetting() throws Exception {
        // 模拟成功删除
        when(blogSettingService.deleteBlogSettingByIds(any(Long[].class))).thenReturn(1);

        // 执行测试
        mockMvc.perform(delete("/system/setting/1,2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogSettingService).deleteBlogSettingByIds(any(Long[].class));
    }

    /**
     * 测试通过设置键更新设置值接口 (PUT) - 更新现有配置
     */
    @Test
    void testUpdateSettingByKey_Put_UpdateExisting() throws Exception {
        // 模拟成功更新
        when(blogSettingService.updateSettingValueByKey("blog_name", "新博客名称")).thenReturn(1);

        // 模拟现有配置
        List<com.ruoyi.system.domain.SysConfig> configList = new ArrayList<>();
        com.ruoyi.system.domain.SysConfig config = new com.ruoyi.system.domain.SysConfig();
        config.setConfigKey("blog_name");
        config.setConfigValue("旧博客名称");
        configList.add(config);

        when(configService.selectConfigList(any(com.ruoyi.system.domain.SysConfig.class)))
            .thenReturn(configList);
        when(configService.updateConfig(any(com.ruoyi.system.domain.SysConfig.class)))
            .thenReturn(1);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("settingKey", "blog_name");
        params.put("settingValue", "新博客名称");

        // 执行测试
        mockMvc.perform(put("/system/setting/updateByKey")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogSettingService).updateSettingValueByKey("blog_name", "新博客名称");
        verify(configService).updateConfig(any(com.ruoyi.system.domain.SysConfig.class));
    }

    /**
     * 测试通过设置键更新设置值接口 (PUT) - 创建新配置
     */
    @Test
    void testUpdateSettingByKey_Put_CreateNew() throws Exception {
        // 模拟成功更新
        when(blogSettingService.updateSettingValueByKey("new_key", "新值")).thenReturn(1);

        // 模拟无现有配置
        when(configService.selectConfigList(any(com.ruoyi.system.domain.SysConfig.class)))
            .thenReturn(new ArrayList<>());
        when(configService.insertConfig(any(com.ruoyi.system.domain.SysConfig.class)))
            .thenReturn(1);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("settingKey", "new_key");
        params.put("settingValue", "新值");

        // 执行测试
        mockMvc.perform(put("/system/setting/updateByKey")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogSettingService).updateSettingValueByKey("new_key", "新值");
        verify(configService).insertConfig(any(com.ruoyi.system.domain.SysConfig.class));
    }

    /**
     * 测试通过设置键更新设置值接口 (PUT) - 更新失败
     */
    @Test
    void testUpdateSettingByKey_Put_Failure() throws Exception {
        // 模拟更新失败
        when(blogSettingService.updateSettingValueByKey("blog_name", "新博客名称")).thenReturn(0);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("settingKey", "blog_name");
        params.put("settingValue", "新博客名称");

        // 执行测试
        mockMvc.perform(put("/system/setting/updateByKey")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));

        verify(blogSettingService).updateSettingValueByKey("blog_name", "新博客名称");
        // 验证不会调用 configService
        verify(configService, never()).selectConfigList(any(com.ruoyi.system.domain.SysConfig.class));
    }

    /**
     * 测试通过设置键更新设置值接口 (POST) - 更新现有配置
     */
    @Test
    void testUpdateSettingByKey_Post_UpdateExisting() throws Exception {
        // 模拟成功更新
        when(blogSettingService.updateSettingValueByKey("blog_name", "新博客名称")).thenReturn(1);

        // 模拟现有配置
        List<com.ruoyi.system.domain.SysConfig> configList = new ArrayList<>();
        com.ruoyi.system.domain.SysConfig config = new com.ruoyi.system.domain.SysConfig();
        config.setConfigKey("blog_name");
        config.setConfigValue("旧博客名称");
        configList.add(config);

        when(configService.selectConfigList(any(com.ruoyi.system.domain.SysConfig.class)))
            .thenReturn(configList);
        when(configService.updateConfig(any(com.ruoyi.system.domain.SysConfig.class)))
            .thenReturn(1);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("settingKey", "blog_name");
        params.put("settingValue", "新博客名称");

        // 执行测试
        mockMvc.perform(post("/system/setting/updateByKey")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogSettingService).updateSettingValueByKey("blog_name", "新博客名称");
        verify(configService).updateConfig(any(com.ruoyi.system.domain.SysConfig.class));
    }

    /**
     * 测试通过设置键更新设置值接口 (POST) - 创建新配置
     */
    @Test
    void testUpdateSettingByKey_Post_CreateNew() throws Exception {
        // 模拟成功更新
        when(blogSettingService.updateSettingValueByKey("new_key", "新值")).thenReturn(1);

        // 模拟无现有配置
        when(configService.selectConfigList(any(com.ruoyi.system.domain.SysConfig.class)))
            .thenReturn(new ArrayList<>());
        when(configService.insertConfig(any(com.ruoyi.system.domain.SysConfig.class)))
            .thenReturn(1);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("settingKey", "new_key");
        params.put("settingValue", "新值");

        // 执行测试
        mockMvc.perform(post("/system/setting/updateByKey")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(blogSettingService).updateSettingValueByKey("new_key", "新值");
        verify(configService).insertConfig(any(com.ruoyi.system.domain.SysConfig.class));
    }

    /**
     * 测试通过设置键更新设置值接口 (POST) - 更新失败
     */
    @Test
    void testUpdateSettingByKey_Post_Failure() throws Exception {
        // 模拟更新失败
        when(blogSettingService.updateSettingValueByKey("blog_name", "新博客名称")).thenReturn(0);

        // 准备请求体
        Map<String, Object> params = new HashMap<>();
        params.put("settingKey", "blog_name");
        params.put("settingValue", "新博客名称");

        // 执行测试
        mockMvc.perform(post("/system/setting/updateByKey")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));

        verify(blogSettingService).updateSettingValueByKey("blog_name", "新博客名称");
        // 验证不会调用 configService
        verify(configService, never()).selectConfigList(any(com.ruoyi.system.domain.SysConfig.class));
    }
}