package com.deer.wms.review.manage.service.impl;

import com.deer.wms.review.manage.dao.CaseDetailMapper;
import com.deer.wms.review.manage.model.CaseDetail;
import com.deer.wms.review.manage.model.CaseDetailCriteria;
import com.deer.wms.review.manage.service.CaseDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.deer.wms.review.manage.model.CaseDetailDto;

/**
 * Created by guo on 2018/10/24.
 */
@Service
@Transactional
public class CaseDetailServiceImpl extends AbstractService<CaseDetail, Integer> implements CaseDetailService {

    @Autowired
    private CaseDetailMapper caseDetailMapper;

    @Override
    public List<CaseDetailDto> findList(CaseDetailCriteria criteria) {
        return caseDetailMapper.findList(criteria);
    }

    @Override
    public List<CaseDetailDto> findNowList(CaseDetailCriteria criteria) {
        return caseDetailMapper.findNowList(criteria);
    }

    @Override
    public List<CaseDetail> findNoReview(Integer caseMasterId) {
        return caseDetailMapper.findNoReview(caseMasterId);
    }

    @Override
    public List<CaseDetailDto> findByMasterId(Integer caseMasterId) {
        return caseDetailMapper.findByMasterId(caseMasterId);
    }
}
