package com.deer.wms.ASN.manage.model.file;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 质检单详情归档
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "qc_detail_file")
public class QcDetailFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qc_detail_file_id")
    private Integer qcDetailFileId;

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
     * 状态 0- 未质检  1-已质检
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
     * 预期数量
     */
    @Column(name = "expect_quantity")
    private Double expectQuantity;

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
}
