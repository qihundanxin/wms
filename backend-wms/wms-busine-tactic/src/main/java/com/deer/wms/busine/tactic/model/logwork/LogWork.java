package com.deer.wms.busine.tactic.model.logwork;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 代码工作日志
 *
 * @author xuesinuo
 */
@Data
@Table(name = "log_work")
public class LogWork {
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
     * 链路ID
     */
    private String traceId;

    /**
     * 工作ID，类似http_request_id，为工作的标识符
     */
    private String workId;

    /**
     * 工作模式1:http 2:cron 3:tcp
     */
    private Integer workType;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 是否成功完成work
     */
    private Boolean success;

    /**
     * 任务发起的user
     */
    private Long userId;

    /**
     * 来源信息Json
     */
    private String fromInfo;

    /**
     * 时长
     */
    private Long time;
}
