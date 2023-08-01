package com.deer.wms.review.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.review.manage.model.CaseDetail;
import com.deer.wms.review.manage.model.CaseDetailCriteria;
import com.deer.wms.review.manage.model.CaseDetailDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CaseDetailMapper extends Mapper<CaseDetail> {
    List<CaseDetailDto> findList(CaseDetailCriteria criteria);

    List<CaseDetailDto> findNowList(CaseDetailCriteria criteria);

    List<CaseDetail> findNoReview(@Param("caseMasterId") Integer caseMasterId);

    List<CaseDetailDto> findByMasterId(@Param("caseMasterId") Integer caseMasterId);

}