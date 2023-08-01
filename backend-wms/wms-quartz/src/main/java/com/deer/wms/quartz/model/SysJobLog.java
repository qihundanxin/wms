package com.deer.wms.quartz.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 定时任务调度日志表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "sys_job_log")
public class SysJobLog implements Serializable {
    private static final long serialVersionUID = 1L;

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }


    // 非数据库字段
    private Date startTime;
    private Date endTime;
    private String createBy;
    private String updateBy;
    private Date updateTime;
    private String remark;

    /**
     * 任务日志ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_log_id")
    private Long jobLogId;

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
     * 日志信息
     */
    @Column(name = "job_message")
    private String jobMessage;

    /**
     * 执行状态（0正常 1失败）
     */
    @Column(name = "status")
    private String status;

    /**
     * 异常信息
     */
    @Column(name = "exception_info")
    private String exceptionInfo;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}
