package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 文章对象 blog_article
 * 
 * @author ruoyi
 * @date 2025-07-16
 */
public class BlogArticle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 文章标题 */
    @Excel(name = "文章标题")
    private String title;

    /** 摘要 */
    @Excel(name = "摘要")
    private String summary;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 封面图片 */
    @Excel(name = "封面图片")
    private String coverImage;

    /** 分类ID */
    @Excel(name = "分类ID")
    private Long categoryId;

    /** 作者ID */
    @Excel(name = "作者ID")
    private Long authorId;

    /** 状态 0草稿 1发布 */
    @Excel(name = "状态 0草稿 1发布")
    private Long status;

    /** 是否置顶 0否 1是 */
    @Excel(name = "是否置顶 0否 1是")
    private Long isTop;

    /** 是否推荐 0否 1是 */
    @Excel(name = "是否推荐 0否 1是")
    private Long isRecommend;

    /** 浏览量 */
    @Excel(name = "浏览量")
    private Long viewCount;

    /** 评论数 */
    @Excel(name = "评论数")
    private Long commentCount;

    /** 删除标志 0正常 1删除 */
    private Long delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setSummary(String summary) 
    {
        this.summary = summary;
    }

    public String getSummary() 
    {
        return summary;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setCoverImage(String coverImage) 
    {
        this.coverImage = coverImage;
    }

    public String getCoverImage() 
    {
        return coverImage;
    }

    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }

    public void setAuthorId(Long authorId) 
    {
        this.authorId = authorId;
    }

    public Long getAuthorId() 
    {
        return authorId;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setIsTop(Long isTop) 
    {
        this.isTop = isTop;
    }

    public Long getIsTop() 
    {
        return isTop;
    }

    public void setIsRecommend(Long isRecommend) 
    {
        this.isRecommend = isRecommend;
    }

    public Long getIsRecommend() 
    {
        return isRecommend;
    }

    public void setViewCount(Long viewCount) 
    {
        this.viewCount = viewCount;
    }

    public Long getViewCount() 
    {
        return viewCount;
    }

    public void setCommentCount(Long commentCount) 
    {
        this.commentCount = commentCount;
    }

    public Long getCommentCount() 
    {
        return commentCount;
    }

    public void setDelFlag(Long delFlag) 
    {
        this.delFlag = delFlag;
    }

    public Long getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("summary", getSummary())
            .append("content", getContent())
            .append("coverImage", getCoverImage())
            .append("categoryId", getCategoryId())
            .append("authorId", getAuthorId())
            .append("status", getStatus())
            .append("isTop", getIsTop())
            .append("isRecommend", getIsRecommend())
            .append("viewCount", getViewCount())
            .append("commentCount", getCommentCount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
