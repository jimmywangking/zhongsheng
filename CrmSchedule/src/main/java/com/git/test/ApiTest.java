package com.git.test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONArray;
import com.git.util.SignUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * API 测试类 - 测试合同订单接口调用
 */
public class ApiTest {

    public static void main(String[] args) {
        try {
            String appId = "41314";
            String secretId = "s74gdLX96ULx";
            String secretKey = "7ecd94df-fdfd-4dd5-9a38-9fa470ec5c33";
            String type = "list_simple";

            // 构建 POST 数据
            Map<String, Object> postData = new LinkedHashMap<>();
            postData.put("stamp", System.currentTimeMillis() / 1000L);
            postData.put("datatype", "150");
            postData.put("page", 1);
            postData.put("pagesize", 10);

            String postDataStr = JSON.toJSONString(postData);

            System.out.println("========== 1. POST DATA ==========");
            System.out.println(postDataStr);
            System.out.println();

            // 生成签名
            String sign = SignUtil.appSign(appId, secretKey, type, postDataStr);

            System.out.println("========== 2. 签名源字符串 ==========");
            System.out.println(appId + appId + type);
            System.out.println();

            System.out.println("========== 3. 签名值 ==========");
            System.out.println(sign);
            System.out.println();

            // 构建请求 URL
            String url = "https://crmapi.jzsoft.cn/apigetdata.aspx?version=v2&sign=" + sign +
                    "&key=" + secretId + "&appid=" + appId + "&type=" + type;

            System.out.println("========== 4. 请求 URL ==========");
            System.out.println(url);
            System.out.println();

            // 发送请求
            System.out.println("========== 5. 请求调用结果 ==========");
            JSONObject result = SignUtil.httpPost(url, postDataStr);

            System.out.println("完整返回结果:");
            System.out.println(result.toJSONString());
            System.out.println();

            // 解析并显示关键信息
            System.out.println("========== 6. 返回结果解析 ==========");
            if (result != null && result.containsKey("result")) {
                JSONObject resultObj = result.getJSONObject("result");
                System.out.println("总记录数：" + resultObj.getInteger("total"));
                System.out.println("当前页：" + resultObj.getInteger("page"));
                System.out.println("每页数量：" + resultObj.getInteger("pagesize"));

                JSONArray data = resultObj.getJSONArray("data");
                if (data != null && data.size() > 0) {
                    System.out.println("\n数据条数：" + data.size());
                    System.out.println("\n第一条数据完整内容:");
                    System.out.println(data.getJSONObject(0).toJSONString());
                } else {
                    System.out.println("\n暂无数据");
                }
            } else {
                System.out.println("API 调用失败或返回空结果");
            }

        } catch (Exception e) {
            System.err.println("发生错误:");
            e.printStackTrace();
        }
    }
}
