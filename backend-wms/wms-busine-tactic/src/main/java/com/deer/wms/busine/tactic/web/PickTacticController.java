package com.deer.wms.busine.tactic.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.busine.tactic.model.pickTactic.PickTactic;
import com.deer.wms.busine.tactic.model.pickTactic.PickTacticCriteria;
import com.deer.wms.busine.tactic.service.PickTacticService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by ll on 2020/02/02.
 *
 * 拣货策略
 */
@RestController
@RequestMapping("/pick/tactics")
public class PickTacticController {

    @Autowired
    private PickTacticService pickTacticService;

    @PostMapping("/add")
    @Authority("pick_tactics_add")
    public Result add(@RequestBody PickTactic pickTactic) {
        List<PickTactic> list = pickTacticService.findList(new PickTacticCriteria(pickTactic.getAllocationRuleCode()));
        if (list.size() > 0) {
            throw new ServiceException(CommonCode.CODE_EXISTS);
        }
        pickTacticService.save(pickTactic);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    @Authority("pick_tactics_delete")
    public Result delete(@PathVariable Integer id) {
        pickTacticService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/deleteById")
    @Authority("pick_tactics_deleteById")
    public Result deleteById(Integer id) {
        pickTacticService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("pick_tactics_update")
    public Result update(@RequestBody PickTactic pickTactic) {
        List<PickTactic> lists = pickTacticService.verify(new PickTacticCriteria(pickTactic.getAllocationRuleCode(), pickTactic.getPickTacticId()));
        if (lists.size() > 0) {
            throw new ServiceException(CommonCode.CODE_EXISTS);
        }
        pickTacticService.update(pickTactic);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("pick_tactics_get")
    public Result detail(@PathVariable Integer id) {
        PickTactic pickTactic = pickTacticService.findById(id);
        return ResultGenerator.genSuccessResult(pickTactic);
    }

    @GetMapping("/list")
    @Authority("pick_tactics_list")
    public Result list(PickTacticCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<PickTactic> list = pickTacticService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
