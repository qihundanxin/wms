/**
 * Copyright 2021 bejson.com
 */
package com.deer.wms.base.system.model.xhc;
import java.util.List;

import lombok.ToString;

/**
 * Auto-generated: 2021-08-05 18:31:58
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class XhcSku {

    private int status;
    private String error;
    private List<Data> data;
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setError(String error) {
        this.error = error;
    }
    public String getError() {
        return error;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
    public List<Data> getData() {
        return data;
    }

}
