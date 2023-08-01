package com.deer.wms.review.manage.service;

import com.deer.wms.review.manage.model.CaseMaster;
import com.deer.wms.review.manage.model.CaseMasterCriteria;

import com.deer.wms.project.root.core.service.Service;

import java.util.List;

import com.deer.wms.review.manage.model.CaseMasterDto;

/**
 * Created by guo on 2018/10/24.
 */
public interface CaseMasterService extends Service<CaseMaster, Integer> {

    List<CaseMasterDto> findList(CaseMasterCriteria criteria);

    Integer createCase(String billNo, Integer billTypeId, Integer userId, Integer wareId, Integer organizationId);

    //审核
    /*
     * 返回值说明：-1不通过  0-流转中 1-已通过
     *
     * */
    Integer review(Integer caseDetailId, Integer userId, Integer reviewResult, String memo);

    void deleteByBillNo(String billNo);

    CaseMaster findByBillNo(String billNo);
}