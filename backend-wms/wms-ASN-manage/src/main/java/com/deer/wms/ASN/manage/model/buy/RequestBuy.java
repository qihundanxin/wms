package com.deer.wms.ASN.manage.model.buy;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 采购单
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "request_buy")
public class RequestBuy {
    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getExpectTime() {
        return DateUtils.dateToStrNullable(this.expectTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = DateUtils.strToDate(expectTime);
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_buy_id")
    private Integer requestBuyId;

    /**
     * 采购单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 采购类型
     */
    @Column(name = "buy_type")
    private Integer buyType;

    /**
     * 状态 -2-保存为草稿 2-等待审核 1-审核通过 0-审核中 -1 -审核退回
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 采购单状态 0-初始化 1-已码盘 2-部分收货  3-全部收货  4-ASN完成 5-超额收货 6-采购完成
     */
    @Column(name = "accept_state")
    private Integer acceptState;

    /**
     * 仓库ID
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 仓库名称
     */
    @Column(name = "ware_name")
    private String wareName;

    /**
     * 货主ID
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 货主名称
     */
    @Column(name = "organization_name")
    private String organizationName;

    /**
     * 供应商ID
     */
    @Column(name = "supplier_id")
    private Integer supplierId;

    /**
     * 供应商名称
     */
    @Column(name = "supplier_name")
    private String supplierName;

    /**
     * 采购总价
     */
    @Column(name = "buy_total_prices")
    private BigDecimal buyTotalPrices;

    /**
     * 服务费
     */
    @Column(name = "service_charge")
    private BigDecimal serviceCharge;

    /**
     * 其他费用
     */
    @Column(name = "other_expenses")
    private BigDecimal otherExpenses;
    /**
     * 付款说明
     */
    @Column(name = "payment_instruction")
    private String paymentInstruction;

    /**
     * 采购发票文件路径
     */
    @Column(name = "invoice_file_path")
    private String invoiceFilePath;
    /**
     * 付款方式
     */
    @Column(name = "payment_method")
    private String paymentMethod;


    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建人名称
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 预期到货时间
     */
    @Column(name = "expect_time")
    private Date expectTime;

    /**
     * 自定义1
     */
    @Column(name = "po_udf_hs1")
    private String poUdfHs1;

    /**
     * 自定义2
     */
    @Column(name = "po_udf_hs2")
    private String poUdfHs2;

    /**
     * 自定义3
     */
    @Column(name = "po_udf_hs3")
    private String poUdfHs3;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 0-提交 -2保存为草稿
     */
    @Column(name = "insert_type")
    private Integer insertType;

    /**
     * 采购单来源 0- 公司新建 2-供应商新建
     */
    @Column(name = "bill_source")
    private Integer billSource;

    /**
     * 强制完成原因
     */
    @Column(name = "force_completion_reason")
    private String forceCompletionReason;

    /**
     * 强制完成用户id
     */
    @Column(name = "fc_user_id")
    private Integer fcUserId;

    /**
     * 强制完成用户名称
     */
    @Column(name = "fc_user_name")
    private String fcUserName;

    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;
}
