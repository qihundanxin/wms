package com.deer.wms.ware.task.constant.quartz;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.ware.task.quartz.OrderService;
import com.deer.wms.ware.task.quartz.PassQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.deer.wms.project.root.log.work.annotation.WorkLogger;
import com.deer.wms.ware.task.service.SoMasterService;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Component("passQSync")
@Slf4j
public class PassQSync {

    @Value("${wx.robot.webhook.missed-orders}")
    private String robotWebhookUrl;

    private Integer interval = 10;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SoMasterService soMasterService;
    
    @Value("${wms.host}")
    private String wmsHost;

    @Autowired
    private CacheTool cache;

    private PassQ passQ;

    @WorkLogger
    public void missOrderAlert(String shopId) {
        List<String> missedBillNos = passQ.getMissedBillNos(shopId).getMissBillNos();
        if (!missedBillNos.isEmpty()) {
            sendMissedBillNos(missedBillNos);
        }
    }

    @WorkLogger
    public void missOrderAlert() {
        missOrderAlert(null);
    }

    private void sendMissedBillNos(List<String> missedBillNos) {
        System.out.println("需要进行手动补单，单号如下：");
        for(String a:missedBillNos){
            System.out.println(a);
        }
        String content = getContent(missedBillNos);
        URI uri = URI.create(robotWebhookUrl);
        HashMap<String, String> text = new HashMap<>();
        text.put("content", content);
        HashMap<String, Object> obj = new HashMap<>();
        obj.put("msgtype", "text");
        obj.put("text", text);
        obj.put("safe", 0);
        ResponseEntity<Object> entity = restTemplate.postForEntity(uri, obj, Object.class);
        log.info("缺少订单: {}", missedBillNos);
        log.info("result: {}", entity);
    }

    private String getContent(List<String> missedBillNos) {
        String maxBillDescribe = "";
        int size = missedBillNos.size();
        if (size > 10) {
            maxBillDescribe = "前十";
            size = 10;
        }
        String tips ;
        if (null != wmsHost && wmsHost.contains("https://wms.foodsup.com")) {
        	tips = "正式服播报 : ";
        }else {
        	tips = "测试服播报 : ";
        }
        return String.format(tips + "WMS缺少订单数: %d, %s订单为: %s", missedBillNos.size(), maxBillDescribe, String.join(",", missedBillNos.subList(0, size)));
    }

}
