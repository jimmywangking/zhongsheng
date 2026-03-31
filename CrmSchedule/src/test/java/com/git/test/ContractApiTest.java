package com.git.test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.git.util.SignUtil;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CRM API 接口测试类
 * 测试合同订单列表和合同详情 API 调用
 */
public class ContractApiTest {

    private static final String APP_ID = "41314";
    private static final String SECRET_ID = "s74gdLX96ULx";
    private static final String SECRET_KEY = "7ecd94df-fdfd-4dd5-9a38-9fa470ec5c33";
    private static final String API_URL = "https://crmapi.jzsoft.cn/apigetdata.aspx";
    private static final String DATATYPE = "150";

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          🧪 CRM API 接口测试 - 合同列表 + 合同详情             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        try {
            String contractId = testContractList();
            if (contractId != null) {
                testContractDetail(contractId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试合同列表 API，返回第一个合同 ID
     */
    public static String testContractList() throws Exception {
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("📋 测试 1: 合同订单列表 API (type=list_simple)");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println();

        String type = "list_simple";
        Map<String, Object> postData = new LinkedHashMap<>();
        postData.put("stamp", System.currentTimeMillis() / 1000L);
        postData.put("datatype", DATATYPE);
        postData.put("page", 1);
        postData.put("pagesize", 10);

        String postDataStr = JSON.toJSONString(postData);
        String sign = SignUtil.appSign(APP_ID, SECRET_KEY, type, postDataStr);
        String url = API_URL + "?version=v2&sign=" + sign + "&key=" + SECRET_ID + "&appid=" + APP_ID + "&type=" + type;

        System.out.println("【请求 URL】");
        System.out.println("  " + url);
        System.out.println();

        System.out.println("【发送请求...】");
        JSONObject response = sendHttpPost(url, postDataStr);

        System.out.println();
        if (response != null && response.containsKey("result")) {
            JSONObject result = response.getJSONObject("result");
            JSONArray data = result.getJSONArray("data");
            
            if (data != null && data.size() > 0) {
                JSONObject firstItem = data.getJSONObject(0);
                String contractId = firstItem.getString("ht_id");
                System.out.println("✅ 获取成功！合同 ID: " + contractId);
                return contractId;
            }
        }
        System.out.println("❌ 获取失败");
        return null;
    }

    /**
     * 测试合同详情 API
     */
    public static void testContractDetail(String contractId) throws Exception {
        System.out.println();
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("📄 测试 2: 合同详情 API (type=view)");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  测试合同 ID: " + contractId);
        System.out.println();

        String type = "view";
        Map<String, Object> postData = new LinkedHashMap<>();
        postData.put("stamp", System.currentTimeMillis() / 1000L);
        postData.put("datatype", DATATYPE);
        postData.put("msgid", contractId);

        String postDataStr = JSON.toJSONString(postData);
        String sign = SignUtil.appSign(APP_ID, SECRET_KEY, type, postDataStr);
        String url = API_URL + "?version=v2&sign=" + sign + "&key=" + SECRET_ID + "&appid=" + APP_ID + "&type=" + type;

        System.out.println("【发送请求...】");
        JSONObject response = sendHttpPost(url, postDataStr);

        System.out.println();
        System.out.println("【返回结果】");
        if (response != null) {
            System.out.println(response.toJSONString());
        } else {
            System.out.println("null");
        }
    }

    public static JSONObject sendHttpPost(String url, String strParam) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject jsonResult = null;
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .build();
        httpPost.setConfig(requestConfig);

        try {
            if (strParam != null) {
                StringEntity entity = new StringEntity(strParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/x-www-form-urlencoded");
                httpPost.setEntity(entity);
            }

            CloseableHttpResponse result = httpClient.execute(httpPost);
            int statusCode = result.getStatusLine().getStatusCode();
            System.out.println("  HTTP 状态码：" + statusCode);

            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(result.getEntity(), "utf-8");
                if (responseBody != null && !responseBody.isEmpty()) {
                    jsonResult = JSON.parseObject(responseBody);
                }
            }
        } catch (Exception e) {
            System.err.println("  请求失败：" + e.getMessage());
        } finally {
            httpClient.close();
            httpPost.releaseConnection();
        }

        return jsonResult;
    }
}
