# CRM Schedule 项目编译运行指南

## 📁 项目目录结构

```
/Users/helloworld/Downloads/CRMSchrdule/
├── CrmSchedule/          # 主项目目录
│   ├── pom.xml          # Maven 配置文件
│   ├── src/             # 源代码
│   └── run.md           # 本文件
└── ...
```

---

## 🔧 一、编译项目

### 1. 进入项目目录

```bash
cd /Users/helloworld/Downloads/CRMSchrdule/CrmSchedule
```

### 2. 执行编译命令

```bash
# 使用本地 Maven
/opt/local/bin/mvn clean package -DskipTests

# 或使用系统 Maven（如果已安装）
mvn clean package -DskipTests
```

### 3. 编译成功标志

```
[INFO] BUILD SUCCESS
[INFO] Total time:  XX.XXX s
```

编译后的 JAR 文件位置：
```
target/crm-schedule-1.2.0.jar
```

---

## 🚀 二、运行服务

### 方式 A：前台运行（开发调试）

```bash
cd /Users/helloworld/Downloads/CRMSchrdule/CrmSchedule
java -jar target/crm-schedule-1.2.0.jar
```

### 方式 B：后台运行（生产环境）

```bash
cd /Users/helloworld/Downloads/CRMSchrdule/CrmSchedule
nohup java -jar target/crm-schedule-1.2.0.jar > /tmp/crm.log 2>&1 &
```

### 方式 C：使用脚本运行

```bash
# 启动服务
./start.sh

# 停止服务
./stop.sh

# 重启服务
./restart.sh
```

---

## ✅ 三、验证服务状态

### 1. 检查健康状态

```bash
curl http://localhost:9001/health
```

**成功响应：**
```
CRM Schedule Service is running!
```

### 2. 检查进程

```bash
ps aux | grep crm-schedule | grep -v grep
```

### 3. 查看日志

```bash
# 实时查看日志
tail -f /tmp/crm.log

# 查看最近 100 行
tail -100 /tmp/crm.log

# 查看错误日志
grep -i "error\|exception" /tmp/crm.log | tail -20
```

---

## 🔄 四、手动触发同步

### 方式 A：使用 curl 命令

```bash
curl -X POST http://localhost:9001/api/sync-logs/trigger
```

**成功响应：**
```json
{
  "code": 200,
  "message": "同步任务已触发，请在日志中查看执行结果",
  "timestamp": "2026-03-31 17:40:55"
}
```

### 方式 B：使用 Dashboard 页面

1. 打开浏览器访问：http://localhost:9001/dashboard.html
2. 点击右上角 **"同步数据"** 按钮

---

## 📊 五、查看同步结果

### 1. 查看 Dashboard 统计数据

```bash
curl http://localhost:9001/api/dashboard/stats | python3 -m json.tool
```

### 2. 查看同步日志

```bash
# 查看同步结果
grep "合同列表同步完成\|合同详情同步完成\|已批量保存" /tmp/crm.log | tail -10
```

**正常输出示例：**
```
2026-03-31 17:40:59 [INFO] - ======== 合同列表同步完成，共获取 154 个合同 ID（最近 90 天） ========
2026-03-31 17:41:34 [INFO] - ======== 已批量保存 100 条合同详情到 contract_detail 表 ========
2026-03-31 17:41:52 [INFO] - ======== 已批量保存 180 条产品明细到 crm_contract_product 表 ========
2026-03-31 17:41:52 [INFO] - ======== 合同详情同步完成，成功：154, 失败：1, 产品明细总数：180 ========
```

---

## 🛑 六、停止服务

### 方式 A：使用 kill 命令

```bash
# 查找进程 ID
ps aux | grep crm-schedule | grep -v grep

# 停止服务
pkill -f crm-schedule

# 或强制停止
pkill -9 -f crm-schedule
```

### 方式 B：使用脚本

```bash
./stop.sh
```

---

## 📋 七、快速操作脚本

### start.sh - 启动服务

```bash
#!/bin/bash
cd /Users/helloworld/Downloads/CRMSchrdule/CrmSchedule

# 检查是否已运行
if ps aux | grep -v grep | grep crm-schedule > /dev/null; then
    echo "⚠️  服务已在运行中"
    exit 1
fi

# 编译
echo "🔧 正在编译..."
/opt/local/bin/mvn clean package -DskipTests -q

if [ $? -ne 0 ]; then
    echo "❌ 编译失败"
    exit 1
fi

echo "✅ 编译成功"

# 启动
echo "🚀 启动服务..."
nohup java -jar target/crm-schedule-1.2.0.jar > /tmp/crm.log 2>&1 &

sleep 10

# 验证
if curl -s http://localhost:9001/health > /dev/null; then
    echo "✅ 服务启动成功"
    echo "📊 Dashboard: http://localhost:9001/dashboard.html"
else
    echo "❌ 服务启动失败，请查看日志：tail -f /tmp/crm.log"
    exit 1
fi
```

### stop.sh - 停止服务

```bash
#!/bin/bash
echo "🛑 停止服务..."
pkill -f crm-schedule
sleep 2

if ps aux | grep -v grep | grep crm-schedule > /dev/null; then
    echo "⚠️  强制停止..."
    pkill -9 -f crm-schedule
fi

echo "✅ 服务已停止"
```

### restart.sh - 重启服务

```bash
#!/bin/bash
./stop.sh
sleep 2
./start.sh
```

### sync.sh - 手动触发同步

```bash
#!/bin/bash
echo "🔄 触发同步任务..."
curl -X POST http://localhost:9001/api/sync-logs/trigger
echo ""
echo "⏳ 等待同步完成..."
sleep 60
echo ""
echo "📊 同步结果:"
grep "合同列表同步完成\|合同详情同步完成\|已批量保存" /tmp/crm.log | tail -5
```

---

## 🔍 八、常用调试命令

### 1. 查看 API 返回数据

```bash
# Dashboard 数据
curl http://localhost:9001/api/dashboard/stats | python3 -m json.tool

# 合同列表
curl http://localhost:9001/api/contracts | python3 -m json.tool
```

### 2. 数据库查询（H2 Console）

浏览器访问：http://localhost:9001/h2-console

**连接配置：**
- JDBC URL: `jdbc:h2:mem:crm_schedule`
- Username: `sa`
- Password: (空)

**常用 SQL：**
```sql
-- 查看合同总数
SELECT COUNT(*) FROM crm_contract;

-- 查看最近 10 条合同
SELECT * FROM crm_contract ORDER BY sync_time DESC LIMIT 10;

-- 查看产品统计
SELECT product_name, SUM(quantity) as total_qty 
FROM crm_contract_product 
GROUP BY product_name 
ORDER BY total_qty DESC LIMIT 10;
```

---

## 📝 九、故障排查

### 问题 1：编译失败

```bash
# 清理 Maven 缓存
rm -rf ~/.m2/repository/com/git

# 重新编译
mvn clean package -DskipTests -U
```

### 问题 2：端口被占用

```bash
# 查找占用 9001 端口的进程
lsof -i :9001

# 停止进程
kill -9 <PID>
```

### 问题 3：服务启动失败

```bash
# 查看详细错误
tail -100 /tmp/crm.log | grep -i "error\|exception"

# 检查 Java 版本
java -version

# 检查 Maven 版本
mvn -version
```

---

## 📞 十、技术支持

- Dashboard 地址：http://localhost:9001/dashboard.html
- H2 控制台：http://localhost:9001/h2-console
- 日志文件：/tmp/crm.log
