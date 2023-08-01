package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.AcceptExMapper;
import com.deer.wms.ASN.manage.model.AcceptEx;
import com.deer.wms.ASN.manage.model.AcceptExCriteria;
import com.deer.wms.ASN.manage.model.AcceptExDto;
import com.deer.wms.ASN.manage.service.AcceptExService;

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
public class AcceptExServiceImpl extends AbstractService<AcceptEx, Integer> implements AcceptExService {

    @Autowired
    private AcceptExMapper acceptExMapper;

    @Override
    public List<AcceptExDto> findList(AcceptExCriteria criteria) {
        return acceptExMapper.findList(criteria);
    }

    @Override
    public List<AcceptEx> findCanTongBu() {
        return acceptExMapper.findCanTongBu();
    }
}
