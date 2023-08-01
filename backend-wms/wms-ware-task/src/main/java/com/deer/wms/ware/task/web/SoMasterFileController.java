package com.deer.wms.ware.task.web;

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
import com.deer.wms.ware.task.model.SO.SoMasterFile;
import com.deer.wms.ware.task.model.SO.SoMasterFileCriteria;
import com.deer.wms.ware.task.service.SoMasterFileService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * SO信息归档api接口
 * 
 * Created by ll on 2020/02/03.
 */
@RestController
@RequestMapping("/so/master/files")
public class SoMasterFileController {

    @Autowired
    private SoMasterFileService soMasterFileService;

    @PostMapping("/insert")
    @Authority("so_master_files_insert")
    public Result add(@RequestBody SoMasterFile soMasterFile) {
        soMasterFileService.save(soMasterFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("so_master_files_delete")
    public Result delete(Integer id) {
        soMasterFileService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("so_master_files_update")
    public Result update(@RequestBody SoMasterFile soMasterFile) {
        soMasterFileService.update(soMasterFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("so_master_files_detail")
    public Result detail(Integer id) {
        SoMasterFile soMasterFile = soMasterFileService.findById(id);
        return ResultGenerator.genSuccessResult(soMasterFile);
    }

    @GetMapping("/list")
    @Authority("so_master_files_list")
    public Result list(SoMasterFileCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SoMasterFile> list = soMasterFileService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
