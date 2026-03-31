package com.git.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Dashboard 统计数据 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {

    /**
     * 合同总数
     */
    private Integer totalContracts;

    /**
     * 合同总金额
     */
    private BigDecimal totalAmount;

    /**
     * 客户总数
     */
    private Integer totalCustomers;

    /**
     * 今日新增合同数
     */
    private Integer todayContracts;

    /**
     * 今日合同金额
     */
    private BigDecimal todayAmount;

    /**
     * 合同状态统计
     */
    private List<StateCount> stateStats;

    /**
     * 合同类型统计
     */
    private List<TypeCount> typeStats;

    /**
     * 客户合同排行（Top 10）
     */
    private List<CustomerRank> customerRanks;

    /**
     * 近期合同趋势（最近 7 天）
     */
    private List<TrendData> trendData;

    /**
     * 回款统计
     */
    private PaymentStats paymentStats;

    /**
     * 产品总数
     */
    private Integer totalProducts;

    /**
     * 产品销量排行
     */
    private List<ProductStat> products;

    /**
     * 数据起始日期
     */
    private String dataStartDate;

    /**
     * 最后更新时间
     */
    private String lastUpdateTime;

    /**
     * 合同状态计数
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StateCount {
        private String state;
        private Integer count;
        private BigDecimal amount;
    }

    /**
     * 合同类型计数
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TypeCount {
        private String type;
        private Integer count;
    }

    /**
     * 客户排行
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerRank {
        private String customerName;
        private Integer contractCount;
        private BigDecimal totalAmount;
    }

    /**
     * 产品统计
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductStat {
        private String productId;
        private String productName;
        private String productCode;
        private Integer quantity;
        private BigDecimal totalPrice;
        private BigDecimal unitPrice;
    }

    /**
     * 趋势数据
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendData {
        private String date;
        private Integer count;
        private BigDecimal amount;
    }

    /**
     * 回款统计
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentStats {
        private BigDecimal totalPaid;      // 已回款总额
        private BigDecimal totalUnpaid;    // 未回款总额
        private BigDecimal paymentRate;    // 回款率
    }
}
