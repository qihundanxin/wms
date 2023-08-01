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
 * 领料单
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "request_master")
public class RequestMaster {
    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_master_id")
    private Integer requestMasterId;

    /**
     * 单号
     */
    @Column(name = "request_no")
    private String requestNo;

    /**
     * 工单
     */
    @Column(name = "work_order_no")
    private String workOrderNo;

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
     * 货主Id
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 货主名称
     */
    @Column(name = "organization_name")
    private String organizationName;

    /**
     * 领料部门Id
     */
    @Column(name = "request_dept_id")
    private Integer requestDeptId;

    /**
     * 领料部门
     */
    @Column(name = "request_dept_name")
    private String requestDeptName;

    /**
     * 领料人id
     */
    @Column(name = "request_user_id")
    private Integer requestUserId;

    /**
     * 领料人
     */
    @Column(name = "request_user_name")
    private String requestUserName;

    /**
     * 领料时间
     */
    @Column(name = "request_date")
    private Date requestDate;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 用途
     */
    @Column(name = "purpose")
    private String purpose;

    /**
     * 发料方式：1-仓库送料 2-部门领料
     */
    @Column(name = "way")
    private Integer way;

    /**
     * 制单时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 制单人id
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 制单人名称
     */
    @Column(name = "create_user_name")
    private String createUserName;
}
