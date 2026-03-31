package com.git.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.git.entity.SyncLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 同步日志 Mapper 接口
 */
@Mapper
public interface SyncLogMapper extends BaseMapper<SyncLog> {

}
