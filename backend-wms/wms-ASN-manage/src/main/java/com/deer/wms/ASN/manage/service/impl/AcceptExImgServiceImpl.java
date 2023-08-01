package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.AcceptExImgMapper;
import com.deer.wms.ASN.manage.model.AcceptExImg;
import com.deer.wms.ASN.manage.model.AcceptExImgCriteria;
import com.deer.wms.ASN.manage.service.AcceptExImgService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2021/01/02.
 */
@Service
@Transactional
public class AcceptExImgServiceImpl extends AbstractService<AcceptExImg, Integer> implements AcceptExImgService {

    @Autowired
    private AcceptExImgMapper acceptExImgMapper;

    @Override
    public List<String> findByAcceptExId(Integer exId) {
        return acceptExImgMapper.findByAcceptExId(exId);
    }

    @Override
    public List<AcceptExImg> findByShipBillCode2(String shipBillCode) {
        return acceptExImgMapper.findByShipBillCode2(shipBillCode);
    }

    @Override
    public List<AcceptExImg> findList(AcceptExImgCriteria criteria) {
        return acceptExImgMapper.findList(criteria);
    }
}
