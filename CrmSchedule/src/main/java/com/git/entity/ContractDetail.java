package com.git.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 合同详情实体类
 */
@Data
@TableName("crm_contract_detail")
public class ContractDetail {

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
     * 合同订单类型
     */
    private String htOrder;

    /**
     * 合同类型
     */
    private String htType;

    /**
     * 合同日期
     */
    private String htDate;

    /**
     * 开始日期
     */
    private String htBegindate;

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
     * 预估毛利
     */
    private BigDecimal htYugumaoli;

    /**
     * 整单折扣率
     */
    private String htMoneyzhekou;

    /**
     * 优惠抹零金额
     */
    private BigDecimal htKjmoney;

    /**
     * 附加费用分类
     */
    private String htFjmoneylx;

    /**
     * 附加费用金额
     */
    private BigDecimal htFjmoney;

    /**
     * 审核状态
     */
    private String shenhestate;

    /**
     * 发货状态
     */
    private String htFahuostate;

    /**
     * 是否开票
     */
    private String diyDdl2;

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
     * 联系人 ID
     */
    private String htLxrid;

    /**
     * 联系信息
     */
    private String htLxrinfo;

    /**
     * 付款方式
     */
    private String htPaymode;

    /**
     * 客户签约人
     */
    private String htCusub;

    /**
     * 我方签约人
     */
    private String htWesub;

    /**
     * 外币备注
     */
    private String htSummemo;

    /**
     * 交付地点
     */
    private String htDeliplace;

    /**
     * 发货方式
     */
    private String htWuliutype;

    /**
     * 预计运费
     */
    private BigDecimal htYunfeimoney;

    /**
     * 收货地址 ID
     */
    private String fahuoaddressid;

    /**
     * 合同正文
     */
    private String htContract;

    /**
     * 备注
     */
    private String htRemark;

    /**
     * 项目 ID
     */
    private String projectId;

    /**
     * 对应机会
     */
    private String htXshid;

    /**
     * 销售人员
     */
    private String diyStr63312;

    /**
     * 计业绩门店
     */
    private String diyRdo63326;

    /**
     * 销售大区
     */
    private String diyRdo100701;

    /**
     * 带单人
     */
    private String diyStr63315;

    /**
     * 带单系数
     */
    private String diyStr63314;

    /**
     * 带单费金额
     */
    private String diyStr63316;

    /**
     * 公司赠品金额
     */
    private String diyNum63318;

    /**
     * 活动满减金额
     */
    private String diyNum63320;

    /**
     * 返现金额
     */
    private String diyNum63319;

    /**
     * 改装费用
     */
    private String diyNum136662;

    /**
     * 订单折让总额
     */
    private String diyNum63321;

    /**
     * 订单折让率
     */
    private String diyNum136663;

    /**
     * 本单提成
     */
    private String diyNum63322;

    /**
     * 客户城市
     */
    private String diyRdo72744;

    /**
     * 订单折让率（旧）
     */
    private String diyStr63313;

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
