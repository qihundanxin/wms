package com.deer.wms.busine.tactic.web;

import com.deer.wms.busine.tactic.model.PrintDetail;
import com.deer.wms.busine.tactic.model.PrintDetailCriteria;
import com.deer.wms.busine.tactic.service.PrintDetailService;
import com.deer.wms.intercept.annotation.Authority;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
* Created by  on 2021/07/30.
 *
 * 打印详情
*/
@RestController
@RequestMapping("/print/details")
public class PrintDetailController {

    @Autowired
    private PrintDetailService printDetailService;

    @PostMapping("/add")
    @Authority("print_details_add")
    public Result add(@RequestBody PrintDetail printDetail) {
        printDetailService.save(printDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("print_details_delete")
    public Result delete(  Integer id) {
        printDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("print_details_update")
    public Result update(@RequestBody PrintDetail printDetail) {
        printDetailService.update(printDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("print_details_get")
    public Result detail(@PathVariable Integer id) {
        PrintDetail printDetail = printDetailService.findById(id);
        return ResultGenerator.genSuccessResult(printDetail);
    }

    @GetMapping("/list")
    @Authority("print_details_list")
    public Result list(PrintDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<PrintDetail> list = printDetailService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


}
