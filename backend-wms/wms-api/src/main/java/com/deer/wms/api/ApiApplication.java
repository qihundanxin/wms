package com.deer.wms.api;

import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.deer.wms.file.configurer.FileSetting;

/**
 * 项目后台API入口 spring boot 1000 10000 分布式部署 1 5 mysql
 * 
 * 
 * Created by guo on 2018/8/11.
 */
@EnableAsync
@SpringBootApplication
@EnableScheduling // 支持定时任务
@EnableConfigurationProperties({ FileSetting.class })
@ComponentScan(value = {
        "com.deer.wms.api",
        "com.deer.wms.project.root",
        "com.deer.wms.intercept.common.data",
        "com.deer.wms.file",
        "com.deer.wms.system.manage",
        "com.deer.wms.base.system",
        "com.deer.wms.review.manage",
        "com.deer.wms.quartz",
        "com.deer.wms.busine.tactic",
        "com.deer.wms.inventory",
        "com.deer.wms.ware.task",
        "com.deer.wms.ASN.manage",
        "com.deer.wms.ureport",
        "com.deer.wms.lzb.erp"
})
public class ApiApplication {
    public static void main(String[] args) {
        System.out.println(TimeZone.getDefault());
        System.out.println(ZoneId.systemDefault());
        System.out.println(new Date());
        SpringApplication.run(ApiApplication.class, args);
    }
}