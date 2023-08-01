package com.deer.wms.ware.task.web;

import javax.annotation.Resource;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.ware.task.model.SO.SoLogCriteria;
import com.deer.wms.ware.task.service.SoLogService;

/**
 * 出库日志
 */
@RestController
@RequestMapping("/so/log")
public class SoLogController {

    @Resource
    private SoLogService soLogService;

    @PostMapping("/list")
    @Authority("so_log_list")
    public Result list(@RequestBody SoLogCriteria criteria) {
        return ResultGenerator.genSuccessResult(soLogService.findList(criteria));
    }
}
