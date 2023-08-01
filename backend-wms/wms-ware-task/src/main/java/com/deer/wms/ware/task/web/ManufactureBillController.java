package com.deer.wms.ware.task.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.ware.task.model.ManufactureBill;
import com.deer.wms.ware.task.model.ManufactureBillCriteria;
import com.deer.wms.ware.task.service.ManufactureBillService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by ll on 2020/03/06.
 *
 * 加工单
 */
@RestController
@RequestMapping("/manufacture/bills")
public class ManufactureBillController {

    @Autowired
    private ManufactureBillService manufactureBillService;

    @PostMapping
    @Authority("manufacture_bills_add")
    public Result add(@RequestBody ManufactureBill manufactureBill) {
        manufactureBillService.save(manufactureBill);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    @Authority("manufacture_bills_delete")
    public Result delete(@PathVariable Integer id) {
        manufactureBillService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    @Authority("manufacture_bills_update")
    public Result update(@RequestBody ManufactureBill manufactureBill) {
        manufactureBillService.update(manufactureBill);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("manufacture_bills_get")
    public Result detail(@PathVariable Integer id) {
        ManufactureBill manufactureBill = manufactureBillService.findById(id);
        return ResultGenerator.genSuccessResult(manufactureBill);
    }

    @GetMapping
    @Authority("manufacture_bills_list")
    public Result list(ManufactureBillCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ManufactureBill> list = manufactureBillService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
