package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.Currency.Currency;
import com.deer.wms.base.system.model.Currency.CurrencyCriteria;
import com.deer.wms.base.system.model.Currency.CurrencyDto;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface CurrencyMapper extends Mapper<Currency> {

    List<CurrencyDto> findList(CurrencyCriteria criteria);
}