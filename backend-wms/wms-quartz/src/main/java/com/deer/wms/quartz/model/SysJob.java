package com.deer.wms.quartz.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.StringUtils;
import com.deer.wms.quartz.util.CronUtils;

import lombok.Data;

/**
 * 定时任务调度表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "sys_job")
public class SysJob implements Serializable {
    private static final long serialVersionUID = 1L;

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getUpdateTime() {
        return DateUtils.dateToStrNullable(this.updateTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = DateUtils.strToDate(updateTime);
    }

    /**
     * @return 下一次执行时间
     */
    public Date getNextValidTime() {
        if (StringUtils.isNotEmpty(cronExpression)) {
            return CronUtils.getNextExecution(cronExpression);
        }
        return null;
    }

    /**
     * 任务ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;

    /**
     * 任务名称
     */
    @Column(name = "job_name")
    private String jobName;

    /**
     * 任务组名
     */
    @Column(name = "job_group")
    private String jobGroup;

    /**
     * 任务方法
     */
    @Column(name = "method_name")
    private String methodName;

    /**
     * 方法参数
     */
    @Column(name = "method_params")
    private String methodParams;

    /**
     * cron执行表达式
     */
    @Column(name = "cron_expression")
    private String cronExpression;

    /**
     * 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
     */
    @Column(name = "misfire_policy")
    private String misfirePolicy;

    /**
     * 是否并发执行（0允许 1禁止）
     */
    @Column(name = "concurrent")
    private String concurrent;

    /**
     * 状态（0正常 1暂停）
     */
    @Column(name = "status")
    private String status;

    /**
     * 创建者
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新者
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 备注信息
     */
    @Column(name = "remark")
    private String remark;
}
