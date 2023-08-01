package com.deer.wms.ASN.manage.web;

import javax.annotation.Resource;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.ASN.manage.model.in.AcceptLogCriteria;
import com.deer.wms.ASN.manage.service.AcceptLogService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

/**
 * 入库日志
 * TODO 页面没有，暂时未增加权限标识
 */
@RestController
@RequestMapping("/accept/log")
public class AcceptLogController {

    @Resource
    private AcceptLogService acceptLogService;

    @PostMapping("/list")
    @Authority("accept_log_list")
    public Result list(@RequestBody AcceptLogCriteria acceptLogCriteria) {
        return ResultGenerator.genSuccessResult(acceptLogService.findList(acceptLogCriteria));
    }
}
