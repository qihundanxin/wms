package com.deer.wms.quartz.task;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.deer.wms.base.system.model.xhc.OrderIDSData;
import com.deer.wms.busine.tactic.config.PassQuanConfig;
import com.deer.wms.busine.tactic.config.PassQuanConfig.PassQuanPathConfig;
import com.deer.wms.project.root.log.work.annotation.WorkLogger;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.HttpClientUtil;
import com.deer.wms.ware.task.service.SoMasterService;

import lombok.extern.slf4j.Slf4j;

/**
 * 轮询补单
 *
 * @author berwin
 */
@Component("reordertask")
@Slf4j
public class ReOrderTask {
    @Autowired
    private SoMasterService soMasterService;

    @Autowired
    private PassQuanConfig passQuanConfig;

    @Autowired
    private PassQuanPathConfig passQuanPathConfig;
    
    /**
     * 轮询补单
     */
    @WorkLogger
    public void autoReOrderTask( ) {
        log.info("开始轮询补单：{1} ", DateUtils.getNowDateTimeString());
    	String res="";
    	
    	Date curdate = DateUtils.getNowDateTime();
    	String fristTime = DateUtils.getFristTimeHalf(curdate);
    	String starTime = fristTime.split(",")[0];
    	String endTime  = fristTime.split(",")[1];
		try {
			String starTimeLong = DateUtils.dateToStampByS(starTime);
	    	String endTimeLong = DateUtils.dateToStampByS(endTime);
	    	
	    	URIBuilder urlB;
	    	HttpGet httpGet;
            urlB = new URIBuilder(passQuanConfig.getHost() + passQuanPathConfig.getGetReplayOrderids());
		
			urlB.addParameter("start_time", starTimeLong);
			urlB.addParameter("end_time", endTimeLong);
            log.info("轮询补单寻找订单号 ：{2} starTime: " + starTime + "   starTimeLong： " + starTimeLong + " endTimeLong: " + endTimeLong);
			
	    	httpGet = new HttpGet(urlB.build());

	        httpGet.setHeader("Accept","application/json");
	        httpGet.setHeader("Content-Type", "application/json");
            for (Map.Entry<String, String> entry : passQuanConfig.getHeads().entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                if (key != null && val != null) {
                    httpGet.setHeader(key, val);
                }
            }

            res = HttpClientUtil.doGet(passQuanConfig.getHost() + passQuanPathConfig.getGetReplayOrderids(), httpGet);
	        
		} catch (URISyntaxException e) {
            log.error("轮询补单 -- URISyntaxException 错误：{5} ", e.getMessage());
		} catch (ParseException e) {
            log.error("开始轮询补单 -- ParseException 错误：{6} ", e.getMessage());
		}
		
		OrderIDSData xhcSku= JSON.parseObject(res, OrderIDSData.class);
		if(xhcSku!=null ) {
	        List<String> orderIDs = xhcSku.getData();
	
	        if(orderIDs!=null && orderIDs.size() > 0){
	        	soMasterService.replayOrder(orderIDs);
	        }
		}
        log.info("结束轮询补单：{8} ", DateUtils.getNowDateTimeString());
    }

}