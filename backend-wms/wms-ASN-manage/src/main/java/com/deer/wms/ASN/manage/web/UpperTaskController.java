package com.deer.wms.ASN.manage.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.ASN.manage.model.asn.AsnDetail;
import com.deer.wms.ASN.manage.model.asn.AsnDetailCriteria;
import com.deer.wms.ASN.manage.model.asn.AsnDetailDto;
import com.deer.wms.ASN.manage.model.asn.AsnMaster;
import com.deer.wms.ASN.manage.model.upper.UpperTask;
import com.deer.wms.ASN.manage.model.upper.UpperTaskCriteria;
import com.deer.wms.ASN.manage.model.upper.UpperTaskInsert;
import com.deer.wms.ASN.manage.service.AsnDetailService;
import com.deer.wms.ASN.manage.service.AsnMasterService;
import com.deer.wms.ASN.manage.service.UpperTaskService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.ItemBatch;
import com.deer.wms.inventory.service.ItemBatchService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 上架api接口
 * 
 * Created by guo on 2019/12/19.
 */
@RestController
@RequestMapping("/upper/tasks")
public class UpperTaskController {

    @Autowired
    private UpperTaskService upperTaskService;

    @Autowired
    private AsnMasterService asnMasterService;

    @Autowired
    private AsnDetailService asnDetailService;

    @Autowired
    private ItemBatchService itemBatchService;

    @PostMapping("/insert")
    @Authority("upper_tasks_insert")
    public Result add(@RequestBody UpperTask upperTask) {
        upperTaskService.save(upperTask);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("upper_tasks_delete")
    public Result delete(Integer id) {
        upperTaskService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("upper_tasks_update")
    public Result update(@RequestBody UpperTask upperTask) {
        upperTaskService.update(upperTask);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("upper_tasks_detail")
    public Result detail(@PathVariable Integer id) {
        UpperTask upperTask = upperTaskService.findById(id);
        return ResultGenerator.genSuccessResult(upperTask);
    }

    @GetMapping("/list")
    @Authority("upper_tasks_list")
    public Result list(UpperTaskCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<UpperTask> list = upperTaskService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 查询合适的货位
     */
    @GetMapping("/findUpperCell")
    @Authority("upper_tasks_findUpperCell")
    public Result findUpperCell(Inventory inventory) {
        String cellCode = upperTaskService.findUpperCell(inventory);
        return ResultGenerator.genSuccessResult(cellCode);
    }

    /**
     * 上架
     */
    @PostMapping("/upperTask")
    @Authority("upper_tasks_upperTask")
    public Result upperTask(@RequestBody UpperTaskInsert upperTaskInsert, @User CurrentUser currentUser) {

        Integer userId = currentUser.getUserId();
        String userName = currentUser.getUserName();
        String createTime = DateUtils.getNowDateTimeString();

        upperTaskInsert.setUpperTaskUserId(userId);
        upperTaskInsert.setUpperTaskUserName(userName);
        upperTaskInsert.setCreateTime(createTime);
        upperTaskService.upperTask(upperTaskInsert);

        ItemBatch itemBatch = itemBatchService.findById(upperTaskInsert.getBatchId());
        String detailNo = itemBatch.getDetailNo();
        if (!detailNo.equals("")) {
            AsnDetail asnDetail = asnDetailService.findBy("detailNo", detailNo);
            asnDetail.setUpperQuantity(upperTaskInsert.getToQuantity() + asnDetail.getUpperQuantity());
            //上架以后更新detail的状态
            if (asnDetail.getUpperQuantity().equals(asnDetail.getAcceptQuantity())) {
                asnDetail.setState(4);//已完成
            }
            asnDetailService.update(asnDetail);
            //根据detail的条数判断 master的asn收货状态
            AsnDetailCriteria criteria = new AsnDetailCriteria();
            criteria.setBillNo(asnDetail.getBillNo());
            List<AsnDetailDto> asnDetails = asnDetailService.findList(criteria);
            AsnMaster asnMaster = asnMasterService.findBy("billNo", asnDetail.getBillNo());
            for (int i = 0; i < asnDetails.size(); i++) {
                Integer state = asnDetails.get(i).getState();
                if (state == 4) {
                    asnMaster.setAsnState(4);
                }
            }
            asnMasterService.update(asnMaster);
        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 生成上架任务
     */
    @PostMapping("/generateInCellTask")
    @Authority("upper_tasks_generateInCellTask")
    public Result generateInCellTask(@RequestBody List<Inventory> inventories) {
        upperTaskService.generateInCellTask(inventories);
        return ResultGenerator.genSuccessResult();
    }
}
