package com.git.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.git.entity.SyncLog;
import com.git.mapper.SyncLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 同步日志服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SyncLogService {

    private final SyncLogMapper syncLogMapper;

    /**
     * 保存同步日志
     */
    public void saveLog(SyncLog syncLog) {
        syncLog.setCreateTime(LocalDateTime.now());
        syncLogMapper.insert(syncLog);
    }

    /**
     * 创建成功日志
     */
    public SyncLog createSuccessLog(String taskName, String syncDate, int totalCount,
                                     int successCount, int failCount, LocalDateTime startTime, LocalDateTime endTime) {
        SyncLog syncLog = new SyncLog();
        syncLog.setTaskName(taskName);
        syncLog.setSyncDate(syncDate);
        syncLog.setTotalCount(totalCount);
        syncLog.setSuccessCount(successCount);
        syncLog.setFailCount(failCount);
        syncLog.setStartTime(startTime);
        syncLog.setEndTime(endTime);
        syncLog.setStatus(1);
        return syncLog;
    }

    /**
     * 创建失败日志
     */
    public SyncLog createFailLog(String taskName, String syncDate, String errorMsg,
                                  LocalDateTime startTime, LocalDateTime endTime) {
        SyncLog syncLog = new SyncLog();
        syncLog.setTaskName(taskName);
        syncLog.setSyncDate(syncDate);
        syncLog.setStartTime(startTime);
        syncLog.setEndTime(endTime);
        syncLog.setStatus(0);
        syncLog.setErrorMsg(errorMsg);
        return syncLog;
    }

    /**
     * 查询所有日志
     */
    public List<SyncLog> listAll() {
        return syncLogMapper.selectList(null);
    }

    /**
     * 根据同步日期查询日志
     */
    public List<SyncLog> listBySyncDate(String syncDate) {
        return syncLogMapper.selectList(
                new LambdaQueryWrapper<SyncLog>().eq(SyncLog::getSyncDate, syncDate)
        );
    }
}
