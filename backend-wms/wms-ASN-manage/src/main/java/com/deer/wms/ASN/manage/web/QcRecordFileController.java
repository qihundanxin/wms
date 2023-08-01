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

import com.deer.wms.ASN.manage.model.qc.QcRecordFile;
import com.deer.wms.ASN.manage.model.qc.QcRecordFileCriteria;
import com.deer.wms.ASN.manage.model.qc.QcRecordFileDto;
import com.deer.wms.ASN.manage.service.QcRecordFileService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 质检记录归档api接口
 * 
 * Created by guo on 2020/01/19.
 */
@RestController
@RequestMapping("/qc/record/files")
public class QcRecordFileController {

    @Autowired
    private QcRecordFileService qcRecordFileService;

    @PostMapping("/insert")
    @Authority("qc_record_files_insert")
    public Result add(@RequestBody QcRecordFile qcRecordFile) {
        qcRecordFileService.save(qcRecordFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("qc_record_files_delete")
    public Result delete(Integer id) {
        qcRecordFileService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("qc_record_files_update")
    public Result update(@RequestBody QcRecordFile qcRecordFile) {
        qcRecordFileService.update(qcRecordFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("qc_record_files_detail")
    public Result detail(@PathVariable Integer id) {
        QcRecordFile qcRecordFile = qcRecordFileService.findById(id);
        return ResultGenerator.genSuccessResult(qcRecordFile);
    }

    @GetMapping("/list")
    @Authority("qc_record_files_list")
    public Result list(QcRecordFileCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<QcRecordFileDto> list = qcRecordFileService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
