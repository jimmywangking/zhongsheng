完成总结

    最终代码结构

      1 CrmSchedule/
      2 ├── src/main/java/com/git/
      3 │   ├── ScheduleApplication.java          # 启动类
      4 │   ├── config/
      5 │   │   ├── CrmApiProperties.java         # CRM API 配置类
      6 │   │   └── MyMetaObjectHandler.java      # MyBatis 自动填充配置
      7 │   ├── controller/
      8 │   │   ├── ContractController.java       # 合同查询接口
      9 │   │   ├── ContractDetailController.java # 合同详情查询接口
     10 │   │   └── SyncLogController.java        # 同步日志接口
     11 │   ├── entity/
     12 │   │   ├── Contract.java                 # 合同实体
     13 │   │   ├── ContractDetail.java           # 合同详情实体
     14 │   │   └── SyncLog.java                  # 同步日志实体
     15 │   ├── mapper/
     16 │   │   ├── ContractMapper.java
     17 │   │   ├── ContractDetailMapper.java
     18 │   │   └── SyncLogMapper.java
     19 │   ├── service/
     20 │   │   ├── CrmApiClient.java             # CRM API 客户端
     21 │   │   ├── ContractService.java          # 合同服务
     22 │   │   ├── ContractDetailService.java    # 合同详情服务
     23 │   │   └── SyncLogService.java           # 同步日志服务
     24 │   ├── task/
     25 │   │   └── SyncTaskJob.java              # 定时同步任务
     26 │   └── util/
     27 │       ├── CovertUtil.java               # 数据转换工具
     28 │       └── SignUtil.java                 # API 签名工具
     29 ├── src/main/resources/
     30 │   ├── application.yml                   # 应用配置
     31 │   ├── db/
     32 │   │   └── schema.sql                    # 数据库初始化脚本
     33 │   └── crm-logback.xml                   # 日志配置
     34 ├── pom.xml                               # Maven 依赖配置
     35 └── README.md                             # 项目文档

    数据库表（3 张）


    ┌───────────────────┬────────────────┐
    │ 表名              │ 描述           │
    ├───────────────────┼────────────────┤
    │ crm_contract      │ 合同列表表     │
    │ crm_contract_detail │ 合同详情表     │
    │ crm_sync_log        │ 同步任务日志表 │
    └───────────────────┴────────────────┘


    主要改进

     1. 使用 MySQL 存储数据 - 免费开源，本地可运行
     2. 敏感配置移至 application.yml - API 凭证集中管理
     3. 使用 MyBatis-Plus - 简化数据库操作
     4. 重构 API 调用逻辑 - 抽取 CrmApiClient 统一处理
     5. 添加 REST API 接口 - 可查询合同数据和日志
     6. 完善日志记录 - 记录每次同步任务执行情况

    运行步骤

      1 # 1. 创建 MySQL 数据库
      2 mysql -u root -p < src/main/resources/db/schema.sql
      3 
      4 # 2. 修改 application.yml 中的数据库密码
      5 
      6 # 3. 运行项目
      7 cd CrmSchedule
      8 mvn spring-boot:run
      9 
     10 # 4. 访问接口
     11 # http://localhost:9001/health
     12 # http://localhost:9001/api/contracts