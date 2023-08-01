package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.in.AcceptAuditRecord;
import com.deer.wms.ASN.manage.model.in.AcceptAuditRecordCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lixuehe
 * @date 2023/1/11
 */
public interface AcceptAuditRecordMapper extends Mapper<AcceptAuditRecord> {

    List<AcceptAuditRecord> findList(AcceptAuditRecordCriteria criteria);


}
