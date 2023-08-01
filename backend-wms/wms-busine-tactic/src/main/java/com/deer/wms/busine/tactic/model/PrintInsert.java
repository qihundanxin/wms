package com.deer.wms.busine.tactic.model;

import java.util.List;

import lombok.ToString;

@ToString
public class PrintInsert {
    private PrintIp printIp;

    public PrintIp getPrintIp() {
        return printIp;
    }

    public void setPrintIp(PrintIp printIp) {
        this.printIp = printIp;
    }

    public List<PrintDetail> getList() {
        return list;
    }

    public void setList(List<PrintDetail> list) {
        this.list = list;
    }

    private List<PrintDetail> list;

}
