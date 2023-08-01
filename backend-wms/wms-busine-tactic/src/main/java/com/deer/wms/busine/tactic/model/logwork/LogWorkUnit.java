package com.deer.wms.busine.tactic.model.logwork;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 工作单元记录（Method级记录）
 *
 * @author xuesinuo
 */
@Data
@Table(name = "log_work_unit")
public class LogWorkUnit {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 开始时间
     */
    private Date begin;

    /**
     * 结束时间
     */
    private Date end;

    /**
     * 执行方法全限定名
     */
    private String method;

    /**
     * 传入参数toJsonString
     */
    private String args;

    /**
     * 返回结果toString
     */
    private String result;

    /**
     * 出现的异常
     */
    private String throwable;

    /**
     * 备注
     */
    private String remark;

    /**
     * 代码工作日志ID
     */
    private Long logWorkId;

    /**
     * 时长
     */
    private Long time;
}
