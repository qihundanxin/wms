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

import com.deer.wms.ASN.manage.model.file.RequestBuyFile;
import com.deer.wms.ASN.manage.model.file.RequestBuyFileCriteria;
import com.deer.wms.ASN.manage.service.RequestBuyFileService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 归档采购信息api接口
 * 
 * Created by guo on 2020/01/05.
 */
@RestController
@RequestMapping("/request/buy/files")
public class RequestBuyFileController {

    @Autowired
    private RequestBuyFileService requestBuyFileService;

    @PostMapping("/insert")
    @Authority("request_buy_files_insert")
    public Result add(@RequestBody RequestBuyFile requestBuyFile) {
        requestBuyFileService.save(requestBuyFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("request_buy_files_delete")
    public Result delete(Integer id) {
        requestBuyFileService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("request_buy_files_update")
    public Result update(@RequestBody RequestBuyFile requestBuyFile) {
        requestBuyFileService.update(requestBuyFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("request_buy_files_detail")
    public Result detail(@PathVariable Integer id) {
        RequestBuyFile requestBuyFile = requestBuyFileService.findById(id);
        return ResultGenerator.genSuccessResult(requestBuyFile);
    }

    @GetMapping("/list")
    @Authority("request_buy_files_list")
    public Result list(RequestBuyFileCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<RequestBuyFile> list = requestBuyFileService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
