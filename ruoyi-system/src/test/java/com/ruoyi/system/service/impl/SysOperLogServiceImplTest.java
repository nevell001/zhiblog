package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.mapper.SysOperLogMapper;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 操作日志服务层单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class SysOperLogServiceImplTest {

    @Mock
    private SysOperLogMapper operLogMapper;

    @InjectMocks
    private SysOperLogServiceImpl sysOperLogService;

    private SysOperLog testOperLog;

    @BeforeEach
    void setUp() {
        testOperLog = new SysOperLog();
        testOperLog.setOperId(1L);
        testOperLog.setTitle("测试操作");
        testOperLog.setOperName("admin");
        testOperLog.setOperIp("127.0.0.1");
        testOperLog.setStatus(0);
    }

    /**
     * 测试新增操作日志
     */
    @Test
    void testInsertOperlog() {
        sysOperLogService.insertOperlog(testOperLog);

        verify(operLogMapper).insertOperlog(any(SysOperLog.class));
    }

    /**
     * 测试查询系统操作日志集合
     */
    @Test
    void testSelectOperLogList() {
        List<SysOperLog> operLogList = Arrays.asList(testOperLog);
        when(operLogMapper.selectOperLogList(any(SysOperLog.class))).thenReturn(operLogList);

        List<SysOperLog> result = sysOperLogService.selectOperLogList(new SysOperLog());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试操作", result.get(0).getTitle());
        verify(operLogMapper).selectOperLogList(any(SysOperLog.class));
    }

    /**
     * 测试查询系统操作日志集合 - 空结果
     */
    @Test
    void testSelectOperLogList_Empty() {
        when(operLogMapper.selectOperLogList(any(SysOperLog.class))).thenReturn(Collections.emptyList());

        List<SysOperLog> result = sysOperLogService.selectOperLogList(new SysOperLog());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(operLogMapper).selectOperLogList(any(SysOperLog.class));
    }

    /**
     * 测试批量删除系统操作日志 - 成功
     */
    @Test
    void testDeleteOperLogByIds_Success() {
        when(operLogMapper.deleteOperLogByIds(any(Long[].class))).thenReturn(2);

        int result = sysOperLogService.deleteOperLogByIds(new Long[]{1L, 2L});

        assertEquals(2, result);
        verify(operLogMapper).deleteOperLogByIds(any(Long[].class));
    }

    /**
     * 测试批量删除系统操作日志 - 空数组
     */
    @Test
    void testDeleteOperLogByIds_Empty() {
        when(operLogMapper.deleteOperLogByIds(any(Long[].class))).thenReturn(0);

        int result = sysOperLogService.deleteOperLogByIds(new Long[]{});

        assertEquals(0, result);
        verify(operLogMapper).deleteOperLogByIds(any(Long[].class));
    }

    /**
     * 测试批量删除系统操作日志 - 失败
     */
    @Test
    void testDeleteOperLogByIds_Failure() {
        when(operLogMapper.deleteOperLogByIds(any(Long[].class))).thenReturn(0);

        int result = sysOperLogService.deleteOperLogByIds(new Long[]{1L, 2L});

        assertEquals(0, result);
        verify(operLogMapper).deleteOperLogByIds(any(Long[].class));
    }

    /**
     * 测试查询操作日志详细
     */
    @Test
    void testSelectOperLogById() {
        when(operLogMapper.selectOperLogById(1L)).thenReturn(testOperLog);

        SysOperLog result = sysOperLogService.selectOperLogById(1L);

        assertNotNull(result);
        assertEquals("测试操作", result.getTitle());
        verify(operLogMapper).selectOperLogById(1L);
    }

    /**
     * 测试查询操作日志详细 - 不存在
     */
    @Test
    void testSelectOperLogById_NotFound() {
        when(operLogMapper.selectOperLogById(999L)).thenReturn(null);

        SysOperLog result = sysOperLogService.selectOperLogById(999L);

        assertNull(result);
        verify(operLogMapper).selectOperLogById(999L);
    }

    /**
     * 测试清空操作日志
     */
    @Test
    void testCleanOperLog() {
        sysOperLogService.cleanOperLog();

        verify(operLogMapper).cleanOperLog();
    }
}