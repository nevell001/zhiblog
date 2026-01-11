package com.ruoyi.system.service.impl;

import com.ruoyi.common.cache.UnifiedCacheManager;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.mapper.SysConfigMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 参数配置服务层实现单元测试
 *
 * @author test
 * @date 2026-01-11
 */
@ExtendWith(MockitoExtension.class)
class SysConfigServiceImplTest {

    @Mock
    private SysConfigMapper configMapper;

    @Mock
    private UnifiedCacheManager unifiedCacheManager;

    @InjectMocks
    private SysConfigServiceImpl configService;

    private SysConfig testConfig;

    @BeforeEach
    void setUp() {
        testConfig = new SysConfig();
        testConfig.setConfigId(1L);
        testConfig.setConfigKey("sys.account.captchaEnabled");
        testConfig.setConfigValue("true");
        testConfig.setConfigType("N");
    }

    /**
     * 测试根据ID查询参数配置
     */
    @Test
    void testSelectConfigById() {
        when(configMapper.selectConfig(any(SysConfig.class))).thenReturn(testConfig);

        SysConfig result = configService.selectConfigById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getConfigId());
        assertEquals("sys.account.captchaEnabled", result.getConfigKey());
        verify(configMapper, times(1)).selectConfig(any(SysConfig.class));
    }

    /**
     * 测试根据键名查询参数配置 - 从缓存获取
     */
    @Test
    void testSelectConfigByKey_FromCache() {
        when(unifiedCacheManager.get(anyString(), eq(String.class))).thenReturn("true");

        String result = configService.selectConfigByKey("sys.account.captchaEnabled");

        assertEquals("true", result);
        verify(unifiedCacheManager, times(1)).get(anyString(), eq(String.class));
        verify(configMapper, never()).selectConfig(any(SysConfig.class));
    }

    /**
     * 测试根据键名查询参数配置 - 从数据库获取
     */
    @Test
    void testSelectConfigByKey_FromDatabase() {
        when(unifiedCacheManager.get(anyString(), eq(String.class))).thenReturn(null);
        when(configMapper.selectConfig(any(SysConfig.class))).thenReturn(testConfig);

        String result = configService.selectConfigByKey("sys.account.captchaEnabled");

        assertEquals("true", result);
        verify(unifiedCacheManager, times(1)).set(anyString(), eq("true"));
        verify(configMapper, times(1)).selectConfig(any(SysConfig.class));
    }

    /**
     * 测试根据键名查询参数配置 - 不存在的配置
     */
    @Test
    void testSelectConfigByKey_NotFound() {
        when(unifiedCacheManager.get(anyString(), eq(String.class))).thenReturn(null);
        when(configMapper.selectConfig(any(SysConfig.class))).thenReturn(null);

        String result = configService.selectConfigByKey("non.existent.key");

        assertEquals("", result);
    }

    /**
     * 测试获取验证码开关 - 开启
     */
    @Test
    void testSelectCaptchaEnabled_True() {
        when(unifiedCacheManager.get(anyString(), eq(String.class))).thenReturn("true");

        boolean result = configService.selectCaptchaEnabled();

        assertTrue(result);
    }

    /**
     * 测试获取验证码开关 - 关闭
     */
    @Test
    void testSelectCaptchaEnabled_False() {
        when(unifiedCacheManager.get(anyString(), eq(String.class))).thenReturn("false");

        boolean result = configService.selectCaptchaEnabled();

        assertFalse(result);
    }

    /**
     * 测试获取验证码开关 - 未配置（默认开启）
     */
    @Test
    void testSelectCaptchaEnabled_Default() {
        when(unifiedCacheManager.get(anyString(), eq(String.class))).thenReturn(null);
        when(configMapper.selectConfig(any(SysConfig.class))).thenReturn(null);

        boolean result = configService.selectCaptchaEnabled();

        assertTrue(result);
    }

    /**
     * 测试查询参数配置列表
     */
    @Test
    void testSelectConfigList() {
        List<SysConfig> configList = Arrays.asList(testConfig);
        when(configMapper.selectConfigList(any(SysConfig.class))).thenReturn(configList);

        List<SysConfig> result = configService.selectConfigList(new SysConfig());

        assertEquals(1, result.size());
        assertEquals("sys.account.captchaEnabled", result.get(0).getConfigKey());
    }

    /**
     * 测试新增参数配置
     */
    @Test
    void testInsertConfig() {
        when(configMapper.insertConfig(any(SysConfig.class))).thenReturn(1);

        int result = configService.insertConfig(testConfig);

        assertEquals(1, result);
        verify(unifiedCacheManager, times(1)).set(anyString(), eq("true"));
    }

    /**
     * 测试修改参数配置 - 键名未改变
     */
    @Test
    void testUpdateConfig_SameKey() {
        SysConfig existingConfig = new SysConfig();
        existingConfig.setConfigId(1L);
        existingConfig.setConfigKey("sys.account.captchaEnabled");

        when(configMapper.selectConfigById(1L)).thenReturn(existingConfig);
        when(configMapper.updateConfig(any(SysConfig.class))).thenReturn(1);

        int result = configService.updateConfig(testConfig);

        assertEquals(1, result);
        verify(unifiedCacheManager, never()).delete(anyString());
        verify(unifiedCacheManager, times(1)).set(anyString(), eq("true"));
    }

    /**
     * 测试修改参数配置 - 键名改变
     */
    @Test
    void testUpdateConfig_DifferentKey() {
        SysConfig existingConfig = new SysConfig();
        existingConfig.setConfigId(1L);
        existingConfig.setConfigKey("old.key");

        testConfig.setConfigKey("new.key");

        when(configMapper.selectConfigById(1L)).thenReturn(existingConfig);
        when(configMapper.updateConfig(any(SysConfig.class))).thenReturn(1);

        int result = configService.updateConfig(testConfig);

        assertEquals(1, result);
        verify(unifiedCacheManager, times(1)).delete(anyString());
        verify(unifiedCacheManager, times(1)).set(anyString(), eq("true"));
    }

    /**
     * 测试批量删除参数信息
     */
    @Test
    void testDeleteConfigByIds() {
        when(configMapper.selectConfig(any(SysConfig.class))).thenReturn(testConfig);
        when(configMapper.deleteConfigById(anyLong())).thenReturn(1);

        configService.deleteConfigByIds(new Long[]{1L});

        verify(configMapper, times(1)).deleteConfigById(1L);
        verify(unifiedCacheManager, times(1)).delete(anyString());
    }

    /**
     * 测试批量删除参数信息 - 内置参数（应抛出异常）
     */
    @Test
    void testDeleteConfigByIds_BuiltInConfig() {
        testConfig.setConfigType("Y");
        when(configMapper.selectConfig(any(SysConfig.class))).thenReturn(testConfig);

        assertThrows(ServiceException.class, () -> {
            configService.deleteConfigByIds(new Long[]{1L});
        });

        verify(configMapper, never()).deleteConfigById(anyLong());
    }

    /**
     * 测试加载参数缓存数据
     */
    @Test
    void testLoadingConfigCache() {
        List<SysConfig> configList = Arrays.asList(testConfig);
        when(configMapper.selectConfigList(any(SysConfig.class))).thenReturn(configList);

        configService.loadingConfigCache();

        verify(unifiedCacheManager, times(1)).set(anyString(), eq("true"));
    }

    /**
     * 测试清空参数缓存数据
     */
    @Test
    void testClearConfigCache() {
        when(unifiedCacheManager.keys(anyString())).thenReturn(Collections.singleton("sys_config:key"));

        configService.clearConfigCache();

        verify(unifiedCacheManager, times(1)).delete(anyCollection());
    }

    /**
     * 测试重置参数缓存数据
     */
    @Test
    void testResetConfigCache() {
        List<SysConfig> configList = Arrays.asList(testConfig);
        when(unifiedCacheManager.keys(anyString())).thenReturn(Collections.singleton("sys_config:key"));
        when(configMapper.selectConfigList(any(SysConfig.class))).thenReturn(configList);

        configService.resetConfigCache();

        verify(unifiedCacheManager, times(1)).delete(anyCollection());
        verify(unifiedCacheManager, times(1)).set(anyString(), eq("true"));
    }

    /**
     * 测试校验参数键名是否唯一 - 唯一
     */
    @Test
    void testCheckConfigKeyUnique_Unique() {
        when(configMapper.checkConfigKeyUnique(anyString())).thenReturn(null);

        boolean result = configService.checkConfigKeyUnique(testConfig);

        assertTrue(result);
    }

    /**
     * 测试校验参数键名是否唯一 - 不唯一
     */
    @Test
    void testCheckConfigKeyUnique_NotUnique() {
        SysConfig existingConfig = new SysConfig();
        existingConfig.setConfigId(2L);

        when(configMapper.checkConfigKeyUnique(anyString())).thenReturn(existingConfig);

        boolean result = configService.checkConfigKeyUnique(testConfig);

        assertFalse(result);
    }

    /**
     * 测试校验参数键名是否唯一 - 相同ID
     */
    @Test
    void testCheckConfigKeyUnique_SameId() {
        SysConfig existingConfig = new SysConfig();
        existingConfig.setConfigId(1L);

        when(configMapper.checkConfigKeyUnique(anyString())).thenReturn(existingConfig);

        boolean result = configService.checkConfigKeyUnique(testConfig);

        assertTrue(result);
    }
}