package com.deer.wms.base.system.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 打印机信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "printer_info")
public class PrinterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 仓库id
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * IP
     */
    @Column(name = "ip")
    private String ip;

    /**
     * 端口
     */
    @Column(name = "port")
    private String port;

    /**
     * 打印机Index
     */
    @Column(name = "printer_index")
    private String printerIndex;

    /**
     * 纸张
     */
    @Column(name = "page")
    private String page;

    /**
     * 打印方向: 1-纵向 2-横向
     */
    @Column(name = "fx")
    private String fx;

    /**
     * 单页/双页: 1-单页 2-双页
     */
    @Column(name = "ds")
    private String ds;

    /**
     * 1-PDA默认
     */
    @Column(name = "pda_default")
    private String pdaDefault;
}
