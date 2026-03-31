package com.git.test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

/**
 * 测试解析用户提供的 API 返回格式
 */
public class TestUserApiFormat {

    public static void main(String[] args) {
        // 用户提供的 API 返回 JSON
        String userJson = "{\n" +
            "    \"time\": \"2026-03-27 19:07:00\",\n" +
            "    \"success\": true,\n" +
            "    \"errcode\": \"0\",\n" +
            "    \"errmsg\": \"\",\n" +
            "    \"dataname\": {\n" +
            "        \"ht_id\": \"ID\",\n" +
            "        \"ht_customerid\": \"对应客户\"\n" +
            "    },\n" +
            "    \"ccc\": 1,\n" +
            "    \"data\": {\n" +
            "        \"detail\": {\n" +
            "            \"ht_id\": \"10322\",\n" +
            "            \"ht_customerid\": {\n" +
            "                \"title\": \"测试 8745745612\",\n" +
            "                \"id\": \"6764\",\n" +
            "                \"number\": \"KHZL2026032702\"\n" +
            "            },\n" +
            "            \"ht_title\": \"自动主题 -1111111\",\n" +
            "            \"ht_number\": \"HTDDA00065\",\n" +
            "            \"ht_summoney\": \"3000.0000\",\n" +
            "            \"ht_state\": \"结束\",\n" +
            "            \"ht_remark\": \"\",\n" +
            "            \"child_mx\": [\n" +
            "                {\n" +
            "                    \"产品 ID\": \"6397\",\n" +
            "                    \"产品编号\": \"成本测试测试\",\n" +
            "                    \"产品名称\": \"1111111\",\n" +
            "                    \"数量\": \"3\",\n" +
            "                    \"单价\": \"1000\",\n" +
            "                    \"总价\": \"3000\",\n" +
            "                    \"成本单价\": \"300\",\n" +
            "                    \"成本总价\": \"900\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    }\n" +
            "}";

        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║     测试解析用户提供的 API 返回格式                               ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        try {
            JSONObject json = JSON.parseObject(userJson);

            System.out.println("【1. 检查 JSON 结构】");
            System.out.println("  errcode: " + json.getString("errcode"));
            System.out.println("  success: " + json.getBoolean("success"));
            System.out.println("  有 data 字段：" + json.containsKey("data"));
            System.out.println();

            if (json.containsKey("data")) {
                JSONObject dataObj = json.getJSONObject("data");
                System.out.println("【2. 解析 data 对象】");
                System.out.println("  有 detail 字段：" + dataObj.containsKey("detail"));
                System.out.println();

                if (dataObj.containsKey("detail")) {
                    JSONObject detail = dataObj.getJSONObject("detail");

                    System.out.println("【3. 解析 detail 对象 - 合同基本信息】");
                    System.out.println("  ht_id: " + detail.getString("ht_id"));
                    System.out.println("  ht_number: " + detail.getString("ht_number"));
                    System.out.println("  ht_title: " + detail.getString("ht_title"));
                    System.out.println("  ht_summoney: " + detail.getString("ht_summoney"));
                    System.out.println("  ht_state: " + detail.getString("ht_state"));

                    JSONObject customer = detail.getJSONObject("ht_customerid");
                    if (customer != null) {
                        System.out.println("  ht_customer: " + customer.getString("title"));
                    }
                    System.out.println();

                    System.out.println("【4. 解析 child_mx - 产品明细】");
                    if (detail.containsKey("child_mx")) {
                        var childMx = detail.getJSONArray("child_mx");
                        System.out.println("  产品数量：" + childMx.size());
                        System.out.println();

                        for (int i = 0; i < childMx.size(); i++) {
                            JSONObject product = childMx.getJSONObject(i);
                            System.out.println("  ┌─────────────────────────────────────┐");
                            System.out.println("  │ 产品 " + (i + 1));
                            System.out.println("  ├─────────────────────────────────────┤");
                            System.out.println("  │ 产品 ID:   " + product.getString("产品 ID"));
                            System.out.println("  │ 产品编号：" + product.getString("产品编号"));
                            System.out.println("  │ 产品名称：" + product.getString("产品名称"));
                            System.out.println("  │ 数量：    " + product.getString("数量"));
                            System.out.println("  │ 单价：    ¥" + product.getString("单价"));
                            System.out.println("  │ 总价：    ¥" + product.getString("总价"));
                            System.out.println("  │ 成本单价：¥" + product.getString("成本单价"));
                            System.out.println("  │ 成本总价：¥" + product.getString("成本总价"));
                            System.out.println("  └─────────────────────────────────────┘");
                            System.out.println();
                        }
                    } else {
                        System.out.println("  ⚠️ 没有 child_mx 字段");
                    }

                    System.out.println("╔════════════════════════════════════════════════════════════════╗");
                    System.out.println("║          ✅ 解析成功！                                        ║");
                    System.out.println("╚════════════════════════════════════════════════════════════════╝");
                }
            }

        } catch (Exception e) {
            System.out.println("❌ 解析失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
