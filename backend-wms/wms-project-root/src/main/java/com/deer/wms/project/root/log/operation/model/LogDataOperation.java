package com.deer.wms.project.root.log.operation.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DataOperationLogTool.DataOperationLog;

import lombok.Data;

/**
 * 数据操作日志表，
 * 
 * 区别于{@link DataOperationLog}，本类用于数据库交互，{@link DataOperationLog}是日志接口的通用参数
 * 
 * @author xuesinuo
 *
 */
@Data
@Table(name = "log_data_operation")
public class LogDataOperation {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_data_operation_id")
    private Long logDataOperationId;
    /**
     * 仓库ID
     */
    @Column(name = "ware_id")
    private Long wareId;
    /**
     * 模块（表）：入库、出库等
     */
    @Column(name = "module")
    private String module;
    /**
     * 操作具体：发运、入库验收等
     */
    @Column(name = "operation")
    private String operation;
    /**
     * 被操作的单据、数据编号
     */
    @Column(name = "operation_code")
    private String operationCode;
    /**
     * 被操作的单据、数据ID
     */
    @Column(name = "operation_id")
    private Long operationId;
    /**
     * 修改前的数据（修改、删除）
     */
    @Column(name = "before_data")
    private String beforeData;
    /**
     * 修改后的数据（新增、修改、操作）
     */
    @Column(name = "after_data")
    private String afterData;
    /**
     * 操作描述
     */
    @Column(name = "message")
    private String message;
    /**
     * 耗时（毫秒）
     */
    @Column(name = "timer")
    private Long timer;
    /**
     * 操作人ID，-1系统自动触发
     */
    @Column(name = "operation_user_id")
    private Long operationUserId;
    /**
     * 操作人名称
     */
    @Column(name = "operation_user_name")
    private String operationUserName;
    /**
     * 操作时间
     */
    @Column(name = "operation_datetime")
    private LocalDateTime operationDatetime;
    /**
     * 日志编号（32位UUID格式）
     */
    @Column(name = "uuid")
    private String uuid;
}
