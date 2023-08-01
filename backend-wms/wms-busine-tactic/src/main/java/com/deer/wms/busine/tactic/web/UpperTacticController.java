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
import com.deer.wms.busine.tactic.model.Upper.UpperTacticCriteria;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDto;
import com.deer.wms.busine.tactic.service.UpperTacticService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 上架策略主表api接口
 * 
 * Created by guo on 2019/11/28.
 */
@RestController
@RequestMapping("/upper/tactics")
public class UpperTacticController {

    @Autowired
    private UpperTacticService upperTacticService;

    @PostMapping("/insert")
    @Authority("upper_tactics_insert")
    public Result add(@RequestBody UpperTactic upperTactic) {
        upperTacticService.save(upperTactic);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("upper_tactics_delete")
    public Result delete(Integer id) {
        upperTacticService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("upper_tactics_update")
    public Result update(@RequestBody UpperTactic upperTactic) {
        upperTacticService.update(upperTactic);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("upper_tactics_detail")
    public Result detail(Integer id) {
        UpperTactic upperTactic = upperTacticService.findById(id);
        return ResultGenerator.genSuccessResult(upperTactic);
    }

    @GetMapping("/list")
    @Authority("upper_tactics_list")
    public Result list(UpperTacticCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<UpperTacticDto> list = upperTacticService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
