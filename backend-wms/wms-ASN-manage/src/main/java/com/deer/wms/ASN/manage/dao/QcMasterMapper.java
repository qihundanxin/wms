package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.qc.QcMaster;
import com.deer.wms.ASN.manage.model.qc.QcMasterCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QcMasterMapper extends Mapper<QcMaster> {

    List<QcMaster> findList(QcMasterCriteria criteria);

    void deleteByBillNo(@Param("billNo") String billNo);

}