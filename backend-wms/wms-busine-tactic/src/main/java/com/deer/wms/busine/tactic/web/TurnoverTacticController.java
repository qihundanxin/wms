package com.deer.wms.busine.tactic.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.busine.tactic.model.TurnoverTactic;
import com.deer.wms.busine.tactic.model.TurnoverTacticCriteria;
import com.deer.wms.busine.tactic.model.TurnoverTacticDetail;
import com.deer.wms.busine.tactic.service.TurnoverTacticDetailService;
import com.deer.wms.busine.tactic.service.TurnoverTacticService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by  on 2020/03/15.
 *
 * 库存周转规则主表
 */
@RestController
@RequestMapping("/turnover/tactics")
public class TurnoverTacticController {

    @Autowired
    private TurnoverTacticService turnoverTacticService;
    @Autowired
    private TurnoverTacticDetailService turnoverTacticDetailService;

    @PostMapping("/insert")
    @Transactional
    @Authority("turnover_tactics_insert")
    public Result add(@RequestBody TurnoverTacticCriteria criteria) {
        if (criteria.getTurnoverTacticDetails().size() <= 0) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "请选择批次属性");
        }
        for (int i = 0; i < criteria.getTurnoverTacticDetails().size(); i++) {
            TurnoverTacticDetail turnoverTacticDetail = criteria.getTurnoverTacticDetails().get(i);
            turnoverTacticDetail.setTurnoverTacticDetailId(null);
            turnoverTacticDetail.setTurnoverCode(criteria.getTurnoverCode());
            turnoverTacticDetail.setSortPriority(i + 1);
            turnoverTacticDetailService.save(turnoverTacticDetail);
        }
        TurnoverTactic turnoverTactic = new TurnoverTactic();
        turnoverTactic.setTurnoverCode(criteria.getTurnoverCode());
        turnoverTactic.setTurnoverDescription(criteria.getTurnoverDescription());
        turnoverTactic.setBatchTacticCode(criteria.getBatchTacticCode());
        turnoverTacticService.save(turnoverTactic);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/updateById")
    @Transactional
    @Authority("turnover_tactics_updateById")
    public Result updateById(@RequestBody TurnoverTacticCriteria criteria) {
        if (criteria.getTurnoverTacticDetails().size() <= 0) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "请选择批次属性");
        }
        TurnoverTactic turnoverTactic = turnoverTacticService.findById(criteria.getTurnoverMasterId());
        turnoverTacticDetailService.deleteByTurnoverCode(turnoverTactic.getTurnoverCode());
        turnoverTactic.setTurnoverDescription(criteria.getTurnoverDescription());
        turnoverTactic.setBatchTacticCode(criteria.getBatchTacticCode());
        turnoverTactic.setBatchTacticCode(criteria.getBatchTacticCode());
        for (int i = 0; i < criteria.getTurnoverTacticDetails().size(); i++) {
            TurnoverTacticDetail turnoverTacticDetail = criteria.getTurnoverTacticDetails().get(i);
            turnoverTacticDetail.setTurnoverTacticDetailId(null);
            turnoverTacticDetail.setTurnoverCode(criteria.getTurnoverCode());
            turnoverTacticDetail.setSortPriority(i + 1);
            turnoverTacticDetailService.save(turnoverTacticDetail);
        }
        turnoverTacticService.update(turnoverTactic);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    @Authority("turnover_tactics_delete")
    public Result delete(@PathVariable Integer id) {
        turnoverTacticService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    @Authority("turnover_tactics_update")
    public Result update(@RequestBody TurnoverTactic turnoverTactic) {
        turnoverTacticService.update(turnoverTactic);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("turnover_tactics_get")
    public Result detail(@PathVariable Integer id) {
        TurnoverTactic turnoverTactic = turnoverTacticService.findById(id);
        return ResultGenerator.genSuccessResult(turnoverTactic);
    }

    @GetMapping("/list")
    @Authority("turnover_tactics_list")
    public Result list(TurnoverTacticCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<TurnoverTactic> list = turnoverTacticService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/deleteByIdAndTurnoverCode")
    @Authority("turnover_tactics_deleteByIdAndTurnoverCode")
    public Result deleteByIdAndTurnoverCode(TurnoverTacticCriteria criteria) {
        turnoverTacticService.deleteTurnoverDetailAndMaster(criteria);
        return ResultGenerator.genSuccessResult();
    }
}
