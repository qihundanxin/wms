package com.deer.wms.base.system.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.model.Currency.Currency;
import com.deer.wms.base.system.model.Currency.CurrencyCriteria;
import com.deer.wms.base.system.model.Currency.CurrencyDto;
import com.deer.wms.base.system.service.CurrencyService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 货币信息api
 * 
 * Created by guo on 2019/11/22.
 */
@RestController
@RequestMapping("/currencys")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    /**
     * 新增币种信息
     */
    @PostMapping("/insert")
    @Authority("currencys_insert")
    public Result add(@RequestBody Currency currency) {
        currencyService.save(currency);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("currencys_delete")
    public Result delete(Integer id) {
        currencyService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("currencys_update")
    public Result update(@RequestBody Currency currency) {
        currencyService.update(currency);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("currencys_detail")
    public Result detail(Integer id) {
        Currency currency = currencyService.findById(id);
        return ResultGenerator.genSuccessResult(currency);
    }

    @GetMapping("/list")
    @Authority("currencys_list")
    public Result list(CurrencyCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<CurrencyDto> list = currencyService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
