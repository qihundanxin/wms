package com.deer.wms.ware.task.model.SO;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * SO发运单 detail归档
 *
 * @author xuesinuo
 */
@Data
@Table(name = "so_detail_file")
public class SoDetailFile {
    @Id
    @Column(name = "so_detail_file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer soDetailFileId;

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
     * 状态 0-新建
     */
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
     * 订货数
     */
    @Column(name = "order_quantity")
    private Double orderQuantity;

    /**
     * 预分配数
     */
    @Column(name = "expect_allotted_quantity")
    private Double expectAllottedQuantity;

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
     * 发货数量
     */
    @Column(name = "delivery_quantity")
    private Double deliveryQuantity;

    /**
     * 自定义1
     */
    @Column(name = "so_udf_ds1")
    private String soUdfDs1;

    /**
     * 自定义2
     */
    @Column(name = "so_udf_ds2")
    private String soUdfDs2;

    /**
     * 自定义3
     */
    @Column(name = "so_udf_ds3")
    private String soUdfDs3;

    /**
     * 备注
     */
    private String memo;

    /**
     * 合并单号
     */
    @Column(name = "merge_no")
    private String mergeNo;
}