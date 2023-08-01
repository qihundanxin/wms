package com.deer.wms.inventory.model.combinePallet;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 合托任务
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "combine_pallet_task")
public class CombinePalletTask {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getCompleteTime() {
        return DateUtils.dateToStrNullable(this.completeTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = DateUtils.strToDate(completeTime);
    }

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
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 完成时间
     */
    @Column(name = "complete_time")
    private Date completeTime;
}
