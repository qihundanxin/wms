package com.deer.wms.base.system.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.ToString;

@ToString
public class ImportModel {

    private MultipartFile model;

    private Integer aaa;
    private Integer bbb;

    public Integer getAaa() {
        return aaa;
    }

    public void setAaa(Integer aaa) {
        this.aaa = aaa;
    }

    public Integer getBbb() {
        return bbb;
    }

    public void setBbb(Integer bbb) {
        this.bbb = bbb;
    }

    public MultipartFile getModel() {
        return model;
    }

    public void setModel(MultipartFile model) {
        this.model = model;
    }
}
