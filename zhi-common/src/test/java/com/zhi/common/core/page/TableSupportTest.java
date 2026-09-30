package com.zhi.common.core.page;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 分页参数解析测试。
 *
 * <p>pageSize 完全由请求参数决定，不设上限时 {@code ?pageSize=100000} 会把整张表
 * （含 longtext 正文）拉进内存，是可被单请求触发的内存放大。</p>
 */
class TableSupportTest
{
    @AfterEach
    void tearDown()
    {
        RequestContextHolder.resetRequestAttributes();
    }

    private void bindRequest(String pageSize)
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        if (pageSize != null)
        {
            request.setParameter("pageSize", pageSize);
        }
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    @DisplayName("超大 pageSize 必须被压到上限")
    void clampsHugePageSize()
    {
        bindRequest("100000");
        assertEquals(TableSupport.MAX_PAGE_SIZE, TableSupport.getPageDomain().getPageSize());
    }

    @Test
    @DisplayName("非正数 pageSize 归一到 1")
    void normalizesNonPositivePageSize()
    {
        bindRequest("0");
        assertEquals(Integer.valueOf(1), TableSupport.getPageDomain().getPageSize());

        bindRequest("-5");
        assertEquals(Integer.valueOf(1), TableSupport.getPageDomain().getPageSize());
    }

    @Test
    @DisplayName("正常值与默认值保持不变")
    void keepsNormalPageSize()
    {
        bindRequest(null);
        assertEquals(Integer.valueOf(10), TableSupport.getPageDomain().getPageSize());

        bindRequest("50");
        assertEquals(Integer.valueOf(50), TableSupport.getPageDomain().getPageSize());
    }
}
