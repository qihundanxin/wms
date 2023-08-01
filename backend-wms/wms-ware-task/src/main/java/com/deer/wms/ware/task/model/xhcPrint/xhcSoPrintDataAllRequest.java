package com.deer.wms.ware.task.model.xhcPrint;

import java.util.List;
import java.util.Map;

import lombok.ToString;

@ToString
public class xhcSoPrintDataAllRequest {
	private Integer[]  soMasterIds;
	private String  soOrderIds;
	private Integer wareId;
	private List<Map> printInfos;

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

	public List<Map> getPrintInfos() {
		return printInfos;
	}

	public void setPrintInfos(List<Map> printInfos) {
		this.printInfos = printInfos;
	}
}
