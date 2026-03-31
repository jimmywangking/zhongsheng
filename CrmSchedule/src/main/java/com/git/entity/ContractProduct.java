package com.git.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 合同产品明细实体类
 */
@Data
@TableName("crm_contract_product")
public class ContractProduct {

    /**
     * 合同 ID
     */
    private String htId;

    /**
     * 产品 ID
     */
    private String productId;

    /**
     * 产品编号
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品型号
     */
    private String productModel;

    /**
     * 产品规格
     */
    private String productSpec;

    /**
     * 计件单位
     */
    private String unit;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 零售价
     */
    private BigDecimal retailPrice;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 明细备注
     */
    private String remark;

    /**
     * 成本单价
     */
    private BigDecimal costPrice;

    /**
     * 成本总价
     */
    private BigDecimal costTotal;

    /**
     * 同步时间
     */
    private LocalDateTime syncTime;
}
