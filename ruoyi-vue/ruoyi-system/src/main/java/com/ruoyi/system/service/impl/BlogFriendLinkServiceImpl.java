package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.service.BaseServiceImpl;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BlogFriendLink;
import com.ruoyi.system.mapper.BlogFriendLinkMapper;
import com.ruoyi.system.service.IBlogFriendLinkService;

/**
 * 友情链接Service业务层处理
 * 
 * @author nevell
 * @date 2025-09-08
 */
@Service
public class BlogFriendLinkServiceImpl extends BaseServiceImpl<BlogFriendLinkMapper, BlogFriendLink> implements IBlogFriendLinkService
{
    @Autowired
    private BlogFriendLinkMapper blogFriendLinkMapper;

    /**
     * 查询友情链接列表
     * 
     * @param blogFriendLink 友情链接
     * @return 友情链接集合
     */
    @Override
    public List<BlogFriendLink> selectBlogFriendLinkList(BlogFriendLink blogFriendLink)
    {
        return blogFriendLinkMapper.selectBlogFriendLinkList(blogFriendLink);
    }

    /**
     * 查询前台展示的友情链接列表
     * 
     * @return 友情链接集合
     */
    @Override
    public List<BlogFriendLink> selectFrontFriendLinkList()
    {
        return blogFriendLinkMapper.selectFrontFriendLinkList();
    }

    /**
     * 通过ID查询单条数据
     * 
     * @param id 友链ID
     * @return 实例对象
     */
    @Override
    public BlogFriendLink selectBlogFriendLinkById(Long id)
    {
        return blogFriendLinkMapper.selectBlogFriendLinkById(id);
    }

    /**
     * 新增友情链接
     * 
     * @param blogFriendLink 友情链接
     * @return 结果
     */
    @Override
    public int insertBlogFriendLink(BlogFriendLink blogFriendLink)
    {
        blogFriendLink.setCreateBy(SecurityUtils.getUsername());
        return blogFriendLinkMapper.insertBlogFriendLink(blogFriendLink);
    }

    /**
     * 修改友情链接
     * 
     * @param blogFriendLink 友情链接
     * @return 结果
     */
    @Override
    public int updateBlogFriendLink(BlogFriendLink blogFriendLink)
    {
        blogFriendLink.setUpdateBy(SecurityUtils.getUsername());
        return blogFriendLinkMapper.updateBlogFriendLink(blogFriendLink);
    }

    /**
     * 通过主键删除数据
     * 
     * @param id 友链ID
     * @return 影响行数
     */
    @Override
    public int deleteBlogFriendLinkById(Long id)
    {
        return blogFriendLinkMapper.deleteBlogFriendLinkById(id);
    }

    /**
     * 批量删除友情链接
     * 
     * @param ids 需要删除的数据ID
     * @return 影响行数
     */
    @Override
    public int deleteBlogFriendLinkByIds(Long[] ids)
    {
        return blogFriendLinkMapper.deleteBlogFriendLinkByIds(ids);
    }
}