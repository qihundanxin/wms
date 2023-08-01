package com.deer.wms.ware.task.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 领料单详情
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "request_detail")
public class RequestDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_detail_id")
    private Integer requestDetailId;

    /**
     * 领料单id
     */
    @Column(name = "request_master_id")
    private Integer requestMasterId;

    /**
     * 物料单号
     */
    @Column(name = "item_no")
    private String itemNo;

    /**
     * 物料名称
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * 包装id
     */
    @Column(name = "pack_detail_id")
    private Integer packDetailId;

    /**
     * 包装名称
     */
    @Column(name = "pack_detail_name")
    private String packDetailName;

    /**
     * 转换系数
     */
    @Column(name = "trans_ratio")
    private Double transRatio;

    /**
     * 领料数量
     */
    @Column(name = "request_quantity")
    private Double requestQuantity;

    @Column(name = "out_quantity")
    private Double outQuantity;

    @Column(name = "return_quantity")
    private Double returnQuantity;

    @Column(name = "add_quantity")
    private Double addQuantity;
}
