package com.deer.wms.project.root.component;

import javax.annotation.PostConstruct;

import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by guo on 2018/11/15.
 */
@Component
public class RestClient {
    protected RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        OkHttp3ClientHttpRequestFactory okHttpClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory();
        okHttpClientHttpRequestFactory.setConnectTimeout(5000);
        okHttpClientHttpRequestFactory.setReadTimeout(5000);
        okHttpClientHttpRequestFactory.setWriteTimeout(5000);
        restTemplate.setRequestFactory(okHttpClientHttpRequestFactory);
    }

}
