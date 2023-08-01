package com.deer.wms.ware.task.service.impl;

import org.springframework.stereotype.Service;

import com.deer.wms.ware.task.dao.ScanLogMapper;
import com.deer.wms.ware.task.model.ScanLog;
import com.deer.wms.ware.task.service.ScanLogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScanLogServiceImpl implements ScanLogService {

    private final ScanLogMapper dao;
    @Override
    public void save(ScanLog scanLog) {
        scanLog.setId(null);
        dao.insert(scanLog);
    }
}
