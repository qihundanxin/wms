package com.deer.wms.webhook.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.webhook.model.WebHookLog;
import com.deer.wms.webhook.model.WebHookLogParam;
import com.deer.wms.webhook.service.WebHookLogService;

import lombok.RequiredArgsConstructor;

/**
 * WebHook日志
 * 
 * @author xuesinuo
 *
 */
@RestController
@RequestMapping("/webhook/log")
@RequiredArgsConstructor
public class WebHookLogController {
    private final WebHookLogService service;

    @Authority("webhook_log_get")
    @GetMapping("/get")
    public Result add(WebHookLogParam param) {
        List<WebHookLog> resultList = service.getList(param);
        return ResultGenerator.genSuccessResult(resultList);
    }

    // TODO xuesinuo 重新发送
}
