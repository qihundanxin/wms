package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.QcRecordMapper;
import com.deer.wms.ASN.manage.model.qc.*;
import com.deer.wms.ASN.manage.service.QcDetailService;
import com.deer.wms.ASN.manage.service.QcMasterService;
import com.deer.wms.ASN.manage.service.QcRecordFileService;
import com.deer.wms.ASN.manage.service.QcRecordService;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2020/01/15.
 */
@Service
@Transactional
public class QcRecordServiceImpl extends AbstractService<QcRecord, Integer> implements QcRecordService {

    @Autowired
    private QcRecordMapper qcRecordMapper;

    @Autowired
    private QcRecordService qcRecordService;

    @Autowired
    private QcMasterService qcMasterService;

    @Autowired
    private QcDetailService qcDetailService;

    @Autowired
    private QcRecordFileService qcRecordFileService;

    @Override
    public List<QcRecordDto> findList(QcRecordCriteria criteria) {
        return qcRecordMapper.findList(criteria);
    }

    /**
     * 归档
     *
     * @param detailNo
     * @param currentUser
     */
    @Override
    public void createFile(String detailNo, CurrentUser currentUser) {
        QcRecord qcRecord = qcRecordService.findBy("detailNo", detailNo);

        Integer fileUserId = currentUser.getUserId();
        String fileUserName = currentUser.getUserName();
        String fileTime = DateUtils.getNowDateTimeString();

        QcRecordFile qcRecordFile = new QcRecordFile();
        BeanUtils.copyProperties(qcRecordFile, qcRecord);

        qcRecordFile.setCreateUserId(fileUserId);
        qcRecordFile.setCreateUserName(fileUserName);
        qcRecordFile.setCreateTime(fileTime);

        qcRecordFileService.save(qcRecordFile);
        qcRecordService.deleteByDetailNo(detailNo);
    }

    @Override
    public void deleteByDetailNo(String detailNo) {
        qcRecordMapper.deleteByDetailNo(detailNo);
    }
}
