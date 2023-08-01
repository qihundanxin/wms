package com.deer.wms.ASN.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.ASN.manage.model.file.QcMasterFile;
import com.deer.wms.ASN.manage.model.file.QcMasterFileCriteria;
import com.deer.wms.ASN.manage.service.QcMasterFileService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * QC归档api接口
 * 
 * Created by guo on 2020/01/19.
 */
@RestController
@RequestMapping("/qc/master/files")
public class QcMasterFileController {

    @Autowired
    private QcMasterFileService qcMasterFileService;

    @PostMapping("insert")
    @Authority("qc_master_files_insert")
    public Result add(@RequestBody QcMasterFile qcMasterFile) {
        qcMasterFileService.save(qcMasterFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("qc_master_files_delete")
    public Result delete(Integer id) {
        qcMasterFileService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("qc_master_files_update")
    public Result update(@RequestBody QcMasterFile qcMasterFile) {
        qcMasterFileService.update(qcMasterFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("qc_master_files_detail")
    public Result detail(@PathVariable Integer id) {
        QcMasterFile qcMasterFile = qcMasterFileService.findById(id);
        return ResultGenerator.genSuccessResult(qcMasterFile);
    }

    @GetMapping("/list")
    @Authority("qc_master_files_list")
    public Result list(QcMasterFileCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<QcMasterFile> list = qcMasterFileService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
