package com.deer.wms.ware.task.model.SO;

import java.util.List;

import lombok.ToString;

@ToString
public class SoPrintDataAllRequest {
	private Integer[]  soMasterIds;
	private String  soOrderIds;
   private Integer wareId;

   private List<SoMaster> printInfos;

    public Integer[] getSoMasterIds() {
        return soMasterIds;
    }

    public void setSoMasterIds(Integer[] soMasterIds) {
        this.soMasterIds = soMasterIds;
    }

    public String getSoOrderIds() {
        return soOrderIds;
    }

    public void setSoOrderIds(String soOrderIds) {
        this.soOrderIds = soOrderIds;
    }
    
    public Integer getWareId() {
        return wareId;
    }

    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public List<SoMaster> getPrintInfos() {
        return printInfos;
    }

    public void setPrintInfos(List<SoMaster> printInfos) {
        this.printInfos = printInfos;
    }
}
