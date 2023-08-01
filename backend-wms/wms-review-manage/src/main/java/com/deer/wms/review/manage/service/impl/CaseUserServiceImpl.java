package com.deer.wms.review.manage.service.impl;

import com.deer.wms.review.manage.dao.CaseUserMapper;
import com.deer.wms.review.manage.model.CaseUser;
import com.deer.wms.review.manage.service.CaseUserService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/11/19.
 */
@Service
@Transactional
public class CaseUserServiceImpl extends AbstractService<CaseUser, Integer> implements CaseUserService {

    @Autowired
    private CaseUserMapper caseUserMapper;

    @Override
    public List<CaseUser> findNoREview(Integer caseDetailId) {
        return caseUserMapper.findNoReview(caseDetailId);
    }

    @Override
    public List<CaseUser> findByCaseDetailId(Integer caseDetailId) {
        return caseUserMapper.findByCaseDetailId(caseDetailId);
    }

    @Override
    public CaseUser findByDetailAndUserId(Integer caseDetailId, Integer userId) {
        return caseUserMapper.findByDetailAndUserId(caseDetailId, userId);
    }
}
