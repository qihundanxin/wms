package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.in.AcceptAuditRecord;
import com.deer.wms.ASN.manage.model.in.AcceptAuditRecordCriteria;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * 验收审核记录
 *
 * @author lixuehe
 */

public interface AcceptAuditRecordService extends Service<AcceptAuditRecord, Integer> {

    /**
     * 查询验收列表
     * @param criteria
     * @return
     */
    List<AcceptAuditRecord> findList(AcceptAuditRecordCriteria criteria);

    int updateById(AcceptAuditRecord record);

    Boolean approved(Integer id, CurrentUser currentUser);

    Boolean notApproved(Integer id, CurrentUser currentUser);


}
