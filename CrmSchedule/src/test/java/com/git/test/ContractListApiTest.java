package com.git.test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.git.entity.Contract;
import com.git.service.CrmApiClient;
import com.git.util.CovertUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 合同列表 API 测试 - 验证 addtime 过滤最近 10 天数据
 */
@Configuration
@ComponentScan("com.git")
@PropertySource("classpath:application.yml")
class ApiTestConfig {}

public class ContractListApiTest {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          🧪 合同列表 API 测试（最近 10 天）                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        // 1. 计算日期范围
        String startDate = LocalDate.now().minusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("【日期范围】");
        System.out.println("  起始日期：" + startDate);
        System.out.println("  结束日期：" + endDate);
        System.out.println();

        // 2. 调用 API
        ApplicationContext context = new AnnotationConfigApplicationContext(ApiTestConfig.class);
        CrmApiClient apiClient = context.getBean(CrmApiClient.class);

        System.out.println("【API 调用】");
        System.out.println("  请求参数：addtime >= " + startDate + " 00:00:00");
        System.out.println("─────────────────────────────────────────────────────────────────");

        JSONObject response = apiClient.getContractListByStartDate(1, startDate, 100);

        if (response != null) {
            System.out.println("✅ API 调用成功");
            System.out.println();

            // 3. 检查返回结构
            System.out.println("【API 返回结构】");
            System.out.println("  totalCount: " + response.getString("totalCount"));
            System.out.println("  page: " + response.getString("page"));
            System.out.println("  pageSize: " + response.getString("pageSize"));
            System.out.println();

            // 4. 解析数据
            JSONArray dataArray = response.getJSONArray("data");
            if (dataArray != null && !dataArray.isEmpty()) {
                System.out.println("【原始数据】");
                System.out.println("  data 数组长度：" + dataArray.size());
                System.out.println();

                // 5. 转换为 Contract 对象
                List<Contract> contracts = CovertUtil.jsonToContractList(dataArray);
                System.out.println("【解析后的合同】");
                System.out.println("  解析数量：" + contracts.size());
                System.out.println();

                // 6. 显示前 5 条合同的 addtime
                System.out.println("【前 5 条合同的 addtime 字段】");
                System.out.println("─────────────────────────────────────────────────────────────────");
                for (int i = 0; i < Math.min(5, contracts.size()); i++) {
                    Contract c = contracts.get(i);
                    System.out.println(String.format("  %d. ht_id: %-10s | addtime: %-20s | ht_date: %-20s | 标题：%s",
                        i + 1,
                        c.getHtId(),
                        c.getAddtime() != null ? c.getAddtime() : "null",
                        c.getHtDate() != null ? c.getHtDate() : "null",
                        c.getHtTitle() != null && c.getHtTitle().length() > 20 
                            ? c.getHtTitle().substring(0, 20) + "..." 
                            : c.getHtTitle()));
                }
                System.out.println();

                // 7. 验证 addtime 过滤
                System.out.println("【addtime 过滤验证】");
                System.out.println("─────────────────────────────────────────────────────────────────");
                int validCount = 0;
                int invalidCount = 0;
                for (Contract c : contracts) {
                    String addtime = c.getAddtime();
                    if (addtime != null && addtime.length() >= 10) {
                        String date = addtime.substring(0, 10);
                        if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
                            validCount++;
                        } else {
                            invalidCount++;
                            System.out.println("  ❌ 超出范围：addtime=" + addtime + " (范围：" + startDate + " ~ " + endDate + ")");
                        }
                    } else {
                        invalidCount++;
                        System.out.println("  ❌ addtime 为空或格式错误：" + addtime);
                    }
                }
                System.out.println();
                System.out.println("  ✅ 在范围内：" + validCount + " 条");
                System.out.println("  ❌ 超出范围：" + invalidCount + " 条");
                System.out.println();

                // 8. 总结
                System.out.println("╔════════════════════════════════════════════════════════════════╗");
                if (validCount > 0 && invalidCount == 0) {
                    System.out.println("║          ✅ 测试通过！所有合同的 addtime 都在最近 10 天内          ║");
                } else if (validCount > 0) {
                    System.out.println("║          ⚠️  部分通过！有 " + invalidCount + " 条合同超出日期范围                   ║");
                } else {
                    System.out.println("║          ❌ 测试失败！没有合同在最近 10 天内                        ║");
                }
                System.out.println("╚════════════════════════════════════════════════════════════════╝");

            } else {
                System.out.println("❌ data 数组为空");
                System.out.println();
                System.out.println("╔════════════════════════════════════════════════════════════════╗");
                System.out.println("║          ⚠️  最近 10 天没有合同数据                                 ║");
                System.out.println("╚════════════════════════════════════════════════════════════════╝");
            }

        } else {
            System.out.println("❌ API 返回为空");
        }

        System.exit(0);
    }
}
