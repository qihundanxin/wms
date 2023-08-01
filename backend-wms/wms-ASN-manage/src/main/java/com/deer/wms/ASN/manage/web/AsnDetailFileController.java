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

import com.deer.wms.ASN.manage.model.file.AsnDetailFile;
import com.deer.wms.ASN.manage.model.file.AsnDetailFileCriteria;
import com.deer.wms.ASN.manage.model.file.AsnDetailFileDto;
import com.deer.wms.ASN.manage.service.AsnDetailFileService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * ASN详情归档api接口
 * 
 * Created by guo on 2020/01/07.
 */
@RestController
@RequestMapping("/asn/detail/files")
public class AsnDetailFileController {

    @Autowired
    private AsnDetailFileService asnDetailFileService;

    @PostMapping("/insert")
    @Authority("asn_detail_files_insert")
    public Result add(@RequestBody AsnDetailFile asnDetailFile) {
        asnDetailFileService.save(asnDetailFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("asn_detail_files_delete")
    public Result delete(Integer id) {
        asnDetailFileService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("asn_detail_files_update")
    public Result update(@RequestBody AsnDetailFile asnDetailFile) {
        asnDetailFileService.update(asnDetailFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("asn_detail_files_detail")
    public Result detail(@PathVariable Integer id) {
        AsnDetailFile asnDetailFile = asnDetailFileService.findById(id);
        return ResultGenerator.genSuccessResult(asnDetailFile);
    }

    @GetMapping("/list")
    @Authority("asn_detail_files_list")
    public Result list(AsnDetailFileCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AsnDetailFileDto> list = asnDetailFileService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
