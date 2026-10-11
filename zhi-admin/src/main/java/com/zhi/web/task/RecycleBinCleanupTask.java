package com.zhi.web.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.zhi.system.service.IBlogArticleService;

/**
 * 文章回收站清理任务
 *
 * <p>每天 01:20 彻底删除回收站中超过保留期（默认 30 天）的文章，
 * 保留天数由 {@link IBlogArticleService#cleanRecycleBin(Integer)} 决定。</p>
 */
@Component("recycleBinCleanupTask")
public class RecycleBinCleanupTask
{
    private static final Logger log = LoggerFactory.getLogger(RecycleBinCleanupTask.class);

    @Autowired
    private IBlogArticleService blogArticleService;

    /**
     * 每天 01:20：清理回收站中的过期文章
     */
    @Scheduled(cron = "0 20 1 * * ?")
    public void cleanRecycleBin()
    {
        try
        {
            int purged = blogArticleService.cleanRecycleBin(null);
            if (purged > 0)
            {
                log.info("回收站清理完成：purged={}", purged);
            }
        }
        catch (Exception e)
        {
            log.error("清理回收站失败", e);
        }
    }
}
