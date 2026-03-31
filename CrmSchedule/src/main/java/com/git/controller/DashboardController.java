package com.git.controller;

import com.git.dto.DashboardStats;
import com.git.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dashboard 控制器
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * 获取 Dashboard 统计数据
     */
    @GetMapping("/stats")
    public DashboardStats getStats() {
        return dashboardService.getStats();
    }

    /**
     * 获取刷新时间
     */
    @GetMapping("/refresh-time")
    public String getRefreshTime() {
        return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
