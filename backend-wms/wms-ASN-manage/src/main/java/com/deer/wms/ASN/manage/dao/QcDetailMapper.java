package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.qc.QcDetail;
import com.deer.wms.ASN.manage.model.qc.QcDetailCriteria;
import com.deer.wms.ASN.manage.model.qc.QcDetailDto;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface QcDetailMapper extends Mapper<QcDetail> {

    List<QcDetailDto> findList(QcDetailCriteria criteria);

    List<QcDetail> findByBillNo(String billNo);
}