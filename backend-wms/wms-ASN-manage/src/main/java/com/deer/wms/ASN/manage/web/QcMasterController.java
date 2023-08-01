package com.deer.wms.ASN.manage.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.ASN.manage.model.asn.AsnMaster;
import com.deer.wms.ASN.manage.model.file.FileData;
import com.deer.wms.ASN.manage.model.in.AcceptRecord;
import com.deer.wms.ASN.manage.model.in.AcceptRecordCriteria;
import com.deer.wms.ASN.manage.model.qc.QcData;
import com.deer.wms.ASN.manage.model.qc.QcDatas;
import com.deer.wms.ASN.manage.model.qc.QcDetail;
import com.deer.wms.ASN.manage.model.qc.QcInsert;
import com.deer.wms.ASN.manage.model.qc.QcMaster;
import com.deer.wms.ASN.manage.model.qc.QcMasterCriteria;
import com.deer.wms.ASN.manage.service.AcceptRecordService;
import com.deer.wms.ASN.manage.service.AsnMasterService;
import com.deer.wms.ASN.manage.service.QcDetailService;
import com.deer.wms.ASN.manage.service.QcMasterService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryCriteria;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.system.manage.service.ValidatorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * QC信息api接口
 * 
 * Created by guo on 2020/01/15.
 */
@RestController
@RequestMapping("/qc/masters")
public class QcMasterController {

    @Autowired
    private QcMasterService qcMasterService;

    @Autowired
    private QcDetailService qcDetailService;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private AsnMasterService asnMasterService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private AcceptRecordService acceptRecordService;

    @PostMapping
    @Authority("qc_masters_add")
    public Result add(@RequestBody QcMaster qcMaster) {
        qcMasterService.save(qcMaster);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/insert")
    @Authority("qc_masters_insert")
    public Result insert(@RequestBody QcInsert qcInsert, @User CurrentUser currentUser) {


        QcMaster qcMaster = qcInsert.getQcMaster();
        String tableName = "qc_master";
        String nextId = validatorService.getNextIdString(tableName);

        String billNo = "QC" + "-" + nextId;
        Integer createUserId = currentUser.getUserId();
        String createUserName = currentUser.getUserName();
        String createTime = DateUtils.getNowDateTimeString();

        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(billNo);
        billRecord.setRecordType(15);//质检新增
        billRecord.setCreateTime(createTime);
        billRecord.setCreateUserId(createUserId);
        billRecord.setCreateUserName(createUserName);
        billRecordService.save(billRecord);
        //质检 状态
        Integer state = 2;
        /**
         * 草稿
         */
        if (qcInsert.getInsertType() == -2) {
            state = 2;
            BillRecord billRecord1 = new BillRecord();
            billRecord1.setBillNo(billNo);
            billRecord1.setRecordType(16);//QC草稿
            billRecord1.setCreateTime(createTime);
            billRecord1.setCreateUserId(createUserId);
            billRecord1.setCreateUserName(createUserName);
            billRecordService.save(billRecord1);

        }
        /**
         * 提交
         */
        else if (qcInsert.getInsertType() == 0) {
            String billNo1 = qcInsert.getQcMaster().getBillNo();
            //通过草稿提交，则需要先把草稿删掉
            if (billNo1 != null) {
                qcMasterService.deleteByBillNo(billNo1);
                BillRecord billRecord1 = billRecordService.findByBillNoAndType(billNo, 18);//QC草稿删除
                billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
                billRecord1.setModifyUserId(currentUser.getUserId());
                billRecord1.setModifyUserName(currentUser.getUserName());
                billRecordService.update(billRecord1);
            }
            /**
             * 添加审核流
             */
//        -1不通过  0-流转中 1-已通过
            state = caseMasterService.createCase(billNo, 2, currentUser.getUserId(), qcMaster.getWareId(), qcMaster.getOrganizationId());
            BillRecord billRecord1 = new BillRecord();
            billRecord1.setBillNo(billNo);
            billRecord1.setRecordType(17);//QC审核
            billRecord1.setCreateTime(createTime);
            billRecord1.setCreateUserId(createUserId);
            billRecord1.setCreateUserName(createUserName);
            billRecordService.save(billRecord1);
            if (state == 1) {
                billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
                billRecord1.setModifyUserId(createUserId);
                billRecord1.setModifyUserName(createUserName);
                billRecordService.update(billRecord1);
            }
        }
        if (qcInsert.getBillSource() == 0) {

            qcMaster.setBillNo(billNo);
            qcMaster.setCreateUserId(createUserId);
            qcMaster.setCreateUserName(createUserName);
            qcMaster.setCreateTime(createTime);
            qcMaster.setState(state);
            qcMaster.setQcState(0);//0-待质检 1-合格  2-不合格
            qcMaster.setInsertType(qcInsert.getInsertType());
            qcMaster.setBillSource(qcInsert.getBillSource());
            qcMasterService.save(qcMaster);

            List<QcDetail> qcDetails = qcInsert.getQcDetails();
            for (int i = 1; i <= qcDetails.size(); i++) {
                QcDetail qcDetail = qcDetails.get(i - 1);
                qcDetail.setBillNo(qcMaster.getBillNo());
                qcDetail.setDetailNo(qcMaster.getBillNo() + "-" + i);
                qcDetail.setBillSource(qcMaster.getBillSource());
                qcDetailService.save(qcDetail);

            }

            AsnMaster asnMaster = asnMasterService.findBy("billNo", qcMaster.getAsnBillNo());
            asnMaster.setQcState(0); //已生成质检单  状态是待质检
            BillRecord billRecord1 = new BillRecord();
            billRecord1.setBillNo(asnMaster.getBillNo());
            billRecord1.setRecordType(21);//ASN生成质检
            billRecord1.setCreateTime(createTime);
            billRecord1.setCreateUserId(createUserId);
            billRecord1.setCreateUserName(createUserName);
            billRecordService.save(billRecord1);
            asnMasterService.update(asnMaster);

        }
        if (qcInsert.getBillSource() == 1) {

            qcMaster.setBillNo(billNo);
            qcMaster.setCreateUserId(createUserId);
            qcMaster.setCreateUserName(createUserName);
            qcMaster.setCreateTime(createTime);
            qcMaster.setState(state);
            qcMaster.setQcState(0);//0-待质检 1-合格  2-不合格
            qcMaster.setInsertType(qcInsert.getInsertType());
            qcMaster.setBillSource(qcInsert.getBillSource());
            qcMasterService.save(qcMaster);

            List<QcDetail> qcDetails = qcInsert.getQcDetails();
            for (int i = 1; i <= qcDetails.size(); i++) {
                QcDetail qcDetail = qcDetails.get(i - 1);
                qcDetail.setBillNo(qcMaster.getBillNo());
                qcDetail.setDetailNo(qcMaster.getBillNo() + "-" + i);
                qcDetail.setBillSource(qcMaster.getBillSource());
                qcDetail.setQcQuantity(qcDetail.getExpectQuantity());
                qcDetailService.save(qcDetail);

            }
        }

        BillRecord billRecord1 = billRecordService.findByBillNoAndType(billNo, 15);//质检新增
        billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
        billRecord1.setModifyUserId(createUserId);
        billRecord1.setModifyUserName(createUserName);
        billRecordService.update(billRecord1);

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("qc_masters_delete")
    public Result delete(Integer id) {
        qcMasterService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("qc_masters_update")
    public Result update(@RequestBody QcMaster qcMaster) {
        qcMasterService.update(qcMaster);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("qc_masters_detail")
    public Result detail(@PathVariable Integer id) {
        QcMaster qcMaster = qcMasterService.findById(id);
        return ResultGenerator.genSuccessResult(qcMaster);
    }

    /**
     * 审核
     */
    @GetMapping("/review")
    @Authority("qc_masters_review")
    public Result review(String billNo, Integer reviewResult, Integer caseDetailId, String memo, @User CurrentUser currentUser) {
        QcMaster qcMaster = qcMasterService.findBy("billNo", billNo);
        //-1不通过   0-流转中  1-通过  2-初始化
        Integer lastReviewResult = caseMasterService.review(caseDetailId, currentUser.getUserId(), reviewResult, memo);

        if (lastReviewResult == -1) {
            qcMaster.setState(-1);
            qcMasterService.update(qcMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 17);//QC审核
            billRecord.setResult("退回");
            billRecord.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord.setModifyUserId(currentUser.getUserId());
            billRecord.setModifyUserName(currentUser.getUserName());
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 0) {
            qcMaster.setState(0);
            qcMasterService.update(qcMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 17);//QC审核
            billRecord.setResult("流转");
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 2) {
            qcMaster.setState(2);
            qcMasterService.update(qcMaster);
        } else if (lastReviewResult == 1) {
            qcMaster.setState(1);
            qcMasterService.update(qcMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 17);//QC审核
            billRecord.setResult("通过");
            billRecord.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord.setModifyUserId(currentUser.getUserId());
            billRecord.setModifyUserName(currentUser.getUserName());
            billRecordService.update(billRecord);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 重新发起审核
     */
    @GetMapping("/reReview")
    @Authority("qc_masters_reReview")
    public Result reReview(String billNo, @User CurrentUser currentUser) {

        AsnMaster asnMaster = asnMasterService.findBy("billNo", billNo);

        //初始化状态 2
        asnMaster.setState(2);
        asnMasterService.update(asnMaster);
        caseMasterService.createCase(billNo, 2, currentUser.getUserId(), asnMaster.getWareId(), asnMaster.getOrganizationId());

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/list")
    @Authority("qc_masters_list")
    public Result list(QcMasterCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<QcMaster> list = qcMasterService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 归档QC信息 传billNo
     */
    @PostMapping("/file")
    @Authority("qc_masters_file")
    public Result file(@RequestBody FileData fileData, @User CurrentUser currentUser) {

        for (int i = 0; i < fileData.getBillNos().size(); i++) {
            String billNo = fileData.getBillNos().get(i);
            qcMasterService.createFile(billNo, currentUser);
        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 质检
     */
    @PostMapping("/qc")
    @Authority("qc_masters_qc")
    public Result qc(@RequestBody QcData qcData, @User CurrentUser currentUser) {


        Integer userId = currentUser.getUserId();
        String userName = currentUser.getUserName();
        String acceptTime = DateUtils.getNowDateTimeString();

        qcData.setQcUserId(userId);
        qcData.setQcUserName(userName);
        qcData.setQcTime(acceptTime);

        qcMasterService.createQc(qcData, currentUser);

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 批量质检
     */
    @PostMapping("/qcList")
    @Authority("qc_masters_qcList")
    public Result qcList(@RequestBody QcDatas qcDatas, @User CurrentUser currentUser) {

        Integer userId = currentUser.getUserId();
        String userName = currentUser.getUserName();
        String acceptTime = DateUtils.getNowDateTimeString();

        for (int i = 0; i < qcDatas.getQcData().size(); i++) {
            QcData qcData = qcDatas.getQcData().get(i);
            qcData.setQcUserId(userId);
            qcData.setQcUserName(userName);
            qcData.setQcTime(acceptTime);
            qcMasterService.createQc(qcData, currentUser);

        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 获取不同批次的库存
     */
    @GetMapping("/getInventory")
    @Authority("qc_masters_getInventory")
    public Result getInventory(String detailNo) {
        QcDetail qcDetail = qcDetailService.findBy("detailNo", detailNo);
        List<Inventory> inventories = new ArrayList<>();
        if (qcDetail.getAsnDetailNo() != null) {
            AcceptRecordCriteria criteria = new AcceptRecordCriteria();
            criteria.setAsnDetailNo(qcDetail.getAsnDetailNo());
            List<AcceptRecord> acceptRecords = acceptRecordService.findList(criteria);
            if (acceptRecords == null) {
                throw new ServiceException(CommonCode.INVENTORY_ERROR);
            }
            for (AcceptRecord acceptRecord : acceptRecords) {
                Integer batchId = acceptRecord.getBatchId();
                Inventory inventory = inventoryService.findBatchId(batchId);
                inventories.add(inventory);
            }
        }
        if (qcDetail.getAsnDetailNo() == null) {
            QcMaster qcMaster = qcMasterService.findBy("billNo", qcDetail.getBillNo());
            InventoryCriteria criteria = new InventoryCriteria();
            criteria.setWareId(qcMaster.getWareId());
            criteria.setOrganizationId(qcMaster.getOrganizationId());
            criteria.setItemCode(qcDetail.getItemCode());
            List<Inventory> inventorys = inventoryService.findBatchInventory(criteria);
            for (Inventory inventory : inventorys) {
                inventories.add(inventory);
            }
        }

        return ResultGenerator.genSuccessResult(inventories);
    }

}
