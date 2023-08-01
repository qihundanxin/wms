package com.deer.wms.webhook.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.deer.wms.project.root.util.HttpClientUtil;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.webhook.WebHookSender;
import com.deer.wms.webhook.model.WebHookAbility;
import com.deer.wms.webhook.model.WebHookLog;
import com.deer.wms.webhook.model.WebHookSetting;
import com.deer.wms.webhook.model.WebHookShellV1;
import com.deer.wms.webhook.service.WebHookLogService;
import com.deer.wms.webhook.service.WebHookSettingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebHookServiceV1 implements WebHookSender {
    private final ObjectMapper jackson;
    private final WebHookSettingService settingService;
    private final WebHookLogService webHookLogService;

    @Async
    @Override
    public void send(WebHookAbility WebHookAbility, Object data, String key) {
        WebHookSetting param = new WebHookSetting();
        param.setAbility(WebHookAbility);
        param.setHasEnable(true);
        List<WebHookSetting> webHookSetting = settingService.getList(param);
        for (WebHookSetting setting : webHookSetting) {
            if (setting.getHasEnable()) {
                this.send(setting, data, key);
            }
        }
    }

    /**
     * 发送单条WebHook
     * 
     * @param setting
     * @param data
     */
    private boolean send(WebHookSetting setting, Object data, String key) {
        WebHookLog webHookLog = new WebHookLog();
        LocalDateTime nowDateTime = LocalDateTime.now();
        try {
            // 数据
            WebHookShellV1 webHookShellV1 = new WebHookShellV1();
            webHookShellV1.setData(data);
            webHookShellV1.setTime(nowDateTime);
            webHookShellV1.setUuid(IdUtil.fastSimpleUUID());
            webHookShellV1.setVersion(1);
            webHookShellV1.doSign(setting.getSecretKey());
            // HTTP发送
            HttpPost httpPost = new HttpPost(setting.getUrl());
            httpPost.setHeader("Accept", "application/json;charset=UTF-8");
            httpPost.setHeader("Content-Type", "application/json");
            String dataJson = jackson.writeValueAsString(webHookShellV1);
            HttpEntity httpEntity = new StringEntity(dataJson, ContentType.APPLICATION_JSON);
            httpPost.setEntity(httpEntity);
            HttpResponse httpResponse = HttpClientUtil.doHttp(httpPost);
            // 日志
            Boolean hasSuccess = false;
            String httpResultCode = "error";
            String httpResultValue = "[null]";
            if (Np.i(httpResponse).x(x -> x.getStatusLine()).x(x -> x.getStatusCode()).notNull()) {
                httpResultCode = Integer.toString(httpResponse.getStatusLine().getStatusCode());
                hasSuccess = httpResultCode.equals("200");
                try {
                    InputStream in = httpResponse.getEntity().getContent();
                    httpResultValue = new BufferedReader(new InputStreamReader(in))
                            .lines().collect(Collectors.joining(System.lineSeparator()));
                } catch (Exception e) {
                    log.info(setting.getUrl() + "读取WebHook返回Body报错", e);
                }
            }
            webHookLog.setAbility(setting.getAbility());
            webHookLog.setHasSuccess(hasSuccess);
            webHookLog.setKeyWord(key);
            webHookLog.setSendMessage(dataJson);
            webHookLog.setResultMessage(httpResultCode + " " + httpResultValue);
            webHookLog.setSendDatetime(nowDateTime);
            webHookLog.setUrl(setting.getUrl());
            webHookLog.setWebHookSettingId(setting.getWebHookSettingId());
            webHookLogService.add(webHookLog);

            return hasSuccess;
        } catch (Exception e) {
            log.error("WebHook发送时，异常", e);
            return false;
        }
    }

}
