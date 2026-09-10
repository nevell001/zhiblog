package com.zhi.system.mapper;

import java.util.List;
import com.zhi.system.domain.BlogPage;

/**
 * 自定义页面Mapper接口
 * 
 * @author nevell
 * @date 2026-09-10
 */
public interface BlogPageMapper 
{
    /**
     * 查询页面列表
     * 
     * @param blogPage 页面
     * @return 页面集合
     */
    public List<BlogPage> selectBlogPageList(BlogPage blogPage);

    /**
     * 查询已发布页面列表（前台用）
     * 
     * @return 页面集合
     */
    public List<BlogPage> selectPublishedPageList();

    /**
     * 通过ID查询单条数据
     * 
     * @param id 页面ID
     * @return 实例对象
     */
    public BlogPage selectBlogPageById(Long id);

    /**
     * 通过别名查询已发布页面
     * 
     * @param slug 页面别名
     * @return 实例对象
     */
    public BlogPage selectBlogPageBySlug(String slug);

    /**
     * 校验别名是否唯一
     * 
     * @param slug 页面别名
     * @return 已存在的页面（不存在返回 null）
     */
    public BlogPage checkSlugUnique(String slug);

    /**
     * 新增页面
     * 
     * @param blogPage 页面
     * @return 结果
     */
    public int insertBlogPage(BlogPage blogPage);

    /**
     * 修改页面
     * 
     * @param blogPage 页面
     * @return 结果
     */
    public int updateBlogPage(BlogPage blogPage);

    /**
     * 增加浏览次数
     * 
     * @param id 页面ID
     * @return 结果
     */
    public int increaseViewCount(Long id);

    /**
     * 通过主键删除数据
     * 
     * @param id 页面ID
     * @return 影响行数
     */
    public int deleteBlogPageById(Long id);

    /**
     * 批量删除页面
     * 
     * @param ids 需要删除的数据ID
     * @return 影响行数
     */
    public int deleteBlogPageByIds(Long[] ids);
}
