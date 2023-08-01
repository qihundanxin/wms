package com.deer.wms.ASN.manage.model.qc;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 质检detail
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "qc_detail")
public class QcDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qc_detail_id")
    private Integer qcDetailId;

    /**
     * 质检详情单
     */
    @Column(name = "detail_no")
    private String detailNo;

    /**
     * asn详情单
     */
    @Column(name = "asn_detail_no")
    private String asnDetailNo;

    /**
     * 质检master单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 状态 0-待质检 1-合格 2-不合格
     */
    @Column(name = "state")
    private Integer state;

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
     * 单位ID
     */
    @Column(name = "pack_detail_id")
    private Integer packDetailId;

    /**
     * 单位描述
     */
    @Column(name = "pack_describe")
    private String packDescribe;

    /**
     * 转换系数
     */
    @Column(name = "trans_ratio")
    private Double transRatio;

    /**
     * 预期数量
     */
    @Column(name = "expect_quantity")
    private Double expectQuantity;

    /**
     * 质检包装id
     */
    @Column(name = "qc_pack_detail_id")
    private Integer qcPackDetailId;

    /**
     * 质检单位
     */
    @Column(name = "qc_pack_describe")
    private String qcPackDescribe;

    /**
     * 质检转换系数
     */
    @Column(name = "qc_trans_ratio")
    private Double qcTransRatio;

    /**
     * 应检数量
     */
    @Column(name = "qc_quantity")
    private Double qcQuantity;

    /**
     * 已检数量
     */
    @Column(name = "check_quantity")
    private Double checkQuantity;

    /**
     * 应检包装id
     */
    @Column(name = "check_pack_detail_id")
    private Integer checkPackDetailId;

    /**
     * 应检单位
     */
    @Column(name = "check_pack_describe")
    private String checkPackDescribe;

    /**
     * 应检转换系数
     */
    @Column(name = "check_trans_ratio")
    private Double checkTransRatio;

    /**
     * 自定义1
     */
    @Column(name = "qc_udf_ds1")
    private String qcUdfDs1;

    /**
     * 自定义2
     */
    @Column(name = "qc_udf_ds2")
    private String qcUdfDs2;

    /**
     * 自定义3
     */
    @Column(name = "qc_udf_ds3")
    private String qcUdfDs3;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 0-ASN单生成 1-库存生成
     */
    @Column(name = "bill_source")
    private Integer billSource;

    /**
     * 最小合格率(%)
     */
    @Column(name = "min_pass_rate")
    private Double minPassRate;

    /**
     * 库存id
     */
    @Column(name = "inventory_id")
    private Integer inventoryId;
}
