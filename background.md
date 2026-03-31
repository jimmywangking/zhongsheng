Joe: [File] CrmSchedule.rar
Joe: https://crmapi.jzsoft.cn/apitest.aspx?apitest=Lr4aBjjTkcVbf4JQJCZkJ0o9E5WOjSnQR4qGqwD4kOTIE3cD%2bj35WxjnVB8w6JIr3gjhmxl%2fEvGx7Gg9My0jkA%3d%3d
Joe: 有空看看这个
Joe: 抽时间我给你讲讲背景
Thinker: 收到，API文档啊，表还不少
Joe: 现在就用一个api
Joe: 把合同和合同涉及的产品查询出来
Joe: 压缩包里是已经实现了的，每隔10分钟采集一下当天的所有订单（合同），但是缺少产品信息
Thinker: 我刚看了下，这个API文档里合同API里面有产品明细，没有单独的产品id和数量字段。然后代码包里的CrmItem只有部分字段，都没有产品明细。这个api文档是最终交付的后端结果反馈刚开始做？
Joe: [Chat History]
Joe: 你看有帮助吗
Thinker: 哦这个就是查出合同列表，然后拿ht_id再重新请求产品明细。那这个明细也应该是个id吧，每个id对应实体包括了各种产品id（对应产品信息表ID标识154）及对应数量啥的，但是这个数量没看到啥产品订单数量相关的api。
Thinker: 这个到底是要做个啥？
Joe: 这个就是个后台数据导出
Joe: 把crm系统里的订单导出来，要放到分析系统里
Thinker: 那API对应的是crm里面所有的表？然后正在用java往外时时读数据
Thinker: 后台有数据后，再做一个前端做展示？
Joe: 对
Joe: 是的，有了数据以后想做个类似大屏展示订单
Thinker: 明白了
Joe: 那个代码你有空搞不
Joe: 我刚又看了看
Joe: [Photo]
Joe: 第一步从列表接口里拿到合同列表，第二步用合同id到详情列表里拿合同详情
Joe: https://crmapi.jzsoft.cn/apitest.aspx?apitest=Lr4aBjjTkcVbf4JQJCZkJ0o9E5WOjSnQR4qGqwD4kOTIE3cD%2bj35WxjnVB8w6JIr3gjhmxl%2fEvGx7Gg9My0jkA%3d%3d
Thinker: [Audio] 31"
Joe: [Audio] 45"
Thinker: 好，我研究下
Joe: 还有个事，我记得你拿过AI的认证，你对市面上的AI工具以及使用场景熟悉吗？ 比如如何写Prompt是一门技术，什么场景下用什么工具帮助不同的业务部门（有销售，有市场，有自媒体，有财务，有仓库），什么场景下哪个工具能帮助个人提高工作效率
Joe: 能给一个小公司做个培训吗？
Thinker: [Audio] 34"
Thinker: [Photo]
Thinker: [Audio] 60"
Thinker: [Audio] 24"
Joe: 1. 如何写prompt可以挑两三个场景介绍一下使用方式和效果； 2. 如何做图、做视频，哪些工具更牛逼  3. 如何让AI做总结、写PPT文案  4. 如何用AI写代码，处理Excel数据
Thinker: [Audio] 42"
Joe: 我觉得可以从这几个topic，搞个半小时到一个小时的培训
Thinker: [Audio] 36"
Thinker: [Audio] 51"
Joe: 我在想哈，其实中国有很多小企业，没有这个基础，不会用，又想用
Joe: 市场还是不小的
Thinker: [Audio] 30"
Thinker: [Audio] 60"
Thinker: [Audio] 7"
Joe: 但是nano banana也有提示词技巧（怎么渲染，怎么运镜。。。），可以参考大神讲的视频，copy一下思路做个演示
Thinker: [Audio] 21"
Thinker: [Audio] 18"
Joe: 就从这个角度，找些简单但说明问题的demo，扫扫盲
Thinker: [Audio] 38"
Thinker: [Audio] 52"
Joe: 校友会让你讲课给钱不？
Joe: 我有个哥们前一段失业了，现在模仿抖音上的买车砍价的，粉丝两三万了
Thinker: [Audio] 53"
Joe: 抖音只要粉丝过万，就有流量收益
Joe: 你给我弄这个培训，我给你钱哈！
Thinker: [Audio] 17"
Joe: 对，就这种
Thinker: [Audio] 22"
Joe: 这种自媒体是分区域的，比如辽宁的4s店如果想找这种自媒体合作，只能找辽宁的自媒体号，不能找北京的上海的
Thinker: [Audio] 52"
Joe: 就是培训电商公司的普通员工，我弟那个经贸公司，卖电器、马桶、相机、床垫各种商品
Thinker: [Audio] 34"
Joe: 我有空找几个视频，你综合起来，系统消化一下再给别人讲
Joe: 一个方向是写营销文案、营销图片/视频 另一个方向是提高内部员工的工作效率
Thinker: 行，这种可以
Joe: 嗯，就这俩方向，你也多看看
Joe: 咱年前能不能搞出来啊
Joe: 快速迭代，当个事干
Joe: 还有一个重要的技能：翻墙
Thinker: [Audio] 52"
Thinker: 实现总结 1. 创建了合同详情数据模型 (CrmContractDetail.java) 包含合同的所有字段（基本信息、金额、状态、关联信息等） 使用 Lombok 的 @Data 简化代码 2. 添加了详情数据转换方法 (CovertUtil.jsonToCrmContractDetail()) 将 API 返回的 JSON 转换为 CrmContractDetail 对象 处理嵌套 JSON 结构（如 ht_title.value、ht_state.label 等） 3. 实现了合同详情查询方法 (SyncTaskJob.getContractDetail()) 使用 type=view 查询详情 POST 参数：datatype=150、stamp、msgid=contractId 复用现有的签名和 HTTP 请求逻辑 4. 修改了主执行流程 (SyncTaskJob.execute()) 在获取列表数据时，提取所有 ht_id 到 contractIds 列表 列表数据导出完成后，批量获取详情并导出 修复了原文件名拼接问题（使用 currentDay 而不是重复路径） 5. 实现了批量详情导出 (SyncTaskJob.exportContractDetails()) 遍历所有合同 ID，逐个调用详情 API 每 100 条批量写入 Excel，避免内存占用过大 记录成功和失败数量 异常处理：单个合同失败不影响其他合同 6. 添加了配置项 (application.properties) excel.detail.template.path: 详情 Excel 模板路径（默认使用列表模板） excel.detail.file.path: 详情 Excel 保存路径（默认使用列表路径）
Joe: 教这些人使用翻墙技术，看youtube，境外电商上的东西，参考进来
Thinker: [File] application.properties
Thinker: [File] CrmContractDetail.java
Thinker: [File] CovertUtil.java
Thinker: [File] SyncTaskJob.java
Thinker: [Audio] 10"
Joe: 1. API 取数  2. AI 培训 这俩事
Thinker: [Audio] 47"
Thinker: 定时任务触发 ↓ 获取合同列表（分页） ↓ 提取所有 ht_id ↓ 导出列表数据到 crm_YYYY-MM-DD.xlsx ↓ 批量获取合同详情（遍历 ht_id） ↓ 导出详情数据到 crm_detail_YYYY-MM-DD.xlsx ↓ 任务完成
Joe: 嗯，那先把API这个调通也行
Joe: 我先搞后面的数据分析
Thinker: [OK]