package com.deer.wms.busine.tactic.web;

import com.deer.wms.busine.tactic.model.WaveTacticDianshang;
import com.deer.wms.busine.tactic.model.WaveTacticDianshangCriteria;
import com.deer.wms.busine.tactic.model.WaveTacticDianshangDto;
import com.deer.wms.busine.tactic.service.WaveTacticDianshangService;
import com.deer.wms.intercept.annotation.Authority;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by  on 2020/11/07.
 *
 * 波次策略
 */
@RestController
@RequestMapping("/wave/tactic/dianshangs")
public class WaveTacticDianshangController {

    @Autowired
    private WaveTacticDianshangService waveTacticDianshangService;

    @PostMapping("/add")
    @Authority("wave_tactic_dianshangs_add")
    public Result add(@RequestBody WaveTacticDianshang waveTacticDianshang) {
        waveTacticDianshangService.save(waveTacticDianshang);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("wave_tactic_dianshangs_delete")
    public Result delete(Integer id) {
        waveTacticDianshangService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("wave_tactic_dianshangs_update")
    public Result update(@RequestBody WaveTacticDianshang waveTacticDianshang) {
        waveTacticDianshangService.update(waveTacticDianshang);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("wave_tactic_dianshangs_get")
    public Result detail(@PathVariable Integer id) {
        WaveTacticDianshang waveTacticDianshang = waveTacticDianshangService.findById(id);
        return ResultGenerator.genSuccessResult(waveTacticDianshang);
    }

    @GetMapping("/list")
    @Authority("wave_tactic_dianshangs_list")
    public Result list(WaveTacticDianshangCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<WaveTacticDianshangDto> list = waveTacticDianshangService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
