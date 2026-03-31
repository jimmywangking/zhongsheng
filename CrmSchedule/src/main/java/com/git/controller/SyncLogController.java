package com.git.controller;

import com.git.entity.SyncLog;
import com.git.service.SyncLogService;
import com.git.task.SyncTaskJob;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同步日志查询接口
 */
@RestController
@RequestMapping("/api/sync-logs")
@RequiredArgsConstructor
public class SyncLogController {

    private final SyncLogService syncLogService;
    private final SyncTaskJob syncTaskJob;

    /**
     * 查询所有日志
     */
    @GetMapping
    public List<SyncLog> listAll() {
        return syncLogService.listAll();
    }

    /**
     * 根据同步日期查询日志
     */
    @GetMapping("/date/{syncDate}")
    public List<SyncLog> listBySyncDate(@PathVariable String syncDate) {
        return syncLogService.listBySyncDate(syncDate);
    }

    /**
     * 手动触发同步任务
     */
    @PostMapping("/trigger")
    public Map<String, Object> triggerSync() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 异步执行同步任务
            new Thread(() -> {
                try {
                    syncTaskJob.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            
            result.put("code", 200);
            result.put("message", "同步任务已触发，请在日志中查看执行结果");
            result.put("timestamp", java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            ));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "触发失败：" + e.getMessage());
        }
        return result;
    }
}
