package com.git.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.git.config.CrmApiProperties;
import com.git.util.SignUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CRM API 客户端服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CrmApiClient {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final CrmApiProperties apiProperties;

    /**
     * 获取合同列表数据（按日期查询）
     */
    public JSONObject getContractList(int page, String currentDay, int pageSize) {
        String type = "list_simple";
        Map<String, Object> postData = buildBasePostData();

        postData.put("page", page);
        postData.put("pagesize", pageSize);

        // 如果指定了日期，添加时间查询条件
        if (currentDay != null && !currentDay.isEmpty()) {
            Map<String, Object> queryParams = new LinkedHashMap<>();
            Map<String, Object> addTimeParams = new LinkedHashMap<>();

            addTimeParams.put("connector", "17");
            addTimeParams.put("queryType", "field");
            addTimeParams.put("jzValue", new String[]{currentDay, currentDay});
            queryParams.put("addtime", addTimeParams);
            postData.put("queryObj", queryParams);
        }

        return executeApiRequest(type, postData);
    }

    /**
     * 获取合同列表数据（查询当天数据）
     * 使用 addtime 查询条件：addtime > 今天零点
     * API 返回格式：{"result": {"data": [...], "total": xxx}}
     */
    public JSONObject getContractListToday(int page, int pageSize) {
        return getContractListByStartDate(page, LocalDate.now().format(DATE_FORMATTER), pageSize);
    }

    /**
     * 获取合同列表数据（查询指定日期之后的数据）
     * API 返回格式：{"data": [{"detail": {...}], "totalCount": xxx}
     */
    public JSONObject getContractListByStartDate(int page, String startDate, int pageSize) {
        String type = "list_simple";
        Map<String, Object> postData = buildBasePostData();

        postData.put("page", page);
        postData.put("pagesize", pageSize);

        // 直接传递 addtime 字段（与浏览器测试一致）
        postData.put("addtime", startDate + " 00:00:00");

        log.info("======== 查询合同，addtime >= {} ========", startDate);

        return executeApiRequest(type, postData, true);  // isList = true
    }

    /**
     * 获取合同详情
     * API 返回格式：{"data": {"detail": {..., "child_mx": [...]}}}
     */
    public JSONObject getContractDetail(String contractId) {
        String type = "view";
        Map<String, Object> postData = buildBasePostData();
        postData.put("msgid", contractId);

        log.info("======== 请求合同详情，ID: {} ========", contractId);

        JSONObject response = executeApiRequest(type, postData, false);

        if (response != null) {
            log.info("======== 合同详情返回，ID: {} ========", contractId);
            log.info("======== 合同详情原始响应：{} ========", response.toJSONString().substring(0, Math.min(500, response.toJSONString().length())));
            return response;
        }

        log.warn("======== 合同详情返回为空，ID: {} ========", contractId);
        return null;
    }

    /**
     * 构建基础 POST 数据
     */
    private Map<String, Object> buildBasePostData() {
        Map<String, Object> postData = new LinkedHashMap<>();
        postData.put("stamp", System.currentTimeMillis() / 1000L);
        postData.put("datatype", apiProperties.getDatatype());
        return postData;
    }

    /**
     * 执行 API 请求
     * @param isList 是否为列表 API（列表 API 返回 result.data，详情 API 返回 data）
     */
    private JSONObject executeApiRequest(String type, Map<String, Object> postData, boolean isList) {
        String postDataStr = JSON.toJSONString(postData);
        String appSign;

        try {
            appSign = SignUtil.appSign(apiProperties.getId(), apiProperties.getSecretKey(), type, postDataStr);
        } catch (Exception e) {
            log.error("CRM API 签名生成失败，type: {}", type, e);
            return null;
        }

        String payUrl = apiProperties.getUrl() + "?version=v2&sign=" + appSign +
                "&key=" + apiProperties.getSecretId() +
                "&appid=" + apiProperties.getId() +
                "&type=" + type;

        log.debug("请求 URL: {}", payUrl);
        log.debug("POST 数据：{}", postDataStr);

        JSONObject response = SignUtil.httpPost(payUrl, postDataStr);

        if (response != null) {
            log.debug("API 原始响应：{}", response.toJSONString());

            // 检查是否有错误码
            if (response.containsKey("errcode")) {
                String errcode = response.getString("errcode");
                if (!"0".equals(errcode)) {
                    log.error("API 返回错误：errcode={}, errmsg={}", errcode, response.getString("errmsg"));
                    return null;
                }
            }

            if (isList) {
                // 列表 API 返回 {"data": [...], "totalCount": xxx, ...}（直接返回 response）
                log.debug("API response: {}", response.toJSONString());
                return response;
            } else {
                // 详情 API 返回 {"data": {"detail": {...}, "child_mx": [...]}}
                JSONObject data = response.getJSONObject("data");
                if (data != null) {
                    log.debug("API data: {}", data.toJSONString());
                    return data;
                } else {
                    log.warn("API 返回中没有 data 字段");
                    return response;
                }
            }
        }
        return null;
    }

    /**
     * 执行 API 请求（默认不是列表 API）
     */
    private JSONObject executeApiRequest(String type, Map<String, Object> postData) {
        return executeApiRequest(type, postData, false);
    }
}
