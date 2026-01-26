package com.ruoyi.system.mapper;

import java.util.Date;
import java.util.List;
import com.ruoyi.system.domain.BlogEmailCode;

/**
 * 博客邮箱验证码Mapper接口
 *
 * @author nevell
 * @date 2025-01-26
 */
public interface BlogEmailCodeMapper
{
    /**
     * 查询博客邮箱验证码
     *
     * @param id 博客邮箱验证码主键
     * @return 博客邮箱验证码
     */
    public BlogEmailCode selectBlogEmailCodeById(Long id);

    /**
     * 查询博客邮箱验证码列表
     *
     * @param blogEmailCode 博客邮箱验证码
     * @return 博客邮箱验证码集合
     */
    public List<BlogEmailCode> selectBlogEmailCodeList(BlogEmailCode blogEmailCode);

    /**
     * 根据邮箱和验证码类型查询最新的未使用验证码
     *
     * @param email 邮箱地址
     * @param codeType 验证码类型
     * @return 博客邮箱验证码
     */
    public BlogEmailCode selectLatestUnusedCode(String email, String codeType);

    /**
     * 新增博客邮箱验证码
     *
     * @param blogEmailCode 博客邮箱验证码
     * @return 结果
     */
    public int insertBlogEmailCode(BlogEmailCode blogEmailCode);

    /**
     * 修改博客邮箱验证码
     *
     * @param blogEmailCode 博客邮箱验证码
     * @return 结果
     */
    public int updateBlogEmailCode(BlogEmailCode blogEmailCode);

    /**
     * 删除博客邮箱验证码
     *
     * @param id 博客邮箱验证码主键
     * @return 结果
     */
    public int deleteBlogEmailCodeById(Long id);

    /**
     * 批量删除博客邮箱验证码
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogEmailCodeByIds(Long[] ids);

    /**
     * 标记验证码为已使用
     *
     * @param id 验证码ID
     * @param useTime 使用时间
     * @return 结果
     */
    public int markCodeAsUsed(Long id, Date useTime);

    /**
     * 删除过期的验证码
     *
     * @return 结果
     */
    public int deleteExpiredCodes();

    /**
     * 统计指定时间段内某邮箱发送的验证码数量
     *
     * @param email 邮箱地址
     * @param startTime 开始时间
     * @return 验证码数量
     */
    public int countCodesByEmailAndTime(String email, Date startTime);

    /**
     * 统计指定时间段内某IP发送的验证码数量
     *
     * @param ipAddress IP地址
     * @param startTime 开始时间
     * @return 验证码数量
     */
    public int countCodesByIpAndTime(String ipAddress, Date startTime);
}
