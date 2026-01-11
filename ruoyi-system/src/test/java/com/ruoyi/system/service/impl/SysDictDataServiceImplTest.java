package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.system.mapper.SysDictDataMapper;
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
class SysDictDataServiceImplTest {

    @Mock
    private SysDictDataMapper dictDataMapper;

    @InjectMocks
    private SysDictDataServiceImpl dictDataService;

    private SysDictData testDictData;

    @BeforeEach
    void setUp() {
        testDictData = new SysDictData();
        testDictData.setDictCode(1L);
        testDictData.setDictType("sys_user_sex");
        testDictData.setDictValue("0");
        testDictData.setDictLabel("男");
    }

    /**
     * 测试根据条件分页查询字典数据
     */
    @Test
    void testSelectDictDataList() {
        List<SysDictData> dictDataList = Arrays.asList(testDictData);
        when(dictDataMapper.selectDictDataList(any(SysDictData.class))).thenReturn(dictDataList);

        List<SysDictData> result = dictDataService.selectDictDataList(new SysDictData());

        assertEquals(1, result.size());
        assertEquals("男", result.get(0).getDictLabel());
        verify(dictDataMapper, times(1)).selectDictDataList(any(SysDictData.class));
    }

    /**
     * 测试根据字典类型和字典键值查询字典标签
     */
    @Test
    void testSelectDictLabel() {
        when(dictDataMapper.selectDictLabel("sys_user_sex", "0")).thenReturn("男");

        String result = dictDataService.selectDictLabel("sys_user_sex", "0");

        assertEquals("男", result);
        verify(dictDataMapper, times(1)).selectDictLabel("sys_user_sex", "0");
    }

    /**
     * 测试根据字典数据ID查询信息
     */
    @Test
    void testSelectDictDataById() {
        when(dictDataMapper.selectDictDataById(1L)).thenReturn(testDictData);

        SysDictData result = dictDataService.selectDictDataById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getDictCode());
        assertEquals("sys_user_sex", result.getDictType());
        verify(dictDataMapper, times(1)).selectDictDataById(1L);
    }

    /**
     * 测试批量删除字典数据信息
     */
    @Test
    void testDeleteDictDataByIds() {
        when(dictDataMapper.selectDictDataById(1L)).thenReturn(testDictData);
        when(dictDataMapper.selectDictDataByType("sys_user_sex")).thenReturn(Collections.emptyList());
        when(dictDataMapper.deleteDictDataById(1L)).thenReturn(1);

        try (MockedStatic<com.ruoyi.common.utils.DictUtils> mockedDictUtils = mockStatic(com.ruoyi.common.utils.DictUtils.class)) {
            dictDataService.deleteDictDataByIds(new Long[]{1L});

            verify(dictDataMapper, times(1)).deleteDictDataById(1L);
            mockedDictUtils.verify(() -> com.ruoyi.common.utils.DictUtils.setDictCache(eq("sys_user_sex"), anyList()));
        }
    }

    /**
     * 测试新增保存字典数据信息
     */
    @Test
    void testInsertDictData() {
        when(dictDataMapper.insertDictData(any(SysDictData.class))).thenReturn(1);
        when(dictDataMapper.selectDictDataByType("sys_user_sex")).thenReturn(Collections.emptyList());

        try (MockedStatic<com.ruoyi.common.utils.DictUtils> mockedDictUtils = mockStatic(com.ruoyi.common.utils.DictUtils.class)) {
            int result = dictDataService.insertDictData(testDictData);

            assertEquals(1, result);
            mockedDictUtils.verify(() -> com.ruoyi.common.utils.DictUtils.setDictCache(eq("sys_user_sex"), anyList()));
        }
    }

    /**
     * 测试修改保存字典数据信息
     */
    @Test
    void testUpdateDictData() {
        when(dictDataMapper.updateDictData(any(SysDictData.class))).thenReturn(1);
        when(dictDataMapper.selectDictDataByType("sys_user_sex")).thenReturn(Collections.emptyList());

        try (MockedStatic<com.ruoyi.common.utils.DictUtils> mockedDictUtils = mockStatic(com.ruoyi.common.utils.DictUtils.class)) {
            int result = dictDataService.updateDictData(testDictData);

            assertEquals(1, result);
            mockedDictUtils.verify(() -> com.ruoyi.common.utils.DictUtils.setDictCache(eq("sys_user_sex"), anyList()));
        }
    }

    /**
     * 测试查询空列表
     */
    @Test
    void testSelectDictDataList_Empty() {
        when(dictDataMapper.selectDictDataList(any(SysDictData.class))).thenReturn(Collections.emptyList());

        List<SysDictData> result = dictDataService.selectDictDataList(new SysDictData());

        assertTrue(result.isEmpty());
    }

    /**
     * 测试查询不存在的字典标签
     */
    @Test
    void testSelectDictLabel_NotFound() {
        when(dictDataMapper.selectDictLabel("sys_user_sex", "99")).thenReturn(null);

        String result = dictDataService.selectDictLabel("sys_user_sex", "99");

        assertNull(result);
    }

    /**
     * 测试查询不存在的字典数据ID
     */
    @Test
    void testSelectDictDataById_NotFound() {
        when(dictDataMapper.selectDictDataById(999L)).thenReturn(null);

        SysDictData result = dictDataService.selectDictDataById(999L);

        assertNull(result);
    }

    /**
     * 测试批量删除多个字典数据
     */
    @Test
    void testDeleteDictDataByIds_Multiple() {
        SysDictData dictData2 = new SysDictData();
        dictData2.setDictCode(2L);
        dictData2.setDictType("sys_user_sex");

        when(dictDataMapper.selectDictDataById(1L)).thenReturn(testDictData);
        when(dictDataMapper.selectDictDataById(2L)).thenReturn(dictData2);
        when(dictDataMapper.selectDictDataByType("sys_user_sex")).thenReturn(Collections.emptyList());
        when(dictDataMapper.deleteDictDataById(anyLong())).thenReturn(1);

        try (MockedStatic<com.ruoyi.common.utils.DictUtils> mockedDictUtils = mockStatic(com.ruoyi.common.utils.DictUtils.class)) {
            dictDataService.deleteDictDataByIds(new Long[]{1L, 2L});

            verify(dictDataMapper, times(2)).deleteDictDataById(anyLong());
        }
    }
}