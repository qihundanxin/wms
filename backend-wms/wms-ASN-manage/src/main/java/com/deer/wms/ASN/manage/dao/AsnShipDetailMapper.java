package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.asn.AsnShipDetail;
import com.deer.wms.ASN.manage.model.asn.AsnShipDetailDto;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface AsnShipDetailMapper extends Mapper<AsnShipDetail> {

    List<AsnShipDetailDto> findByBillNo(String billNo);
}