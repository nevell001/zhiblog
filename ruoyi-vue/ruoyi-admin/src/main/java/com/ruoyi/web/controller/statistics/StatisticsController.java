package com.ruoyi.web.controller.statistics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.annotation.Anonymous;
import java.util.HashMap;
import java.util.Map;

/**
 * 统计分析控制器
 */
@RestController
@RequestMapping("/system-stats")
public class StatisticsController {
    
    /**
     * 获取系统概览统计信息
     */
    @GetMapping("/overview")
    @Anonymous
    public AjaxResult getOverview() {
        Map<String, Object> data = new HashMap<>();
        data.put("articleCount", 100);
        data.put("userCount", 50);
        data.put("commentCount", 200);
        data.put("viewCount", 1000);
        data.put("onlineUsers", 10);
        data.put("todayVisits", 50);
        data.put("systemUptime", "30天");
        data.put("memoryUsage", "45%");
        
        return AjaxResult.success(data);
    }
}