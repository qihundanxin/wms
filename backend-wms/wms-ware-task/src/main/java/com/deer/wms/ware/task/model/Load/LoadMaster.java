package com.deer.wms.ware.task.model.Load;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 装车单
 *
 * @author xuesinuo
 */
@Data
@Table(name = "load_master")
public class LoadMaster {

    // 兼容旧的String类型日期
    public String getLoadStart() {
        return DateUtils.dateToStrNullable(this.loadStart, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setLoadStart(String loadStart) {
        this.loadStart = DateUtils.strToDate(loadStart);
    }

    public String getLoadFinish() {
        return DateUtils.dateToStrNullable(this.loadFinish, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setLoadFinish(String loadFinish) {
        this.loadFinish = DateUtils.strToDate(loadFinish);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 装车单编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 车牌号
     */
    @Column(name = "license_plate_no")
    private String licensePlateNo;

    /**
     * 车型 :1-type1 2-type2 3-type3
     */
    @Column(name = "vehicle_type")
    private Integer vehicleType;

    /**
     * 司机
     */
    @Column(name = "driver_name")
    private String driverName;

    /**
     * 装车人员
     */
    @Column(name = "loader_name")
    private String loaderName;

    /**
     * 装车开始时间
     */
    @Column(name = "load_start")
    private Date loadStart;

    /**
     * 装车结束时间
     */
    @Column(name = "load_finish")
    private Date loadFinish;

    /**
     * 大小限制
     */
    @Column(name = "size_limit")
    private Double sizeLimit;

    /**
     * 重量限制
     */
    @Column(name = "weight_limit")
    private Double weightLimit;

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
     * 波次号
     */
    @Column(name = "wave_id")
    private Integer waveId;

    /**
     * desc1
     */
    @Column(name = "desc1")
    private String desc1;

    /**
     * desc2
     */
    @Column(name = "desc2")
    private String desc2;

    /**
     * desc3
     */
    @Column(name = "desc3")
    private String desc3;

    /**
     * desc4
     */
    @Column(name = "desc4")
    private String desc4;
}
