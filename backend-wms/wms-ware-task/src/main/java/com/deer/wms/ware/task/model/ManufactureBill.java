package com.deer.wms.ware.task.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 加工单
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "manufacture_bill")
public class ManufactureBill {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getFinishTime() {
        return DateUtils.dateToStrNullable(this.finishTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = DateUtils.strToDate(finishTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 单号
     */
    @Column(name = "code")
    private String code;

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
     * 加工数量
     */
    @Column(name = "quantity")
    private Double quantity;

    /**
     * 已完成数量
     */
    @Column(name = "finished_quantity")
    private Double finishedQuantity;

    /**
     * 工序描述
     */
    @Column(name = "procedure_desc")
    private String procedureDesc;

    /**
     * 状态 0-初始化 1-已完成
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 完成时间
     */
    @Column(name = "finish_time")
    private Date finishTime;
}
