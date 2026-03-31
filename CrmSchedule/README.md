# CRM Schedule - CRM 数据同步服务

## 项目简介

这是一个基于 Spring Boot 的 CRM 数据同步服务，定时从 CRM API 获取合同数据并存储到 MySQL 数据库中。

## 技术栈

- **框架**: Spring Boot 3.1.5
- **数据库**: MySQL 8.0+
- **ORM**: MyBatis-Plus 3.5.4.1
- **HTTP 客户端**: Apache HttpClient
- **JSON 处理**: FastJSON2
- **Excel 处理**: EasyExcel (可选)

## 项目结构

```
CrmSchedule/
├── src/main/java/com/git/
│   ├── ScheduleApplication.java      # 启动类
│   ├── config/                       # 配置类
│   │   ├── CrmApiProperties.java     # CRM API 配置
│   │   └── MyMetaObjectHandler.java  # MyBatis 自动填充
│   ├── controller/                   # 控制器
│   │   ├── ContractController.java   # 合同查询接口
│   │   ├── ContractDetailController.java  # 合同详情查询接口
│   │   └── SyncLogController.java    # 同步日志接口
│   ├── entity/                       # 实体类
│   │   ├── Contract.java             # 合同实体
│   │   ├── ContractDetail.java       # 合同详情实体
│   │   └── SyncLog.java              # 同步日志实体
│   ├── mapper/                       # Mapper 接口
│   │   ├── ContractMapper.java
│   │   ├── ContractDetailMapper.java
│   │   └── SyncLogMapper.java
│   ├── service/                      # 服务层
│   │   ├── CrmApiClient.java         # CRM API 客户端
│   │   ├── ContractService.java      # 合同服务
│   │   ├── ContractDetailService.java # 合同详情服务
│   │   └── SyncLogService.java       # 同步日志服务
│   ├── task/                         # 定时任务
│   │   └── SyncTaskJob.java          # 数据同步任务
│   └── util/                         # 工具类
│       ├── CovertUtil.java           # 数据转换工具
│       └── SignUtil.java             # API 签名工具
├── src/main/resources/
│   ├── application.yml               # 应用配置
│   ├── db/
│   │   └── schema.sql                # 数据库初始化脚本
│   └── crm-logback.xml               # 日志配置
└── pom.xml                           # Maven 配置
```

## 快速开始

### 1. 环境要求

- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 2. 数据库配置

创建 MySQL 数据库并执行初始化脚本：

```sql
-- 方式一：使用命令行
mysql -u root -p < src/main/resources/db/schema.sql

-- 方式二：手动执行
-- 登录 MySQL 后，复制 schema.sql 内容执行
```

### 3. 修改配置

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/crm_schedule?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

### 4. 运行项目

```bash
# 进入项目目录
cd CrmSchedule

# Maven 编译运行
mvn clean install
mvn spring-boot:run

# 或者直接打包运行
mvn clean package
java -jar target/crm-schedule-1.2.0.jar
```

### 5. 验证

启动成功后，访问以下接口：

- 健康检查：http://localhost:9001/health
- 合同列表：http://localhost:9001/api/contracts
- 合同详情：http://localhost:9001/api/contract-details
- 同步日志：http://localhost:9001/api/sync-logs

## 配置说明

### CRM API 配置

```yaml
crm:
  api:
    id: 41314                      # API ID
    secret-id: s74gdLX96ULx        # Secret ID
    secret-key: 7ecd94df-fdfd-4dd5-9a38-9fa470ec5c33  # Secret Key
    url: https://crmapi.jzsoft.cn/apigetdata.aspx
    datatype: "150"                # 数据类型（合同订单）
```

### 定时任务配置

```yaml
crm:
  task:
    cron: 0 0/10 * * * ?           # Cron 表达式，每 10 分钟执行一次
    query-date-type: today         # 查询日期类型：today/yesterday/custom
    custom-date: ""                # 自定义日期（格式：yyyy-MM-dd）
```

### Excel 导出配置（可选）

```yaml
excel:
  template:
    path: ./template.xlsx
  file:
    path: ./export/
```

## API 接口

### 1. 合同查询接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/contracts` | GET | 查询所有合同 |
| `/api/contracts/page` | GET | 分页查询合同 |
| `/api/contracts/{htId}` | GET | 根据合同 ID 查询 |

### 2. 合同详情接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/contract-details` | GET | 查询所有合同详情 |
| `/api/contract-details/page` | GET | 分页查询合同详情 |
| `/api/contract-details/{htId}` | GET | 根据合同 ID 查询详情 |

### 3. 同步日志接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/sync-logs` | GET | 查询所有日志 |
| `/api/sync-logs/date/{syncDate}` | GET | 根据日期查询日志 |
| `/api/sync-logs/trigger` | POST | 手动触发同步 |

## 数据库表结构

### crm_contract - 合同列表表

| 字段 | 类型 | 描述 |
|------|------|------|
| id | BIGINT | 主键 ID |
| ht_id | VARCHAR | 合同 ID |
| ht_title | VARCHAR | 合同标题 |
| ht_number | VARCHAR | 合同编号 |
| ht_summoney | DECIMAL | 合同总额 |
| ht_customer | VARCHAR | 客户名称 |
| ... | ... | ... |

### crm_contract_detail - 合同详情表

在合同列表表基础上，增加以下字段：
- ht_remark - 备注
- ht_address - 地址
- ht_delivery - 交货方式
- ht_payment - 付款方式

### crm_sync_log - 同步日志表

| 字段 | 类型 | 描述 |
|------|------|------|
| id | BIGINT | 主键 ID |
| task_name | VARCHAR | 任务名称 |
| sync_date | VARCHAR | 同步日期 |
| total_count | INT | 总记录数 |
| success_count | INT | 成功数量 |
| status | TINYINT | 状态 |

## 常见问题

### 1. 数据库连接失败

检查 MySQL 是否启动，用户名密码是否正确。

### 2. 定时任务不执行

检查 cron 表达式配置，确保时区设置正确。

### 3. API 调用失败

检查网络是否通畅，API 凭证是否正确。

## 开发计划

- [ ] 添加配置加密支持（Jasypt）
- [ ] 添加重试机制
- [ ] 添加数据导出 Excel 功能
- [ ] 添加数据可视化界面

## License

MIT
