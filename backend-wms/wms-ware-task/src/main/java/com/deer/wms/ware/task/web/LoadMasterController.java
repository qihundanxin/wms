package com.deer.wms.ware.task.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.BillNoUtils;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.WareInfoService;
import com.deer.wms.ware.task.constant.WareTaskConstant;
import com.deer.wms.ware.task.model.Box.BoxDetail;
import com.deer.wms.ware.task.model.Box.BoxDetailCriteria;
import com.deer.wms.ware.task.model.Load.LoadBoxData;
import com.deer.wms.ware.task.model.Load.LoadDetail;
import com.deer.wms.ware.task.model.Load.LoadMaster;
import com.deer.wms.ware.task.model.Load.LoadMasterCriteria;
import com.deer.wms.ware.task.service.BoxDetailService;
import com.deer.wms.ware.task.service.LoadDetailService;
import com.deer.wms.ware.task.service.LoadMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ll on 2020/02/17.
 *
 * 装车单
 */
@RestController
@RequestMapping("/load/masters")
public class LoadMasterController {

    @Autowired
    private LoadMasterService loadMasterService;
    @Autowired
    private LoadDetailController loadDetailController;
    @Autowired
    private WareInfoService wareInfoService;
    @Autowired
    private BoxDetailService boxDetailService;
    @Autowired
    private LoadDetailService loadDetailService;

    @PostMapping
    @Authority("load_masters_add")
    public Result add(@RequestBody LoadMaster loadMaster) {
        WareInfo ware = wareInfoService.findById(loadMaster.getWareId());
        loadMaster.setWareName(ware.getWareName());
        loadMaster.setCode(BillNoUtils.generateLoadNo());
        loadMasterService.save(loadMaster);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("load_masters_delete")
    public Result delete(Integer id) {
        loadDetailController.deleteByMasterId(id);
        loadMasterService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("load_masters_update")
    public Result update(@RequestBody LoadMaster loadMaster) {
        WareInfo ware = wareInfoService.findById(loadMaster.getWareId());
        loadMaster.setWareName(ware.getWareName());
        loadMasterService.update(loadMaster);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("load_masters_get")
    public Result detail(@PathVariable Integer id) {
        LoadMaster loadMaster = loadMasterService.findById(id);
        return ResultGenerator.genSuccessResult(loadMaster);
    }

    @GetMapping("/list")
    @Authority("load_masters_list")
    public Result list(LoadMasterCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<LoadMaster> list = loadMasterService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/loadBox")
    @Authority("load_masters_loadBox")
    public Result loadBox(@RequestBody LoadBoxData loadBoxData) {
        Integer loadMasterId = loadBoxData.getLoadMasterId();
        LoadMaster loadMaster = loadMasterService.findById(loadMasterId);
        String boxNo = loadBoxData.getBoxNo();
        BoxDetailCriteria criteria = new BoxDetailCriteria();
        criteria.setBoxNo(boxNo);
        List<BoxDetail> boxDetails = boxDetailService.findList(criteria);
        for (BoxDetail boxDetail : boxDetails) {
            LoadDetail loadDetail = new LoadDetail();
            loadDetail.setBoxNo(boxNo);
            loadDetail.setLoadMasterCode(loadMaster.getCode());
            loadDetail.setPackDetailId(boxDetail.getPackDetailId());
            loadDetail.setPackDescribe(boxDetail.getPackDescribe());
            loadDetail.setPickTaskId(boxDetail.getPickTaskId());
            loadDetail.setPickQuantity(boxDetail.getPickQuantity());
            loadDetail.setQuantity(boxDetail.getQuantity());
            loadDetailService.save(loadDetail);

            boxDetail.setState(WareTaskConstant.BOX_DETAIL_LOADED);
            boxDetailService.update(boxDetail);
        }

        return ResultGenerator.genSuccessResult();
    }
}
