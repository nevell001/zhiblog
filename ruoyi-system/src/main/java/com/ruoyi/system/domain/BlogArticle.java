package com.ruoyi.system.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 博客文章对象 blog_article
 * 
 * @author nevell
 * @date 2025-07-18
 */

public class BlogArticle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 文章标题 */
    @Excel(name = "文章标题")
    private String title;

    /** 摘要 */
    @Excel(name = "摘要")
    private String summary;

    /** 文章内容 */
    @Excel(name = "文章内容")
    private String content;

    /** 封面图片 */
    @Excel(name = "封面图片")
    private String coverUrl;

    /** 分类ID */
    @Excel(name = "分类ID")
    private Long categoryId;

    /** 分类名称（非数据库字段，用于显示） */
    private String categoryName;

    /** 作者ID */
    @Excel(name = "作者ID")
    private Long authorId;

    /** 作者姓名 */
    @Excel(name = "作者姓名")
    private String author;

    /** 作者姓名（非数据库字段，用于显示） */
    private String authorName;

    /** 是否置顶 0否 1是 */
    @Excel(name = "是否置顶 0否 1是")
    private Long isTop;

    /** 是否推荐 0否 1是 */
    @Excel(name = "是否推荐 0否 1是")
    private Long isRecommend;

    /** 状态 0草稿 1发布 */
    @Excel(name = "状态 0草稿 1发布")
    private Long status;

    /** 浏览量 */
    @Excel(name = "浏览量")
    private Long viewCount;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private Long likeCount;

    /** 评论数 */
    @Excel(name = "评论数")
    private Long commentCount;

    /** 标签ID列表（用于前端显示和编辑） */
    private List<Long> tagIds;

    /** 标签列表（用于返回给前端） */
    private List<BlogTag> tags;

    /** 删除标志（0代表存在 2代表删除） */
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

    public void setCoverUrl(String coverUrl) 
    {
        this.coverUrl = coverUrl;
    }

    public String getCoverUrl()
    {
        return coverUrl;
    }

    // 兼容性方法，支持 coverImage 字段名
    public void setCoverImage(String coverImage)
    {
        this.coverUrl = coverImage;
    }

    public String getCoverImage()
    {
        return coverUrl;
    }

    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setAuthorId(Long authorId) 
    {
        this.authorId = authorId;
    }

    public Long getAuthorId() 
    {
        return authorId;
    }

    public void setAuthor(String author) 
    {
        this.author = author;
    }

    public String getAuthor() 
    {
        return author;
    }

    public void setAuthorName(String authorName) 
    {
        this.authorName = authorName;
    }

    public String getAuthorName() 
    {
        return authorName;
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

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setViewCount(Long viewCount) 
    {
        this.viewCount = viewCount;
    }

    public Long getViewCount() 
    {
        return viewCount;
    }

    public void setLikeCount(Long likeCount) 
    {
        this.likeCount = likeCount;
    }

    public Long getLikeCount() 
    {
        return likeCount;
    }

    public void setCommentCount(Long commentCount) 
    {
        this.commentCount = commentCount;
    }

    public Long getCommentCount() 
    {
        return commentCount;
    }

    public void setTagIds(List<Long> tagIds) 
    {
        this.tagIds = tagIds;
    }

    public List<Long> getTagIds() 
    {
        return tagIds;
    }

    public void setTags(List<BlogTag> tags) 
    {
        this.tags = tags;
    }

    public List<BlogTag> getTags() 
    {
        return tags;
    }

    public void setDelFlag(Long delFlag) 
    {
        this.delFlag = delFlag;
    }

    public Long getDelFlag() 
    {
        if (delFlag == null) {
            // 如果字段为null，设置默认值并返回
            this.delFlag = 0L;
        }
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("summary", getSummary())
            .append("content", getContent())
            .append("coverUrl", getCoverUrl())
            .append("categoryId", getCategoryId())
            .append("categoryName", getCategoryName())
            .append("authorId", getAuthorId())
            .append("author", getAuthor())
            .append("authorName", getAuthorName())
            .append("isTop", getIsTop())
            .append("isRecommend", getIsRecommend())
            .append("status", getStatus())
            .append("viewCount", getViewCount())
            .append("likeCount", getLikeCount())
            .append("commentCount", getCommentCount())
            .append("tagIds", getTagIds())
            .append("tags", getTags())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
