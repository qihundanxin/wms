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

import com.deer.wms.ASN.manage.model.file.RequestBuyDetailFile;
import com.deer.wms.ASN.manage.model.file.RequestBuyDetailFileCriteria;
import com.deer.wms.ASN.manage.model.file.RequestBuyDetailFileDto;
import com.deer.wms.ASN.manage.service.RequestBuyDetailFileService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 归档采购详情api接口
 * 
 * Created by guo on 2020/01/05.
 */
@RestController
@RequestMapping("/request/buy/detail/files")
public class RequestBuyDetailFileController {

    @Autowired
    private RequestBuyDetailFileService requestBuyDetailFileService;

    @PostMapping("/insert")
    @Authority("request_buy_detail_files_insert")
    public Result add(@RequestBody RequestBuyDetailFile requestBuyDetailFile) {
        requestBuyDetailFileService.save(requestBuyDetailFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("request_buy_detail_files_delete")
    public Result delete(Integer id) {
        requestBuyDetailFileService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("request_buy_detail_files_update")
    public Result update(@RequestBody RequestBuyDetailFile requestBuyDetailFile) {
        requestBuyDetailFileService.update(requestBuyDetailFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("request_buy_detail_files_detail")
    public Result detail(@PathVariable Integer id) {
        RequestBuyDetailFile requestBuyDetailFile = requestBuyDetailFileService.findById(id);
        return ResultGenerator.genSuccessResult(requestBuyDetailFile);
    }

    @GetMapping("/list")
    @Authority("request_buy_detail_files_list")
    public Result list(RequestBuyDetailFileCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<RequestBuyDetailFileDto> list = requestBuyDetailFileService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
