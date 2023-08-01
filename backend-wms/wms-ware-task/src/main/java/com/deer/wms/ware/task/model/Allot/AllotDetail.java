package com.deer.wms.ware.task.model.Allot;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 调拨单详情单信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "allot_detail")
public class AllotDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allot_detail_id")
    private Integer allotDetailId;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 详情单号
     */
    @Column(name = "detail_no")
    private String detailNo;

    /**
     * 状态 0-初始化 1-部分调拨  2-全部调拨 3-部分拣货 4-全部拣货
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
     * 转换系数
     */
    @Column(name = "trans_ratio")
    private Double transRatio;

    /**
     * 调拨数
     */
    @Column(name = "expect_quantity")
    private Double expectQuantity;

    /**
     * 分配数
     */
    @Column(name = "allotted_quantity")
    private Double allottedQuantity;

    /**
     * 拣货数量
     */
    @Column(name = "pick_quantity")
    private Double pickQuantity;

    /**
     * 交货数量
     */
    @Column(name = "delivery_quantity")
    private Double deliveryQuantity;

    /**
     * 自定义1
     */
    @Column(name = "allot_udf_ds1")
    private String allotUdfDs1;

    /**
     * 自定义2
     */
    @Column(name = "allot_udf_ds2")
    private String allotUdfDs2;

    /**
     * 自定义3
     */
    @Column(name = "allot_udf_ds3")
    private String allotUdfDs3;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 重量
     */
    @Column(name = "weight")
    private Double weight;

    /**
     * 大小
     */
    @Column(name = "size")
    private Double size;

    /**
     * 分配包装ID
     */
    @Column(name = "allot_pack_detail_id")
    private Integer allotPackDetailId;

    /**
     * 分配包装描述
     */
    @Column(name = "allot_pack_describe")
    private String allotPackDescribe;

    /**
     * 分配转换系数
     */
    @Column(name = "allot_trans_ratio")
    private Double allotTransRatio;

    /**
     * 拣货包装ID
     */
    @Column(name = "pick_pack_detail_id")
    private Integer pickPackDetailId;

    /**
     * 拣货包装描述
     */
    @Column(name = "pick_pack_describe")
    private String pickPackDescribe;

    /**
     * 拣货转换系数
     */
    @Column(name = "pick_trans_ratio")
    private Double pickTransRatio;

    /**
     * 发货包装ID
     */
    @Column(name = "delivery_pack_detail_id")
    private Integer deliveryPackDetailId;

    /**
     * 发货包装描述
     */
    @Column(name = "delivery_pack_describe")
    private String deliveryPackDescribe;

    /**
     * 发货转换系数
     */
    @Column(name = "delivery_trans_ratio")
    private Double deliveryTransRatio;
}
