package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.CurrencyMapper;
import com.deer.wms.base.system.model.Currency.Currency;
import com.deer.wms.base.system.model.Currency.CurrencyCriteria;
import com.deer.wms.base.system.model.Currency.CurrencyDto;
import com.deer.wms.base.system.service.CurrencyService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/11/22.
 */
@Service
@Transactional
public class CurrencyServiceImpl extends AbstractService<Currency, Integer> implements CurrencyService {

    @Autowired
    private CurrencyMapper currencyMapper;

    @Override
    public List<CurrencyDto> findList(CurrencyCriteria criteria) {
        return currencyMapper.findList(criteria);
    }

}
