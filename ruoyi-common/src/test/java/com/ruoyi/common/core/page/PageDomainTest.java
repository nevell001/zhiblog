package com.ruoyi.common.core.page;

import com.ruoyi.common.utils.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PageDomain 测试类
 */
class PageDomainTest {

    private PageDomain pageDomain;

    @BeforeEach
    void setUp() {
        pageDomain = new PageDomain();
    }

    @Test
    void testGetPageNum_DefaultValue() {
        assertNull(pageDomain.getPageNum());
    }

    @Test
    void testSetPageNum() {
        pageDomain.setPageNum(1);
        assertEquals(1, pageDomain.getPageNum());
    }

    @Test
    void testSetPageNum_Null() {
        pageDomain.setPageNum(null);
        assertNull(pageDomain.getPageNum());
    }

    @Test
    void testGetPageSize_DefaultValue() {
        assertNull(pageDomain.getPageSize());
    }

    @Test
    void testSetPageSize() {
        pageDomain.setPageSize(10);
        assertEquals(10, pageDomain.getPageSize());
    }

    @Test
    void testSetPageSize_Null() {
        pageDomain.setPageSize(null);
        assertNull(pageDomain.getPageSize());
    }

    @Test
    void testGetOrderByColumn_DefaultValue() {
        assertNull(pageDomain.getOrderByColumn());
    }

    @Test
    void testSetOrderByColumn() {
        pageDomain.setOrderByColumn("createTime");
        assertEquals("createTime", pageDomain.getOrderByColumn());
    }

    @Test
    void testSetOrderByColumn_Null() {
        pageDomain.setOrderByColumn(null);
        assertNull(pageDomain.getOrderByColumn());
    }

    @Test
    void testGetIsAsc_DefaultValue() {
        assertEquals("asc", pageDomain.getIsAsc());
    }

    @Test
    void testSetIsAsc_Asc() {
        pageDomain.setIsAsc("asc");
        assertEquals("asc", pageDomain.getIsAsc());
    }

    @Test
    void testSetIsAsc_Desc() {
        pageDomain.setIsAsc("desc");
        assertEquals("desc", pageDomain.getIsAsc());
    }

    @Test
    void testSetIsAsc_Ascending() {
        pageDomain.setIsAsc("ascending");
        assertEquals("asc", pageDomain.getIsAsc());
    }

    @Test
    void testSetIsAsc_Descending() {
        pageDomain.setIsAsc("descending");
        assertEquals("desc", pageDomain.getIsAsc());
    }

    @Test
    void testSetIsAsc_Empty() {
        pageDomain.setIsAsc("");
        assertEquals("asc", pageDomain.getIsAsc());
    }

    @Test
    void testSetIsAsc_Null() {
        pageDomain.setIsAsc(null);
        assertEquals("asc", pageDomain.getIsAsc());
    }

    @Test
    void testSetIsAsc_CaseInsensitive() {
        pageDomain.setIsAsc("ASC");
        assertEquals("ASC", pageDomain.getIsAsc());
        
        pageDomain.setIsAsc("DESC");
        assertEquals("DESC", pageDomain.getIsAsc());
    }

    @Test
    void testGetOrderBy_WithOrderByColumn() {
        pageDomain.setOrderByColumn("createTime");
        String orderBy = pageDomain.getOrderBy();
        assertEquals("create_time asc", orderBy);
    }

    @Test
    void testGetOrderBy_WithOrderByColumnAndDesc() {
        pageDomain.setOrderByColumn("createTime");
        pageDomain.setIsAsc("desc");
        String orderBy = pageDomain.getOrderBy();
        assertEquals("create_time desc", orderBy);
    }

    @Test
    void testGetOrderBy_WithoutOrderByColumn() {
        String orderBy = pageDomain.getOrderBy();
        assertEquals("", orderBy);
    }

    @Test
    void testGetOrderBy_WithNullOrderByColumn() {
        pageDomain.setOrderByColumn(null);
        String orderBy = pageDomain.getOrderBy();
        assertEquals("", orderBy);
    }

    @Test
    void testGetOrderBy_WithEmptyOrderByColumn() {
        pageDomain.setOrderByColumn("");
        String orderBy = pageDomain.getOrderBy();
        assertEquals("", orderBy);
    }

    @Test
    void testGetOrderBy_CamelCaseConversion() {
        pageDomain.setOrderByColumn("createTime");
        String orderBy = pageDomain.getOrderBy();
        assertEquals("create_time asc", orderBy);
    }

    @Test
    void testGetOrderBy_MultipleWordsCamelCase() {
        pageDomain.setOrderByColumn("articleCreateTime");
        String orderBy = pageDomain.getOrderBy();
        assertEquals("article_create_time asc", orderBy);
    }

    @Test
    void testGetOrderBy_SingleWord() {
        pageDomain.setOrderByColumn("name");
        String orderBy = pageDomain.getOrderBy();
        assertEquals("name asc", orderBy);
    }

    @Test
    void testGetOrderBy_WithCustomSort() {
        pageDomain.setOrderByColumn("userId");
        pageDomain.setIsAsc("ascending");
        String orderBy = pageDomain.getOrderBy();
        assertEquals("user_id asc", orderBy);
    }

    @Test
    void testGetReasonable_DefaultValue() {
        assertEquals(Boolean.TRUE, pageDomain.getReasonable());
    }

    @Test
    void testSetReasonable_True() {
        pageDomain.setReasonable(true);
        assertEquals(Boolean.TRUE, pageDomain.getReasonable());
    }

    @Test
    void testSetReasonable_False() {
        pageDomain.setReasonable(false);
        assertEquals(Boolean.FALSE, pageDomain.getReasonable());
    }

    @Test
    void testSetReasonable_Null() {
        pageDomain.setReasonable(null);
        assertEquals(Boolean.TRUE, pageDomain.getReasonable());
    }

    @Test
    void testSetReasonable_AfterTrue() {
        pageDomain.setReasonable(true);
        pageDomain.setReasonable(null);
        assertEquals(Boolean.TRUE, pageDomain.getReasonable());
    }

    @Test
    void testSetReasonable_AfterFalse() {
        pageDomain.setReasonable(false);
        pageDomain.setReasonable(null);
        assertEquals(Boolean.TRUE, pageDomain.getReasonable());
    }

    @Test
    void testFullUsage() {
        pageDomain.setPageNum(2);
        pageDomain.setPageSize(20);
        pageDomain.setOrderByColumn("articleTitle");
        pageDomain.setIsAsc("descending");
        pageDomain.setReasonable(true);

        assertEquals(2, pageDomain.getPageNum());
        assertEquals(20, pageDomain.getPageSize());
        assertEquals("articleTitle", pageDomain.getOrderByColumn());
        assertEquals("desc", pageDomain.getIsAsc());
        assertEquals("article_title desc", pageDomain.getOrderBy());
        assertEquals(Boolean.TRUE, pageDomain.getReasonable());
    }

    @Test
    void testPageNum_ZeroValue() {
        pageDomain.setPageNum(0);
        assertEquals(0, pageDomain.getPageNum());
    }

    @Test
    void testPageNum_NegativeValue() {
        pageDomain.setPageNum(-1);
        assertEquals(-1, pageDomain.getPageNum());
    }

    @Test
    void testPageNum_LargeValue() {
        pageDomain.setPageNum(1000000);
        assertEquals(1000000, pageDomain.getPageNum());
    }

    @Test
    void testPageSize_ZeroValue() {
        pageDomain.setPageSize(0);
        assertEquals(0, pageDomain.getPageSize());
    }

    @Test
    void testPageSize_NegativeValue() {
        pageDomain.setPageSize(-1);
        assertEquals(-1, pageDomain.getPageSize());
    }

    @Test
    void testPageSize_LargeValue() {
        pageDomain.setPageSize(1000000);
        assertEquals(1000000, pageDomain.getPageSize());
    }

    @Test
    void testIsAsc_InvalidValue() {
        pageDomain.setIsAsc("invalid");
        assertEquals("invalid", pageDomain.getIsAsc());
    }

    @Test
    void testIsAsc_MixedCase() {
        pageDomain.setIsAsc("AsCeNdInG");
        assertEquals("AsCeNdInG", pageDomain.getIsAsc());
    }

    @Test
    void testIsAsc_WithSpaces() {
        pageDomain.setIsAsc(" asc ");
        // 注意：StringUtils.isNotEmpty 会检测到非空字符串，但不会trim
        assertEquals(" asc ", pageDomain.getIsAsc());
    }

    @Test
    void testOrderByColumn_WithSpaces() {
        pageDomain.setOrderByColumn(" createTime ");
        assertEquals(" createTime ", pageDomain.getOrderByColumn());
    }

    @Test
    void testOrderByColumn_WithUnderscore() {
        pageDomain.setOrderByColumn("create_time");
        String orderBy = pageDomain.getOrderBy();
        assertEquals("create_time asc", orderBy);
    }

    @Test
    void testOrderByColumn_WithNumbers() {
        pageDomain.setOrderByColumn("article1Title2");
        String orderBy = pageDomain.getOrderBy();
        assertEquals("article1_title2 asc", orderBy);
    }

    @Test
    void testMultipleSetCalls_IsAsc() {
        pageDomain.setIsAsc("asc");
        assertEquals("asc", pageDomain.getIsAsc());
        
        pageDomain.setIsAsc("desc");
        assertEquals("desc", pageDomain.getIsAsc());
        
        pageDomain.setIsAsc("ascending");
        assertEquals("asc", pageDomain.getIsAsc());
        
        pageDomain.setIsAsc("descending");
        assertEquals("desc", pageDomain.getIsAsc());
    }

    @Test
    void testMultipleSetCalls_Reasonable() {
        pageDomain.setReasonable(true);
        assertEquals(Boolean.TRUE, pageDomain.getReasonable());
        
        pageDomain.setReasonable(false);
        assertEquals(Boolean.FALSE, pageDomain.getReasonable());
        
        pageDomain.setReasonable(true);
        assertEquals(Boolean.TRUE, pageDomain.getReasonable());
    }

    @Test
    void testGetOrderBy_AfterChangingIsAsc() {
        pageDomain.setOrderByColumn("userId");
        pageDomain.setIsAsc("asc");
        assertEquals("user_id asc", pageDomain.getOrderBy());
        
        pageDomain.setIsAsc("desc");
        assertEquals("user_id desc", pageDomain.getOrderBy());
    }

    @Test
    void testGetOrderBy_AfterChangingOrderByColumn() {
        pageDomain.setOrderByColumn("userId");
        assertEquals("user_id asc", pageDomain.getOrderBy());
        
        pageDomain.setOrderByColumn("userName");
        assertEquals("user_name asc", pageDomain.getOrderBy());
    }

    @Test
    void testEmptyOrderByColumnWithIsAsc() {
        pageDomain.setOrderByColumn("");
        pageDomain.setIsAsc("desc");
        assertEquals("", pageDomain.getOrderBy());
    }

    @Test
    void testNullOrderByColumnWithIsAsc() {
        pageDomain.setOrderByColumn(null);
        pageDomain.setIsAsc("desc");
        assertEquals("", pageDomain.getOrderBy());
    }

    @Test
    void testGetReasonable_BooleanObject() {
        pageDomain.setReasonable(Boolean.TRUE);
        assertEquals(Boolean.TRUE, pageDomain.getReasonable());
        
        pageDomain.setReasonable(Boolean.FALSE);
        assertEquals(Boolean.FALSE, pageDomain.getReasonable());
    }

    @Test
    void testSettersReturn() {
        // 测试 setter 方法是否正确设置值
        pageDomain.setPageNum(5);
        assertEquals(5, pageDomain.getPageNum());
        
        pageDomain.setPageSize(15);
        assertEquals(15, pageDomain.getPageSize());
        
        pageDomain.setOrderByColumn("testColumn");
        assertEquals("testColumn", pageDomain.getOrderByColumn());
        
        pageDomain.setIsAsc("asc");
        assertEquals("asc", pageDomain.getIsAsc());
        
        pageDomain.setReasonable(true);
        assertEquals(Boolean.TRUE, pageDomain.getReasonable());
    }

    @Test
    void testGetOrderBy_WithSpecialCharacters() {
        pageDomain.setOrderByColumn("user$Name");
        String orderBy = pageDomain.getOrderBy();
        // 注意：StringUtils.toUnderScoreCase 会将 $ 转换为 _
        assertEquals("user$_name asc", orderBy);
    }
}