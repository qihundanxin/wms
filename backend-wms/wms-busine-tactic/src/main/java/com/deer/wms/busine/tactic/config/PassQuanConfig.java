package com.deer.wms.busine.tactic.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "passquan")
public class PassQuanConfig {
    private String host;
    private Map<String, String> heads;

    @Configuration
    @Getter
    @Setter
    @ConfigurationProperties(prefix = "passquan.path")
    public static class PassQuanPathConfig {
        private String getReplayOrderids;
        private String getSku;
        private String getUnfinishedOrder;
        private String replay;
    }
}
