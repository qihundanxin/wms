package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.AsnShipDetailMapper;
import com.deer.wms.ASN.manage.model.asn.AsnShipDetail;
import com.deer.wms.ASN.manage.model.asn.AsnShipDetailDto;
import com.deer.wms.ASN.manage.service.AsnShipDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2020/12/15.
 */
@Service
@Transactional
public class AsnShipDetailServiceImpl extends AbstractService<AsnShipDetail, Integer> implements AsnShipDetailService {

    @Autowired
    private AsnShipDetailMapper asnShipDetailMapper;

    @Override
    public List<AsnShipDetailDto> findByBillNo(String billNo) {
        return asnShipDetailMapper.findByBillNo(billNo);
    }
}
