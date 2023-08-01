package com.deer.wms.ware.task.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.ware.task.model.ShipBoxDetail;
import com.deer.wms.ware.task.model.ShipBoxDetailCriteria;
import com.deer.wms.ware.task.service.ShipBoxDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by  on 2021/01/06.
 *
 * 装运箱明细
 */
@RestController
@RequestMapping("/ship/box/details")
public class ShipBoxDetailController {

    @Autowired
    private ShipBoxDetailService shipBoxDetailService;

    @PostMapping("/add")
    @Authority("ship_boxs_details_add")
    public Result add(@RequestBody ShipBoxDetail shipBoxDetail) {
        shipBoxDetailService.save(shipBoxDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("ship_boxs_details_delete")
    public Result delete(Integer id) {
        shipBoxDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("ship_boxs_details_update")
    public Result update(@RequestBody ShipBoxDetail shipBoxDetail) {
        shipBoxDetailService.update(shipBoxDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("ship_boxs_details_get")
    public Result detail(@PathVariable Integer id) {
        ShipBoxDetail shipBoxDetail = shipBoxDetailService.findById(id);
        return ResultGenerator.genSuccessResult(shipBoxDetail);
    }

    @GetMapping("/list")
    @Authority("ship_boxs_details_list")
    public Result list(ShipBoxDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ShipBoxDetail> list = shipBoxDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
