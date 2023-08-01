/**
  * Copyright 2022 berwin
  */
package com.deer.wms.base.system.model.xhc;
import java.util.List;

import lombok.ToString;

/**
 * Auto-generated: 2022-05-27 12:50:10
 *
 */
@ToString
public class OrderIDSData {

    private int status;
    private String error;
    private List<String> data;
    //private Debug debug;
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

    public void setData(List<String> data) {
         this.data = data;
     }
     public List<String> getData() {
         return data;
     }

    //public void setDebug(Debug debug) {
    //     this.debug = debug;
    // }
    // public Debug getDebug() {
    //     return debug;
    // }

}