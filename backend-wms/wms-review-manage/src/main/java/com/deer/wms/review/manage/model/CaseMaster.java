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
 * 审核master
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "case_master")
public class CaseMaster {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_master_id")
    private Integer caseMasterId;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 0-审核中   1-已完结
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 发起人
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 当前进行中的节点
     */
    @Column(name = "case_node_id")
    private Integer caseNodeId;
}
