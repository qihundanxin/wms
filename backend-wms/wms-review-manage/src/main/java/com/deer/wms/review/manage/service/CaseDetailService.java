package com.deer.wms.review.manage.service;

import com.deer.wms.review.manage.model.CaseDetail;
import com.deer.wms.review.manage.model.CaseDetailCriteria;

import com.deer.wms.project.root.core.service.Service;

import java.util.List;

import com.deer.wms.review.manage.model.CaseDetailDto;

/**
 * Created by guo on 2018/10/24.
 */
public interface CaseDetailService extends Service<CaseDetail, Integer> {

    List<CaseDetailDto> findList(CaseDetailCriteria criteria);

    List<CaseDetailDto> findNowList(CaseDetailCriteria criteria);

    List<CaseDetail> findNoReview(Integer caseMasterId);

    List<CaseDetailDto> findByMasterId(Integer caseMasterId);

}
