package com.deer.wms.busine.tactic.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 打印详情
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "print_detail")
public class PrintDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "print_detail_id")
    private Integer printDetailId;

    /**
     * 打印机Ip id
     */
    @Column(name = "print_ip_id")
    private Integer printIpId;

    /**
     * 打印机ip
     */
    @Column(name = "print_ip")
    private String printIp;

    /**
     * 打印机Index
     */
    @Column(name = "printer_index")
    private Integer printerIndex;

    /**
     * 纸张尺寸
     */
    @Column(name = "print_size")
    private String printSize;

    /**
     * 上边距
     */
    @Column(name = "print_top")
    private Integer printTop;

    /**
     * 左边距
     */
    @Column(name = "print_left")
    private Integer printLeft;

    /**
     * 宽
     */
    @Column(name = "print_width")
    private Integer printWidth;

    /**
     * 高
     */
    @Column(name = "print_height")
    private Integer printHeight;

    /**
     * 方向 : 1-纵向 2-横向
     */
    @Column(name = "print_fangxiang")
    private Integer printFangxiang;

    /**
     * 单页/双页: 1-单页 2-双页
     */
    @Column(name = "print_duplex")
    private Integer printDuplex;

    /**
     * 类型：验收标签；商品标签；货位标签；拣货标签；快递面单；装箱标签；货位组标签；货位标签；二维码/条形码；出库单
     */
    @Column(name = "type")
    private String type;
}
