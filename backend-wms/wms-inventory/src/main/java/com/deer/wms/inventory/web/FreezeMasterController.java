package com.deer.wms.inventory.web;

import java.util.List;

import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.inventory.model.Inventory.InventoryTransactRunCriteria;
import com.deer.wms.project.root.util.Np;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.constant.InventoryConstant;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.freeze.FreezeDetail;
import com.deer.wms.inventory.model.freeze.FreezeDetailCriteria;
import com.deer.wms.inventory.model.freeze.FreezeDetailDto;
import com.deer.wms.inventory.model.freeze.FreezeMaster;
import com.deer.wms.inventory.model.freeze.FreezeMasterCriteria;
import com.deer.wms.inventory.model.freeze.FreezeMasterDto;
import com.deer.wms.inventory.model.freeze.InsertData;
import com.deer.wms.inventory.service.FreezeDetailService;
import com.deer.wms.inventory.service.FreezeMasterService;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.InventoryTransactService;
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
 * 冻结Master api接口
 * 
 * Created by guo on 2019/12/25.
 */
@RestController
@RequestMapping("/freeze/masters")
public class FreezeMasterController {

    @Autowired
    private FreezeMasterService freezeMasterService;

    @Autowired
    private FreezeDetailService freezeDetailService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private InventoryTransactService inventoryTransactService;

    @Autowired
    private CellInfoService cellInfoService;

    /**
     * 新增freezeMaster以及freezeDetail列表
     */
    @Transactional
    @PostMapping("/insert")
    @Authority("freeze_masters_insert")
    public Result add(@RequestBody InsertData insertData, @User CurrentUser currentUser) {
        //垃圾货位不可进行库存调整
        for(FreezeDetail detail:insertData.getFreezeDetails()){
            Integer cellUseType = cellInfoService.getCellType(detail.getCellCode(),insertData.getFreezeMaster().getWareId());
            if(Np.i(cellUseType).eq(-1)){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "包含垃圾货位，不可进行库存冻结");
            }
        }
        Integer state = 2;
        FreezeMaster freezeMaster = insertData.getFreezeMaster();
        String tableName = "freeze_master";
        String nextId = validatorService.getNextIdString(tableName);

        String billNo = "DJ" + "-" + nextId;
        Integer createUserId = currentUser.getUserId();
        String createUserName = currentUser.getUserName();
        String createTime = DateUtils.getNowDateTimeString();

        //添加冻结单跟踪信息
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(billNo);
        billRecord.setRecordType(29);//冻结新增
        billRecord.setCreateTime(createTime);
        billRecord.setCreateUserId(createUserId);
        billRecord.setCreateUserName(createUserName);
        billRecordService.save(billRecord);
        /**
         * 草稿
         */
        if (insertData.getInsertType() == -2) {
            state = 2;
            BillRecord billRecord1 = new BillRecord();
            billRecord1.setBillNo(billNo);
            billRecord1.setRecordType(30);//冻结单草稿
            billRecord1.setCreateTime(createTime);
            billRecord1.setCreateUserId(createUserId);
            billRecord1.setCreateUserName(createUserName);
            billRecordService.save(billRecord1);
        }
        /**
         * 提交
         */
        else if (insertData.getInsertType() == 0) {
            String billNo1 = insertData.getFreezeMaster().getBillNo();
            //通过草稿提交，则需要先把草稿删掉
            if (billNo1 != null) {
                freezeMasterService.deleteByBillNo(billNo1);
                BillRecord billRecord1 = billRecordService.findByBillNoAndType(billNo, 30);//冻结草稿
                billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
                billRecord1.setModifyUserId(currentUser.getUserId());
                billRecord1.setModifyUserName(currentUser.getUserName());
                billRecordService.update(billRecord1);
            }
            /**
             * 添加审核流
             */
//        -1不通过  0-流转中 1-已通过
            state = caseMasterService.createCase(billNo, 4, currentUser.getUserId(), freezeMaster.getWareId(), freezeMaster.getOrganizationId());
            BillRecord billRecord1 = new BillRecord();
            billRecord1.setBillNo(billNo);
            billRecord1.setRecordType(31);//冻结审核
            billRecord1.setCreateTime(createTime);
            billRecord1.setCreateUserId(createUserId);
            billRecord1.setCreateUserName(createUserName);
            billRecordService.save(billRecord1);

        }

        freezeMaster.setBillNo(billNo);
        freezeMaster.setCreateTime(createTime);
        freezeMaster.setCreateUseId(createUserId);
        freezeMaster.setCreateUseName(createUserName);
        freezeMaster.setState(state);
        freezeMaster.setInsertType(insertData.getInsertType());
        freezeMasterService.save(freezeMaster);

        /**
         * 新增freezeDetail列表
         * */

        List<FreezeDetail> freezeDetails = insertData.getFreezeDetails();
        for (int i = 0; i < freezeDetails.size(); i++) {
            String detailNo = billNo + "-" + (i + 1);
            FreezeDetail freezeDetail = freezeDetails.get(i);
            //垃圾货位-不可进行库存冻结操作
            Integer cellUseType = cellInfoService.getCellType(freezeDetail.getCellCode(),freezeMaster.getWareId());
            if(Np.i(cellUseType).eq(-1)){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "此货位不可进行基础移库操作");
            }
            freezeDetail.setDetailNo(detailNo);
            freezeDetail.setCreateTime(createTime);
            freezeDetail.setBillNo(billNo);
            freezeDetail.setCreateUseId(currentUser.getUserId());
            freezeDetail.setCreateUseName(currentUser.getUserName());

            Inventory inventory = inventoryService.findById(freezeDetail.getInventoryId());
            if (freezeDetail.getFreezeQuantity() <= (inventory.getQuantity() - inventory.getFreezeQuantity())) {
                freezeDetail.setFreezeQuantity(freezeDetail.getFreezeQuantity());
            } else {
                throw new ServiceException(CommonCode.FREEZE_ERROR, "冻结数量大于库存余量");
            }
            if (insertData.getInsertType() == -2) {
                freezeDetail.setState(0);
            } else if (insertData.getInsertType() == 0) {
                freezeDetail.setState(1);
            }
            freezeDetailService.save(freezeDetail);
            if (state == 1) {
                inventory.setFreezeQuantity(inventory.getFreezeQuantity()+freezeDetail.getFreezeQuantity());
                inventoryService.update(inventory);
                inventory.setQuantity(freezeDetail.getFreezeQuantity());
                InventoryTransactRunCriteria runCriteria = new InventoryTransactRunCriteria();
                runCriteria.setToInventory(inventory);
                runCriteria.setTransactType(InventoryConstant.INVENTORY_FREEZE);
                runCriteria.setDetailNo(freezeDetail.getDetailNo());
                runCriteria.setCreateUserId(currentUser.getUserId());
                runCriteria.setCreateUserName(currentUser.getUserName());
                inventoryTransactService.run(runCriteria);
            }
        }


        return ResultGenerator.genSuccessResult();
    }

    /**
     * 单条释放
     */
    @GetMapping("/release")
    @Authority("freeze_masters_release")
    public Result release(Integer freezeDetailId, @User CurrentUser currentUser) {
        FreezeDetail freezeDetail = freezeDetailService.findById(freezeDetailId);
        String billNo = freezeDetail.getBillNo();
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(billNo);
        billRecord.setRecordType(34);//单条释放
        billRecord.setCreateTime(DateUtils.getNowDateTimeString());
        billRecord.setCreateUserId(currentUser.getUserId());
        billRecord.setCreateUserName(currentUser.getUserName());
        billRecordService.createBillRecord(billRecord, currentUser);

        Double quantity = freezeDetail.getFreezeQuantity();
        Inventory inventory = inventoryService.findById(freezeDetail.getInventoryId());
        freezeDetail.setState(2);
        freezeDetail.setFreezeQuantity(freezeDetail.getFreezeQuantity() - quantity);
        freezeDetailService.update(freezeDetail);
        inventory.setFreezeQuantity(inventory.getFreezeQuantity()-quantity);
        inventoryService.update(inventory);

        inventory.setQuantity(quantity);
        InventoryTransactRunCriteria runCriteria = new InventoryTransactRunCriteria();
        runCriteria.setToInventory(inventory);
        runCriteria.setTransactType(InventoryConstant.INVENTORY_FREEZE);
        runCriteria.setDetailNo(freezeDetail.getDetailNo());
        runCriteria.setCreateUserId(currentUser.getUserId());
        runCriteria.setCreateUserName( currentUser.getUserName());
        inventoryTransactService.run(runCriteria);

        FreezeDetailCriteria criteria = new FreezeDetailCriteria();
        criteria.setBillNo(freezeDetail.getBillNo());
        List<FreezeDetailDto> list = freezeDetailService.findList(criteria);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getState() != 2) {
                return ResultGenerator.genSuccessResult();
            }
        }
        FreezeMaster freezeMaster = freezeMasterService.findBy("billNo", billNo);
        freezeMaster.setState(2);
        freezeMasterService.update(freezeMaster);
        BillRecord billRecord1 = billRecordService.findByBillNoAndType(billNo, 35);//单条释放
        billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
        billRecord1.setModifyUserId(currentUser.getUserId());
        billRecord1.setModifyUserName(currentUser.getUserName());
        billRecordService.update(billRecord1);

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 整个释放
     */
    @GetMapping("/releaseAll")
    @Authority("freeze_masters_releaseAll")
    public Result releaseAll(String billNo, @User CurrentUser currentUser) {


        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(billNo);
        billRecord.setRecordType(35);//整个释放
        billRecord.setCreateTime(DateUtils.getNowDateTimeString());
        billRecord.setCreateUserId(currentUser.getUserId());
        billRecord.setCreateUserName(currentUser.getUserName());
        billRecordService.createBillRecord(billRecord, currentUser);

        FreezeDetailCriteria criteria = new FreezeDetailCriteria();
        criteria.setBillNo(billNo);
        List<FreezeDetailDto> freezeDetailDtos = freezeDetailService.findList(criteria);
        for (FreezeDetail freezeDetail : freezeDetailDtos) {
            Double quantity = freezeDetail.getFreezeQuantity();
            Inventory inventory = inventoryService.findById(freezeDetail.getInventoryId());
            inventory.setFreezeQuantity(inventory.getFreezeQuantity() - quantity);
            inventoryService.update(inventory);

            freezeDetail.setState(2);
            freezeDetail.setFreezeQuantity(freezeDetail.getFreezeQuantity() - quantity);
            freezeDetailService.update(freezeDetail);
        }
        FreezeMaster freezeMaster = freezeMasterService.findBy("billNo", billNo);
        freezeMaster.setState(2);
        freezeMasterService.update(freezeMaster);
        BillRecord billRecord1 = billRecordService.findByBillNoAndType(billNo, 35);//整个释放
        billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
        billRecord1.setModifyUserId(currentUser.getUserId());
        billRecord1.setModifyUserName(currentUser.getUserName());
        billRecordService.update(billRecord1);

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 审核
     */
    @GetMapping("/review")
    @Authority("freeze_masters_review")
    public Result review(String billNo, Integer reviewResult, Integer caseDetailId, String memo, @User CurrentUser currentUser) {
        FreezeMaster freezeMaster = freezeMasterService.findBy("billNo", billNo);
        //-1不通过   0-流转中  1-通过  2-初始化
        Integer lastReviewResult = caseMasterService.review(caseDetailId, currentUser.getUserId(), reviewResult, memo);

        if (lastReviewResult == -1) {
            freezeMaster.setState(-1);
            freezeMasterService.update(freezeMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 31);//冻结审核
            billRecord.setResult("退回");
            billRecord.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord.setModifyUserId(currentUser.getUserId());
            billRecord.setModifyUserName(currentUser.getUserName());
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 0) {
            freezeMaster.setState(0);
            freezeMasterService.update(freezeMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 31);//冻结审核
            billRecord.setResult("流转");
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 2) {
            freezeMaster.setState(2);
            freezeMasterService.update(freezeMaster);
        } else if (lastReviewResult == 1) {
            freezeMaster.setState(1);
            freezeMasterService.update(freezeMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 31);//冻结审核
            billRecord.setResult("通过");
            billRecord.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord.setModifyUserId(currentUser.getUserId());
            billRecord.setModifyUserName(currentUser.getUserName());
            billRecordService.update(billRecord);
        }
        if (freezeMaster.getState() == 1) {
            FreezeDetailCriteria criteria = new FreezeDetailCriteria();
            criteria.setBillNo(billNo);
            List<FreezeDetailDto> freezeDetails = freezeDetailService.findList(criteria);
            for (FreezeDetail freezeDetail : freezeDetails) {
                Inventory inventory = inventoryService.findById(freezeDetail.getInventoryId());
                inventory.setFreezeQuantity(freezeDetail.getFreezeQuantity());
                if (freezeDetail.getFreezeQuantity().equals(inventory.getQuantity())) {
                    inventory.setState(2); //全部冻结
                } else {
                    inventory.setState(1);  //部分冻结
                }
                inventoryService.update(inventory);
            }

        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 重新发起审核
     */
    @GetMapping("/reReview")
    @Authority("freeze_masters_reReview")
    public Result reReview(String billNo, @User CurrentUser currentUser) {
        FreezeMaster freezeMaster = freezeMasterService.findBy("billNo", billNo);

        //初始化状态 2
        freezeMaster.setState(2);
        freezeMasterService.update(freezeMaster);
        caseMasterService.createCase(billNo, 4, currentUser.getUserId(), freezeMaster.getWareId(), freezeMaster.getOrganizationId());

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除库存冻结单
     * @param id
     * @return
     */
    @GetMapping("/delete")
    @Authority("freeze_masters_delete")
    public Result delete(Integer id,@User CurrentUser currentUser) {
        freezeMasterService.deleteByIdAndReleaseDetail(id,currentUser);
        return ResultGenerator.genSuccessResult();
    }

    @Transactional
    @PostMapping("/update")
    @Authority("freeze_masters_update")
    public Result update(@RequestBody InsertData insertData, @User CurrentUser currentUser) {
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(insertData.getFreezeMaster().getBillNo());
        billRecord.setRecordType(33);//冻结修改
        billRecord.setCreateTime(DateUtils.getNowDateTimeString());
        billRecord.setCreateUserId(currentUser.getUserId());
        billRecord.setCreateUserName(currentUser.getUserName());
        billRecordService.createBillRecord(billRecord, currentUser);

        freezeDetailService.deleteByBillNo(insertData.getFreezeMaster().getBillNo());
        List<FreezeDetail> freezeDetails = insertData.getFreezeDetails();
        for (int i = 0; i < freezeDetails.size(); i++) {
            FreezeDetail freezeDetail = freezeDetails.get(i);
            String detailNo = insertData.getFreezeMaster().getBillNo() + "-" + (i + 1);
            freezeDetail.setDetailNo(detailNo);
            freezeDetail.setCreateTime(insertData.getFreezeMaster().getCreateTime());
            freezeDetail.setBillNo(insertData.getFreezeMaster().getBillNo());
            freezeDetail.setDetailNo(detailNo);
            freezeDetail.setCreateUseId(insertData.getFreezeMaster().getCreateUseId());
            freezeDetail.setCreateUseName(insertData.getFreezeMaster().getCreateUseName());
            Inventory inventory = inventoryService.findById(freezeDetail.getInventoryId());
            if (freezeDetail.getFreezeQuantity() < (inventory.getQuantity() - inventory.getFreezeQuantity())) {
                freezeDetail.setFreezeQuantity(freezeDetail.getFreezeQuantity());
            } else {
                throw new ServiceException(CommonCode.FREEZE_ERROR, "冻结数量大于库存余量");
            }

            if (insertData.getInsertType() == -2) {
                freezeDetail.setState(0);
            } else if (insertData.getInsertType() == 0) {
                freezeDetail.setState(1);
            }
            freezeDetailService.save(freezeDetail);
        }
        BillRecord billRecord1 = billRecordService.findByBillNoAndType(insertData.getFreezeMaster().getBillNo(), 33);//冻结修改;
        billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
        billRecord1.setModifyUserId(currentUser.getUserId());
        billRecord1.setModifyUserName(currentUser.getUserName());
        billRecordService.update(billRecord1);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("freeze_masters_detail")
    public Result detail(@PathVariable Integer id) {
        FreezeMaster freezeMaster = freezeMasterService.findById(id);
        return ResultGenerator.genSuccessResult(freezeMaster);
    }

    @GetMapping("/list")
    @Authority("freeze_masters_list")
    public Result list(FreezeMasterCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<FreezeMasterDto> list = freezeMasterService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
