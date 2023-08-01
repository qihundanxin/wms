package com.deer.wms.review.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.review.manage.model.CaseUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CaseUserMapper extends Mapper<CaseUser> {
    List<CaseUser> findNoReview(@Param("caseDetailId") Integer caseDetailId);

    List<CaseUser> findByCaseDetailId(@Param("caseDetailId") Integer caseDetailId);

    CaseUser findByDetailAndUserId(@Param("caseDetailId") Integer caseDetailId, @Param("userId") Integer userId);
}