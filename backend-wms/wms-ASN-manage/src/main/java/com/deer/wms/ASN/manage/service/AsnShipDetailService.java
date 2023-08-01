package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.asn.AsnShipDetail;
import com.deer.wms.ASN.manage.model.asn.AsnShipDetailDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by  on 2020/12/15.
 */
public interface AsnShipDetailService extends Service<AsnShipDetail, Integer> {

    List<AsnShipDetailDto> findByBillNo(String billNo);

}
