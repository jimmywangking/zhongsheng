package com.git.test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.git.util.SignUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CRM API 接口测试类
 * 测试合同订单列表和合同详情的 API 调用
 */
public class CrmApiFullTest {

    // API 配置
    private static final String APP_ID = "41314";
    private static final String SECRET_ID = "s74gdLX96ULx";
    private static final String SECRET_KEY = "7ecd94df-fdfd-4dd5-9a38-9fa470ec5c33";
    private static final String API_URL = "https://crmapi.jzsoft.cn/apigetdata.aspx";
    private static final String DATATYPE = "150"; // 合同订单

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          🧪 CRM API 接口测试                                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        try {
            // 1. 测试合同列表 API
            testContractList();

            // 2. 测试合同详情 API
            testContractDetail();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试合同列表 API
     */
    public static void testContractList() throws Exception {
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("📋 测试 1: 合同订单列表 API (type=list_simple)");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println();

        String type = "list_simple";

        // 构建 POST 数据
        Map<String, Object> postData = new LinkedHashMap<>();
        postData.put("stamp", System.currentTimeMillis() / 1000L);
        postData.put("datatype", DATATYPE);
        postData.put("page", 1);
        postData.put("pagesize", 10);

        String postDataStr = JSON.toJSONString(postData);

        System.out.println("【请求参数】");
        System.out.println("  POST Data: " + postDataStr);
        System.out.println();

        // 生成签名
        String sign = SignUtil.appSign(APP_ID, SECRET_KEY, type, postDataStr);

        System.out.println("【签名信息】");
        System.out.println("  签名源字符串：" + APP_ID + type + postDataStr);
        System.out.println("  签名值：" + sign);
        System.out.println();

        // 构建请求 URL
        String url = API_URL + "?version=v2&sign=" + sign +
                "&key=" + SECRET_ID +
                "&appid=" + APP_ID +
                "&type=" + type;

        System.out.println("【请求 URL】");
        System.out.println("  " + url);
        System.out.println();

        // 发送请求
        System.out.println("【发送请求...】");
        System.out.println("URL: " + url);
        JSONObject response = SignUtil.httpPost(url, postDataStr);

        System.out.println();
        System.out.println("【返回结果】");
        if (response != null) {
            // 检查是否有错误
            if (response.containsKey("errcode")) {
                String errcode = response.getString("errcode");
                String errmsg = response.getString("errmsg");
                if (!"0".equals(errcode)) {
                    System.out.println("  ❌ API 调用失败!");
                    System.out.println("  错误码：" + errcode);
                    System.out.println("  错误信息：" + errmsg);
                    return;
                }
            }

            // 解析 result
            if (response.containsKey("result")) {
                JSONObject result = response.getJSONObject("result");

                Integer total = result.getInteger("total");
                System.out.println("  ✅ API 调用成功!");
                System.out.println("  总记录数：" + total);

                JSONArray data = result.getJSONArray("data");
                if (data != null && data.size() > 0) {
                    System.out.println("  返回条数：" + data.size());
                    System.out.println();

                    // 显示第一条数据的完整 JSON
                    System.out.println("【第一条数据完整 JSON】");
                    System.out.println("────────────────────────────────────────────────────────");
                    JSONObject firstItem = data.getJSONObject(0);
                    System.out.println(firstItem.toJSONString());
                    System.out.println("────────────────────────────────────────────────────────");
                    System.out.println();

                    // 提取关键字段
                    System.out.println("【第一条数据关键字段】");
                    System.out.println("  ht_id: " + firstItem.getString("ht_id"));
                    System.out.println("  ht_number: " + firstItem.getString("ht_number"));

                    JSONObject htTitle = firstItem.getJSONObject("ht_title");
                    if (htTitle != null) {
                        System.out.println("  ht_title: " + htTitle.getString("value"));
                    }

                    JSONObject htSummoney = firstItem.getJSONObject("ht_summoney");
                    if (htSummoney != null) {
                        System.out.println("  ht_summoney: " + htSummoney.get("value"));
                    }

                    JSONObject htCustomerid = firstItem.getJSONObject("ht_customerid");
                    if (htCustomerid != null) {
                        System.out.println("  ht_customer: " + htCustomerid.getString("label"));
                    }

                    System.out.println("  ht_state: " + (firstItem.getJSONObject("ht_state") != null ?
                            firstItem.getJSONObject("ht_state").getString("label") : "null"));
                    System.out.println("  addtime: " + firstItem.getString("addtime"));
                    System.out.println();

                    // 保存第一个合同 ID 用于详情测试
                    String firstContractId = firstItem.getString("ht_id");
                    System.out.println("📌 已获取第一个合同 ID: " + firstContractId);
                    System.out.println("   将用于下面的详情 API 测试");
                } else {
                    System.out.println("  ⚠️ 数据数组为空");
                }
            } else {
                System.out.println("  ❌ 返回结果中没有 result 字段");
                System.out.println("  完整返回：" + response.toJSONString());
            }
        } else {
            System.out.println("  ❌ HTTP 请求失败，返回 null");
        }

        System.out.println();
    }

    /**
     * 测试合同详情 API
     */
    public static void testContractDetail() throws Exception {
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("📄 测试 2: 合同详情 API (type=view)");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println();

        // 先获取一个合同 ID
        String contractId = getFirstContractId();

        if (contractId == null || contractId.isEmpty()) {
            System.out.println("  ❌ 无法获取合同 ID，跳过详情测试");
            return;
        }

        System.out.println("  测试合同 ID: " + contractId);
        System.out.println();

        String type = "view";

        // 构建 POST 数据
        Map<String, Object> postData = new LinkedHashMap<>();
        postData.put("stamp", System.currentTimeMillis() / 1000L);
        postData.put("datatype", DATATYPE);
        postData.put("msgid", contractId);

        String postDataStr = JSON.toJSONString(postData);

        System.out.println("【请求参数】");
        System.out.println("  POST Data: " + postDataStr);
        System.out.println();

        // 生成签名
        String sign = SignUtil.appSign(APP_ID, SECRET_KEY, type, postDataStr);

        System.out.println("【签名信息】");
        System.out.println("  签名源字符串：" + APP_ID + type + postDataStr);
        System.out.println("  签名值：" + sign);
        System.out.println();

        // 构建请求 URL
        String url = API_URL + "?version=v2&sign=" + sign +
                "&key=" + SECRET_ID +
                "&appid=" + APP_ID +
                "&type=" + type;

        System.out.println("【请求 URL】");
        System.out.println("  " + url);
        System.out.println();

        // 发送请求
        System.out.println("【发送请求...】");
        System.out.println("URL: " + url);
        JSONObject response = SignUtil.httpPost(url, postDataStr);

        System.out.println();
        System.out.println("【返回结果】");
        if (response != null) {
            // 检查是否有错误
            if (response.containsKey("errcode")) {
                String errcode = response.getString("errcode");
                String errmsg = response.getString("errmsg");
                if (!"0".equals(errcode)) {
                    System.out.println("  ❌ API 调用失败!");
                    System.out.println("  错误码：" + errcode);
                    System.out.println("  错误信息：" + errmsg);
                    return;
                }
            }

            // 解析 result
            if (response.containsKey("result")) {
                JSONObject result = response.getJSONObject("result");

                System.out.println("  ✅ API 调用成功!");
                System.out.println();

                // 检查是否有 data 字段
                JSONObject detailData = result.getJSONObject("data");
                if (detailData == null) {
                    System.out.println("  ⚠️ 返回结果中没有 data 字段，直接使用 result");
                    detailData = result;
                }

                if (detailData != null && !detailData.isEmpty()) {
                    // 显示完整 JSON
                    System.out.println("【合同详情完整 JSON】");
                    System.out.println("────────────────────────────────────────────────────────");
                    System.out.println(detailData.toJSONString());
                    System.out.println("────────────────────────────────────────────────────────");
                    System.out.println();

                    // 提取关键字段
                    System.out.println("【合同详情关键字段】");
                    System.out.println("  ht_id: " + detailData.getString("ht_id"));
                    System.out.println("  ht_number: " + detailData.getString("ht_number"));

                    JSONObject htTitle = detailData.getJSONObject("ht_title");
                    if (htTitle != null) {
                        System.out.println("  ht_title: " + htTitle.getString("value"));
                    }

                    JSONObject htSummoney = detailData.getJSONObject("ht_summoney");
                    if (htSummoney != null) {
                        System.out.println("  ht_summoney: " + htSummoney.get("value"));
                    }

                    JSONObject htCustomerid = detailData.getJSONObject("ht_customerid");
                    if (htCustomerid != null) {
                        System.out.println("  ht_customer: " + htCustomerid.getString("label"));
                    }

                    // 详情特有字段
                    JSONObject htRemark = detailData.getJSONObject("ht_remark");
                    if (htRemark != null) {
                        System.out.println("  ht_remark: " + htRemark.getString("value"));
                    }

                    JSONObject htAddress = detailData.getJSONObject("ht_address");
                    if (htAddress != null) {
                        System.out.println("  ht_address: " + htAddress.getString("value"));
                    }

                    System.out.println("  ht_state: " + (detailData.getJSONObject("ht_state") != null ?
                            detailData.getJSONObject("ht_state").getString("label") : "null"));
                    System.out.println("  addtime: " + detailData.getString("addtime"));
                    System.out.println();

                    System.out.println("✅ 合同详情 API 测试完成!");
                } else {
                    System.out.println("  ❌ 详情数据为空");
                    System.out.println("  完整返回：" + response.toJSONString());
                }
            } else {
                System.out.println("  ❌ 返回结果中没有 result 字段");
                System.out.println("  完整返回：" + response.toJSONString());
            }
        } else {
            System.out.println("  ❌ HTTP 请求失败，返回 null");
        }

        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          🎉 所有测试完成！                                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }

    /**
     * 获取第一个合同 ID
     */
    public static String getFirstContractId() throws Exception {
        String type = "list_simple";

        Map<String, Object> postData = new LinkedHashMap<>();
        postData.put("stamp", System.currentTimeMillis() / 1000L);
        postData.put("datatype", DATATYPE);
        postData.put("page", 1);
        postData.put("pagesize", 1);

        String postDataStr = JSON.toJSONString(postData);
        String sign = SignUtil.appSign(APP_ID, SECRET_KEY, type, postDataStr);

        String url = API_URL + "?version=v2&sign=" + sign +
                "&key=" + SECRET_ID +
                "&appid=" + APP_ID +
                "&type=" + type;

        JSONObject response = SignUtil.httpPost(url, postDataStr);

        if (response != null && response.containsKey("result")) {
            JSONObject result = response.getJSONObject("result");
            JSONArray data = result.getJSONArray("data");

            if (data != null && data.size() > 0) {
                JSONObject firstItem = data.getJSONObject(0);
                return firstItem.getString("ht_id");
            }
        }

        return null;
    }
}
