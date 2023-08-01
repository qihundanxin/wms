package com.deer.wms.base.system.web;

import com.deer.wms.base.system.model.SeedingCell;
import com.deer.wms.base.system.model.SeedingCellCriteria;
import com.deer.wms.base.system.model.SeedingCellDto;
import com.deer.wms.base.system.service.SeedingCellService;
import com.deer.wms.intercept.annotation.Authority;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by  on 2021/03/06.
 * 播种工位
 */
@RestController
@RequestMapping("/seeding/cells")
public class SeedingCellController {

    @Autowired
    private SeedingCellService seedingCellService;

    @PostMapping("/add")
    @Authority("seeding_cells_add")
    public Result add(@RequestBody SeedingCell seedingCell) {
        seedingCellService.save(seedingCell);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("seeding_cells_delete")
    public Result delete(Integer id) {
        seedingCellService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("seeding_cells_update")
    public Result update(@RequestBody SeedingCell seedingCell) {
        seedingCellService.update(seedingCell);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/list")
    @Authority("seeding_cells_list")
    public Result list(SeedingCellCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SeedingCell> list = seedingCellService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findByWallCode")
    @Authority("seeding_cells_findByWallCode")
    public Result findByWallCode(String wallCode, Integer wareId) {
        List<SeedingCellDto> list = seedingCellService.findByWallCode(wallCode, wareId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
