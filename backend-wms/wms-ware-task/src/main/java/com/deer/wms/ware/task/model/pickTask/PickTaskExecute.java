package com.deer.wms.ware.task.model.pickTask;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 拣货任务执行情况
 * 
 * @author xuesinuo
 *
 */
@Data
@Table(name = "pick_task_execute")
public class PickTaskExecute {
    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    /** 拣货任务ID */
    private Integer pickTaskId;
    /** SO单详情ID */
    private Integer soDetailId;
    /** SO单ID */
    private Integer soMasterId;
    /** 仓库ID */
    private Integer wareId;
    /** 仓库名 */
    private String wareName;
    /** 从货位拣出 */
    private String fromCellCode;
    /** 放到货位 */
    private String toCellCode;
    /** SKU编号 */
    private String itemCode;
    /** 商品名称 */
    private String itemName;
    /** 本次拣出数量 */
    private Double reviewQuantity;
    /** 放到的容器号 */
    private String boxCode;
    /** 备注 */
    private String memo;
    /** 拣货人id */
    private Integer executeUserId;
    /** 拣货时间 */
    private Date executeTime;
    /** 拣货人Name */
    private String executeUserName;
    /** 二维码信息 */
    private String qrCode;
    /** 是否成功 */
    private Boolean hasSuccess;
}
