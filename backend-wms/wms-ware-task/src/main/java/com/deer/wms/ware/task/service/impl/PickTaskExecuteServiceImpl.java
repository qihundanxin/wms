package com.deer.wms.ware.task.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.ware.task.dao.PickTaskExecuteMapper;
import com.deer.wms.ware.task.model.pickTask.PickTaskExecute;
import com.deer.wms.ware.task.service.PickTaskExecuteService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PickTaskExecuteServiceImpl implements PickTaskExecuteService {
    private final PickTaskExecuteMapper dao;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public PickTaskExecute save(PickTaskExecute pickTaskExecute) {
        if (pickTaskExecute.getExecuteTime() == null) {
            pickTaskExecute.setExecuteTime(new Date());
        }
        dao.insert(pickTaskExecute);
        return pickTaskExecute;
    }

    @Override
    public List<PickTaskExecute> getByQrCode(String qrCode) {
        return dao.selectByQrCode(qrCode);
    }
}
