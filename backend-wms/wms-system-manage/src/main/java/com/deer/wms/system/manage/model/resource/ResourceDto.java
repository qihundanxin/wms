package com.deer.wms.system.manage.model.resource;

import javax.persistence.Column;

import lombok.ToString;

@ToString
public class ResourceDto extends Resource {

    /**
     * 路径类型：P=页面；I=接口；
     */
    @Column(name = "url_type")
    private String urlType;

    /**
     * 请求方式：GET；POST；PUT；DELETE；
     */
    private String method;

    /**
     * 路径地址
     */
    private String url;

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
