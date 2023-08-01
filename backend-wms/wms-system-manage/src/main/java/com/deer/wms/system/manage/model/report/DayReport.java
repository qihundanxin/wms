package com.deer.wms.system.manage.model.report;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 日报表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "day_report")
public class DayReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_report_id")
    private Integer dayReportId;

    /**
     * 仓库id
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 仓库名称
     */
    @Column(name = "ware_name")
    private String wareName;

    /**
     * 货主id
     */
    @Column(name = "orgnization_id")
    private Integer orgnizationId;

    /**
     * 货主名称
     */
    @Column(name = "orgnization_name")
    private String orgnizationName;

    /**
     * 物料编码
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 物料名称
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * 开始库存---前一天的结束库存
     */
    @Column(name = "start_quantity")
    private Double startQuantity;

    /**
     * 入库量
     */
    @Column(name = "in_quantity")
    private Double inQuantity;

    /**
     * 出库量
     */
    @Column(name = "out_quantity")
    private Double outQuantity;

    /**
     * 数量
     */
    @Column(name = "quantity")
    private Double quantity;

    /**
     * 修改日期
     */
    @Column(name = "modify_date")
    private Date modifyDate;

    /**
     * 报告时间
     */
    @Column(name = "report_date")
    private Date reportDate;
}
