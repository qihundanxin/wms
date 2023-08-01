package com.deer.wms.inventory.service.impl;

import com.deer.wms.inventory.dao.CombinePalletTaskMapper;
import com.deer.wms.inventory.model.combinePallet.CombinePalletTask;
import com.deer.wms.inventory.service.CombinePalletTaskService;
import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2020/06/14.
 */
@Service
@Transactional
public class CombinePalletTaskServiceImpl extends AbstractService<CombinePalletTask, Integer> implements CombinePalletTaskService {

    @Autowired
    private CombinePalletTaskMapper combinePalletTaskMapper;

    @Override
    public List<CombinePalletTask> taskWithBoxCode(String boxCode) {
        return combinePalletTaskMapper.taskWithBoxCode(boxCode);
    }
}
