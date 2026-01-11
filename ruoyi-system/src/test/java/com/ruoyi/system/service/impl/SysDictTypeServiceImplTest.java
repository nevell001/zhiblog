package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.system.mapper.SysDictTypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 字典业务层处理单元测试
 *
 * @author test
 * @date 2026-01-11
 */
@ExtendWith(MockitoExtension.class)
class SysDictTypeServiceImplTest {

    @Mock
    private SysDictTypeMapper dictTypeMapper;

    @Mock
    private SysDictDataMapper dictDataMapper;

    @InjectMocks
    private SysDictTypeServiceImpl dictTypeService;

    private SysDictType testDictType;

    private SysDictData testDictData;

    @BeforeEach
    void setUp() {
        testDictType = new SysDictType();
        testDictType.setDictId(1L);
        testDictType.setDictType("sys_user_sex");
        testDictType.setDictName("用户性别");

        testDictData = new SysDictData();
        testDictData.setDictCode(1L);
        testDictData.setDictType("sys_user_sex");
        testDictData.setDictValue("0");
        testDictData.setDictLabel("男");
        testDictData.setDictSort(1L);
    }

    /**
     * 测试根据条件分页查询字典类型
     */
    @Test
    void testSelectDictTypeList() {
        List<SysDictType> dictTypeList = Arrays.asList(testDictType);
        when(dictTypeMapper.selectDictTypeList(any(SysDictType.class))).thenReturn(dictTypeList);

        List<SysDictType> result = dictTypeService.selectDictTypeList(new SysDictType());

        assertEquals(1, result.size());
        assertEquals("用户性别", result.get(0).getDictName());
        verify(dictTypeMapper, times(1)).selectDictTypeList(any(SysDictType.class));
    }

    /**
     * 测试根据所有字典类型
     */
    @Test
    void testSelectDictTypeAll() {
        List<SysDictType> dictTypeList = Arrays.asList(testDictType);
        when(dictTypeMapper.selectDictTypeAll()).thenReturn(dictTypeList);

        List<SysDictType> result = dictTypeService.selectDictTypeAll();

        assertEquals(1, result.size());
        verify(dictTypeMapper, times(1)).selectDictTypeAll();
    }

    /**
     * 测试根据字典类型查询字典数据 - 从缓存获取
     */
    @Test
    void testSelectDictDataByType_FromCache() {
        List<SysDictData> cachedData = Arrays.asList(testDictData);

        try (MockedStatic<com.ruoyi.common.utils.DictUtils> mockedDictUtils = mockStatic(com.ruoyi.common.utils.DictUtils.class)) {
            mockedDictUtils.when(() -> com.ruoyi.common.utils.DictUtils.getDictCache("sys_user_sex")).thenReturn(cachedData);

            List<SysDictData> result = dictTypeService.selectDictDataByType("sys_user_sex");

            assertEquals(1, result.size());
            assertEquals("男", result.get(0).getDictLabel());
            verify(dictDataMapper, never()).selectDictDataByType(anyString());
        }
    }

    /**
     * 测试根据字典类型查询字典数据 - 从数据库获取
     */
    @Test
    void testSelectDictDataByType_FromDatabase() {
        List<SysDictData> dbData = Arrays.asList(testDictData);

        try (MockedStatic<com.ruoyi.common.utils.DictUtils> mockedDictUtils = mockStatic(com.ruoyi.common.utils.DictUtils.class)) {
            mockedDictUtils.when(() -> com.ruoyi.common.utils.DictUtils.getDictCache("sys_user_sex")).thenReturn(null);
            when(dictDataMapper.selectDictDataByType("sys_user_sex")).thenReturn(dbData);

            List<SysDictData> result = dictTypeService.selectDictDataByType("sys_user_sex");

            assertEquals(1, result.size());
            verify(dictDataMapper, times(1)).selectDictDataByType("sys_user_sex");
            mockedDictUtils.verify(() -> com.ruoyi.common.utils.DictUtils.setDictCache(eq("sys_user_sex"), anyList()));
        }
    }

    /**
     * 测试根据字典类型查询字典数据 - 不存在
     */
    @Test
    void testSelectDictDataByType_NotFound() {
        try (MockedStatic<com.ruoyi.common.utils.DictUtils> mockedDictUtils = mockStatic(com.ruoyi.common.utils.DictUtils.class)) {
            mockedDictUtils.when(() -> com.ruoyi.common.utils.DictUtils.getDictCache("non_existent")).thenReturn(null);
            when(dictDataMapper.selectDictDataByType("non_existent")).thenReturn(Collections.emptyList());

            List<SysDictData> result = dictTypeService.selectDictDataByType("non_existent");

            assertNull(result);
        }
    }

    /**
     * 测试根据字典类型ID查询信息
     */
    @Test
    void testSelectDictTypeById() {
        when(dictTypeMapper.selectDictTypeById(1L)).thenReturn(testDictType);

        SysDictType result = dictTypeService.selectDictTypeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getDictId());
        assertEquals("用户性别", result.getDictName());
        verify(dictTypeMapper, times(1)).selectDictTypeById(1L);
    }

    /**
     * 测试根据字典类型查询信息
     */
    @Test
    void testSelectDictTypeByType() {
        when(dictTypeMapper.selectDictTypeByType("sys_user_sex")).thenReturn(testDictType);

        SysDictType result = dictTypeService.selectDictTypeByType("sys_user_sex");

        assertNotNull(result);
        assertEquals("sys_user_sex", result.getDictType());
        verify(dictTypeMapper, times(1)).selectDictTypeByType("sys_user_sex");
    }

    /**
     * 测试批量删除字典类型信息
     */
    @Test
    void testDeleteDictTypeByIds() {
        when(dictTypeMapper.selectDictTypeById(1L)).thenReturn(testDictType);
        when(dictDataMapper.countDictDataByType("sys_user_sex")).thenReturn(0);
        when(dictTypeMapper.deleteDictTypeById(1L)).thenReturn(1);

        try (MockedStatic<com.ruoyi.common.utils.DictUtils> mockedDictUtils = mockStatic(com.ruoyi.common.utils.DictUtils.class)) {
            dictTypeService.deleteDictTypeByIds(new Long[]{1L});

            verify(dictTypeMapper, times(1)).deleteDictTypeById(1L);
            mockedDictUtils.verify(() -> com.ruoyi.common.utils.DictUtils.removeDictCache("sys_user_sex"));
        }
    }

    /**
     * 测试批量删除字典类型信息 - 已分配（应抛出异常）
     */
    @Test
    void testDeleteDictTypeByIds_Allocated() {
        when(dictTypeMapper.selectDictTypeById(1L)).thenReturn(testDictType);
        when(dictDataMapper.countDictDataByType("sys_user_sex")).thenReturn(1);

        assertThrows(ServiceException.class, () -> {
            dictTypeService.deleteDictTypeByIds(new Long[]{1L});
        });

        verify(dictTypeMapper, never()).deleteDictTypeById(anyLong());
    }

    /**
     * 测试加载字典缓存数据
     */
    @Test
    void testLoadingDictCache() {
        List<SysDictData> dictDataList = Arrays.asList(testDictData);
        when(dictDataMapper.selectDictDataList(any(SysDictData.class))).thenReturn(dictDataList);

        try (MockedStatic<com.ruoyi.common.utils.DictUtils> mockedDictUtils = mockStatic(com.ruoyi.common.utils.DictUtils.class)) {
            dictTypeService.loadingDictCache();

            mockedDictUtils.verify(() -> com.ruoyi.common.utils.DictUtils.setDictCache(eq("sys_user_sex"), anyList()));
        }
    }

    /**
     * 测试清空字典缓存数据
     */
    @Test
    void testClearDictCache() {
        try (MockedStatic<com.ruoyi.common.utils.DictUtils> mockedDictUtils = mockStatic(com.ruoyi.common.utils.DictUtils.class)) {
            dictTypeService.clearDictCache();

            mockedDictUtils.verify(com.ruoyi.common.utils.DictUtils::clearDictCache);
        }
    }

    /**
     * 测试重置字典缓存数据
     */
    @Test
    void testResetDictCache() {
        List<SysDictData> dictDataList = Arrays.asList(testDictData);
        when(dictDataMapper.selectDictDataList(any(SysDictData.class))).thenReturn(dictDataList);

        try (MockedStatic<com.ruoyi.common.utils.DictUtils> mockedDictUtils = mockStatic(com.ruoyi.common.utils.DictUtils.class)) {
            dictTypeService.resetDictCache();

            mockedDictUtils.verify(com.ruoyi.common.utils.DictUtils::clearDictCache);
            mockedDictUtils.verify(() -> com.ruoyi.common.utils.DictUtils.setDictCache(eq("sys_user_sex"), anyList()));
        }
    }

    /**
     * 测试新增保存字典类型信息
     */
    @Test
    void testInsertDictType() {
        when(dictTypeMapper.insertDictType(any(SysDictType.class))).thenReturn(1);

        try (MockedStatic<com.ruoyi.common.utils.DictUtils> mockedDictUtils = mockStatic(com.ruoyi.common.utils.DictUtils.class)) {
            int result = dictTypeService.insertDictType(testDictType);

            assertEquals(1, result);
            mockedDictUtils.verify(() -> com.ruoyi.common.utils.DictUtils.setDictCache("sys_user_sex", null));
        }
    }

    /**
     * 测试修改保存字典类型信息
     */
    @Test
    void testUpdateDictType() {
        SysDictType oldDict = new SysDictType();
        oldDict.setDictId(1L);
        oldDict.setDictType("old_type");

        List<SysDictData> dictDataList = Arrays.asList(testDictData);

        when(dictTypeMapper.selectDictTypeById(1L)).thenReturn(oldDict);
        when(dictDataMapper.updateDictDataType("old_type", "sys_user_sex")).thenReturn(1);
        when(dictTypeMapper.updateDictType(any(SysDictType.class))).thenReturn(1);
        when(dictDataMapper.selectDictDataByType("sys_user_sex")).thenReturn(dictDataList);

        try (MockedStatic<com.ruoyi.common.utils.DictUtils> mockedDictUtils = mockStatic(com.ruoyi.common.utils.DictUtils.class)) {
            int result = dictTypeService.updateDictType(testDictType);

            assertEquals(1, result);
            verify(dictDataMapper, times(1)).updateDictDataType("old_type", "sys_user_sex");
            mockedDictUtils.verify(() -> com.ruoyi.common.utils.DictUtils.setDictCache(eq("sys_user_sex"), anyList()));
        }
    }

    /**
     * 测试校验字典类型称是否唯一 - 唯一
     */
    @Test
    void testCheckDictTypeUnique_Unique() {
        when(dictTypeMapper.checkDictTypeUnique("sys_user_sex")).thenReturn(null);

        boolean result = dictTypeService.checkDictTypeUnique(testDictType);

        assertTrue(result);
    }

    /**
     * 测试校验字典类型称是否唯一 - 不唯一
     */
    @Test
    void testCheckDictTypeUnique_NotUnique() {
        SysDictType existingDict = new SysDictType();
        existingDict.setDictId(2L);

        when(dictTypeMapper.checkDictTypeUnique("sys_user_sex")).thenReturn(existingDict);

        boolean result = dictTypeService.checkDictTypeUnique(testDictType);

        assertFalse(result);
    }

    /**
     * 测试校验字典类型称是否唯一 - 相同ID
     */
    @Test
    void testCheckDictTypeUnique_SameId() {
        SysDictType existingDict = new SysDictType();
        existingDict.setDictId(1L);

        when(dictTypeMapper.checkDictTypeUnique("sys_user_sex")).thenReturn(existingDict);

        boolean result = dictTypeService.checkDictTypeUnique(testDictType);

        assertTrue(result);
    }
}