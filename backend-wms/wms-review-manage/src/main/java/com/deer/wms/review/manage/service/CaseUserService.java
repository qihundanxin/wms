package com.deer.wms.review.manage.service;

import com.deer.wms.review.manage.model.CaseUser;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/11/19.
 */
public interface CaseUserService extends Service<CaseUser, Integer> {

    List<CaseUser> findNoREview(Integer caseDetailId);

    List<CaseUser> findByCaseDetailId(Integer caseDetailId);

    CaseUser findByDetailAndUserId(Integer caseDetailId, Integer UserId);

}
