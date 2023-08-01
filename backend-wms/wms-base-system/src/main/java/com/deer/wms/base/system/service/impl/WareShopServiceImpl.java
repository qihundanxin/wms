package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.WareShopMapper;
import com.deer.wms.base.system.model.WareShop;
import com.deer.wms.base.system.service.WareShopService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by  on 2021/09/08.
 */
@Service
@Transactional
public class WareShopServiceImpl extends AbstractService<WareShop, Integer> implements WareShopService {

    @Autowired
    private WareShopMapper wareShopMapper;

}
