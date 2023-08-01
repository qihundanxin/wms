package com.deer.wms.ware.task.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.ware.task.model.SO.SoDetailFile;
import com.deer.wms.ware.task.model.SO.SoDetailFileCriteria;
import com.deer.wms.ware.task.service.SoDetailFileService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * SO详细信息归档api接口
 * 
 * Created by ll on 2020/02/03.
 */
@RestController
@RequestMapping("/so/detail/files")
public class SoDetailFileController {

    @Autowired
    private SoDetailFileService soDetailFileService;

    @PostMapping("/insert")
    @Authority("so_detail_files_insert")
    public Result add(@RequestBody SoDetailFile soDetailFile) {
        soDetailFileService.save(soDetailFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("so_detail_files_delete")
    public Result delete(Integer id) {
        soDetailFileService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("so_detail_files_update")
    public Result update(@RequestBody SoDetailFile soDetailFile) {
        soDetailFileService.update(soDetailFile);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("so_detail_files_detail")
    public Result detail(Integer id) {
        SoDetailFile soDetailFile = soDetailFileService.findById(id);
        return ResultGenerator.genSuccessResult(soDetailFile);
    }

    @GetMapping("/list")
    @Authority("so_detail_files_list")
    public Result list(SoDetailFileCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SoDetailFile> list = soDetailFileService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
