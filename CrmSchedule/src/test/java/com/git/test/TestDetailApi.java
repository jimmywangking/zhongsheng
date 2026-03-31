package com.git.test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.git.config.CrmApiProperties;
import com.git.service.CrmApiClient;
import com.git.util.SignUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@ComponentScan("com.git")
@PropertySource("classpath:application.yml")
class TestConfig {}

public class TestDetailApi {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          🔍 测试合同详情 API (ID: 10319)                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        CrmApiClient apiClient = context.getBean(CrmApiClient.class);

        // 测试合同 ID 10319
        String contractId = "10319";
        System.out.println("请求合同详情，ID: " + contractId);
        System.out.println("─────────────────────────────────────────────────────────────────");

        JSONObject response = apiClient.getContractDetail(contractId);

        if (response != null) {
            System.out.println("✅ API 调用成功");
            System.out.println();
            
            // 检查顶层结构
            System.out.println("【顶层 Keys】");
            for (String key : response.keySet()) {
                Object value = response.get(key);
                System.out.println("  • " + key + ": " + (value instanceof JSONObject ? "JSONObject" : value.getClass().getSimpleName()));
            }
            System.out.println();

            // 检查 data 结构
            if (response.containsKey("data")) {
                System.out.println("【data 结构】");
                JSONObject data = response.getJSONObject("data");
                if (data != null) {
                    System.out.println("  ✅ data 存在");
                    
                    if (data.containsKey("detail")) {
                        System.out.println("  ✅ data.detail 存在");
                        JSONObject detail = data.getJSONObject("detail");
                        
                        if (detail.containsKey("child_mx")) {
                            System.out.println("  ✅ data.detail.child_mx 存在");
                            com.alibaba.fastjson2.JSONArray childMx = detail.getJSONArray("child_mx");
                            System.out.println("  child_mx 数量：" + childMx.size());
                            
                            if (childMx.size() > 0) {
                                System.out.println();
                                System.out.println("【child_mx[0] 内容】");
                                JSONObject firstProduct = childMx.getJSONObject(0);
                                for (String key : firstProduct.keySet()) {
                                    System.out.println("    " + key + ": " + firstProduct.get(key));
                                }
                            }
                        } else {
                            System.out.println("  ❌ data.detail.child_mx 不存在");
                            System.out.println("  detail Keys: " + detail.keySet());
                        }
                    } else {
                        System.out.println("  ❌ data.detail 不存在");
                    }
                }
            } else {
                System.out.println("❌ data 不存在");
            }

            System.out.println();
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║          ✅ 测试完成                                           ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝");

        } else {
            System.out.println("❌ API 返回为空");
        }

        System.exit(0);
    }
}
