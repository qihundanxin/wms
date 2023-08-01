package com.deer.wms.busine.tactic.web;

import com.deer.wms.busine.tactic.model.RouteTactic;
import com.deer.wms.busine.tactic.model.RouteTacticCriteria;
import com.deer.wms.busine.tactic.service.RouteTacticService;
import com.deer.wms.intercept.annotation.Authority;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by ll on 2020/02/13.
 *
 * 路线规则策略
 */
@RestController
@RequestMapping("/route/tactics")
public class RouteTacticController {

    @Autowired
    private RouteTacticService routeTacticService;

    @PostMapping
    @Authority("route_tactics_add")
    public Result add(@RequestBody RouteTactic routeTactic) {
        routeTacticService.save(routeTactic);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("route_tactics_delete")
    public Result delete(Integer id) {
        routeTacticService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("route_tactics_update")
    public Result update(@RequestBody RouteTactic routeTactic) {
        routeTacticService.update(routeTactic);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("route_tactics_get")
    public Result detail(@PathVariable Integer id) {
        RouteTactic routeTactic = routeTacticService.findById(id);
        return ResultGenerator.genSuccessResult(routeTactic);
    }

    @GetMapping
    @Authority("route_tactics_list")
    public Result list(RouteTacticCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<RouteTactic> list = routeTacticService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
