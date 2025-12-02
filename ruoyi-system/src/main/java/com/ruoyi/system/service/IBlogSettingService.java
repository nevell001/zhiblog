package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BlogSetting;

/**
 * 博客设置Service接口
 * 
 * @author nevell
 * @date 2025-09-08
 */
public interface IBlogSettingService
{
    /**
     * 查询博客设置列表
     * 
     * @param blogSetting 博客设置
     * @return 博客设置集合
     */
    public List<BlogSetting> selectBlogSettingList(BlogSetting blogSetting);

    /**
     * 通过设置键查询设置值
     * 
     * @param settingKey 设置键
     * @return 设置值
     */
    public String selectSettingValueByKey(String settingKey);

    /**
     * 通过ID查询单条数据
     * 
     * @param id 主键ID
     * @return 实例对象
     */
    public BlogSetting selectBlogSettingById(Long id);

    /**
     * 新增博客设置
     * 
     * @param blogSetting 博客设置
     * @return 结果
     */
    public int insertBlogSetting(BlogSetting blogSetting);

    /**
     * 修改博客设置
     * 
     * @param blogSetting 博客设置
     * @return 结果
     */
    public int updateBlogSetting(BlogSetting blogSetting);

    /**
     * 通过设置键修改设置值
     * 
     * @param settingKey 设置键
     * @param settingValue 设置值
     * @return 结果
     */
    public int updateSettingValueByKey(String settingKey, String settingValue);

    /**
     * 通过主键删除数据
     * 
     * @param id 主键ID
     * @return 影响行数
     */
    public int deleteBlogSettingById(Long id);

    /**
     * 批量删除博客设置
     * 
     * @param ids 需要删除的数据ID
     * @return 影响行数
     */
    public int deleteBlogSettingByIds(Long[] ids);
}