package com.zhi.system.domain;

/**
 * 上传文件的引用统计（媒体库删除前检查是否有业务表仍在引用该文件）
 *
 * @author nevell
 */
public class BlogUploadReference
{
    /** 引用来源：article/page/setting/friendLink/userAvatar */
    private String refType;

    /** 该来源下的引用数量 */
    private Long refCount;

    public String getRefType()
    {
        return refType;
    }

    public void setRefType(String refType)
    {
        this.refType = refType;
    }

    public Long getRefCount()
    {
        return refCount;
    }

    public void setRefCount(Long refCount)
    {
        this.refCount = refCount;
    }
}
