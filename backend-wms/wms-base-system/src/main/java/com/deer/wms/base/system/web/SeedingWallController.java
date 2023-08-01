package com.deer.wms.base.system.web;

import com.deer.wms.base.system.model.SeedingCell;
import com.deer.wms.base.system.model.SeedingWall.SeedingWall;
import com.deer.wms.base.system.model.SeedingWall.SeedingWallCriteria;
import com.deer.wms.base.system.service.SeedingCellService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.base.system.service.SeedingWallService;
import com.deer.wms.system.manage.service.WareInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by ll on 2020/02/17.
 * 播种墙
 */
@RestController
@RequestMapping("/seeding/walls")
public class SeedingWallController {

    @Autowired
    private SeedingWallService seedingWallService;
    @Autowired
    private WareInfoService wareInfoService;

    @Autowired
    private SeedingCellService seedingCellService;

    @PostMapping
    @Authority("seeding_walls_add")
    public Result add(@RequestBody SeedingWall seedingWall) {
        WareInfo ware = wareInfoService.findById(seedingWall.getWareId());
        seedingWall.setWareName(ware.getWareName());
        seedingWallService.save(seedingWall);
        for (int x = 1; x <= seedingWall.getRowCount(); x++) {
            for (int y = 1; y <= seedingWall.getColumnCount(); y++) {
                SeedingCell seedingCell = new SeedingCell();
                seedingCell.setSeedingCellCode(seedingWall.getCode() + "-" + x + "-" + y);
                seedingCell.setSeedingWallCode(seedingWall.getCode());
                seedingCell.setX(x);
                seedingCell.setY(y);
                seedingCellService.save(seedingCell);
            }
        }
        SeedingCell seedingCell = new SeedingCell();
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("seeding_walls_delete")
    public Result delete(Integer id) {
        SeedingWall seedingWall = seedingWallService.findById(id);
        seedingCellService.deleteByWallCode(seedingWall.getCode(), seedingWall.getWareId());
        seedingWallService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("seeding_walls_update")
    public Result update(@RequestBody SeedingWall seedingWall) {
        seedingCellService.deleteByWallCode(seedingWall.getCode(), seedingWall.getWareId());
        seedingWallService.update(seedingWall);
        for (int x = 0; x < seedingWall.getRowCount(); x++) {
            for (int y = 0; y < seedingWall.getRowCount(); y++) {
                SeedingCell seedingCell = new SeedingCell();
                seedingCell.setSeedingCellCode(seedingWall.getCode() + "-" + x + "-" + y);
                seedingCell.setSeedingWallCode(seedingWall.getCode());
                seedingCell.setX(x);
                seedingCell.setY(y);
                seedingCellService.save(seedingCell);
            }
        }
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("seeding_walls_get")
    public Result detail(@PathVariable Integer id) {

        SeedingWall seedingWall = seedingWallService.findById(id);

        return ResultGenerator.genSuccessResult(seedingWall);
    }

    @GetMapping
    @Authority("seeding_walls_list")
    public Result list(SeedingWallCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SeedingWall> list = seedingWallService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
