package com.deer.wms.ware.task.model.Box;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 装箱信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "box_detail")
public class BoxDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "box_detail_id")
    private Integer boxDetailId;

    /**
     * so单号
     */
    @Column(name = "bill_no")
    private String billNo;

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
     * 装箱数量
     */
    @Column(name = "quantity")
    private Double quantity;

    /**
     * 拣货任务id
     */
    @Column(name = "pick_task_id")
    private Integer pickTaskId;

    /**
     * 拣货数量
     */
    @Column(name = "pick_quantity")
    private Double pickQuantity;

    /**
     * 包装Id
     */
    @Column(name = "pack_detail_id")
    private Integer packDetailId;

    /**
     * 包装描述
     */
    @Column(name = "pack_describe")
    private String packDescribe;

    /**
     * 箱号
     */
    @Column(name = "box_no")
    private String boxNo;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 出库单详情id
     */
    @Column(name = "so_detail_id")
    private Integer soDetailId;

    /**
     * 出库单id
     */
    @Column(name = "so_master_id")
    private Integer soMasterId;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;
}
