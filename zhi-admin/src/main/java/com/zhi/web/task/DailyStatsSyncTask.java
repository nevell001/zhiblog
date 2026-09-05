package com.zhi.web.task;

import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.zhi.common.utils.DateUtils;
import com.zhi.system.service.IBlogDailyStatsService;

/**
 * 每日 PV/UV 统计任务
 *
 * <p>每小时把当日 Redis 计数汇总入 blog_article_daily_stats（保证近实时）；
 * 每天 00:35 汇总前一日最终值并清理前一日 Redis 计数键。</p>
 */
@Component("dailyStatsSyncTask")
public class DailyStatsSyncTask
{
    private static final Logger log = LoggerFactory.getLogger(DailyStatsSyncTask.class);

    @Autowired
    private IBlogDailyStatsService blogDailyStatsService;

    /**
     * 每小时整点后 20 分：汇总当日
     */
    @Scheduled(cron = "0 20 * * * ?")
    public void syncToday()
    {
        String today = DateUtils.parseDateToStr("yyyyMMdd", new Date());
        try
        {
            int handled = blogDailyStatsService.upsertDailyFromRedis(today);
            if (handled > 0)
            {
                log.info("每日 PV/UV 汇总完成（当日）：date={}, articles={}", today, handled);
            }
        }
        catch (Exception e)
        {
            log.error("汇总当日 PV/UV 失败: date={}", today, e);
        }
    }

    /**
     * 每天 00:35：汇总前一日并清理其 Redis 键
     */
    @Scheduled(cron = "0 35 0 * * ?")
    public void finalizeYesterday()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String yesterday = DateUtils.parseDateToStr("yyyyMMdd", calendar.getTime());
        try
        {
            int handled = blogDailyStatsService.upsertDailyFromRedis(yesterday);
            int cleaned = blogDailyStatsService.cleanupDailyRedisKeys(yesterday);
            if (handled > 0 || cleaned > 0)
            {
                log.info("前一日 PV/UV 定稿完成：date={}, articles={}, cleanedKeys={}", yesterday, handled, cleaned);
            }
        }
        catch (Exception e)
        {
            log.error("定稿前一日 PV/UV 失败: date={}", yesterday, e);
        }
    }
}
