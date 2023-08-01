package com.deer.wms.inventory.model.stockTake;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 盘点任务
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "stock_take_task")
public class StockTakeTask {
    // 兼容旧的String类型日期
    public String getTaskTime() {
        return DateUtils.dateToStrNullable(this.taskTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = DateUtils.strToDate(taskTime);
    }

    public String getTaskTime2() {
        return DateUtils.dateToStrNullable(this.taskTime2, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setTaskTime2(String taskTime2) {
        this.taskTime2 = DateUtils.strToDate(taskTime2);
    }

    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_take_task_id")
    private Integer stockTakeTaskId;

    /**
     * 盘点单id
     */
    @Column(name = "stock_take_master_id")
    private Integer stockTakeMasterId;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 库存id
     */
    @Column(name = "inventory_id")
    private Integer inventoryId;

    /**
     * 初盘-快照数量
     */
    @Column(name = "task_quantity")
    private Double taskQuantity;

    /**
     * 复盘-快照数量2
     */
    @Column(name = "task_quantity2")
    private Double taskQuantity2;

    /**
     * 初盘-盘点人id
     */
    @Column(name = "task_user_id")
    private Integer taskUserId;

    /**
     * 复盘-盘点人id
     */
    @Column(name = "task_user_id2")
    private Integer taskUserId2;

    /**
     * 初盘-盘点人
     */
    @Column(name = "task_user_name")
    private String taskUserName;

    /**
     * 复盘-盘点人
     */
    @Column(name = "task_user_name2")
    private String taskUserName2;

    /**
     * 初盘-盘点时间
     */
    @Column(name = "task_time")
    private Date taskTime;

    /**
     * 复盘-盘点时间2
     */
    @Column(name = "task_time2")
    private Date taskTime2;

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
     * 创建人名称
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 类型： 1(明盘) 2-（暗盘）
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 盘盈/盘亏数量
     */
    @Column(name = "differ_quantity")
    private Double differQuantity;

    /**
     * 复盘-盘盈/盘亏数量
     */
    @Column(name = "differ_quantity2")
    private Double differQuantity2;

    /**
     * 1-未盘，，2-复盘,3-已盘
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 实盘数量
     */
    @Column(name = "actual_quantity")
    private Double actualQuantity;

    /**
     * 复盘数量
     */
    @Column(name = "actual_quantity2")
    private Double actualQuantity2;
}
