package com.deer.wms.ware.task.quartz.impl;

import com.deer.wms.busine.tactic.config.PassQuanConfig;
import com.deer.wms.busine.tactic.config.PassQuanConfig.PassQuanPathConfig;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.project.root.util.StringUtils;
import com.deer.wms.ware.task.quartz.OrderService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private PassQuanConfig passQuanConfig;

    @Autowired
    private PassQuanPathConfig passQuanPathConfig;

    @Autowired
    private CacheTool cache;

    public JsonNode getUnfinishedOrders(Boolean ignoreDriver, String shopId) {
        HttpHeaders headers = new HttpHeaders();
        //noinspection UastIncorrectHttpHeaderInspection
        for (Map.Entry<String, String> entry : passQuanConfig.getHeads().entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if (key != null && val != null) {
                headers.set(key, val);
            }
        }
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(passQuanConfig.getHost() + passQuanPathConfig.getGetUnfinishedOrder());
        if (!StringUtils.isEmpty(shopId)) {
            builder.queryParam("shop_id", shopId);
        }
        if (ignoreDriver) {
            builder.queryParam("ignore_driver", true);
        }
        String url = builder.build().toUriString();
        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode node = responseEntity.getBody();
            if (node.get("status").intValue() == 1) {
                return node.get("data");
            }
        }
        return null;
    }


}
