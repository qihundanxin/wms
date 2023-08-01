package com.deer.wms.ware.task.service.impl;

//import com.deer.wms.ASN.manage.model.asn.AsnData;
//import com.deer.wms.ASN.manage.model.asn.AsnDetail;
//import com.deer.wms.ASN.manage.model.asn.AsnMaster;
//import com.deer.wms.ASN.manage.service.AsnDetailService;
//import com.deer.wms.ASN.manage.service.AsnMasterService;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.system.manage.model.address.ShippingAddress;
import com.deer.wms.system.manage.model.carrier.Carrier;
import com.deer.wms.system.manage.service.CarrierService;
import com.deer.wms.system.manage.service.ShippingAddressService;
import com.deer.wms.system.manage.service.ValidatorService;
import com.deer.wms.ware.task.dao.AllotMasterMapper;
import com.deer.wms.ware.task.model.Allot.*;
import com.deer.wms.ware.task.model.SO.SoData;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.service.AllotDetailService;
import com.deer.wms.ware.task.service.AllotMasterService;

import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.ware.task.service.SoDetailService;
import com.deer.wms.ware.task.service.SoMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ll on 2020/03/04.
 */
@Service
@Transactional
public class AllotMasterServiceImpl extends AbstractService<AllotMaster, Integer> implements AllotMasterService {

    @Autowired
    private AllotMasterMapper allotMasterMapper;

    @Autowired
    private AllotMasterService allotMasterService;

    @Autowired
    private AllotDetailService allotDetailService;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private CaseMasterService caseMasterService;

//    @Autowired
//    private AsnMasterService asnMasterService;

    @Autowired
    private SoMasterService soMasterService;

    @Autowired
    private CarrierService carrierService;

    @Autowired
    private ShippingAddressService shippingAddressService;

    @Autowired
    private BillRecordService billRecordService;

    @Override
    public List<AllotMaster> findList(AllotMasterCriteria criteria) {
        return allotMasterMapper.findList(criteria);
    }

    @Override
    public void createAllotMaster(AllotData allotData, CurrentUser currentUser) {
            /**
             *  新建调拨单据
             */
            AllotMaster allotMaster = allotData.getAllotMaster();
            String tableName = "allot_master";
            String nextId = validatorService.getNextIdString(tableName);

            String billNo = "DB"+"-"+nextId;
            Integer createUserId = currentUser.getUserId();
            String createUserName = currentUser.getUserName();
            String createTime = DateUtils.getNowDateTimeString();
            //添加调拨单跟踪信息
            BillRecord billRecord =new BillRecord();
            billRecord.setBillNo(billNo);
            billRecord.setRecordType(38);//调拨新增
            billRecord.setCreateTime(createTime);
            billRecord.setCreateUserId(createUserId);
            billRecord.setCreateUserName(createUserName);
            billRecordService.createBillRecord(billRecord,currentUser);

            //      状态    2-等待审核(初始化)  1-审核通过  0-审核中  -1-审核退回
                Integer state = 2;
            /**
             * 草稿
             */
            if(allotData.getInsertType()==-2){
                state = 2;
                BillRecord billRecord1 =new BillRecord();
                billRecord1.setBillNo(billNo);
                billRecord1.setRecordType(39);//调拨草稿
                billRecord1.setCreateTime(createTime);
                billRecord1.setCreateUserId(createUserId);
                billRecord1.setCreateUserName(createUserName);
                billRecordService.createBillRecord(billRecord1,currentUser);
            }
                /**
                 * 提交
                 */
            else if(allotData.getInsertType()==0){
                String billNo1 = allotData.getAllotMaster().getBillNo();
                //通过草稿提交，则需要先把草稿删掉
                if(billNo1 != null){
                    allotMasterMapper.deleteByBillNo(billNo1);
                    BillRecord billRecord1 =billRecordService.findByBillNoAndType(billNo,39);//调拨草稿
                    billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
                    billRecord1.setModifyUserId(currentUser.getUserId());
                    billRecord1.setModifyUserName(currentUser.getUserName());
                    billRecordService.update(billRecord1);
                }
                /**
                 * 添加审核流
                 */
                //        -1不通过  0-流转中 1-已通过
                state = caseMasterService.createCase(billNo, 2, currentUser.getUserId(), allotMaster.getFromWareId(),allotMaster.getOrganizationId());
                BillRecord billRecord1 =new BillRecord();
                billRecord1.setBillNo(billNo);
                billRecord1.setRecordType(40);//调拨审核
                billRecord1.setCreateTime(createTime);
                billRecord1.setCreateUserId(createUserId);
                billRecord1.setCreateUserName(createUserName);
                billRecordService.createBillRecord(billRecord1,currentUser);
                if(state==1){
                    billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
                    billRecord1.setModifyUserId(createUserId);
                    billRecord1.setModifyUserName(createUserName);
                    billRecordService.update(billRecord1);
                }

            }

            allotMaster.setCreateUserId(createUserId);
            allotMaster.setCreateUserName(createUserName);
            allotMaster.setCreateTime(createTime);
            allotMaster.setBillNo(billNo);
            allotMaster.setCaseState(state);
            allotMaster.setInsertType(allotData.getInsertType());
            allotMasterService.save(allotMaster);

            List<AllotDetail> allotDetails = allotData.getAllotDetails();
            for(int i=0;i<allotDetails.size();i++){
                String detailNo = billNo + "-" + (i+1);
                AllotDetail allotDetail = allotDetails.get(i);
                allotDetail.setBillNo(billNo);
                allotDetail.setDetailNo(detailNo);
                allotDetailService.save(allotDetail);
            }
            BillRecord billRecord1 =billRecordService.findByBillNoAndType(billNo,38);//调拨新增
            billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord1.setModifyUserId(createUserId);
            billRecord1.setModifyUserName(createUserName);
            billRecordService.update(billRecord1);
    }

    @Override
    public void deleteByBillNo(String billNo) {
        allotMasterMapper.deleteByBillNo(billNo);
    }

    @Override
    public void allot(String billNo) {
        allotMasterMapper.allot(billNo);
    }

}
