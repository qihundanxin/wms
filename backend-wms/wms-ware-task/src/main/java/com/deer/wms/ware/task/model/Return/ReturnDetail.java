package com.deer.wms.ware.task.model.Return;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 退货detail信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "return_detail")
public class ReturnDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_detail_id")
    private Integer returnDetailId;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 详请单号
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
     * 退货数量
     */
    @Column(name = "return_quantity")
    private Double returnQuantity;

    /**
     * 退货自定义1
     */
    @Column(name = "return_udf_ds1")
    private String returnUdfDs1;

    /**
     * 退货自定义2
     */
    @Column(name = "return_udf_ds2")
    private String returnUdfDs2;

    /**
     * 退货自定义3
     */
    @Column(name = "return_udf_ds3")
    private String returnUdfDs3;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 库存ID
     */
    @Column(name = "inventory_id")
    private Integer inventoryId;

    /**
     * 发货状态 1-待处理 2-完成
     */
    @Column(name = "state")
    private Integer state;
}
