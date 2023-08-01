package com.deer.wms.busine.tactic.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.busine.tactic.model.SoMergeTactic;
import com.deer.wms.busine.tactic.model.SoMergeTacticCriteria;
import com.deer.wms.busine.tactic.service.SoMergeTacticService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * SO合并api接口
 * 
 * Created by ll on 2020/02/04.
 */
@RestController
@RequestMapping("/so/merge/tactics")
public class SoMergeTacticController {

    @Autowired
    private SoMergeTacticService soMergeTacticService;

    @PostMapping("/insert")
    @Authority("so_merge_tactics_insert")
    public Result add(@RequestBody SoMergeTactic soMergeTactic) {
        soMergeTacticService.save(soMergeTactic);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("so_merge_tactics_delete")
    public Result delete(@PathVariable Integer id) {
        soMergeTacticService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("so_merge_tactics_update")
    public Result update(@RequestBody SoMergeTactic soMergeTactic) {
        soMergeTacticService.update(soMergeTactic);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("so_merge_tactics_detail")
    public Result detail(Integer id) {
        SoMergeTactic soMergeTactic = soMergeTacticService.findById(id);
        return ResultGenerator.genSuccessResult(soMergeTactic);
    }

    @GetMapping("/list")
    @Authority("so_merge_tactics_list")
    public Result list(SoMergeTacticCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SoMergeTactic> list = soMergeTacticService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
