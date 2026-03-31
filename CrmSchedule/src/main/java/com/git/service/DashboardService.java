package com.git.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.git.dto.DashboardStats;
import com.git.entity.Contract;
import com.git.entity.ContractProduct;
import com.git.mapper.ContractMapper;
import com.git.mapper.ContractProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Dashboard 统计服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ContractMapper contractMapper;
    private final ContractProductMapper productMapper;

    /**
     * 获取 Dashboard 统计数据
     */
    public DashboardStats getStats() {
        DashboardStats stats = new DashboardStats();

        // 1. 获取所有合同数据
        List<Contract> allContracts = contractMapper.selectList(null);

        // 2. 计算基础统计
        stats.setTotalContracts(allContracts.size());
        stats.setTotalAmount(calculateTotalAmount(allContracts));
        stats.setTotalCustomers(countUniqueCustomers(allContracts));

        // 3. 今日统计
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<Contract> todayContracts = filterByDate(allContracts, today);
        stats.setTodayContracts(todayContracts.size());
        stats.setTodayAmount(calculateTotalAmount(todayContracts));

        // 4. 合同状态统计
        stats.setStateStats(calculateStateStats(allContracts));

        // 5. 合同类型统计
        stats.setTypeStats(calculateTypeStats(allContracts));

        // 6. 客户排行
        stats.setCustomerRanks(calculateCustomerRanks(allContracts));

        // 7. 近期趋势
        stats.setTrendData(calculateTrendData(allContracts));

        // 8. 回款统计
        stats.setPaymentStats(calculatePaymentStats(allContracts));

        // 9. 产品统计
        stats.setTotalProducts(calculateTotalProducts());
        stats.setProducts(calculateProductStats());

        // 10. 数据起始日期和最后更新时间
        stats.setDataStartDate(calculateDataStartDate(allContracts));
        stats.setLastUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return stats;
    }

    /**
     * 计算数据起始日期（最早合同的签单日期）
     */
    private String calculateDataStartDate(List<Contract> contracts) {
        return contracts.stream()
                .map(Contract::getHtDate)
                .filter(date -> date != null && date.length() >= 10)
                .map(date -> date.substring(0, 10))
                .min(String::compareTo)
                .orElse("N/A");
    }

    /**
     * 计算产品总数
     */
    private Integer calculateTotalProducts() {
        Long count = productMapper.selectCount(null);
        return count != null ? count.intValue() : 0;
    }

    /**
     * 计算产品统计（销量和收益排行）
     */
    private List<DashboardStats.ProductStat> calculateProductStats() {
        List<ContractProduct> allProducts = productMapper.selectList(null);

        // 按产品 ID 分组统计（productId 是产品唯一标识）
        Map<String, List<ContractProduct>> groupedByProduct = allProducts.stream()
                .filter(p -> p.getProductId() != null && !p.getProductId().isEmpty())
                .collect(Collectors.groupingBy(ContractProduct::getProductId));

        List<DashboardStats.ProductStat> result = new ArrayList<>();
        for (Map.Entry<String, List<ContractProduct>> entry : groupedByProduct.entrySet()) {
            String productId = entry.getKey();
            List<ContractProduct> products = entry.getValue();

            Integer totalQuantity = products.stream()
                    .mapToInt(p -> p.getQuantity() != null ? p.getQuantity() : 0)
                    .sum();

            BigDecimal totalPrice = products.stream()
                    .map(p -> p.getTotalPrice() != null ? p.getTotalPrice() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal avgUnitPrice = products.stream()
                    .map(p -> p.getUnitPrice() != null ? p.getUnitPrice() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(new BigDecimal(products.size()), 2, BigDecimal.ROUND_HALF_UP);

            // 取第一条记录的产品名称和编号
            String productName = products.get(0).getProductName();
            String productCode = products.get(0).getProductCode();

            result.add(new DashboardStats.ProductStat(
                productId, productName, productCode, totalQuantity, totalPrice, avgUnitPrice
            ));
        }

        // 按销量降序排序
        result.sort((a, b) -> b.getQuantity() - a.getQuantity());
        return result;
    }

    /**
     * 计算总金额
     */
    private BigDecimal calculateTotalAmount(List<Contract> contracts) {
        return contracts.stream()
                .map(Contract::getHtSummoney)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 统计独立客户数
     */
    private Integer countUniqueCustomers(List<Contract> contracts) {
        return (int) contracts.stream()
                .map(Contract::getHtCustomer)
                .filter(customer -> customer != null && !customer.isEmpty())
                .distinct()
                .count();
    }

    /**
     * 按日期过滤（使用 ht_date 字段，因为列表 API 不返回 addtime）
     */
    private List<Contract> filterByDate(List<Contract> contracts, String date) {
        return contracts.stream()
                .filter(c -> date.equals(c.getHtDate() != null ? c.getHtDate().substring(0, 10) : null))
                .collect(Collectors.toList());
    }

    /**
     * 计算合同状态统计
     */
    private List<DashboardStats.StateCount> calculateStateStats(List<Contract> contracts) {
        Map<String, List<Contract>> groupedByState = contracts.stream()
                .filter(c -> c.getHtState() != null)
                .collect(Collectors.groupingBy(Contract::getHtState));

        List<DashboardStats.StateCount> result = new ArrayList<>();
        for (Map.Entry<String, List<Contract>> entry : groupedByState.entrySet()) {
            String state = entry.getKey();
            List<Contract> stateContracts = entry.getValue();
            Integer count = stateContracts.size();
            BigDecimal amount = calculateTotalAmount(stateContracts);
            result.add(new DashboardStats.StateCount(state, count, amount));
        }

        // 按数量降序排序
        result.sort((a, b) -> b.getCount() - a.getCount());
        return result;
    }

    /**
     * 计算合同类型统计
     */
    private List<DashboardStats.TypeCount> calculateTypeStats(List<Contract> contracts) {
        Map<String, List<Contract>> groupedByType = contracts.stream()
                .filter(c -> c.getHtType() != null && !c.getHtType().isEmpty())
                .collect(Collectors.groupingBy(Contract::getHtType));

        List<DashboardStats.TypeCount> result = new ArrayList<>();
        for (Map.Entry<String, List<Contract>> entry : groupedByType.entrySet()) {
            result.add(new DashboardStats.TypeCount(entry.getKey(), entry.getValue().size()));
        }

        // 按数量降序排序
        result.sort((a, b) -> b.getCount() - a.getCount());
        return result;
    }

    /**
     * 计算客户排行（Top 10）
     */
    private List<DashboardStats.CustomerRank> calculateCustomerRanks(List<Contract> contracts) {
        Map<String, List<Contract>> groupedByCustomer = contracts.stream()
                .filter(c -> c.getHtCustomer() != null && !c.getHtCustomer().isEmpty())
                .collect(Collectors.groupingBy(Contract::getHtCustomer));

        List<DashboardStats.CustomerRank> result = new ArrayList<>();
        for (Map.Entry<String, List<Contract>> entry : groupedByCustomer.entrySet()) {
            String customer = entry.getKey();
            List<Contract> customerContracts = entry.getValue();
            Integer count = customerContracts.size();
            BigDecimal amount = calculateTotalAmount(customerContracts);
            result.add(new DashboardStats.CustomerRank(customer, count, amount));
        }

        // 按金额降序排序，取前 10
        result.sort((a, b) -> b.getTotalAmount().compareTo(a.getTotalAmount()));
        return result.stream().limit(10).collect(Collectors.toList());
    }

    /**
     * 计算近期趋势（最近 90 天）
     */
    private List<DashboardStats.TrendData> calculateTrendData(List<Contract> contracts) {
        List<DashboardStats.TrendData> result = new ArrayList<>();

        for (int i = 89; i >= 0; i--) {
            String date = LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<Contract> dateContracts = filterByDate(contracts, date);
            Integer count = dateContracts.size();
            BigDecimal amount = calculateTotalAmount(dateContracts);
            result.add(new DashboardStats.TrendData(date, count, amount));
        }

        return result;
    }

    /**
     * 计算回款统计
     */
    private DashboardStats.PaymentStats calculatePaymentStats(List<Contract> contracts) {
        BigDecimal totalPaid = contracts.stream()
                .map(Contract::getHtHkmoney)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalContract = calculateTotalAmount(contracts);
        BigDecimal totalUnpaid = totalContract.subtract(totalPaid);

        BigDecimal paymentRate = totalContract.compareTo(BigDecimal.ZERO) > 0
                ? totalPaid.divide(totalContract, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"))
                : BigDecimal.ZERO;

        return new DashboardStats.PaymentStats(totalPaid, totalUnpaid, paymentRate);
    }
}
