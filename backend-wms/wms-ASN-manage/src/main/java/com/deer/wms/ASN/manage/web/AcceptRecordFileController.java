package com.deer.wms.ASN.manage.web;

import com.deer.wms.ASN.manage.model.in.AcceptRecordFile;
import com.deer.wms.ASN.manage.model.in.AcceptRecordFileCriteria;
import com.deer.wms.ASN.manage.service.AcceptRecordFileService;
import com.deer.wms.intercept.annotation.Authority;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by guo on 2020/01/09.
 *
 * TODO 接口未使用
 */
@RestController
@RequestMapping("/accept/record/files")
public class AcceptRecordFileController {

    @Autowired
    private AcceptRecordFileService acceptRecordFileService;

    @PostMapping("/insert")
    @Authority("accept_record_files_insertinsert")
    public Result add(@RequestBody AcceptRecordFile acceptRecordFile) {
        acceptRecordFileService.save(acceptRecordFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("accept_record_files_delete")
    public Result delete(@PathVariable Integer id) {
        acceptRecordFileService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("accept_record_files_update")
    public Result update(@RequestBody AcceptRecordFile acceptRecordFile) {
        acceptRecordFileService.update(acceptRecordFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("accept_record_files_detail")
    public Result detail(Integer id) {
        AcceptRecordFile acceptRecordFile = acceptRecordFileService.findById(id);
        return ResultGenerator.genSuccessResult(acceptRecordFile);
    }

    @GetMapping("/list")
    @Authority("accept_record_files_list")
    public Result list(AcceptRecordFileCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AcceptRecordFile> list = acceptRecordFileService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
