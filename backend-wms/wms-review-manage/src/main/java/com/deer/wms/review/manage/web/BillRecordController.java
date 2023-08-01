package com.deer.wms.review.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.model.BillRecordCriteria;
import com.deer.wms.review.manage.service.BillRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 订单跟踪信息api接口
 * 
 * Created by ll on 2020/03/05.
 */
@RestController
@RequestMapping("/bill/records")
public class BillRecordController {

    @Autowired
    private BillRecordService billRecordService;

    @PostMapping
    @Authority("bill_records_add")
    public Result add(@RequestBody BillRecord billRecord) {
        billRecordService.save(billRecord);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("bill_records_delete")
    public Result delete(Integer id) {
        billRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("bill_records_update")
    public Result update(@RequestBody BillRecord billRecord) {
        billRecordService.update(billRecord);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("bill_records_detail")
    public Result detail(Integer id) {
        BillRecord billRecord = billRecordService.findById(id);
        return ResultGenerator.genSuccessResult(billRecord);
    }

    @GetMapping("/list")
    @Authority("bill_records_list")
    public Result list(BillRecordCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<BillRecord> list = billRecordService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
