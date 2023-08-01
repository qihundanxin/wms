package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.ScanLog;

/**
 * 扫码日志service
 */
public interface ScanLogService {
    /**
     * 保存扫码日志
     * 
     * @param scanLog 扫码日志
     */
    void save(ScanLog scanLog);
}
