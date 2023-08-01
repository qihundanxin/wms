package com.deer.wms.ware.task.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * SO发运单日志
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "so_log")
public class SoLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "so_log_id")
    private Integer soLogId;

    /**
     * soMaster Id
     */
    @Column(name = "so_master_id")
    private Integer soMasterId;

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
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 拣货货位编码
     */
    @Column(name = "pick_cell_code")
    private String pickCellCode;

    /**
     * 分拣货位编码
     */
    @Column(name = "sort_cell_code")
    private String sortCellCode;

    /**
     *  波次号
     */
    @Column(name = "wave_id")
    private Integer waveId;

    /**
     * 容器号
     */
    @Column(name = "box_code")
    private String boxCode;

    /**
     * 重量
     */
    @Column(name = "weight")
    private Double weight;

    /**
     * 店铺
     */
    @Column(name = "shop")
    private String shop;

    /**
     * 1-完成
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 明细行数
     */
    @Column(name = "so_structure")
    private Integer soStructure;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建人
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 拣货时间
     */
    @Column(name = "pick_time")
    private Date pickTime;

    /**
     * 拣货ID
     */
    @Column(name = "pick_user_id")
    private Integer pickUserId;

    /**
     * 拣货人
     */
    @Column(name = "pick_user_name")
    private String pickUserName;

    /**
     * 打包时间
     */
    @Column(name = "out_time")
    private Date outTime;

    /**
     * 打包ID
     */
    @Column(name = "out_user_id")
    private Integer outUserId;

    /**
     * 打包人
     */
    @Column(name = "out_user_name")
    private String outUserName;

    /**
     * 快递公司
     */
    @Column(name = "ship_code")
    private String shipCode;

    /**
     * 快递单号
     */
    @Column(name = "ship_bill_code")
    private String shipBillCode;

    /**
     * 创建时间
     */
    @Column(name = "log_create_time")
    private Date logCreateTime;

    /**
     * 是否结束记录 0否 1是
     */
    @Column(name = "end_status")
    private Integer endStatus;
}
