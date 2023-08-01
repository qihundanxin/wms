package com.deer.wms.ware.task.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.ware.task.model.Box.BoxDetail;
import com.deer.wms.ware.task.model.Box.BoxDetailCriteria;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.model.pickTask.PickTask;
import com.deer.wms.ware.task.service.BoxDetailService;
import com.deer.wms.ware.task.service.PickTaskService;
import com.deer.wms.ware.task.service.SoDetailService;
import com.deer.wms.ware.task.service.SoMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by ll on 2020/02/15.
 *
 * 装箱信息
 */
@RestController
@RequestMapping("/box/details")
public class BoxDetailController {

    @Autowired
    private BoxDetailService boxDetailService;

    @Autowired
    private PickTaskService pickTaskService;

    @Autowired
    private SoDetailService soDetailService;

    @Autowired
    private SoMasterService soMasterService;

    /**
     * 装箱复核
     *
     * @param boxDetail
     * @return
     */
    @PostMapping("/insert")
    @Authority("box_details_insert")
    public Result add(@RequestBody BoxDetail boxDetail) {

        String time = DateUtils.getNowDateTimeString();

        SoMaster soMaster = soMasterService.findById(boxDetail.getSoMasterId());
        boxDetail.setBillNo(soMaster.getBillNo());
        boxDetailService.save(boxDetail);

        PickTask pickTask = pickTaskService.findById(boxDetail.getPickTaskId());
        pickTask.setReviewQuantity(pickTask.getReviewQuantity() + boxDetail.getQuantity());
        pickTaskService.update(pickTask);

        SoDetail soDetail = soDetailService.findById(boxDetail.getSoDetailId());
        soDetail.setDeliveryQuantity(soDetail.getDeliveryQuantity() + boxDetail.getQuantity());
        soDetailService.update(soDetail);

        soMaster.setActualDeliveryTime(time);
        soMasterService.update(soMaster);
        return ResultGenerator.genSuccessResult();
    }

    //撤回
    @GetMapping("/withdraw")
    @Authority("box_details_withdraw")
    public Result withdraw(Integer boxDetailId) {

        BoxDetail boxDetail = boxDetailService.findById(boxDetailId);

        PickTask pickTask = pickTaskService.findById(boxDetail.getPickTaskId());
        pickTask.setReviewQuantity(pickTask.getReviewQuantity() - boxDetail.getQuantity());  //实际分拣数
        pickTaskService.update(pickTask);

        SoDetail soDetail = soDetailService.findById(boxDetail.getSoDetailId());
        SoMaster soMaster = soMasterService.findBy("billNo", soDetail.getBillNo());

        soMaster.setActualDeliveryTime(null); //实际发货时间
        soMasterService.update(soMaster);

        soDetail.setDeliveryQuantity(soDetail.getDeliveryQuantity() - boxDetail.getQuantity());  //发货数量
        soDetailService.update(soDetail);

        boxDetailService.deleteById(boxDetailId);

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("box_details_delete")
    public Result delete(Integer id) {
        boxDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("box_details_update")
    public Result update(@RequestBody BoxDetail boxDetail) {
        boxDetailService.update(boxDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("box_details_detail")
    public Result detail(@PathVariable Integer id) {
        BoxDetail boxDetail = boxDetailService.findById(id);
        return ResultGenerator.genSuccessResult(boxDetail);
    }

    @GetMapping("/list")
    @Authority("box_details_list")
    public Result list(BoxDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<BoxDetail> list = boxDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
