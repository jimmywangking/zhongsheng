-- CRM 数据库表结构 (H2 版本)
-- H2 内存数据库

-- 合同列表表
CREATE TABLE IF NOT EXISTS crm_contract (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ht_id VARCHAR(50) DEFAULT NULL,
    "key" VARCHAR(100) DEFAULT NULL,
    ht_title VARCHAR(500) DEFAULT NULL,
    ht_number VARCHAR(100) DEFAULT NULL,
    ht_type VARCHAR(100) DEFAULT NULL,
    ht_date VARCHAR(20) DEFAULT NULL,
    ht_enddate VARCHAR(20) DEFAULT NULL,
    ht_summoney DECIMAL(18,2) DEFAULT 0.00,
    ht_state VARCHAR(50) DEFAULT NULL,
    ht_hkmoney DECIMAL(18,2) DEFAULT 0.00,
    hk_total DECIMAL(18,2) DEFAULT 0.00,
    ht_billmoney DECIMAL(18,2) DEFAULT 0.00,
    ht_maoli DECIMAL(18,2) DEFAULT 0.00,
    ht_chengben DECIMAL(18,2) DEFAULT 0.00,
    ht_ckstate VARCHAR(50) DEFAULT NULL,
    ht_billmoneycha DECIMAL(18,2) DEFAULT 0.00,
    tuihuo_state DECIMAL(18,2) DEFAULT 0.00,
    ht_customerid INT DEFAULT 0,
    ht_customer VARCHAR(200) DEFAULT NULL,
    ht_preside VARCHAR(100) DEFAULT NULL,
    data_userid VARCHAR(50) DEFAULT NULL,
    addtime VARCHAR(20) DEFAULT NULL,
    update_date VARCHAR(20) DEFAULT NULL,
    lastbaojia INT DEFAULT NULL,
    sync_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_ht_id UNIQUE (ht_id)
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_ht_customer ON crm_contract (ht_customer);
CREATE INDEX IF NOT EXISTS idx_ht_date ON crm_contract (ht_date);
CREATE INDEX IF NOT EXISTS idx_sync_time ON crm_contract (sync_time);

-- 合同详情表（根据 API 返回的 JSON 对象重新设计）
CREATE TABLE IF NOT EXISTS crm_contract_detail (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ht_id VARCHAR(50) DEFAULT NULL,
    "key" VARCHAR(100) DEFAULT NULL,
    
    -- 基本信息
    ht_title VARCHAR(500) DEFAULT NULL,
    ht_number VARCHAR(100) DEFAULT NULL,
    ht_order VARCHAR(100) DEFAULT NULL,
    ht_type VARCHAR(100) DEFAULT NULL,
    ht_date VARCHAR(20) DEFAULT NULL,
    ht_begindate VARCHAR(20) DEFAULT NULL,
    ht_enddate VARCHAR(20) DEFAULT NULL,
    ht_state VARCHAR(50) DEFAULT NULL,
    
    -- 金额信息
    ht_summoney DECIMAL(18,2) DEFAULT 0.00,
    ht_hkmoney DECIMAL(18,2) DEFAULT 0.00,
    hk_total DECIMAL(18,2) DEFAULT 0.00,
    ht_billmoney DECIMAL(18,2) DEFAULT 0.00,
    ht_maoli DECIMAL(18,2) DEFAULT 0.00,
    ht_chengben DECIMAL(18,2) DEFAULT 0.00,
    ht_billmoneycha DECIMAL(18,2) DEFAULT 0.00,
    tuihuo_state DECIMAL(18,2) DEFAULT 0.00,
    ht_yugumaoli DECIMAL(18,2) DEFAULT 0.00,
    ht_moneyzhekou VARCHAR(20) DEFAULT NULL,
    ht_kjmoney DECIMAL(18,2) DEFAULT 0.00,
    ht_fjmoneylx VARCHAR(100) DEFAULT NULL,
    ht_fjmoney DECIMAL(18,2) DEFAULT 0.00,
    
    -- 状态信息
    ht_ckstate VARCHAR(50) DEFAULT NULL,
    shenhestate VARCHAR(50) DEFAULT NULL,
    ht_fahuostate VARCHAR(50) DEFAULT NULL,
    diy_ddl2 VARCHAR(50) DEFAULT NULL,
    
    -- 客户信息
    ht_customerid INT DEFAULT 0,
    ht_customer VARCHAR(200) DEFAULT NULL,
    ht_preside VARCHAR(100) DEFAULT NULL,
    data_userid VARCHAR(50) DEFAULT NULL,
    
    -- 联系信息
    ht_lxrid VARCHAR(50) DEFAULT NULL,
    ht_lxrinfo VARCHAR(200) DEFAULT NULL,
    
    -- 付款信息
    ht_paymode VARCHAR(100) DEFAULT NULL,
    ht_cusub VARCHAR(200) DEFAULT NULL,
    ht_wesub VARCHAR(200) DEFAULT NULL,
    
    -- 地址信息
    ht_summemo VARCHAR(500) DEFAULT NULL,
    ht_deliplace VARCHAR(500) DEFAULT NULL,
    ht_wuliutype VARCHAR(100) DEFAULT NULL,
    ht_yunfeimoney DECIMAL(18,2) DEFAULT 0.00,
    fahuoaddressid VARCHAR(100) DEFAULT NULL,
    
    -- 合同信息
    ht_contract TEXT DEFAULT NULL,
    ht_remark TEXT DEFAULT NULL,
    
    -- 关联信息
    project_id VARCHAR(100) DEFAULT NULL,
    ht_xshid VARCHAR(100) DEFAULT NULL,
    
    -- 自定义字段
    diy_str63312 VARCHAR(200) DEFAULT NULL,
    diy_rdo63326 VARCHAR(200) DEFAULT NULL,
    diy_rdo100701 VARCHAR(200) DEFAULT NULL,
    diy_str63315 VARCHAR(200) DEFAULT NULL,
    diy_str63314 VARCHAR(200) DEFAULT NULL,
    diy_str63316 VARCHAR(200) DEFAULT NULL,
    diy_num63318 VARCHAR(200) DEFAULT NULL,
    diy_num63320 VARCHAR(200) DEFAULT NULL,
    diy_num63319 VARCHAR(200) DEFAULT NULL,
    diy_num136662 VARCHAR(200) DEFAULT NULL,
    diy_num63321 VARCHAR(200) DEFAULT NULL,
    diy_num136663 VARCHAR(200) DEFAULT NULL,
    diy_num63322 VARCHAR(200) DEFAULT NULL,
    diy_rdo72744 VARCHAR(200) DEFAULT NULL,
    diy_str63313 VARCHAR(200) DEFAULT NULL,
    
    -- 时间信息
    addtime VARCHAR(20) DEFAULT NULL,
    update_date VARCHAR(20) DEFAULT NULL,
    lastbaojia INT DEFAULT NULL,
    
    sync_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_ht_id_detail UNIQUE (ht_id)
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_ht_customer_detail ON crm_contract_detail (ht_customer);
CREATE INDEX IF NOT EXISTS idx_ht_date_detail ON crm_contract_detail (ht_date);
CREATE INDEX IF NOT EXISTS idx_sync_time_detail ON crm_contract_detail (sync_time);

-- 合同产品明细表
CREATE TABLE IF NOT EXISTS crm_contract_product (
    ht_id VARCHAR(50) NOT NULL,
    product_id VARCHAR(50) NOT NULL,

    -- 产品信息
    product_code VARCHAR(100) DEFAULT NULL,
    product_name VARCHAR(200) DEFAULT NULL,
    product_model VARCHAR(100) DEFAULT NULL,
    product_spec VARCHAR(200) DEFAULT NULL,
    unit VARCHAR(20) DEFAULT NULL,
    quantity INT DEFAULT 0,
    retail_price DECIMAL(18,2) DEFAULT 0.00,
    unit_price DECIMAL(18,2) DEFAULT 0.00,
    total_price DECIMAL(18,2) DEFAULT 0.00,
    remark VARCHAR(500) DEFAULT NULL,
    cost_price DECIMAL(18,2) DEFAULT 0.00,
    cost_total DECIMAL(18,2) DEFAULT 0.00,

    sync_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (ht_id, product_id)
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_product_ht_id ON crm_contract_product (ht_id);
CREATE INDEX IF NOT EXISTS idx_product_code ON crm_contract_product (product_code);

-- 同步任务日志表
CREATE TABLE IF NOT EXISTS crm_sync_log (
    id BIGINT NOT NULL AUTO_INCREMENT,
    task_name VARCHAR(100) DEFAULT NULL,
    sync_date VARCHAR(20) DEFAULT NULL,
    total_count INT DEFAULT 0,
    success_count INT DEFAULT 0,
    fail_count INT DEFAULT 0,
    start_time TIMESTAMP DEFAULT NULL,
    end_time TIMESTAMP DEFAULT NULL,
    status SMALLINT DEFAULT 1,
    error_msg VARCHAR(1000) DEFAULT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_sync_date ON crm_sync_log (sync_date);
CREATE INDEX IF NOT EXISTS idx_create_time ON crm_sync_log (create_time);
