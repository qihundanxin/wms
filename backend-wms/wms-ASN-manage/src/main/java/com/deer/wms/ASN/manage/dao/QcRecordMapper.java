package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.qc.QcRecord;
import com.deer.wms.ASN.manage.model.qc.QcRecordCriteria;
import com.deer.wms.ASN.manage.model.qc.QcRecordDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QcRecordMapper extends Mapper<QcRecord> {

    List<QcRecordDto> findList(QcRecordCriteria criteria);

    void deleteByDetailNo(@Param("detailNo") String detailNo);
}