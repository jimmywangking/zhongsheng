package com.git.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 合同实体类
 */
@Data
@TableName("crm_contract")
public class Contract {

    /**
     * 主键 ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 合同 ID
     */
    private String htId;

    /**
     * 主键
     */
    @TableField("\"key\"")
    private String key;

    /**
     * 合同标题
     */
    private String htTitle;

    /**
     * 合同编号
     */
    private String htNumber;

    /**
     * 合同类型
     */
    private String htType;

    /**
     * 合同日期
     */
    private String htDate;

    /**
     * 合同结束日期
     */
    private String htEnddate;

    /**
     * 合同总额
     */
    private BigDecimal htSummoney;

    /**
     * 合同状态
     */
    private String htState;

    /**
     * 回款金额
     */
    private BigDecimal htHkmoney;

    /**
     * 回款总额
     */
    private BigDecimal hkTotal;

    /**
     * 开票金额
     */
    private BigDecimal htBillmoney;

    /**
     * 毛利
     */
    private BigDecimal htMaoli;

    /**
     * 成本
     */
    private BigDecimal htChengben;

    /**
     * 出库状态
     */
    private String htCkstate;

    /**
     * 开票差额
     */
    private BigDecimal htBillmoneycha;

    /**
     * 退货状态
     */
    private BigDecimal tuihuoState;

    /**
     * 客户 ID
     */
    private Integer htCustomerid;

    /**
     * 客户名称
     */
    private String htCustomer;

    /**
     * 负责人
     */
    private String htPreside;

    /**
     * 创建人 ID
     */
    private String dataUserid;

    /**
     * 创建时间
     */
    private String addtime;

    /**
     * 更新时间
     */
    private String updateDate;

    /**
     * 最后报价
     */
    private Integer lastbaojia;

    /**
     * 同步时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime syncTime;
}
