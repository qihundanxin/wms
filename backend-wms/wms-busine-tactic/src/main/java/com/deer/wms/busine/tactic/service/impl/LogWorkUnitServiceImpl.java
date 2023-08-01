package com.deer.wms.busine.tactic.service.impl;

import org.springframework.stereotype.Service;

import com.deer.wms.busine.tactic.dao.LogWorkUnitMapper;
import com.deer.wms.busine.tactic.model.logwork.LogWorkUnit;
import com.deer.wms.busine.tactic.service.LogWorkUnitService;
import com.deer.wms.project.root.log.work.annotation.WorkLogger;

import lombok.RequiredArgsConstructor;

/**
 * 工作单元记录（Method级记录） Impl
 *
 * @author xuesinuo
 * @since 2022-01-24
 */
@RequiredArgsConstructor
@Service
public class LogWorkUnitServiceImpl implements LogWorkUnitService {
    private final LogWorkUnitMapper mapper;

    @WorkLogger(false)
    @Override
    public LogWorkUnit post(LogWorkUnit entity) {
        mapper.insert(entity);
        return entity;
    }
}
