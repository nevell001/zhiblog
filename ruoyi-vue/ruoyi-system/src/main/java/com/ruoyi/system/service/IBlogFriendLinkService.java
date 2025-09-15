package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BlogFriendLink;
import com.ruoyi.common.core.service.BaseService;

/**
 * 友情链接Service接口
 * 
 * @author nevell
 * @date 2025-09-08
 */
public interface IBlogFriendLinkService extends BaseService<BlogFriendLink>
{
    /**
     * 查询友情链接列表
     * 
     * @param blogFriendLink 友情链接
     * @return 友情链接集合
     */
    public List<BlogFriendLink> selectBlogFriendLinkList(BlogFriendLink blogFriendLink);

    /**
     * 查询前台展示的友情链接列表
     * 
     * @return 友情链接集合
     */
    public List<BlogFriendLink> selectFrontFriendLinkList();

    /**
     * 通过ID查询单条数据
     * 
     * @param id 友链ID
     * @return 实例对象
     */
    public BlogFriendLink selectBlogFriendLinkById(Long id);

    /**
     * 新增友情链接
     * 
     * @param blogFriendLink 友情链接
     * @return 结果
     */
    public int insertBlogFriendLink(BlogFriendLink blogFriendLink);

    /**
     * 修改友情链接
     * 
     * @param blogFriendLink 友情链接
     * @return 结果
     */
    public int updateBlogFriendLink(BlogFriendLink blogFriendLink);

    /**
     * 通过主键删除数据
     * 
     * @param id 友链ID
     * @return 影响行数
     */
    public int deleteBlogFriendLinkById(Long id);

    /**
     * 批量删除友情链接
     * 
     * @param ids 需要删除的数据ID
     * @return 影响行数
     */
    public int deleteBlogFriendLinkByIds(Long[] ids);
}