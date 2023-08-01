package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.QcDetailMapper;
import com.deer.wms.ASN.manage.model.qc.QcDetail;
import com.deer.wms.ASN.manage.model.qc.QcDetailCriteria;
import com.deer.wms.ASN.manage.model.qc.QcDetailDto;
import com.deer.wms.ASN.manage.service.QcDetailService;
import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2020/01/15.
 */
@Service
@Transactional
public class QcDetailServiceImpl extends AbstractService<QcDetail, Integer> implements QcDetailService {

    @Autowired
    private QcDetailMapper qcDetailMapper;

    @Override
    public List<QcDetailDto> findList(QcDetailCriteria criteria) {
        return qcDetailMapper.findList(criteria);
    }

    @Override
    public List<QcDetail> findByBillNo(String billNo) {
        return qcDetailMapper.findByBillNo(billNo);
    }
}
