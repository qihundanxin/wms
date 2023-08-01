package com.deer.wms.base.system.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.model.PageSet.PageSet;
import com.deer.wms.base.system.model.PageSet.PageSetCriteria;
import com.deer.wms.base.system.model.PageSet.PageSetDto;
import com.deer.wms.base.system.service.PageSetService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 页面显示api接口
 * 
 * Created by guo on 2019/11/27.
 *
 * TODO  接口未使用
 */
@RestController
@RequestMapping("/page/sets")
public class PageSetController {

    @Autowired
    private PageSetService pageSetService;

    @PostMapping("/insert")
    @Authority("page_sets_insert")
    public Result add(@RequestBody PageSet pageSet) {
        pageSetService.save(pageSet);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("page_sets_delete")
    public Result delete(Integer id) {
        pageSetService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("page_sets_update")
    public Result update(@RequestBody PageSet pageSet) {
        pageSetService.update(pageSet);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("page_sets_detail")
    public Result detail(@PathVariable Integer id) {
        PageSet pageSet = pageSetService.findById(id);
        return ResultGenerator.genSuccessResult(pageSet);
    }

    @GetMapping("/list")
    @Authority("page_sets_list")
    public Result list(PageSetCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<PageSetDto> list = pageSetService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
