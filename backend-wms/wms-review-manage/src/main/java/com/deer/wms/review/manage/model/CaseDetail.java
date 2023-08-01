package com.deer.wms.review.manage.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 审核详情记录
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "case_detail")
public class CaseDetail {

    // 兼容旧的String类型日期
    public String getTaskTime() {
        return DateUtils.dateToStrNullable(this.taskTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = DateUtils.strToDate(taskTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_detail_id")
    private Integer caseDetailId;

    /**
     * 审核 masterId
     */
    @Column(name = "case_master_id")
    private Integer caseMasterId;

    /**
     * 排序
     */
    @Column(name = "order_no")
    private Integer orderNo;

    /**
     * 0-与节点  1-或节点
     */
    @Column(name = "detail_type")
    private Integer detailType;

    /**
     * 任务时间
     */
    @Column(name = "task_time")
    private Date taskTime;

    /**
     * 审核结果   -1：未审核    0：未通过   1-通过
     */
    @Column(name = "task_result")
    private Integer taskResult;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 审核节点id
     */
    @Column(name = "review_node_id")
    private Integer reviewNodeId;

    /**
     * 审核节点名称
     */
    @Column(name = "review_node_name")
    private String reviewNodeName;
}
