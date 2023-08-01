package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.qc.QcDetail;
import com.deer.wms.ASN.manage.model.qc.QcDetailCriteria;
import com.deer.wms.ASN.manage.model.qc.QcDetailDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2020/01/15.
 */
public interface QcDetailService extends Service<QcDetail, Integer> {

    List<QcDetailDto> findList(QcDetailCriteria criteria);

    List<QcDetail> findByBillNo(String billNo);

}
