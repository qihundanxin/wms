package com.deer.wms.inventory.model.combinePallet;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 合托任务明细
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "combine_pallet_task_detail")
public class CombinePalletTaskDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 任务号
     */
    @Column(name = "task_no")
    private String taskNo;

    /**
     * 托盘号
     */
    @Column(name = "pallet_code")
    private String palletCode;

    /**
     * 货位码
     */
    @Column(name = "cell_code")
    private String cellCode;

    /**
     * 料号
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 批次
     */
    @Column(name = "batch")
    private String batch;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 是否是目标托盘
     */
    @Column(name = "is_target")
    private Integer isTarget;
}
