package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.SelectProductMapper;
import com.deer.wms.base.system.model.SelectProduct;
import com.deer.wms.base.system.service.SelectProductService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2020/12/19.
 */
@Service
@Transactional
public class SelectProductServiceImpl extends AbstractService<SelectProduct, Integer> implements SelectProductService {

    @Autowired
    private SelectProductMapper selectProductMapper;

    @Override
    public void deleteAll() {
        selectProductMapper.deleteAll();
    }

    @Override
    public List<SelectProduct> selectBy(String where) {
        return selectProductMapper.selectBy(where);
    }
}
