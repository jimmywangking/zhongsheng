package com.git.test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.git.entity.Contract;
import com.git.util.CovertUtil;

/**
 * 测试合同列表数据解析
 */
public class ParseTest {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          🧪 合同数据解析测试                                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        // 模拟 API 返回的数据
        String jsonData = "{" +
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
                "}}" +
            "]" +
        "}";

        try {
            JSONObject response = JSON.parseObject(jsonData);
            JSONArray dataArray = response.getJSONArray("data");

            System.out.println("【原始数据】");
            System.out.println("  data 数组长度：" + dataArray.size());
            System.out.println();

            // 解析
            System.out.println("【调用 jsonToContractList】");
            java.util.List<Contract> contracts = CovertUtil.jsonToContractList(dataArray);
            System.out.println("  解析结果数量：" + contracts.size());
            System.out.println();

            if (!contracts.isEmpty()) {
                System.out.println("【解析后的合同】");
                Contract c = contracts.get(0);
                System.out.println("  ht_id: " + c.getHtId());
                System.out.println("  ht_number: " + c.getHtNumber());
                System.out.println("  ht_title: " + c.getHtTitle());
                System.out.println("  ht_date: " + c.getHtDate());
                System.out.println("  ht_state: " + c.getHtState());
                System.out.println("  ht_summoney: " + c.getHtSummoney());
                System.out.println("  ht_customer: " + c.getHtCustomer());
                System.out.println("  ht_preside: " + c.getHtPreside());
                System.out.println("  data_userid: " + c.getDataUserid());
            }

            System.out.println();
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║          ✅ 测试完成                                           ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.out.println("❌ 测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
