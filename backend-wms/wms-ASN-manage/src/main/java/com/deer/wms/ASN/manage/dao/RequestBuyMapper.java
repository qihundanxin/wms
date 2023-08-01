package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.buy.RequestBuy;
import com.deer.wms.ASN.manage.model.buy.RequestBuyCriteria;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RequestBuyMapper extends Mapper<RequestBuy> {

    List<RequestBuyDto> findList(RequestBuyCriteria criteria);

    void deleteByBillNo(@Param("billNo") String billNo);

    RequestBuyDto findByIdAndVersion(@Param("requestBuyId")Integer requestBuyId, @Param("version")Integer version);

    int update(RequestBuyDto requestBuyDto);
}