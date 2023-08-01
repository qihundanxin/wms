package com.deer.wms.ware.task.service.impl;

import com.deer.wms.ware.task.dao.ReturnDetailMapper;
import com.deer.wms.ware.task.model.Return.ReturnDetail;
import com.deer.wms.ware.task.model.Return.ReturnDetailCriteria;
import com.deer.wms.ware.task.service.ReturnDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2020/04/19.
 */
@Service
@Transactional
public class ReturnDetailServiceImpl extends AbstractService<ReturnDetail, Integer> implements ReturnDetailService {

    @Autowired
    private ReturnDetailMapper returnDetailMapper;

    @Override
    public List<ReturnDetail> findByBillNo(String billNo) {
        return returnDetailMapper.findByBillNo(billNo);
    }

    @Override
    public List<ReturnDetail> findList(ReturnDetailCriteria criteria) {
        return returnDetailMapper.findList(criteria);
    }
}
