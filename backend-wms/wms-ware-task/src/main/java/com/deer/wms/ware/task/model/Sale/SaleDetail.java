package com.deer.wms.ware.task.model.Sale;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 销售单详情
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "sale_detail")
public class SaleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_detail_id")
    private Integer saleDetailId;

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
     * 转换系数
     */
    @Column(name = "trans_ratio")
    private Double transRatio;

    /**
     * 销售单状态 0-初始化 2-部分发红  3-全部发货 
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 销售数量
     */
    @Column(name = "expect_quantity")
    private Double expectQuantity;

    /**
     * 自定义1
     */
    @Column(name = "sale_udf_ds1")
    private String saleUdfDs1;

    /**
     * 自定义2
     */
    @Column(name = "sale_udf_ds2")
    private String saleUdfDs2;

    /**
     * 自定义3
     */
    @Column(name = "sale_udf_ds3")
    private String saleUdfDs3;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;
}
