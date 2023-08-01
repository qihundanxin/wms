package com.deer.wms.ware.task.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 扫码日志
 */
@Data
@Table(name = "scan_log")
public class ScanLog {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * 操作人
     */
    @Column(name = "create_user_id")
    private Integer createUserId;
    /**
     * 操作时间
     */
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 是否成功，null为不区分成功失败
     */
    @Column(name = "has_success")
    private Boolean hasSuccess;
    /**
     * 扫描的内容
     */
    @Column(name = "scan_value")
    private String scanValue;
    /**
     * 扫码场景：1按拣货任务拣货 2一码一拣 3出库扫码
     */
    @Column(name = "scan_type")
    private Integer scanType;
}
