package com.zhi.common.core.page;

import com.zhi.common.core.text.Convert;
import com.zhi.common.utils.ServletUtils;

/**
 * 表格数据处理
 * 
 * @author ruoyi
 */
public class TableSupport
{
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 分页参数合理化
     */
    public static final String REASONABLE = "reasonable";

    /** 单页最大条数（前端分页器最多给到 50，这里留出余量） */
    public static final int MAX_PAGE_SIZE = 100;

    /**
     * 封装分页对象
     */
    public static PageDomain getPageDomain()
    {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(Convert.toInt(ServletUtils.getParameter(PAGE_NUM), 1));
        // 上限 100：pageSize 完全由请求参数控制，不设上限时 ?pageSize=100000 会把整表（含正文）拉进内存
        // 上限 100：pageSize 完全由请求参数控制，不设上限时 ?pageSize=100000 会把整表（含正文）拉进内存
        pageDomain.setPageSize(Math.min(Math.max(Convert.toInt(ServletUtils.getParameter(PAGE_SIZE), 10), 1), MAX_PAGE_SIZE));
        pageDomain.setOrderByColumn(ServletUtils.getParameter(ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtils.getParameter(IS_ASC));
        pageDomain.setReasonable(ServletUtils.getParameterToBool(REASONABLE));
        return pageDomain;
    }

    public static PageDomain buildPageRequest()
    {
        return getPageDomain();
    }
}
