package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.mapper.SysNoticeMapper;
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
 * 公告服务层单元测试
 *
 * @author test
 * @date 2025-07-18
 */
@ExtendWith(MockitoExtension.class)
class SysNoticeServiceImplTest {

    @Mock
    private SysNoticeMapper noticeMapper;

    @InjectMocks
    private SysNoticeServiceImpl sysNoticeService;

    private SysNotice testNotice;

    @BeforeEach
    void setUp() {
        testNotice = new SysNotice();
        testNotice.setNoticeId(1L);
        testNotice.setNoticeTitle("测试公告");
        testNotice.setNoticeContent("测试公告内容");
        testNotice.setNoticeType("1");
    }

    /**
     * 测试查询公告信息
     */
    @Test
    void testSelectNoticeById() {
        when(noticeMapper.selectNoticeById(1L)).thenReturn(testNotice);

        SysNotice result = sysNoticeService.selectNoticeById(1L);

        assertNotNull(result);
        assertEquals("测试公告", result.getNoticeTitle());
        verify(noticeMapper).selectNoticeById(1L);
    }

    /**
     * 测试查询公告信息 - 不存在
     */
    @Test
    void testSelectNoticeById_NotFound() {
        when(noticeMapper.selectNoticeById(999L)).thenReturn(null);

        SysNotice result = sysNoticeService.selectNoticeById(999L);

        assertNull(result);
        verify(noticeMapper).selectNoticeById(999L);
    }

    /**
     * 测试查询公告列表
     */
    @Test
    void testSelectNoticeList() {
        List<SysNotice> noticeList = Arrays.asList(testNotice);
        when(noticeMapper.selectNoticeList(any(SysNotice.class))).thenReturn(noticeList);

        List<SysNotice> result = sysNoticeService.selectNoticeList(new SysNotice());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试公告", result.get(0).getNoticeTitle());
        verify(noticeMapper).selectNoticeList(any(SysNotice.class));
    }

    /**
     * 测试查询公告列表 - 空结果
     */
    @Test
    void testSelectNoticeList_Empty() {
        when(noticeMapper.selectNoticeList(any(SysNotice.class))).thenReturn(Collections.emptyList());

        List<SysNotice> result = sysNoticeService.selectNoticeList(new SysNotice());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(noticeMapper).selectNoticeList(any(SysNotice.class));
    }

    /**
     * 测试新增公告 - 成功
     */
    @Test
    void testInsertNotice_Success() {
        when(noticeMapper.insertNotice(any(SysNotice.class))).thenReturn(1);

        int result = sysNoticeService.insertNotice(testNotice);

        assertEquals(1, result);
        verify(noticeMapper).insertNotice(any(SysNotice.class));
    }

    /**
     * 测试新增公告 - 失败
     */
    @Test
    void testInsertNotice_Failure() {
        when(noticeMapper.insertNotice(any(SysNotice.class))).thenReturn(0);

        int result = sysNoticeService.insertNotice(testNotice);

        assertEquals(0, result);
        verify(noticeMapper).insertNotice(any(SysNotice.class));
    }

    /**
     * 测试修改公告 - 成功
     */
    @Test
    void testUpdateNotice_Success() {
        when(noticeMapper.updateNotice(any(SysNotice.class))).thenReturn(1);

        int result = sysNoticeService.updateNotice(testNotice);

        assertEquals(1, result);
        verify(noticeMapper).updateNotice(any(SysNotice.class));
    }

    /**
     * 测试修改公告 - 失败
     */
    @Test
    void testUpdateNotice_Failure() {
        when(noticeMapper.updateNotice(any(SysNotice.class))).thenReturn(0);

        int result = sysNoticeService.updateNotice(testNotice);

        assertEquals(0, result);
        verify(noticeMapper).updateNotice(any(SysNotice.class));
    }

    /**
     * 测试删除公告对象 - 成功
     */
    @Test
    void testDeleteNoticeById_Success() {
        when(noticeMapper.deleteNoticeById(1L)).thenReturn(1);

        int result = sysNoticeService.deleteNoticeById(1L);

        assertEquals(1, result);
        verify(noticeMapper).deleteNoticeById(1L);
    }

    /**
     * 测试删除公告对象 - 不存在
     */
    @Test
    void testDeleteNoticeById_NotFound() {
        when(noticeMapper.deleteNoticeById(999L)).thenReturn(0);

        int result = sysNoticeService.deleteNoticeById(999L);

        assertEquals(0, result);
        verify(noticeMapper).deleteNoticeById(999L);
    }

    /**
     * 测试批量删除公告信息 - 成功
     */
    @Test
    void testDeleteNoticeByIds_Success() {
        when(noticeMapper.deleteNoticeByIds(any(Long[].class))).thenReturn(2);

        int result = sysNoticeService.deleteNoticeByIds(new Long[]{1L, 2L});

        assertEquals(2, result);
        verify(noticeMapper).deleteNoticeByIds(any(Long[].class));
    }

    /**
     * 测试批量删除公告信息 - 空数组
     */
    @Test
    void testDeleteNoticeByIds_Empty() {
        when(noticeMapper.deleteNoticeByIds(any(Long[].class))).thenReturn(0);

        int result = sysNoticeService.deleteNoticeByIds(new Long[]{});

        assertEquals(0, result);
        verify(noticeMapper).deleteNoticeByIds(any(Long[].class));
    }

    /**
     * 测试批量删除公告信息 - 失败
     */
    @Test
    void testDeleteNoticeByIds_Failure() {
        when(noticeMapper.deleteNoticeByIds(any(Long[].class))).thenReturn(0);

        int result = sysNoticeService.deleteNoticeByIds(new Long[]{1L, 2L});

        assertEquals(0, result);
        verify(noticeMapper).deleteNoticeByIds(any(Long[].class));
    }
}