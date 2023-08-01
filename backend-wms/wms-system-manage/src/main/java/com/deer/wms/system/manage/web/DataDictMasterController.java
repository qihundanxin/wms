package com.deer.wms.system.manage.web;

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
import com.deer.wms.system.manage.model.DataDictMaster;
import com.deer.wms.system.manage.model.DataDictMasterCriteria;
import com.deer.wms.system.manage.service.DataDictDetailService;
import com.deer.wms.system.manage.service.DataDictMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 数据字典信息api接口
 * 
 * Created by on 2019/11/25.
 */
@RestController
@RequestMapping("/data/dict/masters")
public class DataDictMasterController {

    @Autowired
    private DataDictMasterService dataDictMasterService;

    @Autowired
    private DataDictDetailService dataDictDetailService;

    @PostMapping("/insert")
    @Authority("data_dict_masters_insert")
    public Result add(@RequestBody DataDictMaster dataDictMaster) {
        dataDictMasterService.createDataDictMaster(dataDictMaster);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("data_dict_masters_delete")
    public Result delete(Integer id) {
        dataDictMasterService.deleteById(id);
        dataDictDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("data_dict_masters_update")
    public Result update(@RequestBody DataDictMaster dataDictMaster) {
        dataDictMasterService.update(dataDictMaster);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("data_dict_masters_detail")
    public Result detail(@PathVariable Integer id) {
        DataDictMaster dataDictMaster = dataDictMasterService.findById(id);
        return ResultGenerator.genSuccessResult(dataDictMaster);
    }

    @GetMapping("/list")
    @Authority("data_dict_masters_list")
    public Result list(DataDictMasterCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<DataDictMaster> list = dataDictMasterService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
