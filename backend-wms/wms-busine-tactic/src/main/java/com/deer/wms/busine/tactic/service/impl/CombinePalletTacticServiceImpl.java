package com.deer.wms.busine.tactic.service.impl;

import com.deer.wms.busine.tactic.dao.CombinePalletTacticMapper;
import com.deer.wms.busine.tactic.model.CombinePalletTactic;
import com.deer.wms.busine.tactic.service.CombinePalletTacticService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by  on 2020/06/14.
 */
@Service
@Transactional
public class CombinePalletTacticServiceImpl extends AbstractService<CombinePalletTactic, Integer> implements CombinePalletTacticService {

    @Autowired
    private CombinePalletTacticMapper combinePalletTacticMapper;

}
