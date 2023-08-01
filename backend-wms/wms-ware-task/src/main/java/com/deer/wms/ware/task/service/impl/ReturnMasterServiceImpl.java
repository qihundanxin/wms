package com.deer.wms.ware.task.service.impl;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.system.manage.service.ValidatorService;
import com.deer.wms.ware.task.dao.ReturnMasterMapper;
import com.deer.wms.ware.task.model.Return.ReturnData;
import com.deer.wms.ware.task.model.Return.ReturnDetail;
import com.deer.wms.ware.task.model.Return.ReturnMaster;
import com.deer.wms.ware.task.service.ReturnDetailService;
import com.deer.wms.ware.task.service.ReturnMasterService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/03/13.
 */
@Service
@Transactional
public class ReturnMasterServiceImpl extends AbstractService<ReturnMaster, Integer> implements ReturnMasterService {

    @Autowired
    private ReturnMasterMapper returnMasterMapper;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private ReturnMasterService returnMasterService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private ReturnDetailService returnDetailService;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public void deleteByBillNo(String billNo) {
        returnMasterMapper.deleteByBillNo(billNo);
    }

    @Override
    public void create(ReturnData returnData, CurrentUser currentUser) {

        /**
         *  新建TH单据
         */
        ReturnMaster returnMaster = returnData.getReturnMaster();
        String tableName = "return_master";
        String nextId = validatorService.getNextIdString(tableName);

        String billNo = "TH" + "-" + nextId;
        Integer createUserId = currentUser.getUserId();
        String createUserName = currentUser.getUserName();
        String createTime = DateUtils.getNowDateTimeString();
        //添加ASN单跟踪信息
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(billNo);
        billRecord.setRecordType(47);//退货新增
        billRecord.setCreateTime(createTime);
        billRecord.setCreateUserId(createUserId);
        billRecord.setCreateUserName(createUserName);
        billRecordService.createBillRecord(billRecord, currentUser);

        //      状态    2-等待审核(初始化)  1-审核通过  0-审核中  -1-审核退回
        Integer state = 2;

        /**
         * 草稿
         */
        if (returnData.getInsertType() == -2) {
            state = 2;
            BillRecord billRecord1 = new BillRecord();
            billRecord1.setBillNo(billNo);
            billRecord1.setRecordType(48);//退货草稿
            billRecord1.setCreateTime(createTime);
            billRecord1.setCreateUserId(createUserId);
            billRecord1.setCreateUserName(createUserName);
            billRecordService.createBillRecord(billRecord1, currentUser);
        }
        /**
         * 提交
         */
        else if (returnData.getInsertType() == 0) {
            String billNo1 = returnData.getReturnMaster().getBillNo();
            //通过草稿提交，则需要先把草稿删掉
            if (billNo1 != null) {
                List<ReturnDetail> returnDetails = returnDetailService.findByBillNo(billNo1);
                for (ReturnDetail returnDetail : returnDetails) {
                    Inventory inventory = inventoryService.findById(returnDetail.getInventoryId());
                    inventory.setAllotQuantity(inventory.getAllotQuantity() - returnDetail.getReturnQuantity());
                    inventory.setPackDetailId(returnDetail.getPackDetailId());
                    inventory.setPackDescribe(returnDetail.getPackDescribe());
                    inventory.setTransRatio(returnDetail.getTransRatio());
                    // version
                    inventoryService.update(inventory);
                }
                returnMasterService.deleteByBillNo(billNo1);

                BillRecord billRecord1 = billRecordService.findByBillNoAndType(billNo, 48);//退货草稿
                billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
                billRecord1.setModifyUserId(currentUser.getUserId());
                billRecord1.setModifyUserName(currentUser.getUserName());
                billRecordService.update(billRecord1);
            }
            /**
             * 添加审核流
             */
//        -1不通过  0-流转中 1-已通过
            state = caseMasterService.createCase(billNo, 9, currentUser.getUserId(), returnMaster.getWareId(), returnMaster.getOrganizationId());
            BillRecord billRecord1 = new BillRecord();
            billRecord1.setBillNo(billNo);
            billRecord1.setRecordType(49);//退货审核
            billRecord1.setCreateTime(createTime);
            billRecord1.setCreateUserId(createUserId);
            billRecord1.setCreateUserName(createUserName);
            billRecordService.createBillRecord(billRecord1, currentUser);
            if (state == 1) {
                billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
                billRecord1.setModifyUserId(createUserId);
                billRecord1.setModifyUserName(createUserName);
                billRecordService.update(billRecord1);
            }
        }

        returnMaster.setCreateUserId(createUserId);
        returnMaster.setCreateUserName(createUserName);
        returnMaster.setCreateTime(createTime);
        returnMaster.setBillNo(billNo);
        returnMaster.setState(state);
        returnMaster.setInsertType(returnData.getInsertType());
        returnMaster.setBillSource(returnData.getBillSource());
        returnMasterService.save(returnMaster);

        List<ReturnDetail> returnDetails = returnData.getReturnDetails();
        for (int i = 0; i < returnDetails.size(); i++) {
            String detailNo = billNo + "-" + (i + 1);
            ReturnDetail returnDetail = returnDetails.get(i);
            returnDetail.setBillNo(billNo);
            returnDetail.setDetailNo(detailNo);
            returnDetailService.save(returnDetail);

            //更新库存的分配数量
            Inventory inventory = inventoryService.findById(returnDetail.getInventoryId());
            inventory.setAllotQuantity(inventory.getAllotQuantity() + returnDetail.getReturnQuantity());
            inventory.setPackDetailId(returnDetail.getPackDetailId());
            inventory.setPackDescribe(returnDetail.getPackDescribe());
            inventory.setTransRatio(returnDetail.getTransRatio());
            // version
            inventoryService.update(inventory);
        }
        BillRecord billRecord1 = billRecordService.findByBillNoAndType(billNo, 47);//退货新增
        billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
        billRecord1.setModifyUserId(createUserId);
        billRecord1.setModifyUserName(createUserName);
        billRecordService.update(billRecord1);

    }
}
