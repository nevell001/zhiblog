package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.mapper.SysLogininforMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 系统访问日志情况信息服务层单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class SysLogininforServiceImplTest {

    @Mock
    private SysLogininforMapper logininforMapper;

    @InjectMocks
    private SysLogininforServiceImpl sysLogininforService;

    private SysLogininfor testLogininfor;

    @BeforeEach
    void setUp() {
        testLogininfor = new SysLogininfor();
        testLogininfor.setInfoId(1L);
        testLogininfor.setUserName("admin");
        testLogininfor.setIpaddr("127.0.0.1");
        testLogininfor.setStatus("0");
    }

    /**
     * 测试新增系统登录日志
     */
    @Test
    void testInsertLogininfor() {
        sysLogininforService.insertLogininfor(testLogininfor);

        verify(logininforMapper).insertLogininfor(any(SysLogininfor.class));
    }

    /**
     * 测试查询系统登录日志集合
     */
    @Test
    void testSelectLogininforList() {
        List<SysLogininfor> logininforList = Arrays.asList(testLogininfor);
        when(logininforMapper.selectLogininforList(any(SysLogininfor.class))).thenReturn(logininforList);

        List<SysLogininfor> result = sysLogininforService.selectLogininforList(new SysLogininfor());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("admin", result.get(0).getUserName());
        verify(logininforMapper).selectLogininforList(any(SysLogininfor.class));
    }

    /**
     * 测试查询系统登录日志集合 - 空结果
     */
    @Test
    void testSelectLogininforList_Empty() {
        when(logininforMapper.selectLogininforList(any(SysLogininfor.class))).thenReturn(Collections.emptyList());

        List<SysLogininfor> result = sysLogininforService.selectLogininforList(new SysLogininfor());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(logininforMapper).selectLogininforList(any(SysLogininfor.class));
    }

    /**
     * 测试批量删除系统登录日志 - 成功
     */
    @Test
    void testDeleteLogininforByIds_Success() {
        when(logininforMapper.deleteLogininforByIds(any(Long[].class))).thenReturn(2);

        int result = sysLogininforService.deleteLogininforByIds(new Long[]{1L, 2L});

        assertEquals(2, result);
        verify(logininforMapper).deleteLogininforByIds(any(Long[].class));
    }

    /**
     * 测试批量删除系统登录日志 - 空数组
     */
    @Test
    void testDeleteLogininforByIds_Empty() {
        when(logininforMapper.deleteLogininforByIds(any(Long[].class))).thenReturn(0);

        int result = sysLogininforService.deleteLogininforByIds(new Long[]{});

        assertEquals(0, result);
        verify(logininforMapper).deleteLogininforByIds(any(Long[].class));
    }

    /**
     * 测试批量删除系统登录日志 - 失败
     */
    @Test
    void testDeleteLogininforByIds_Failure() {
        when(logininforMapper.deleteLogininforByIds(any(Long[].class))).thenReturn(0);

        int result = sysLogininforService.deleteLogininforByIds(new Long[]{1L, 2L});

        assertEquals(0, result);
        verify(logininforMapper).deleteLogininforByIds(any(Long[].class));
    }

    /**
     * 测试清空系统登录日志
     */
    @Test
    void testCleanLogininfor() {
        sysLogininforService.cleanLogininfor();

        verify(logininforMapper).cleanLogininfor();
    }

    /**
     * 测试获取用户活跃度（按月统计登录次数）
     */
    @Test
    void testSelectUserActivity() {
        List<Map<String, Object>> activityList = Arrays.asList(
            Collections.singletonMap("month", "2025-01"),
            Collections.singletonMap("count", 100)
        );
        when(logininforMapper.selectUserActivity()).thenReturn(activityList);

        List<Map<String, Object>> result = sysLogininforService.selectUserActivity();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(logininforMapper).selectUserActivity();
    }

    /**
     * 测试获取用户活跃度 - 空结果
     */
    @Test
    void testSelectUserActivity_Empty() {
        when(logininforMapper.selectUserActivity()).thenReturn(Collections.emptyList());

        List<Map<String, Object>> result = sysLogininforService.selectUserActivity();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(logininforMapper).selectUserActivity();
    }

    /**
     * 测试获取今日登录次数
     */
    @Test
    void testSelectTodayLoginCount() {
        when(logininforMapper.selectTodayLoginCount()).thenReturn(10L);

        Long result = sysLogininforService.selectTodayLoginCount();

        assertEquals(10L, result);
        verify(logininforMapper).selectTodayLoginCount();
    }

    /**
     * 测试获取今日登录次数 - 无登录
     */
    @Test
    void testSelectTodayLoginCount_Zero() {
        when(logininforMapper.selectTodayLoginCount()).thenReturn(0L);

        Long result = sysLogininforService.selectTodayLoginCount();

        assertEquals(0L, result);
        verify(logininforMapper).selectTodayLoginCount();
    }
}