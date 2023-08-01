package com.deer.wms.ASN.manage.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 异常入库日志
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "accept_log")
public class AcceptLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accept_log_id")
    private Integer acceptLogId;

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
     * 物料货主名称
     */
    @Column(name = "item_original_name")
    private String itemOriginalName;

    /**
     * 商品图片
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 规格
     */
    @Column(name = "spec")
    private String spec;

    /**
     * 型号
     */
    @Column(name = "model")
    private String model;

    /**
     * 批次ID
     */
    @Column(name = "batch_id")
    private Integer batchId;

    /**
     * 批次名称
     */
    @Column(name = "batch_name")
    private String batchName;

    /**
     * 货位编码
     */
    @Column(name = "cell_code")
    private String cellCode;

    /**
     * 类型: 0-默认验收 1-采购入库 2-ASN单 3-盲收 4-冻结
     */
    @Column(name = "bill_type")
    private Integer billType;

    /**
     * 状态: 0-失败 1-成功 2-待上架
     */
    @Column(name = "accept_type")
    private Integer acceptType;

    /**
     * 1-合格
     */
    @Column(name = "qc_type")
    private Integer qcType;

    /**
     * 同步状态: 0-默认 1-同步erp中 2-同步成功
     */
    @Column(name = "accept_state")
    private Integer acceptState;

    /**
     * 验收人ID
     */
    @Column(name = "accept_user_id")
    private Integer acceptUserId;

    /**
     * 签收/贴标/质检人
     */
    @Column(name = "accept_user_name")
    private String acceptUserName;

    /**
     * 验收时间
     */
    @Column(name = "accept_time")
    private Date acceptTime;

    /**
     * 上架ID
     */
    @Column(name = "up_user_id")
    private Integer upUserId;

    /**
     * 上架人
     */
    @Column(name = "up_user_name")
    private String upUserName;

    /**
     * 上架时间
     */
    @Column(name = "up_time")
    private Date upTime;

    /**
     * 预期数量
     */
    @Column(name = "expect_quantity")
    private Double expectQuantity;

    /**
     * 入库数量
     */
    @Column(name = "quantity")
    private Double quantity;

    /**
     * 异常数量
     */
    @Column(name = "ex_quantity")
    private Double exQuantity;

    /**
     * 采购单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 验收记录id
     */
    @Column(name = "accept_record_id")
    private Integer acceptRecordId;

    /**
     * 是否结束记录 0否 1是
     */
    @Column(name = "end_status")
    private Integer endStatus;
}
