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

import com.deer.wms.ASN.manage.model.file.AsnMasterFile;
import com.deer.wms.ASN.manage.model.file.AsnMasterFileCriteria;
import com.deer.wms.ASN.manage.model.file.AsnMasterFileDto;
import com.deer.wms.ASN.manage.service.AsnMasterFileService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * ASN归档api接口
 * 
 * Created by guo on 2020/01/07.
 */
@RestController
@RequestMapping("/asn/master/files")
public class AsnMasterFileController {

    @Autowired
    private AsnMasterFileService asnMasterFileService;

    @PostMapping("/insert")
    @Authority("asn_master_files_insert")
    public Result add(@RequestBody AsnMasterFile asnMasterFile) {
        asnMasterFileService.save(asnMasterFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("asn_master_files_delete")
    public Result delete(Integer id) {
        asnMasterFileService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("asn_master_files_update")
    public Result update(@RequestBody AsnMasterFile asnMasterFile) {
        asnMasterFileService.update(asnMasterFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("asn_master_files_detail")
    public Result detail(@PathVariable Integer id) {
        AsnMasterFile asnMasterFile = asnMasterFileService.findById(id);
        return ResultGenerator.genSuccessResult(asnMasterFile);
    }

    @GetMapping("/list")
    @Authority("asn_master_files_list")
    public Result list(AsnMasterFileCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AsnMasterFileDto> list = asnMasterFileService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
