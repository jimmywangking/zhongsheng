package com.git.test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.git.entity.ContractDetail;
import com.git.entity.ContractProduct;
import com.git.util.CovertUtil;

import java.util.List;

/**
 * 合同详情解析测试
 */
public class ContractDetailParseTest {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          🧪 合同详情解析测试                                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        // 模拟 API 返回数据（基于浏览器测试结果）
        String apiResponse = "{" +
            "\"data\":{" +
                "\"detail\":{" +
                    "\"ht_id\":\"10319\"," +
                    "\"ht_title\":\"自动主题 - 如意 54654654\"," +
                    "\"ht_number\":\"HTDDA00062\"," +
                    "\"ht_summoney\":\"1200.0000\"," +
                    "\"ht_state\":\"执行中\"," +
                    "\"ht_customerid\":{\"title\":\"王媛媛\",\"id\":\"6761\"}," +
                    "\"child_mx\":[" +
                        "{" +
                            "\"产品 ID\":\"6398\"," +
                            "\"产品编号\":\"11155\"," +
                            "\"产品名称\":\"如意 54654654\"," +
                            "\"产品型号\":\"\"," +
                            "\"产品规格\":\"\"," +
                            "\"计件单位\":\"个\"," +
                            "\"数量\":\"1\"," +
                            "\"单价\":\"1200\"," +
                            "\"总价\":\"1200\"," +
                            "\"明细备注\":\"\"," +
                            "\"成本单价\":\"800\"," +
                            "\"成本总价\":\"800\"" +
                        "}" +
                    "]" +
                "}" +
            "}" +
        "}";

        try {
            JSONObject result = JSON.parseObject(apiResponse);

            System.out.println("【1. 解析合同详情】");
            ContractDetail detail = CovertUtil.jsonToContractDetail(result);
            if (detail != null) {
                System.out.println("  ✅ 合同详情解析成功");
                System.out.println("  合同 ID: " + detail.getHtId());
                System.out.println("  合同编号：" + detail.getHtNumber());
                System.out.println("  合同标题：" + detail.getHtTitle());
                System.out.println("  合同金额：" + detail.getHtSummoney());
                System.out.println("  合同状态：" + detail.getHtState());
                System.out.println("  客户名称：" + detail.getHtCustomer());
            } else {
                System.out.println("  ❌ 合同详情解析失败");
            }
            System.out.println();

            System.out.println("【2. 解析产品明细】");
            List<ContractProduct> products = CovertUtil.jsonToContractProducts(result, detail.getHtId());
            if (products != null && !products.isEmpty()) {
                System.out.println("  ✅ 产品明细解析成功，数量：" + products.size());
                System.out.println();
                for (int i = 0; i < products.size(); i++) {
                    ContractProduct p = products.get(i);
                    System.out.println("  产品 " + (i + 1) + ":");
                    System.out.println("    产品 ID: " + p.getProductId());
                    System.out.println("    产品编号：" + p.getProductCode());
                    System.out.println("    产品名称：" + p.getProductName());
                    System.out.println("    数量：" + p.getQuantity());
                    System.out.println("    单价：¥" + p.getUnitPrice());
                    System.out.println("    总价：¥" + p.getTotalPrice());
                    System.out.println("    成本单价：¥" + p.getCostPrice());
                    System.out.println("    成本总价：¥" + p.getCostTotal());
                }
            } else {
                System.out.println("  ❌ 产品明细解析失败");
            }
            System.out.println();

            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║          ✅ 测试通过！                                        ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.out.println("❌ 测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
