package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BlogSetting;
import com.ruoyi.system.mapper.BlogSettingMapper;
import com.ruoyi.system.service.IBlogSettingService;

/**
 * 博客设置Service业务层处理
 * 
 * @author nevell
 * @date 2025-09-08
 */
@Service
public class BlogSettingServiceImpl implements IBlogSettingService
{
    @Autowired
    private BlogSettingMapper blogSettingMapper;

    /**
     * 查询博客设置列表
     * 
     * @param blogSetting 博客设置
     * @return 博客设置集合
     */
    @Override
    public List<BlogSetting> selectBlogSettingList(BlogSetting blogSetting)
    {
        return blogSettingMapper.selectBlogSettingList(blogSetting);
    }

    /**
     * 通过设置键查询设置值
     * 
     * @param settingKey 设置键
     * @return 设置值
     */
    @Override
    public String selectSettingValueByKey(String settingKey)
    {
        return blogSettingMapper.selectSettingValueByKey(settingKey);
    }

    /**
     * 通过ID查询单条数据
     * 
     * @param id 主键ID
     * @return 实例对象
     */
    @Override
    public BlogSetting selectBlogSettingById(Long id)
    {
        return blogSettingMapper.selectBlogSettingById(id);
    }

    /**
     * 新增博客设置
     * 
     * @param blogSetting 博客设置
     * @return 结果
     */
    @Override
    public int insertBlogSetting(BlogSetting blogSetting)
    {
        blogSetting.setCreateBy(SecurityUtils.getUsername());
        return blogSettingMapper.insertBlogSetting(blogSetting);
    }

    /**
     * 修改博客设置
     * 
     * @param blogSetting 博客设置
     * @return 结果
     */
    @Override
    public int updateBlogSetting(BlogSetting blogSetting)
    {
        blogSetting.setUpdateBy(SecurityUtils.getUsername());
        return blogSettingMapper.updateBlogSetting(blogSetting);
    }

    /**
     * 通过设置键修改设置值
     * 
     * @param settingKey 设置键
     * @param settingValue 设置值
     * @return 结果
     */
    @Override
    public int updateSettingValueByKey(String settingKey, String settingValue)
    {
        return blogSettingMapper.updateSettingValueByKey(settingKey, settingValue);
    }

    /**
     * 通过主键删除数据
     * 
     * @param id 主键ID
     * @return 影响行数
     */
    @Override
    public int deleteBlogSettingById(Long id)
    {
        return blogSettingMapper.deleteBlogSettingById(id);
    }

    /**
     * 批量删除博客设置
     * 
     * @param ids 需要删除的数据ID
     * @return 影响行数
     */
    @Override
    public int deleteBlogSettingByIds(Long[] ids)
    {
        return blogSettingMapper.deleteBlogSettingByIds(ids);
    }
}