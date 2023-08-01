package com.deer.wms.review.manage.service.impl;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.dao.BillRecordMapper;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.model.BillRecordCriteria;
import com.deer.wms.review.manage.service.BillRecordService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/03/05.
 */
@Service
@Transactional
public class BillRecordServiceImpl extends AbstractService<BillRecord, Integer> implements BillRecordService {

    @Autowired
    private BillRecordMapper billRecordMapper;

    @Autowired
    private BillRecordService billRecordService;

    @Override
    public List<BillRecord> findList(BillRecordCriteria criteria) {
        return billRecordMapper.findList(criteria);
    }

    @Override
    public void updateBillRecord(BillRecord billRecord, CurrentUser currentUser) {
        if (billRecord.getBillRecordId() == null) {
            billRecord.setCreateUserId(currentUser.getUserId());
            billRecord.setCreateUserName(currentUser.getUserName());
            billRecord.setCreateTime(DateUtils.getNowDateTimeString());
            billRecordService.save(billRecord);
        } else {
            billRecord.setModifyUserId(currentUser.getUserId());
            billRecord.setModifyUserName(currentUser.getUserName());
            billRecord.setModifyTime(DateUtils.getNowDateTimeString());
            billRecordService.update(billRecord);
        }

    }

    @Override
    public void createBillRecord(BillRecord billRecord, CurrentUser currentUser) {
        billRecord.setCreateUserId(currentUser.getUserId());
        billRecord.setCreateUserName(currentUser.getUserName());
        billRecord.setCreateTime(DateUtils.getNowDateTimeString());
        billRecordService.save(billRecord);
    }

    @Override
    public BillRecord findByBillNoAndType(String billNo, Integer recordType) {
        BillRecordCriteria criteria = new BillRecordCriteria();
        criteria.setBillNo(billNo);
        criteria.setRecordType(recordType);
        BillRecord billRecord = billRecordMapper.findByBillNoAndType(criteria);
        if (billRecord == null) {
            billRecord = new BillRecord();
            billRecord.setBillNo(billNo);
            billRecord.setRecordType(recordType);
        }
        return billRecord;
    }
}
