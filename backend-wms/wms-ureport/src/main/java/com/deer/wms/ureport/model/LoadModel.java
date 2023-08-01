package com.deer.wms.ureport.model;

import java.util.HashMap;
import java.util.Map;

import lombok.ToString;

@ToString
public class LoadModel {
    private String fileName;
    private Map<String, Object> parameters = new HashMap<>();

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
