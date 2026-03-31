-- CRM 数据库表结构
-- 使用 MySQL 8.0+

-- 创建数据库
CREATE DATABASE IF NOT EXISTS crm_schedule DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE crm_schedule;

-- 合同列表表
CREATE TABLE IF NOT EXISTS `crm_contract` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `ht_id` VARCHAR(50) DEFAULT NULL COMMENT '合同 ID',
    `key` VARCHAR(100) DEFAULT NULL COMMENT '主键',
    `ht_title` VARCHAR(500) DEFAULT NULL COMMENT '合同标题',
    `ht_number` VARCHAR(100) DEFAULT NULL COMMENT '合同编号',
    `ht_type` VARCHAR(100) DEFAULT NULL COMMENT '合同类型',
    `ht_date` VARCHAR(20) DEFAULT NULL COMMENT '合同日期',
    `ht_enddate` VARCHAR(20) DEFAULT NULL COMMENT '合同结束日期',
    `ht_summoney` DECIMAL(18,2) DEFAULT 0.00 COMMENT '合同总额',
    `ht_state` VARCHAR(50) DEFAULT NULL COMMENT '合同状态',
    `ht_hkmoney` DECIMAL(18,2) DEFAULT 0.00 COMMENT '回款金额',
    `hk_total` DECIMAL(18,2) DEFAULT 0.00 COMMENT '回款总额',
    `ht_billmoney` DECIMAL(18,2) DEFAULT 0.00 COMMENT '开票金额',
    `ht_maoli` DECIMAL(18,2) DEFAULT 0.00 COMMENT '毛利',
    `ht_chengben` DECIMAL(18,2) DEFAULT 0.00 COMMENT '成本',
    `ht_ckstate` VARCHAR(50) DEFAULT NULL COMMENT '出库状态',
    `ht_billmoneycha` DECIMAL(18,2) DEFAULT 0.00 COMMENT '开票差额',
    `tuihuo_state` DECIMAL(18,2) DEFAULT 0.00 COMMENT '退货状态',
    `ht_customerid` INT(11) DEFAULT 0 COMMENT '客户 ID',
    `ht_customer` VARCHAR(200) DEFAULT NULL COMMENT '客户名称',
    `ht_preside` VARCHAR(100) DEFAULT NULL COMMENT '负责人',
    `data_userid` VARCHAR(50) DEFAULT NULL COMMENT '创建人 ID',
    `addtime` VARCHAR(20) DEFAULT NULL COMMENT '创建时间',
    `update_date` VARCHAR(20) DEFAULT NULL COMMENT '更新时间',
    `lastbaojia` INT(11) DEFAULT NULL COMMENT '最后报价',
    `sync_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '同步时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_ht_id` (`ht_id`),
    KEY `idx_ht_customer` (`ht_customer`),
    KEY `idx_ht_date` (`ht_date`),
    KEY `idx_sync_time` (`sync_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合同列表表';

-- 合同详情表
CREATE TABLE IF NOT EXISTS `crm_contract_detail` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `ht_id` VARCHAR(50) DEFAULT NULL COMMENT '合同 ID',
    `key` VARCHAR(100) DEFAULT NULL COMMENT '主键',
    `ht_title` VARCHAR(500) DEFAULT NULL COMMENT '合同标题',
    `ht_number` VARCHAR(100) DEFAULT NULL COMMENT '合同编号',
    `ht_type` VARCHAR(100) DEFAULT NULL COMMENT '合同类型',
    `ht_date` VARCHAR(20) DEFAULT NULL COMMENT '合同日期',
    `ht_enddate` VARCHAR(20) DEFAULT NULL COMMENT '合同结束日期',
    `ht_summoney` DECIMAL(18,2) DEFAULT 0.00 COMMENT '合同总额',
    `ht_state` VARCHAR(50) DEFAULT NULL COMMENT '合同状态',
    `ht_hkmoney` DECIMAL(18,2) DEFAULT 0.00 COMMENT '回款金额',
    `hk_total` DECIMAL(18,2) DEFAULT 0.00 COMMENT '回款总额',
    `ht_billmoney` DECIMAL(18,2) DEFAULT 0.00 COMMENT '开票金额',
    `ht_maoli` DECIMAL(18,2) DEFAULT 0.00 COMMENT '毛利',
    `ht_chengben` DECIMAL(18,2) DEFAULT 0.00 COMMENT '成本',
    `ht_ckstate` VARCHAR(50) DEFAULT NULL COMMENT '出库状态',
    `ht_billmoneycha` DECIMAL(18,2) DEFAULT 0.00 COMMENT '开票差额',
    `tuihuo_state` DECIMAL(18,2) DEFAULT 0.00 COMMENT '退货状态',
    `ht_customerid` INT(11) DEFAULT 0 COMMENT '客户 ID',
    `ht_customer` VARCHAR(200) DEFAULT NULL COMMENT '客户名称',
    `ht_preside` VARCHAR(100) DEFAULT NULL COMMENT '负责人',
    `data_userid` VARCHAR(50) DEFAULT NULL COMMENT '创建人 ID',
    `addtime` VARCHAR(20) DEFAULT NULL COMMENT '创建时间',
    `update_date` VARCHAR(20) DEFAULT NULL COMMENT '更新时间',
    `lastbaojia` INT(11) DEFAULT NULL COMMENT '最后报价',
    `ht_remark` TEXT DEFAULT NULL COMMENT '备注',
    `ht_address` VARCHAR(500) DEFAULT NULL COMMENT '地址',
    `ht_delivery` VARCHAR(200) DEFAULT NULL COMMENT '交货方式',
    `ht_payment` VARCHAR(200) DEFAULT NULL COMMENT '付款方式',
    `sync_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '同步时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_ht_id` (`ht_id`),
    KEY `idx_ht_customer` (`ht_customer`),
    KEY `idx_ht_date` (`ht_date`),
    KEY `idx_sync_time` (`sync_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合同详情表';

-- 同步任务日志表
CREATE TABLE IF NOT EXISTS `crm_sync_log` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `task_name` VARCHAR(100) DEFAULT NULL COMMENT '任务名称',
    `sync_date` VARCHAR(20) DEFAULT NULL COMMENT '同步日期',
    `total_count` INT(11) DEFAULT 0 COMMENT '总记录数',
    `success_count` INT(11) DEFAULT 0 COMMENT '成功数量',
    `fail_count` INT(11) DEFAULT 0 COMMENT '失败数量',
    `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
    `status` TINYINT(4) DEFAULT 1 COMMENT '状态 1-成功 0-失败',
    `error_msg` TEXT DEFAULT NULL COMMENT '错误信息',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_sync_date` (`sync_date`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='同步任务日志表';
