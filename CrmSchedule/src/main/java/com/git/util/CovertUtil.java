package com.git.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.git.entity.Contract;
import com.git.entity.ContractDetail;
import com.git.entity.ContractProduct;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据转换工具类
 */
@Slf4j
public class CovertUtil {

    /**
     * 将 JSON 数组转换为 Contract 列表（列表 API 格式）
     * API 返回格式：{"data": [{"detail": {...}}]}
     */
    public static List<Contract> jsonToContractList(JSONArray data) {
        List<Contract> itemList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            JSONObject item = data.getJSONObject(i);
            // 从 item.detail 中提取合同数据
            JSONObject detail = item.getJSONObject("detail");
            if (detail != null) {
                itemList.add(jsonToContract(detail));
            }
        }
        return itemList;
    }

    /**
     * 将 JSON 对象转换为 Contract（列表 API 格式 - detail 对象）
     */
    public static Contract jsonToContract(JSONObject json) {
        Contract item = new Contract();
        item.setHtId(json.getString("ht_id"));
        item.setKey(json.getString("ht_id"));
        item.setLastbaojia(0);
        item.setHtTitle(json.getString("ht_title"));
        item.setHtNumber(json.getString("ht_number"));
        item.setHtType(json.getString("ht_type"));
        item.setHtDate(parseDate(json.getString("ht_date")));
        item.setHtEnddate(parseDate(json.getString("ht_enddate")));
        item.setHtSummoney(parseBigDecimal(json.getString("ht_summoney")));
        item.setHtState(json.getString("ht_state"));
        item.setHtHkmoney(parseBigDecimal(json.getString("ht_hkmoney")));
        item.setHtBillmoney(parseBigDecimal(json.getString("ht_billmoney")));
        item.setHtMaoli(parseBigDecimal(json.getString("ht_maoli")));
        item.setHtChengben(parseBigDecimal(json.getString("ht_chengben")));
        item.setHtCkstate(json.getString("ht_ckstate"));
        item.setAddtime(parseDate(json.getString("addtime")));
        
        // 解析客户信息
        JSONObject customerObj = json.getJSONObject("ht_customerid");
        if (customerObj != null) {
            item.setHtCustomer(customerObj.getString("title"));
            item.setHtCustomerid(parseInteger(customerObj.getString("id")));
        }
        
        // 解析负责人
        JSONObject presideObj = json.getJSONObject("ht_preside");
        if (presideObj != null) {
            item.setHtPreside(presideObj.getString("title"));
        }
        
        // 解析创建人
        JSONObject dataUseridObj = json.getJSONObject("data_userid");
        if (dataUseridObj != null) {
            item.setDataUserid(dataUseridObj.getString("title"));
        }
        
        return item;
    }

    /**
     * 将 JSON 对象转换为 ContractDetail（详情 API 格式）
     * API 返回格式：{"detail": {...}}
     */
    public static ContractDetail jsonToContractDetail(JSONObject result) {
        if (result == null) {
            return null;
        }

        // 从 detail 字段直接获取（API 返回格式：{"detail": {...}}）
        JSONObject detailData = result.getJSONObject("detail");
        if (detailData == null || detailData.isEmpty()) {
            return null;
        }

        ContractDetail detail = new ContractDetail();

        // 解析基本字段
        detail.setHtId(detailData.getString("ht_id"));
        detail.setKey(detailData.getString("ht_id"));
        detail.setHtTitle(detailData.getString("ht_title"));
        detail.setHtNumber(detailData.getString("ht_number"));
        detail.setHtOrder(detailData.getString("ht_order"));
        detail.setHtType(detailData.getString("ht_type"));
        detail.setHtDate(parseDate(detailData.getString("ht_date")));
        detail.setHtBegindate(parseDate(detailData.getString("ht_begindate")));
        detail.setHtEnddate(parseDate(detailData.getString("ht_enddate")));
        detail.setHtSummoney(parseBigDecimal(detailData.getString("ht_summoney")));
        detail.setHtState(detailData.getString("ht_state"));
        detail.setHtHkmoney(parseBigDecimal(detailData.getString("ht_hkmoney")));
        detail.setHtBillmoney(parseBigDecimal(detailData.getString("ht_billmoney")));
        detail.setHtMaoli(parseBigDecimal(detailData.getString("ht_maoli")));
        detail.setHtChengben(parseBigDecimal(detailData.getString("ht_chengben")));
        detail.setHtYugumaoli(parseBigDecimal(detailData.getString("ht_yugumaoli")));
        detail.setHtCkstate(detailData.getString("ht_ckstate"));
        detail.setShenhestate(detailData.getString("shenhestate"));
        detail.setHtFahuostate(detailData.getString("ht_fahuostate"));
        detail.setDiyDdl2(detailData.getString("diy_ddl2"));
        detail.setHtRemark(detailData.getString("ht_remark"));
        detail.setHtContract(detailData.getString("ht_contract"));
        detail.setAddtime(detailData.getString("addtime"));
        detail.setUpdateDate(detailData.getString("update_date"));
        detail.setLastbaojia(detailData.getInteger("lastbaojia"));

        // 解析客户信息
        JSONObject customerObj = detailData.getJSONObject("ht_customerid");
        if (customerObj != null) {
            detail.setHtCustomer(customerObj.getString("title"));
            detail.setHtCustomerid(parseInteger(customerObj.getString("id")));
        }

        // 解析负责人
        JSONObject presideObj = detailData.getJSONObject("ht_preside");
        if (presideObj != null) {
            detail.setHtPreside(presideObj.getString("title"));
        }

        // 解析创建人
        JSONObject dataUseridObj = detailData.getJSONObject("data_userid");
        if (dataUseridObj != null) {
            detail.setDataUserid(dataUseridObj.getString("title"));
        }

        return detail;
    }

    /**
     * 解析产品明细数组
     * API 返回格式：{"detail": {"child_mx": [...]}}
     */
    public static List<ContractProduct> jsonToContractProducts(JSONObject result, String htId) {
        List<ContractProduct> products = new ArrayList<>();

        if (result == null) {
            return products;
        }

        // 从 detail.child_mx 获取产品明细（API 返回格式：{"detail": {...}}）
        JSONObject detailData = result.getJSONObject("detail");
        if (detailData == null) {
            return products;
        }

        JSONArray childMx = detailData.getJSONArray("child_mx");
        if (childMx == null || childMx.isEmpty()) {
            return products;
        }

        // 调试日志：记录原始 JSON 和字段名
        log.info("======== 产品明细原始数据，合同 ID: {}, child_mx 数量：{} ========", htId, childMx.size());
        for (int i = 0; i < childMx.size(); i++) {
            JSONObject productJson = childMx.getJSONObject(i);
            log.info("  --- 产品 {} ---", i + 1);
            log.info("  完整 JSON: {}", productJson.toJSONString());
            log.info("  字段列表:");
            for (String key : productJson.keySet()) {
                Object value = productJson.get(key);
                // 显示字段名的字节表示，确认是否有空格
                log.info("    '{}': {}", key, value);
            }
        }

        for (int i = 0; i < childMx.size(); i++) {
            JSONObject productJson = childMx.getJSONObject(i);
            ContractProduct product = new ContractProduct();
            product.setHtId(htId);
            product.setProductCode(productJson.getString("产品编号"));
            product.setProductId(productJson.getString("产品ID"));
            product.setProductName(productJson.getString("产品名称"));
            product.setProductModel(productJson.getString("产品型号"));
            product.setProductSpec(productJson.getString("产品规格"));
            product.setUnit(productJson.getString("计件单位"));

            // 解析数量（字符串转整数）
            String qtyStr = productJson.getString("数量");
            product.setQuantity(qtyStr != null && !qtyStr.isEmpty() ? Integer.parseInt(qtyStr) : 0);

            // 解析价格
            String unitPriceStr = productJson.getString("单价");
            product.setUnitPrice(unitPriceStr != null && !unitPriceStr.isEmpty() ? new BigDecimal(unitPriceStr) : BigDecimal.ZERO);

            String totalPriceStr = productJson.getString("总价");
            product.setTotalPrice(totalPriceStr != null && !totalPriceStr.isEmpty() ? new BigDecimal(totalPriceStr) : BigDecimal.ZERO);

            product.setRemark(productJson.getString("明细备注"));

            String costPriceStr = productJson.getString("成本单价");
            product.setCostPrice(costPriceStr != null && !costPriceStr.isEmpty() ? new BigDecimal(costPriceStr) : BigDecimal.ZERO);

            String costTotalStr = productJson.getString("成本总价");
            product.setCostTotal(costTotalStr != null && !costTotalStr.isEmpty() ? new BigDecimal(costTotalStr) : BigDecimal.ZERO);

            products.add(product);
        }

        return products;
    }

    // ==================== 辅助方法 ====================

    private static String parseDate(Object value) {
        if (value == null) return null;
        String dateStr = String.valueOf(value);
        if (dateStr.contains("/")) {
            // 格式：2026/3/25 0:00:00 → 2026-03-25
            String[] parts = dateStr.split(" ")[0].split("/");
            if (parts.length == 3) {
                return String.format("%s-%02d-%02d", parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            }
        }
        return dateStr.length() >= 10 ? dateStr.substring(0, 10) : dateStr;
    }

    private static String parseStringValue(Object value) {
        if (value == null) return null;
        if (value instanceof String) {
            return (String) value;
        }
        return String.valueOf(value);
    }

    private static Integer parseIntegerValue(Object value) {
        if (value == null) return 0;
        try {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception e) {
            return 0;
        }
    }

    private static Integer parseInteger(String value) {
        if (value == null || value.isEmpty()) return 0;
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private static BigDecimal parseBigDecimalValue(Object value) {
        if (value == null) return BigDecimal.ZERO;
        try {
            if (value instanceof BigDecimal) {
                return (BigDecimal) value;
            }
            if (value instanceof Number) {
                return new BigDecimal(value.toString());
            }
            return new BigDecimal(String.valueOf(value));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private static BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isEmpty()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private static JSONObject parseObjectValue(Object value) {
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }
        return null;
    }

    private static String getLabelAsString(JSONObject json, String fieldName) {
        if (!json.containsKey(fieldName)) return null;
        Object obj = json.get(fieldName);
        if (obj instanceof JSONObject) {
            JSONObject fieldObj = (JSONObject) obj;
            return fieldObj.getString("label");
        } else if (obj instanceof String) {
            return (String) obj;
        }
        return null;
    }

    private static String parseLabelValue(Object value) {
        if (value == null) return null;
        if (value instanceof JSONObject) {
            JSONObject obj = (JSONObject) value;
            // 尝试从 label 获取
            if (obj.containsKey("label")) {
                return obj.getString("label");
            }
            // 尝试从 value 数组获取第一个 label
            if (obj.containsKey("value")) {
                Object val = obj.get("value");
                if (val instanceof JSONArray) {
                    JSONArray arr = (JSONArray) val;
                    if (arr.size() > 0) {
                        JSONObject first = arr.getJSONObject(0);
                        if (first != null && first.containsKey("label")) {
                            return first.getString("label");
                        }
                    }
                }
            }
        }
        return String.valueOf(value);
    }

    private static String getValueAsString(JSONObject json, String fieldName) {
        if (!json.containsKey(fieldName)) return null;
        Object obj = json.get(fieldName);
        if (obj instanceof JSONObject) {
            JSONObject fieldObj = (JSONObject) obj;
            return fieldObj.getString("value");
        } else if (obj instanceof String) {
            return (String) obj;
        }
        return null;
    }

    private static BigDecimal getValueAsBigDecimal(JSONObject json, String fieldName) {
        if (!json.containsKey(fieldName)) return BigDecimal.ZERO;
        Object obj = json.get(fieldName);
        if (obj instanceof JSONObject) {
            JSONObject fieldObj = (JSONObject) obj;
            Object value = fieldObj.get("value");
            if (value instanceof BigDecimal) return (BigDecimal) value;
            if (value instanceof Number) return new BigDecimal(value.toString());
        } else if (obj instanceof Number) {
            return new BigDecimal(obj.toString());
        }
        return BigDecimal.ZERO;
    }

    private static Integer getValueAsInteger(JSONObject json, String fieldName) {
        if (!json.containsKey(fieldName)) return 0;
        Object obj = json.get(fieldName);
        if (obj instanceof JSONObject) {
            JSONObject fieldObj = (JSONObject) obj;
            Object value = fieldObj.get("value");
            if (value instanceof Integer) return (Integer) value;
            if (value instanceof Number) return ((Number) value).intValue();
        } else if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        return 0;
    }

    private static String getValueFromArray(JSONObject json, String fieldName) {
        if (!json.containsKey(fieldName)) return null;
        Object obj = json.get(fieldName);
        if (obj instanceof JSONObject) {
            JSONObject fieldObj = (JSONObject) obj;
            JSONArray valueArray = fieldObj.getJSONArray("value");
            if (valueArray != null && !valueArray.isEmpty()) {
                JSONObject firstItem = valueArray.getJSONObject(0);
                if (firstItem != null) {
                    return firstItem.getString("label");
                }
            }
        }
        return null;
    }
}
