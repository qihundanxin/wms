package com.deer.wms.webhook.web;

import java.util.Date;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.validation.group.Post;
import com.deer.wms.webhook.model.WebHookSetting;
import com.deer.wms.webhook.service.WebHookSettingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * WebHook设置
 * 
 * @author xuesinuo
 *
 */
@Slf4j
@RestController
@RequestMapping("/webhook/setting")
@RequiredArgsConstructor
public class WebHookSettingController {
    private final WebHookSettingService service;

    /** 返回秘钥 */
    @Authority("webhook_setting_add")
    @PostMapping("/add")
    public Result add(@Validated(Post.class) @RequestBody WebHookSetting entity, @User CurrentUser currentUser) {
        entity.setCreateUserId(currentUser.getUserId().longValue());
        entity.setCreateTime(new Date());
        WebHookSetting result = service.add(entity);
        return ResultGenerator.genSuccessResult(result.getSecretKey());
    }

    @Authority("webhook_setting_del")
    @GetMapping("/del")
    public Result del(Long id) {
        service.del(id);
        return ResultGenerator.genSuccessResult();
    }

    @Authority("webhook_setting_enable")
    @PostMapping("/enable")
    public Result del(@Validated(WebHookSetting.Enable.class) @RequestBody WebHookSetting entity) {
        service.setEnable(entity.getWebHookSettingId(), entity.getHasEnable());
        return ResultGenerator.genSuccessResult();
    }

    @Authority("webhook_setting_list")
    @GetMapping("/list")
    public Result getList(WebHookSetting param) {
        List<WebHookSetting> resultList = service.getList(param);
        return ResultGenerator.genSuccessResult(resultList);
    }

    // 测试webhook是否推送成功的回环API
    @PostMapping("/ex")
    public Object ex(@RequestBody String body) {
        log.info(body);
        return body;
    }
}
