package com.deer.wms.inventory.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.constant.InventoryConstant;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryTransact;
import com.deer.wms.inventory.model.move.InventoryMove;
import com.deer.wms.inventory.model.move.InventoryMoveCriteria;
import com.deer.wms.inventory.model.move.InventoryMoveDetail;
import com.deer.wms.inventory.model.move.InventoryMoveDto;
import com.deer.wms.inventory.model.move.MoveInventory;
import com.deer.wms.inventory.service.InventoryMoveDetailService;
import com.deer.wms.inventory.service.InventoryMoveService;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.InventoryTransactService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.system.manage.service.ValidatorService;

/**
 * 库存转移master api接口
 * 
 * Created by guo on 2019/12/27.
 */
@RestController
@RequestMapping("/inventory/moves")
public class InventoryMoveController {

    @Autowired
    private InventoryMoveService inventoryMoveService;

    @Autowired
    private InventoryMoveDetailService inventoryMoveDetailService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryTransactService inventoryTransactService;

    @Autowired
    private ValidatorService validatorService;

    /**
     * 移库
     */
    @PostMapping
    @Authority("inventory_moves_add")
    public Result add(@RequestBody MoveInventory moveInventory, @User CurrentUser currentUser) {

        /**
         *  新建移库单据
         */
        InventoryMove inventoryMove = moveInventory.getInventoryMove();
        String tableName = "inventory_move";
        String nextId = validatorService.getNextIdString(tableName);

        String billNo = "YD" + "-" + nextId;
        Integer createUserId = currentUser.getUserId();
        String createUserName = currentUser.getUserName();
        String createTime = DateUtils.getNowDateTimeString();
        Integer state = 2;
        /**
         * 草稿
         */
        if (moveInventory.getInsertType() == -2) {
            state = 2;
        }
        /**
         * 提交
         */
        else if (moveInventory.getInsertType() == 0) {
            String billNo1 = moveInventory.getInventoryMove().getBillNo();
            //通过草稿提交，则需要先把草稿删掉
            if (billNo1 != null) {
                inventoryMoveService.deleteByBillNo(billNo1);
            }
            //创建审核流
            state = caseMasterService.createCase(billNo, 6, currentUser.getUserId(), moveInventory.getInventoryMove().getWareId(), moveInventory.getInventoryMove().getOrganizationId());
        }
        inventoryMove.setBillNo(billNo);
        inventoryMove.setCreateTime(createTime);
        inventoryMove.setCreateUserId(createUserId);
        inventoryMove.setCreateUserName(createUserName);
        inventoryMove.setState(state);
        inventoryMove.setInsertType(moveInventory.getInsertType());
        inventoryMoveService.save(inventoryMove);

        /**
         * 新增inventoryMoveDetail列表
         * */
        List<InventoryMoveDetail> inventoryMoveDetails = moveInventory.getInventoryMoveDetails();
        for (int i = 0; i < inventoryMoveDetails.size(); i++) {
            InventoryMoveDetail inventoryMoveDetail = inventoryMoveDetails.get(i);
            InventoryTransact inventoryTransact = new InventoryTransact();
            inventoryTransact.setTransactType(InventoryConstant.TRANSACT_MOVE);
            BeanUtils.copyProperties(inventoryMoveDetail, inventoryTransact);
            inventoryTransactService.run(inventoryTransact);

            Inventory inventory = inventoryService.findById(inventoryMoveDetail.getInventoryId());
            String detailNo = billNo + "-" + (i + 1);
            inventoryMoveDetail.setDetailNo(detailNo);
            inventoryMoveDetail.setBillNo(inventoryMove.getBillNo());
            inventoryMoveDetail.setDetailNo(detailNo);
            BeanUtils.copyProperties(inventoryMoveDetail, inventoryMoveDetail);

            if (inventoryMoveDetail.getToQuantity() <= (inventory.getQuantity() - inventory.getFreezeQuantity())) {
                inventoryMoveDetail.setToQuantity(inventoryMoveDetail.getToQuantity());
            } else {
                throw new ServiceException(CommonCode.MOVE_ERROR, "转移数量大于库存余量");
            }

            if (moveInventory.getInsertType() == -2) {
                inventoryMoveDetail.setState(0);
            } else if (moveInventory.getInsertType() == 0) {
                inventoryMoveDetail.setState(1);
            }
            inventoryMoveDetailService.save(inventoryMoveDetail);
        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 审核
     */
    @GetMapping("/review")
    @Authority("inventory_moves_review")
    public Result review(String billNo, Integer reviewResult, Integer caseDetailId, String memo, @User CurrentUser currentUser) {
        InventoryMove inventoryMove = inventoryMoveService.findBy("billNo", billNo);
        //-1不通过   0-流转中  1-通过  2-初始化
        Integer lastReviewResult = caseMasterService.review(caseDetailId, currentUser.getUserId(), reviewResult, memo);

        if (lastReviewResult == -1) {
            inventoryMove.setState(-1);
            inventoryMoveService.update(inventoryMove);
        } else if (lastReviewResult == 0) {
            inventoryMove.setState(0);
            inventoryMoveService.update(inventoryMove);
        } else if (lastReviewResult == 2) {
            inventoryMove.setState(2);
            inventoryMoveService.update(inventoryMove);
        } else if (lastReviewResult == 1) {
            inventoryMove.setState(1);
            inventoryMoveService.update(inventoryMove);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 重新发起审核
     */
    @GetMapping("/reReview")
    @Authority("inventory_moves_reReview")
    public Result reReview(String billNo, @User CurrentUser currentUser) {
        InventoryMove inventoryMove = inventoryMoveService.findBy("billNo", billNo);

        //初始化状态 2
        inventoryMove.setState(2);
        inventoryMoveService.update(inventoryMove);
        caseMasterService.createCase(billNo, 6, currentUser.getUserId(), inventoryMove.getWareId(), inventoryMove.getOrganizationId());

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("inventory_moves_delete")
    public Result delete(Integer id) {
        inventoryMoveService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 增删detail
     */
    @PostMapping("/update")
    @Authority("inventory_moves_update")
    public Result update(@RequestBody MoveInventory moveInventory, @User CurrentUser currentUser) {
        inventoryMoveDetailService.deleteByBillNo(moveInventory.getInventoryMove().getBillNo());
        List<InventoryMoveDetail> inventoryMoveDetails = moveInventory.getInventoryMoveDetails();
        for (int i = 0; i < inventoryMoveDetails.size(); i++) {
            InventoryMoveDetail inventoryMoveDetail = inventoryMoveDetails.get(i);
            String detailNo = moveInventory.getInventoryMove().getBillNo() + "-" + (i + 1);
            inventoryMoveDetail.setDetailNo(detailNo);
            inventoryMoveDetail.setBillNo(moveInventory.getInventoryMove().getBillNo());
            inventoryMoveDetail.setDetailNo(detailNo);
            Inventory inventory = inventoryService.findById(inventoryMoveDetail.getInventoryId());
            if (inventoryMoveDetail.getToQuantity() <= (inventory.getQuantity() - inventory.getFreezeQuantity())) {
                inventoryMoveDetail.setToQuantity(inventoryMoveDetail.getToQuantity());
            } else {
                throw new ServiceException(CommonCode.MOVE_ERROR, "转移数量大于库存余量");
            }

            if (moveInventory.getInsertType() == -2) {
                inventoryMoveDetail.setState(0);
            } else if (moveInventory.getInsertType() == 0) {
                inventoryMoveDetail.setState(1);
            }
            inventoryMoveDetailService.save(inventoryMoveDetail);
        }
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("inventory_moves_detail")
    public Result detail(Integer id) {
        InventoryMove inventoryMove = inventoryMoveService.findById(id);
        return ResultGenerator.genSuccessResult(inventoryMove);
    }

    @GetMapping("/list")
    @Authority("inventory_moves_list")
    public Result list(InventoryMoveCriteria criteria) {
        List<InventoryMoveDto> list = inventoryMoveService.findList(criteria);
        return ResultGenerator.genSuccessResult(list);
    }

}
