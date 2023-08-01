package com.deer.wms.ureport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 装箱
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "report_operate")
public class ReportOperate {
    // 兼容旧的String类型日期
    public String getReportDate() {
        return DateUtils.dateToStrNullable(this.reportDate, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setReportDate(String reportDate) {
        this.reportDate = DateUtils.strToDate(reportDate);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_operate_id")
    private Integer reportOperateId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 用户名称
     */
    @Column(name = "user_name")
    private String userName;

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
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 数量1
     */
    @Column(name = "quantity1")
    private Integer quantity1;

    /**
     * 数量2
     */
    @Column(name = "quantity2")
    private Integer quantity2;

    /**
     * 1-验收 2-上虚拟车 3-上架 4-拣货   5-分播  6-打包  7-盘点
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 装箱时间
     */
    @Column(name = "report_date")
    private Date reportDate;
}
