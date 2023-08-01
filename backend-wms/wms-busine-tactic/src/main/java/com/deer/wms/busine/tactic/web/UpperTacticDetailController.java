package com.deer.wms.busine.tactic.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.busine.tactic.model.Upper.UpperTactic;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDetail;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDetailCriteria;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDetailDto;
import com.deer.wms.busine.tactic.service.UpperTacticDetailService;
import com.deer.wms.busine.tactic.service.UpperTacticService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 上架策略子表api接口
 * 
 * Created by guo on 2019/11/28.
 */
@RestController
@RequestMapping("/upper/tactic/details")
public class UpperTacticDetailController {

    @Autowired
    private UpperTacticDetailService upperTacticDetailService;

    @Autowired
    private UpperTacticService upperTacticService;

    @PostMapping("/insert")
    @Authority("upper_tactic_details_insert")
    public Result add(@RequestBody UpperTacticDetailDto upperTacticDetail) {
        UpperTactic upperTactic = upperTacticService.findById(upperTacticDetail.getUpperTacticId());
        upperTacticDetail.setUpperTacticCode(upperTactic.getUpperTacticCode());
        upperTacticDetailService.save(upperTacticDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("upper_tactic_details_delete")
    public Result delete(Integer id) {
        upperTacticDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("upper_tactic_details_update")
    public Result update(@RequestBody UpperTacticDetailDto upperTacticDetail) {
        upperTacticDetailService.update(upperTacticDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("upper_tactic_details_detail")
    public Result detail(Integer id) {
        UpperTacticDetail upperTacticDetail = upperTacticDetailService.findById(id);
        return ResultGenerator.genSuccessResult(upperTacticDetail);
    }

    @GetMapping("/list")
    @Authority("upper_tactic_details_list")
    public Result list(UpperTacticDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<UpperTacticDetailDto> list = upperTacticDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findByAreaCode")
    @Authority("upper_tactic_details_findByAreaCode")
    public Result findByAreaCode(String areaCode) {
        List<UpperTacticDetail> list = upperTacticDetailService.findByAreaCode(areaCode);
        return ResultGenerator.genSuccessResult(list);
    }

}
