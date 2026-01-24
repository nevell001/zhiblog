package com.ruoyi.common.exception;

/**
 * 文章标题重复异常
 * 当尝试新增或更新文章时，标题与已有文章重复
 *
 * @author ruoyi
 */
public class DuplicateArticleTitleException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public DuplicateArticleTitleException(String message)
    {
        super(message);
    }

    public DuplicateArticleTitleException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
