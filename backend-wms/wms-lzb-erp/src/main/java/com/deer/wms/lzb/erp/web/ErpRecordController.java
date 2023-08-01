package com.deer.wms.lzb.erp.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.inventory.model.ErpRecord;
import com.deer.wms.inventory.model.ErpRecordCriteria;
import com.deer.wms.lzb.erp.service.ErpRecordService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by  on 2021/02/14.
 */
@RestController
@RequestMapping("/erp/records")
@Slf4j
public class ErpRecordController {

    @GetMapping("/test")
    @Authority("erp_records_test")
    public Result get() {
        log.info("#############erp/records/test");
        return ResultGenerator.genSuccessResult();
    }

    @Autowired
    private ErpRecordService erpRecordService;

    @PostMapping("/add")
    @Authority("erp_records_add")
    public Result add(@RequestBody ErpRecord erpRecord) {
        erpRecordService.save(erpRecord);
        return ResultGenerator.genSuccessResult();
    }



    @GetMapping("/delete")
    @Authority("erp_records_delete")
    public Result delete(Integer id) {
        erpRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("erp_records_update")
    public Result update(@RequestBody ErpRecord erpRecord) {
        erpRecordService.update(erpRecord);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("erp_records_get")
    public Result detail(@PathVariable Integer id) {
        ErpRecord erpRecord = erpRecordService.findById(id);
        return ResultGenerator.genSuccessResult(erpRecord);
    }

    @GetMapping("/list")
    @Authority("erp_records_list")
    public Result list(ErpRecordCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ErpRecord> list = erpRecordService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
