package com.git.test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.git.entity.Contract;
import com.git.util.CovertUtil;

import java.util.List;

/**
 * 完整流程测试：API 返回 → jsonToContractList → extractContractIds
 */
public class FullFlowTest {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          🧪 完整流程测试                                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        // 模拟 API 返回的数据（基于真实日志）
        String apiResponse = "{" +
            "\"totalCount\":\"13\"," +
            "\"data\":[" +
                "{\"detail\":{" +
                    "\"ht_id\":\"10319\"," +
                    "\"ht_title\":\"自动主题 - 如意 54654654\"," +
                    "\"ht_number\":\"HTDDA00062\"," +
                    "\"ht_date\":\"2026/3/27 0:00:00\"," +
                    "\"ht_state\":\"执行中\"," +
                    "\"ht_summoney\":\"1200.0000\"," +
                    "\"ht_customerid\":{\"title\":\"王媛媛\",\"id\":\"6761\"}," +
                    "\"ht_preside\":{\"title\":\"卞经理\",\"id\":\"1\"}," +
                    "\"data_userid\":{\"title\":\"金智 CRM--墨岩\",\"id\":\"94\"}" +
                "}}," +
                "{\"detail\":{" +
                    "\"ht_id\":\"10320\"," +
                    "\"ht_title\":\"测试 20260327\"," +
                    "\"ht_number\":\"HTDDA00063\"," +
                    "\"ht_date\":\"2026/3/27 0:00:00\"," +
                    "\"ht_state\":\"执行中\"," +
                    "\"ht_summoney\":\"12000.0000\"" +
                "}}" +
            "]" +
        "}";

        try {
            JSONObject response = JSON.parseObject(apiResponse);
            JSONArray dataArray = response.getJSONArray("data");

            System.out.println("【1. API 返回数据】");
            System.out.println("  totalCount: " + response.getString("totalCount"));
            System.out.println("  data 长度：" + dataArray.size());
            System.out.println();

            // 2. 解析合同列表
            System.out.println("【2. 调用 jsonToContractList】");
            List<Contract> contracts = CovertUtil.jsonToContractList(dataArray);
            System.out.println("  解析数量：" + contracts.size());
            if (!contracts.isEmpty()) {
                for (int i = 0; i < contracts.size(); i++) {
                    Contract c = contracts.get(i);
                    System.out.println("    " + (i+1) + ". ht_id=" + c.getHtId() + ", ht_number=" + c.getHtNumber());
                }
            }
            System.out.println();

            // 3. 提取合同 ID
            System.out.println("【3. 提取合同 ID】");
            java.util.List<String> contractIds = new java.util.ArrayList<>();
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject item = dataArray.getJSONObject(i);
                JSONObject detail = item.getJSONObject("detail");
                if (detail != null) {
                    String htId = detail.getString("ht_id");
                    if (htId != null && !htId.isEmpty()) {
                        contractIds.add(htId);
                        System.out.println("    提取到：" + htId);
                    }
                }
            }
            System.out.println("  合同 ID 总数：" + contractIds.size());
            System.out.println();

            // 4. 总结
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            if (contracts.size() > 0 && contractIds.size() > 0) {
                System.out.println("║          ✅ 测试通过！解析和提取都正常                          ║");
            } else {
                System.out.println("║          ❌ 测试失败！请检查代码                                  ║");
            }
            System.out.println("╚════════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.out.println("❌ 测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
