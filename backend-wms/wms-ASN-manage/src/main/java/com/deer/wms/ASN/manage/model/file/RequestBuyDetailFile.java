package com.deer.wms.ASN.manage.model.file;

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
 * 采购详情归档
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "request_buy_detail_file")
public class RequestBuyDetailFile {
    // 兼容旧的String类型日期
    public String getAcceptTime() {
        return DateUtils.dateToStrNullable(this.acceptTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = DateUtils.strToDate(acceptTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_buy_detail_id")
    private Integer requestBuyDetailId;

    /**
     * 采购单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 单据号
     */
    @Column(name = "detail_no")
    private String detailNo;

    /**
     * 物料编码
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 物料名称
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * 包装详情ID
     */
    @Column(name = "pack_detail_id")
    private Integer packDetailId;

    /**
     * 包装描述
     */
    @Column(name = "pack_describe")
    private String packDescribe;

    /**
     * 2-等待审核 1-审核通过 0-审核中 -1 -审核退回
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 预期数量
     */
    @Column(name = "expect_quantity")
    private Double expectQuantity;

    /**
     * 验收数量
     */
    @Column(name = "accept_quantity")
    private Double acceptQuantity;

    /**
     * asn 数量
     */
    @Column(name = "asn_quantity")
    private Double asnQuantity;

    /**
     * 验收时间
     */
    @Column(name = "accept_time")
    private Date acceptTime;

    /**
     * 批次属性编码
     */
    @Column(name = "batch_tactic_code")
    private String batchTacticCode;

    /**
     * 自定义1
     */
    @Column(name = "po_udf_ds1")
    private String poUdfDs1;

    /**
     * 自定义2
     */
    @Column(name = "po_udf_ds2")
    private String poUdfDs2;

    /**
     * 自定义3
     */
    @Column(name = "po_udf_ds3")
    private String poUdfDs3;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 进价（默认加币）
     */
    @Column(name = "price_in")
    private BigDecimal priceIn;

    /**
     * 进价税率
     */
    @Column(name = "tax_rate_in")
    private BigDecimal taxRateIn;
}
