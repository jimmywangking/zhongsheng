# CRM API 接口测试报告

## 测试概述

本次测试验证了 CRM API 接口的完整调用流程，包括签名生成、列表查询、详情查询等功能。

---

## 测试结果

### ✅ 单元测试 (CovertUtilTest) - 7/7 通过

| 测试项 | 状态 | 说明 |
|--------|------|------|
| testJsonToContract | ✅ | JSON 转 Contract 对象 |
| testJsonToContractDetail | ✅ | JSON 转 ContractDetail 对象 |
| testJsonToContractList | ✅ | JSON 数组转 Contract 列表 |
| testHandleNullValues | ✅ | 处理空值和缺失字段 |
| testHandleMoneyFields | ✅ | 处理不同格式的金额字段 |
| testHandleArrayFields | ✅ | 处理数组类型字段 |
| testHandleEmptyArray | ✅ | 处理空数组 |

### ✅ 集成测试 (CrmApiTest) - 5/5 通过

| 测试项 | 状态 | 说明 |
|--------|------|------|
| testSignGeneration | ✅ | 签名生成算法 |
| testContractListApi | ✅ | 合同订单列表 API 调用 |
| testContractDetailApi | ✅ | 单个合同详情 API 调用 |
| testPostDataGeneration | ✅ | POST 数据生成 |
| testSignConsistency | ✅ | 签名一致性验证 |

---

## API 调用实际返回数据

### 1. 请求信息

**POST DATA:**
```json
{
  "stamp": 1774581983,
  "datatype": "150",
  "page": 1,
  "pagesize": 10
}
```

**签名源字符串:**
```
41314list_simple{"stamp":1774581983,"datatype":"150","page":1,"pagesize":10}
```

**请求 URL:**
```
https://crmapi.jzsoft.cn/apigetdata.aspx?version=v2&sign=[签名值]&key=s74gdLX96ULx&appid=41314&type=list_simple
```

### 2. 返回结果概览

```json
{
  "errcode": 0,
  "errmsg": "",
  "result": {
    "total": 87269,
    "data": [ ... 10 条记录 ... ]
  }
}
```

**总记录数:** 87,269 条

### 3. 单条数据字段解析

#### 基本信息
| 字段 | 值 | 说明 |
|------|-----|------|
| ht_id | 89491 | 合同 ID |
| key | 89491 | 主键 |
| ht_number | DM20260327608002 | 合同编号 |

#### 合同标题（嵌套对象）
```json
"ht_title": {
  "label": "易先生（索尼）13922800908【客户自提】PS5+ 游戏机手柄（黑色）",
  "value": "易先生（索尼）13922800908【客户自提】PS5+ 游戏机手柄（黑色）"
}
```

#### 合同类型（数组嵌套）
```json
"ht_type": {
  "viewType": "colorFontList",
  "value": [
    {
      "viewType": "colorFont",
      "label": "自然零售",
      "color": "#606266"
    }
  ]
}
```

#### 金额字段（嵌套对象）
```json
"ht_summoney": {
  "label": "¥4,158.00",
  "value": 4158.0
}
```

#### 状态字段（嵌套对象）
```json
"ht_state": {
  "viewType": "colorFont",
  "label": "执行中",
  "color": "#FFB600"
}
```

#### 客户字段（链接类型）
```json
"ht_customerid": {
  "viewType": "link",
  "value": 8777,
  "label": "易先生（索尼）15238950908",
  "formKey": "148"
}
```

#### 其他字段
| 字段 | 值 | 说明 |
|------|-----|------|
| ht_date | 2026-03-26 | 合同日期 |
| ht_enddate | 2026-03-26 | 合同结束日期 |
| ht_preside | 廖智稳 | 负责人 |
| addtime | 2026-03-27 11:21:21 | 创建时间 |
| updateDate | 2026-03-27 11:23:40 | 更新时间 |

---

## 数据格式总结

### 字段值类型

1. **简单字符串**: `ht_number`, `ht_date`, `ht_preside`, `addtime`, `updateDate`
2. **嵌套对象 (label+value)**: `ht_title`, `ht_state`, `ht_ckstate`
3. **嵌套对象 (label+value+color)**: `ht_state`, `ht_type`
4. **嵌套对象 (value 为数字)**: `ht_summoney`, `ht_hkmoney`, `ht_maoli`
5. **链接类型 (viewType=link)**: `ht_customerid`
6. **数组类型 (viewType=colorFontList)**: `ht_type`

### 解析规则

1. **字符串字段**: 直接获取 `json.getString(fieldName)`
2. **嵌套对象**: 获取 `json.getJSONObject(fieldName).getString("value")` 或 `getLabel()`
3. **金额数字**: 获取 `json.getJSONObject(fieldName).getBigDecimal("value")`
4. **数组字段**: 获取第一个元素的 label 值

---

## 代码修改总结

### 1. 签名算法修复 (`SignUtil.java`)

**修改前:**
```java
String plainText = id + id + type;  // ❌ 错误
```

**修改后:**
```java
String plainText = id + type + postData;  // ✅ 正确
```

### 2. 数据转换工具增强 (`CovertUtil.java`)

新增方法：
- `getValueAsString()` - 获取嵌套字符串值
- `getLabelAsString()` - 获取嵌套 label 值
- `getValueAsBigDecimal()` - 获取嵌套金额值
- `getValueAsInteger()` - 获取嵌套整数值
- `getValueFromArray()` - 获取数组类型字段的第一个 label

---

## 运行测试

```bash
# 运行所有测试
mvn test

# 运行数据转换测试
mvn test -Dtest=CovertUtilTest

# 运行 API 集成测试
mvn test -Dtest=CrmApiTest
```

---

## 测试结论

✅ **所有测试通过 (12/12)**

1. 签名算法正确，可以成功调用 API
2. 数据转换工具可以正确解析 API 返回的复杂嵌套结构
3. 合同列表和详情接口都可以正常获取数据
4. 单元测试覆盖了各种边界情况（空值、数组、不同格式等）

---

**测试时间:** 2026-03-27  
**测试人员:** AI Assistant  
**测试环境:** macOS + JDK 21 + Maven 3.9.14
