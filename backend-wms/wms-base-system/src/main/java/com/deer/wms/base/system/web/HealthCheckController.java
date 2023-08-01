/**
 * Copyright (c) 2020 All Rights Reserved.
 */
package com.deer.wms.base.system.web;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

/**
 * 健康检查
 * 
 * @author owen
 * @version $$Id: HealthCheckController, v 0.1 2021/3/26 下午10:19 owen Exp $$
 * TODO 暂时未增加权限标识
 */
@RequestMapping("/health")
@RestController
public class HealthCheckController {

    @GetMapping("/check")
    @Authority("health-check")
    public Result check() {

        return ResultGenerator.genSuccessResult("ok");
    }
}
