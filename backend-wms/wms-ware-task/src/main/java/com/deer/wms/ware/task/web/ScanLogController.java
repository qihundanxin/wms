package com.deer.wms.ware.task.web;

import java.util.Date;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.ware.task.model.ScanLog;
import com.deer.wms.ware.task.service.ScanLogService;

import lombok.AllArgsConstructor;

/**
 * 扫码记录
 */
@AllArgsConstructor
@RestController
@RequestMapping("/scanLog")
public class ScanLogController {
    private final ScanLogService service;

    /**
     * 记录扫码日志
     */
    @PostMapping("/save")
    @Authority("scanLog_save") // 配置到通用中
    public Result saveScanLog(@RequestBody ScanLog param, @User CurrentUser user) {
        param.setCreateUserId(user.getUserId());
        param.setCreateTime(new Date());
        service.save(param);
        return ResultGenerator.genSuccessResult();
    }
}
