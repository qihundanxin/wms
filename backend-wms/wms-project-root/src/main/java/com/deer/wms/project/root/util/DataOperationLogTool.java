package com.deer.wms.project.root.util;

import java.time.LocalDateTime;

import com.deer.wms.project.root.constant.DataOperationLogModule;
import com.deer.wms.project.root.constant.DataOperationLogOperation;

import lombok.Data;

/**
 * 数据操作日志工具
 * 
 * @author xuesinuo
 *
 */
public interface DataOperationLogTool {
    /**
     * 数据操作日志（DataOperationLog -> DoLog）
     * 
     * @author xuesinuo
     *
     */
    @Data
    public static class DataOperationLog<T> {
        /**
         * 操作时间
         */
        private LocalDateTime operationDatetime;
        /**
         * 日志编号（32位UUID格式）
         */
        private String uuid;
        /**
         * ID
         */
        private Long logDataOperationId;
        /**
         * 仓库ID
         */
        private Long wareId;
        /**
         * {@link DataOperationLogModule}
         */
        private String module;
        /**
         * {@link DataOperationLogOperation}
         */
        private String operation;
        /**
         * 被操作的单据、数据编号
         */
        private String operationCode;
        /**
         * 被操作的单据、数据ID
         */
        private Long operationId;
        /**
         * 修改前的数据（修改、删除）
         */
        private T beforeData;
        /**
         * 修改后的数据（新增、修改、操作）
         */
        private T afterData;
        /**
         * 操作描述
         */
        private String message;
        /**
         * 耗时（毫秒）
         */
        private Long timer;
        /**
         * 操作人ID，-1系统自动触发
         */
        private Long operationUserId;
        /**
         * 操作人名称
         */
        private String operationUserName;
    }

    /**
     * 保存日志<br>
     * 请使用此模版：
     * 
     * <pre>
     * // DoLog
     * Long time2 = System.currentTimeMillis();
     * DataOperationLog<?> doLog = new DataOperationLogTool.DataOperationLog<>();
     * // DoLog必填项
     * doLog.setModule(DataOperationLogModule.X);
     * doLog.setOperation(DataOperationLogOperation.X_Y);
     * doLog.setOperationUserId(-1L);
     * doLog.setOperationUserName("系统自动触发");
     * // DoLog选填项
     * doLog.setBeforeData(oldObj);
     * doLog.setAfterData(newObj);
     * doLog.setMessage("备注信息，没有就不要填了");
     * doLog.setOperationCode(order.getCode());// 尽可能填写
     * doLog.setOperationId(order.getId());// 尽可能填写
     * doLog.setTimer(time2 - time1);
     * doLog.setWareId(order.getWareId());// 尽可能填写
     * doLogger.save(doLog);
     * </pre>
     * 
     * @param doLog 日志信息
     */
    <T> void save(DataOperationLog<T> doLog);
}
