package com.deer.wms.busine.tactic.web;

import com.deer.wms.busine.tactic.model.CombinePalletTactic;
import com.deer.wms.busine.tactic.model.CombinePalletTacticCriteria;
import com.deer.wms.busine.tactic.service.CombinePalletTacticService;
import com.deer.wms.intercept.annotation.Authority;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by  on 2020/06/14.
 *
 * 合托策略
 */
@RestController
@RequestMapping("/combine/pallet/tactics")
public class CombinePalletTacticController {

    @Autowired
    private CombinePalletTacticService combinePalletTacticService;

    @PostMapping("/add")
    @Authority("combine_pallet_tactics_add")
    public Result add(@RequestBody CombinePalletTactic combinePalletTactic) {
        combinePalletTacticService.save(combinePalletTactic);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("combine_pallet_tactics_delete")
    public Result delete(Integer id) {
        combinePalletTacticService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("combine_pallet_tactics_update")
    public Result update(@RequestBody CombinePalletTactic combinePalletTactic) {
        combinePalletTacticService.update(combinePalletTactic);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("combine_pallet_tactics_get")
    public Result detail(@PathVariable Integer id) {
        CombinePalletTactic combinePalletTactic = combinePalletTacticService.findById(id);
        return ResultGenerator.genSuccessResult(combinePalletTactic);
    }

    @GetMapping("/list")
    @Authority("combine_pallet_tactics_list")
    public Result list(CombinePalletTacticCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<CombinePalletTactic> list = combinePalletTacticService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
