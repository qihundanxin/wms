package com.deer.wms.ASN.manage.model.qc;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "qc_record_file")
public class QcRecordFile {
    @Id
    @Column(name = "qc_record_file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer qcRecordFileId;

    /**
     * 质检单号
     */
    @Column(name = "qc_bill_no")
    private String qcBillNo;

    /**
     * 质检详情单号
     */
    @Column(name = "qc_detail_no")
    private String qcDetailNo;

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
     * 实检数量
     */
    @Column(name = "real_quantity")
    private Double realQuantity;

    /**
     * 合格数量
     */
    @Column(name = "qualified_quantity")
    private Double qualifiedQuantity;

    /**
     * 不合格数量
     */
    @Column(name = "fied_quantity")
    private Double fiedQuantity;

    /**
     * 检验时间
     */
    @Column(name = "qc_time")
    private Date qcTime;

    /**
     * 检验人ID
     */
    @Column(name = "qc_user_id")
    private Integer qcUserId;

    /**
     * 检验人
     */
    @Column(name = "qc_user_name")
    private String qcUserName;

    /**
     * 检验结果
     */
    @Column(name = "qc_result")
    private Integer qcResult;

    /**
     * 质检单类型 0-ASN生成  1-库存生成
     */
    @Column(name = "bill_type")
    private Integer billType;

    /**
     * 归档人ID
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 归档人
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 归档时间
     */
    @Column(name = "create_time")
    private String createTime;
}