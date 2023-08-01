package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.Currency.Currency;
import com.deer.wms.base.system.model.Currency.CurrencyCriteria;
import com.deer.wms.base.system.model.Currency.CurrencyDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/11/22.
 */
public interface CurrencyService extends Service<Currency, Integer> {

    List<CurrencyDto> findList(CurrencyCriteria criteria);

}
