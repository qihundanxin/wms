package com.deer.wms.api.configurer;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.bstek.ureport.console.UReportServlet;

/**
 * Ureport2 配置类
 *
 * @author qiaolin
 * @version 2018年5月9日
 */

@ImportResource("classpath:ureport-console-context.xml")
@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackages = "com.deer.wms")
public class UreportConfig {

    @Bean
    public ServletRegistrationBean buildUReportServlet() {
        /**
         * @param servlet
         * 
         * @param urlMappings 值为“/ureport/*”的 urlMappings 是一定不能变的，否则系统将无法运行。
         * 
         */
        return new ServletRegistrationBean(new UReportServlet(), "/ureport/*");

    }
}