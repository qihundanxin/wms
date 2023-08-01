package com.deer.wms.ware.task.model.Load;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 装车单拣货明细
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "load_detail")
public class LoadDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 装车单编码
     */
    @Column(name = "load_master_code")
    private String loadMasterCode;

    /**
     * 拣货单ID
     */
    @Column(name = "pick_task_id")
    private Integer pickTaskId;

    /**
     * 拣货数量
     */
    @Column(name = "pick_quantity")
    private Double pickQuantity;

    /**
     * 装车数量
     */
    @Column(name = "quantity")
    private Double quantity;

    /**
     * 包装 id
     */
    @Column(name = "pack_detail_id")
    private Integer packDetailId;

    /**
     * 单位
     */
    @Column(name = "pack_describe")
    private String packDescribe;

    /**
     * 箱号
     */
    @Column(name = "box_no")
    private String boxNo;
}
