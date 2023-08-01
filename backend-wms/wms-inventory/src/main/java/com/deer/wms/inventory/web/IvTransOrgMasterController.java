package com.deer.wms.inventory.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.inventory.model.Inventory.InventoryTransactRunCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.constant.InventoryConstant;
import com.deer.wms.inventory.model.IvTransOrgDetail;
import com.deer.wms.inventory.model.IvTransOrgDetailCriteria;
import com.deer.wms.inventory.model.IvTransOrgInsert;
import com.deer.wms.inventory.model.IvTransOrgMaster;
import com.deer.wms.inventory.model.IvTransOrgMasterCriteria;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.InventoryTransactService;
import com.deer.wms.inventory.service.IvTransOrgDetailService;
import com.deer.wms.inventory.service.IvTransOrgMasterService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.BillNoUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by ll on 2020/03/13.
 *
 * 货权转移
 */
@RestController
@RequestMapping("/iv/trans/org/masters")
public class IvTransOrgMasterController {

    @Autowired
    private IvTransOrgMasterService ivTransOrgMasterService;
    @Autowired
    private IvTransOrgDetailService ivTransOrgDetailService;
    @Autowired
    private CaseMasterService caseMasterService;
    @Autowired
    private InventoryTransactService inventoryTransactService;
    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    @Authority("iv_trans_org_masters_add")
    public Result add(@RequestBody IvTransOrgMaster ivTransOrgMaster) {
        ivTransOrgMasterService.save(ivTransOrgMaster);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    @Authority("iv_trans_org_masters_delete")
    public Result delete(@PathVariable Integer id) {
        ivTransOrgMasterService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    @Authority("iv_trans_org_masters_update")
    public Result update(@RequestBody IvTransOrgMaster ivTransOrgMaster) {
        ivTransOrgMasterService.update(ivTransOrgMaster);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("iv_trans_org_masters_get")
    public Result detail(@PathVariable Integer id) {
        IvTransOrgMaster ivTransOrgMaster = ivTransOrgMasterService.findById(id);
        return ResultGenerator.genSuccessResult(ivTransOrgMaster);
    }

    @PostMapping("/list")
    @Authority("iv_trans_org_masters_list")
    public Result list(@RequestBody IvTransOrgMasterCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<IvTransOrgMaster> list = ivTransOrgMasterService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Transactional
    @PostMapping("/insert")
    @Authority("iv_trans_org_masters_insert")
    public Result insert(@RequestBody IvTransOrgInsert insert, @User CurrentUser user) {

        String code = BillNoUtils.generateIvTransOrgNo();
        IvTransOrgMaster master = insert.getIvTransOrgMaster();
        master.setUserId(user.getUserId());
        master.setUserName(user.getUserName());
        master.setCreateTime(DateUtils.getNowDateTimeString());
        master.setCode(code);
        // 添加审核流
        Integer state = caseMasterService.createCase(code, 10, user.getUserId(), master.getWareId(), master.getFromOrgId());
        master.setCaseState(state);
        ivTransOrgMasterService.save(master);

        for (IvTransOrgDetail item : insert.getIvTransOrgDetails()) {
            item.setMasterId(master.getId());
            ivTransOrgDetailService.save(item);
        }

        return ResultGenerator.genSuccessResult();
    }

    @Transactional
    @GetMapping("/review")
    @Authority("iv_trans_org_masters_review")
    public Result review(String billNo, Integer reviewResult, Integer caseDetailId, String memo, @User CurrentUser currentUser) {
        IvTransOrgMaster master = ivTransOrgMasterService.findBy("code", billNo);
        Integer result = caseMasterService.review(caseDetailId, currentUser.getUserId(), reviewResult, memo);
        if (result.equals(1)) {
            master.setCaseState(1);
        } else if (result.equals(-1)) {
            master.setCaseState(-1);
        }
        ivTransOrgMasterService.update(master);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 确认转移
     * @param masterId
     * @param user
     * @return
     */
    @Transactional
    @GetMapping("/commitTransfer")
    @Authority("iv_trans_org_masters_commitTransfer")
    public Result commitTransfer(Integer masterId, @User CurrentUser user) {

        IvTransOrgMaster master = ivTransOrgMasterService.findById(masterId);
        master.setState(1);
        ivTransOrgMasterService.update(master);

        IvTransOrgDetailCriteria criteria = new IvTransOrgDetailCriteria();
        criteria.setMasterId(masterId);
        List<IvTransOrgDetail> list = ivTransOrgDetailService.findList(criteria);
//        return ResultGenerator.genSuccessResult();
        for (IvTransOrgDetail item : list) {
            Inventory fromIv = inventoryService.findById(item.getInventoryId());
            fromIv.setQuantity(fromIv.getQuantity()-item.getQuantity());
            inventoryService.update(fromIv);

            Inventory toIv = new Inventory();
            BeanUtils.copyProperties(fromIv, toIv);
            toIv.setQuantity(item.getQuantity());
            toIv.setOrganizationId(master.getToOrgId());
            toIv.setOrganizationName(master.getToOrgName());
            inventoryService.save(toIv);
            InventoryTransactRunCriteria runCriteria = new InventoryTransactRunCriteria();
            runCriteria.setFromInventory(fromIv);
            runCriteria.setToInventory(toIv);
            runCriteria.setTransactType(InventoryConstant.INVENTORY_TRANS_ORG);
            runCriteria.setDetailNo( master.getCode());
            runCriteria.setCreateUserId(user.getUserId());
            runCriteria.setCreateUserName(user.getUserName());
            inventoryTransactService.run(runCriteria);
        }

        return ResultGenerator.genSuccessResult();
    }
}
