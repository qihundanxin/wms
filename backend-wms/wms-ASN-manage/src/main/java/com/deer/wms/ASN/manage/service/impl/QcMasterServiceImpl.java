package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.QcMasterMapper;
import com.deer.wms.ASN.manage.model.asn.AsnDetail;
import com.deer.wms.ASN.manage.model.asn.AsnMaster;
import com.deer.wms.ASN.manage.model.file.AsnDetailFile;
import com.deer.wms.ASN.manage.model.file.AsnMasterFile;
import com.deer.wms.ASN.manage.model.file.QcDetailFile;
import com.deer.wms.ASN.manage.model.file.QcMasterFile;
import com.deer.wms.ASN.manage.model.qc.*;
import com.deer.wms.ASN.manage.service.*;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2020/01/15.
 */
@Service
@Transactional
public class QcMasterServiceImpl extends AbstractService<QcMaster, Integer> implements QcMasterService {

    @Autowired
    private QcMasterMapper qcMasterMapper;

    @Autowired
    private QcMasterService qcMasterService;

    @Autowired
    private QcDetailService qcDetailService;

    @Autowired
    private QcMasterFileService qcMasterFileService;

    @Autowired
    private QcDetailFileService qcDetailFileService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private QcRecordService qcRecordService;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public void createQc(QcData qcData, CurrentUser currentUser) {
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(qcData.getQcBillNo());
        QcMaster qcMaster1 = qcMasterService.findBy("billNo", qcData.getQcBillNo());
        billRecord.setRecordType(12);//质检
        billRecord.setCreateTime(DateUtils.getNowDateTimeString());
        billRecord.setCreateUserId(currentUser.getUserId());
        billRecord.setCreateUserName(currentUser.getUserName());

        billRecordService.createBillRecord(billRecord, currentUser);
        //ASN单生成
        if (qcData.getBillSource() == 0) {

            QcRecord qcRecord = new QcRecord();
            QcDetail qcDetail = qcDetailService.findBy("detailNo", qcData.getQcDetailNo());
            BeanUtils.copyProperties(qcData, qcRecord);
            qcRecord.setBillType(qcData.getBillSource());
            qcRecord.setCheckQuantity(qcDetail.getCheckQuantity() + qcData.getRealQuantity());
            qcRecordService.save(qcRecord);
            qcRecord.setFiedQuantity(qcRecord.getRealQuantity() - qcRecord.getQualifiedQuantity());
            qcRecordService.update(qcRecord);

            qcDetail.setCheckQuantity(qcDetail.getCheckQuantity() + qcRecord.getCheckQuantity());
            qcDetailService.update(qcDetail);

            Double one = qcRecord.getQualifiedQuantity() / qcRecord.getQcQuantity();
            //更新detail的状态
            if (one >= qcRecord.getMinPassRate()) {
                qcDetail.setState(1);//合格
            }
            if (one < qcRecord.getMinPassRate()) {
                qcDetail.setState(2);//不合格
            }
            qcDetailService.update(qcDetail);
            //更新qc master状态
            QcDetailCriteria criteria = new QcDetailCriteria();
            criteria.setBillNo(qcRecord.getQcBillNo());
            List<QcDetailDto> qcDetails = qcDetailService.findList(criteria);

            Boolean qcAccept = true;
            QcMaster qcMaster = qcMasterService.findBy("billNo", qcDetail.getBillNo());
            for (QcDetail qcDetail2 : qcDetails) {
                if (qcDetail2.getState() != 1) {
                    qcAccept = false;
                }
                qcMaster.setQcState(2);
                qcMasterService.update(qcMaster);
            }
            if (qcAccept) {
                qcMaster.setQcState(1);
                qcMasterService.update(qcMaster);
            }
            //更新质检订单质检记录
            BillRecord billRecord2 = billRecordService.findByBillNoAndType(qcMaster.getBillNo(), 12);//质检
            billRecord2.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord2.setModifyUserId(qcRecord.getQcUserId());
            billRecord2.setModifyUserName(qcRecord.getQcUserName());
            billRecordService.update(billRecord2);
            //更新ASN订单质检记录
            BillRecord billRecord1 = billRecordService.findByBillNoAndType(qcMaster.getAsnBillNo(), 21);//ASN生成质检
            billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord1.setModifyUserId(qcRecord.getQcUserId());
            billRecord1.setModifyUserName(qcRecord.getQcUserName());
            billRecordService.update(billRecord1);

        }
        //库存余量生成
        if (qcData.getBillSource() == 1) {
            QcRecord qcRecord = new QcRecord();
            QcDetail qcDetail = qcDetailService.findBy("detailNo", qcData.getQcDetailNo());
            BeanUtils.copyProperties(qcData, qcRecord);
            qcRecord.setBillType(qcData.getBillSource());
            qcRecord.setCheckQuantity(qcDetail.getCheckQuantity() + qcData.getRealQuantity());
            qcRecordService.save(qcRecord);

            qcRecord.setFiedQuantity(qcRecord.getRealQuantity() - qcRecord.getQualifiedQuantity());
            qcRecordService.update(qcRecord);

            qcDetail.setCheckQuantity(qcDetail.getCheckQuantity() + qcRecord.getCheckQuantity());
            qcDetailService.update(qcDetail);
            Inventory inventory = inventoryService.findById(qcRecord.getInventoryId());

            if (qcRecord.getQualifiedQuantity().equals(qcRecord.getRealQuantity())) {
                inventory.setQcState(1);//合格
            }
            if (!qcRecord.getQualifiedQuantity().equals(qcRecord.getRealQuantity())) {
                inventory.setQcState(2);//不合格
            }
            // version
            inventoryService.update(inventory);

            //更新质检订单质检记录
            BillRecord billRecord2 = billRecordService.findByBillNoAndType(qcDetail.getBillNo(), 12);//质检
            billRecord2.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord2.setModifyUserId(qcRecord.getQcUserId());
            billRecord2.setModifyUserName(qcRecord.getQcUserName());
            billRecordService.update(billRecord2);
        }

    }

    @Override
    public void deleteByBillNo(String billNo) {
        qcMasterMapper.deleteByBillNo(billNo);
    }

    @Override
    public List<QcMaster> findList(QcMasterCriteria criteria) {
        return qcMasterMapper.findList(criteria);
    }

    @Override
    public void createFile(String billNo, CurrentUser currentUser) {
        QcMaster qcMaster = qcMasterService.findBy("billNo", billNo);
//        if(qcMaster.getAsnState() !=3){
//            throw new ServiceException(CommonCode.ASN_FILE_ERROR);
//        }
        Integer fileUserId = currentUser.getUserId();
        String fileUserName = currentUser.getUserName();
        String fileTime = DateUtils.getNowDateTimeString();
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(billNo);
        billRecord.setRecordType(19);//QC归档
        billRecord.setCreateTime(fileTime);
        billRecord.setCreateUserId(fileUserId);
        billRecord.setCreateUserName(fileUserName);
        billRecordService.save(billRecord);
        QcMasterFile qcMasterFile = new QcMasterFile();
        BeanUtils.copyProperties(qcMaster, qcMasterFile);

        qcMasterFile.setFileUserId(fileUserId);
        qcMasterFile.setFileUserName(fileUserName);
        qcMasterFile.setFileTime(fileTime);
        qcMasterFileService.save(qcMasterFile);

        List<QcDetail> qcDetails = qcDetailService.findByBillNo(billNo);

//        BeanUtils.copyProperties(asnDetails, asnDetailFile);
        for (int i = 0; i < qcDetails.size(); i++) {
            QcDetailFile qcDetailFile = new QcDetailFile();
            qcDetailFile.setBillNo(qcDetails.get(i).getBillNo());
            qcDetailFile.setAsnDetailNo(qcDetails.get(i).getAsnDetailNo());
            qcDetailFile.setDetailNo(qcDetails.get(i).getDetailNo());
            qcDetailFile.setItemCode(qcDetails.get(i).getItemCode());
            qcDetailFile.setItemName(qcDetails.get(i).getItemName());
            qcDetailFile.setPackDetailId(qcDetails.get(i).getPackDetailId());
            qcDetailFile.setPackDescribe(qcDetails.get(i).getPackDescribe());
            qcDetailFile.setExpectQuantity(qcDetails.get(i).getExpectQuantity());
            qcDetailFile.setCheckQuantity(qcDetails.get(i).getCheckQuantity());
            qcDetailFile.setQcQuantity(qcDetails.get(i).getQcQuantity());
            qcDetailFile.setMemo(qcDetails.get(i).getMemo());
            qcDetailFile.setState(qcDetails.get(i).getState());
            qcDetailFile.setQcUdfDs1(qcDetails.get(i).getQcUdfDs1());
            qcDetailFile.setQcUdfDs2(qcDetails.get(i).getQcUdfDs2());
            qcDetailFile.setQcUdfDs3(qcDetails.get(i).getQcUdfDs3());

            qcDetailFileService.save(qcDetailFile);

        }
        qcMasterService.deleteByBillNo(billNo);
        BillRecord billRecord1 = billRecordService.findByBillNoAndType(billNo, 19);//QC归档
        billRecord1.setModifyUserId(fileUserId);
        billRecord1.setModifyUserName(fileUserName);
        billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
        billRecordService.update(billRecord1);
    }

}
