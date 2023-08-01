package com.deer.wms.busine.tactic.web;

import com.deer.wms.busine.tactic.model.TurnoverTacticDetail;
import com.deer.wms.busine.tactic.model.TurnoverTacticDetailCriteria;
import com.deer.wms.busine.tactic.service.TurnoverTacticDetailService;
import com.deer.wms.intercept.annotation.Authority;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by  on 2020/03/15.
 *
 * 库存周转规则详情表
 */
@RestController
@RequestMapping("/turnover/tactic/details")
public class TurnoverTacticDetailController {

    @Autowired
    private TurnoverTacticDetailService turnoverTacticDetailService;

    @PostMapping
    @Authority("turnover_tactic_details_add")
    public Result add(@RequestBody TurnoverTacticDetail turnoverTacticDetail) {
        turnoverTacticDetailService.save(turnoverTacticDetail);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    @Authority("turnover_tactic_details_delete")
    public Result delete(@PathVariable Integer id) {
        turnoverTacticDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    @Authority("turnover_tactic_details_update")
    public Result update(@RequestBody TurnoverTacticDetail turnoverTacticDetail) {
        turnoverTacticDetailService.update(turnoverTacticDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("turnover_tactic_details_get")
    public Result detail(@PathVariable Integer id) {
        TurnoverTacticDetail turnoverTacticDetail = turnoverTacticDetailService.findById(id);
        return ResultGenerator.genSuccessResult(turnoverTacticDetail);
    }

    @GetMapping("/list")
    @Authority("turnover_tactic_details_list")
    public Result list(TurnoverTacticDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<TurnoverTacticDetail> list = turnoverTacticDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
