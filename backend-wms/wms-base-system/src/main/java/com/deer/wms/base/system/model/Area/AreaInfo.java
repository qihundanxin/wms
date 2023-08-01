package com.deer.wms.base.system.model.Area;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 货区信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "area_info")
public class AreaInfo {
    // 兼容旧的 getter setter
    public Boolean getBoxReplenish() {
        return this.isBoxReplenish;
    }

    public void setBoxReplenish(Boolean isBoxReplenish) {
        this.isBoxReplenish = isBoxReplenish;
    }

    public Boolean getScatteredReplenish() {
        return this.isScatteredReplenish;
    }

    public void setScatteredReplenish(Boolean isScatteredReplenish) {
        this.isScatteredReplenish = isScatteredReplenish;
    }

    // 兼容旧的String类型日期
    public String getAddTime() {
        return DateUtils.dateToStrNullable(this.addTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setAddTime(String addTime) {
        this.addTime = DateUtils.strToDate(addTime);
    }

    /**
     * 货区ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "area_id")
    private Integer areaId;

    /**
     * 货区编码
     */
    @Column(name = "area_code")
    private String areaCode;

    /**
     * 货区名
     */
    @Column(name = "area_name")
    private String areaName;

    /**
     * 添加时间
     */
    @Column(name = "add_time")
    private Date addTime;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 优先级
     */
    @Column(name = "priority_level")
    private Integer priorityLevel;

    /**
     * 仓库ID
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 货区类型 1-收货区 2-仓储区 3-拣货区 4-发货复核区 5-移动货位区
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 整箱补货来源 false-0 true-1
     */
    @Column(name = "is_box_replenish")
    private Boolean isBoxReplenish;

    /**
     * 拆零补货来源 false-0 true-1
     */
    @Column(name = "is_scattered_replenish")
    private Boolean isScatteredReplenish;

    /**
     * 拣货过渡货位
     */
    @Column(name = "pick_transit_cell")
    private String pickTransitCell;

    /**
     * 入库过渡货位
     */
    @Column(name = "storage_transit_cell")
    private String storageTransitCell;

    /**
     * 货区基点
     */
    @Column(name = "area_centre")
    private String areaCentre;

    /**
     * 1-系统内置
     */
    @Column(name = "is_sys")
    private Integer isSys;
}
