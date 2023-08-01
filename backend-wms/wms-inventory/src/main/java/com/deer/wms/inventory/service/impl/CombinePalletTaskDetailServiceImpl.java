package com.deer.wms.inventory.service.impl;

import com.deer.wms.inventory.dao.CombinePalletTaskDetailMapper;
import com.deer.wms.inventory.model.combinePallet.CombinePalletTaskDetail;
import com.deer.wms.inventory.service.CombinePalletTaskDetailService;

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
public class CombinePalletTaskDetailServiceImpl extends AbstractService<CombinePalletTaskDetail, Integer> implements CombinePalletTaskDetailService {

    @Autowired
    private CombinePalletTaskDetailMapper combinePalletTaskDetailMapper;

    @Override
    public List<CombinePalletTaskDetail> detailsWithBoxCode(String boxCode) {
        return combinePalletTaskDetailMapper.detailsWithBoxCode(boxCode);
    }
}
