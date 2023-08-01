package com.deer.wms.ware.task.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 *  装运箱详情
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "ship_box_detail")
public class ShipBoxDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ship_box_detail_id")
    private Integer shipBoxDetailId;

    /**
     *  快递单号
     */
    @Column(name = "ship_bill_code")
    private String shipBillCode;

    /**
     * 装箱id
     */
    @Column(name = "ship_box_id")
    private Integer shipBoxId;
}
