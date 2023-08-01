package com.deer.wms.busine.tactic.model;

import java.util.List;

import lombok.ToString;

@ToString
public class PrintIpDto extends PrintIp{
   private List<PrintDetail> list;

    public List<PrintDetail> getList() {
        return list;
    }

    public void setList(List<PrintDetail> list) {
        this.list = list;
    }
}
