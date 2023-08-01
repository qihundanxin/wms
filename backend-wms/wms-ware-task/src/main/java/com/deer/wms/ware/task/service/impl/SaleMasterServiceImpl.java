package com.deer.wms.ware.task.service.impl;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.system.manage.service.ValidatorService;
import com.deer.wms.ware.task.dao.SaleMasterMapper;
import com.deer.wms.ware.task.model.SO.SoData;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.model.Sale.SaleData;
import com.deer.wms.ware.task.model.Sale.SaleDetail;
import com.deer.wms.ware.task.model.Sale.SaleMaster;
import com.deer.wms.ware.task.model.Sale.SaleMasterCriteria;
import com.deer.wms.ware.task.service.SaleDetailService;
import com.deer.wms.ware.task.service.SaleMasterService;

import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.ware.task.service.SoMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ll on 2020/03/13.
 */
@Service
@Transactional
public class SaleMasterServiceImpl extends AbstractService<SaleMaster, Integer> implements SaleMasterService {

    @Autowired
    private SaleMasterMapper saleMasterMapper;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private SaleMasterService saleMasterService;

    @Autowired
    private SaleDetailService saleDetailService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private SoMasterService soMasterService;

    @Override
    public List<SaleMaster> findList(SaleMasterCriteria criteria) {
        return saleMasterMapper.findList(criteria);
    }

    @Override
    public void createSale(SaleData saleData, CurrentUser currentUser) {

        /**
         *  新建XS单据
         */
        SaleMaster saleMaster = saleData.getSaleMaster();
        String tableName = "sale_master";
        String nextId = validatorService.getNextIdString(tableName);

        String billNo = "XS" + "-" + nextId;
        Integer createUserId = currentUser.getUserId();
        String createUserName = currentUser.getUserName();
        String createTime = DateUtils.getNowDateTimeString();
        //添加ASN单跟踪信息
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(billNo);
        billRecord.setRecordType(43);//销售新增
        billRecord.setCreateTime(createTime);
        billRecord.setCreateUserId(createUserId);
        billRecord.setCreateUserName(createUserName);
        billRecordService.createBillRecord(billRecord, currentUser);

        //      状态    2-等待审核(初始化)  1-审核通过  0-审核中  -1-审核退回
        Integer state = 2;

        /**
         * 草稿
         */
        if (saleData.getInsertType() == -2) {
            state = 2;
            BillRecord billRecord1 = new BillRecord();
            billRecord1.setBillNo(billNo);
            billRecord1.setRecordType(44);//销售草稿
            billRecord1.setCreateTime(createTime);
            billRecord1.setCreateUserId(createUserId);
            billRecord1.setCreateUserName(createUserName);
            billRecordService.createBillRecord(billRecord1, currentUser);
        }
        /**
         * 提交
         */
        else if (saleData.getInsertType() == 0) {
            String billNo1 = saleData.getSaleMaster().getBillNo();
            //通过草稿提交，则需要先把草稿删掉
            if (billNo1 != null) {
                saleMasterService.deleteByBillNo(billNo1);
                BillRecord billRecord1 = billRecordService.findByBillNoAndType(billNo, 44);//销售草稿
                billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
                billRecord1.setModifyUserId(currentUser.getUserId());
                billRecord1.setModifyUserName(currentUser.getUserName());
                billRecordService.update(billRecord1);
            }
            /**
             * 添加审核流
             */
//        -1不通过  0-流转中 1-已通过
            state = caseMasterService.createCase(billNo, 8, currentUser.getUserId(), saleMaster.getWareId(), saleMaster.getOrganizationId());
            BillRecord billRecord1 = new BillRecord();
            billRecord1.setBillNo(billNo);
            billRecord1.setRecordType(45);//销售审核
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

        saleMaster.setCreateUserId(createUserId);
        saleMaster.setCreateUserName(createUserName);
        saleMaster.setCreateTime(createTime);
        saleMaster.setBillNo(billNo);
        saleMaster.setState(state);
        saleMaster.setInsertType(saleData.getInsertType());
        saleMaster.setBillSource(saleData.getBillSource());
        saleMasterService.save(saleMaster);

        List<SaleDetail> saleDetails = saleData.getSaleDetails();
        for (int i = 0; i < saleDetails.size(); i++) {
            String detailNo = billNo + "-" + (i + 1);
            SaleDetail saleDetail = saleDetails.get(i);
            saleDetail.setBillNo(billNo);
            saleDetail.setDetailNo(detailNo);
            saleDetailService.save(saleDetail);
        }
        BillRecord billRecord1 = billRecordService.findByBillNoAndType(billNo, 43);//销售新增
        billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
        billRecord1.setModifyUserId(createUserId);
        billRecord1.setModifyUserName(createUserName);
        billRecordService.update(billRecord1);

    }

    @Override
    public void createSo(SaleData saleData, CurrentUser currentUser) {
        //生成SO单
        Integer createUserId = currentUser.getUserId();
        String createUserName = currentUser.getUserName();
        String createTime = DateUtils.getNowDateTimeString();
        SoData soData = new SoData();
        soData.setBillSource(4);//销售生成
        soData.setInsertType(0);//提交
        BillRecord billRecord1 = new BillRecord();
        billRecord1.setBillNo(saleData.getSaleMaster().getBillNo());
        billRecord1.setRecordType(46);//生成SO
        billRecord1.setCreateTime(createTime);
        billRecord1.setCreateUserId(createUserId);
        billRecord1.setCreateUserName(createUserName);
        billRecordService.createBillRecord(billRecord1, currentUser);

        SoMaster soMaster = new SoMaster();
        BeanUtils.copyProperties(saleData.getSaleMaster(), soMaster);
        soMaster.setBillNo(null);
        soMaster.setAllotBillNo(saleData.getSaleMaster().getBillNo());
        soMaster.setWareId(saleData.getSaleMaster().getWareId());
        soMaster.setWareName(saleData.getSaleMaster().getWareName());
        soMaster.setSoType(30);
        soData.setSoMaster(soMaster);

        List<SoDetail> soDetails = new ArrayList<>();

        for (SaleDetail saleDetail : saleData.getSaleDetails()) {
            SoDetail soDetail = new SoDetail();
            BeanUtils.copyProperties(saleDetail, soDetail);
            soDetail.setAllotDetailNo(saleDetail.getDetailNo());
            soDetail.setOrderQuantity(saleDetail.getExpectQuantity());
            soDetail.setBillNo(null);
            soDetail.setDetailNo(null);
            soDetails.add(soDetail);
        }
        soData.setSoDetails(soDetails);
        soMasterService.createSoMaster(soData, currentUser);

    }

    @Override
    public void deleteByBillNo(String billNo) {
        saleMasterMapper.deleteByBillNo(billNo);
    }

}
