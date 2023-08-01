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

import com.deer.wms.ASN.manage.model.file.QcDetailFile;
import com.deer.wms.ASN.manage.model.file.QcDetailFileCriteria;
import com.deer.wms.ASN.manage.service.QcDetailFileService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * QC详情归档api接口
 * 
 * Created by guo on 2020/01/19.
 */
@RestController
@RequestMapping("/qc/detail/files")
public class QcDetailFileController {

    @Autowired
    private QcDetailFileService qcDetailFileService;

    @PostMapping("/insert")
    @Authority("qc_detail_files_insert")
    public Result add(@RequestBody QcDetailFile qcDetailFile) {
        qcDetailFileService.save(qcDetailFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("qc_detail_files_delete")
    public Result delete(Integer id) {
        qcDetailFileService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("update")
    @Authority("qc_detail_files_update")
    public Result update(@RequestBody QcDetailFile qcDetailFile) {
        qcDetailFileService.update(qcDetailFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("qc_detail_files_detail")
    public Result detail(@PathVariable Integer id) {
        QcDetailFile qcDetailFile = qcDetailFileService.findById(id);
        return ResultGenerator.genSuccessResult(qcDetailFile);
    }

    @GetMapping("/list")
    @Authority("qc_detail_files_list")
    public Result list(QcDetailFileCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<QcDetailFile> list = qcDetailFileService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
