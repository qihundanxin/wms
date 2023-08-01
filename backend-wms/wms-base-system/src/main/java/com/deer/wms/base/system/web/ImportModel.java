package com.deer.wms.base.system.web;

import org.springframework.web.multipart.MultipartFile;

public class ImportModel {
    private MultipartFile model;

    public MultipartFile getModel() {
        return model;
    }

    public void setModel(MultipartFile model) {
        this.model = model;
    }
}