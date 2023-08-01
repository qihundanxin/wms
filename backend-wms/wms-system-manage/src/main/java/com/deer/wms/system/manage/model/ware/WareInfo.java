package com.deer.wms.system.manage.model.ware;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 仓库信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "ware_info")
public class WareInfo {
    // 兼容旧的String类型日期
    public String getAddTime() {
        return DateUtils.dateToStrNullable(this.addTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setAddTime(String addTime) {
        this.addTime = DateUtils.strToDate(addTime);
    }

    /**
     * 仓库ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 仓库编码
     */
    @Column(name = "ware_code")
    private String wareCode;

    /**
     * 仓库名
     */
    @Column(name = "ware_name")
    private String wareName;

    /**
     * 新增时间
     */
    @Column(name = "add_time")
    private Date addTime;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 仓库负责人
     */
    @Column(name = "duty_user_id")
    private Integer dutyUserId;

    /**
     * 所属国家
     */
    @Column(name = "country")
    private String country;

    /**
     * 所属省份
     */
    @Column(name = "province")
    private String province;

    /**
     * 所属城市
     */
    @Column(name = "city")
    private String city;

    /**
     * 所属区、县
     */
    @Column(name = "area")
    private String area;

    /**
     * 详细地址
     */
    @Column(name = "detail_address")
    private String detailAddress;

    /**
     * 状态:0正常，1暂停
     */
    // 数据库类型为Integer
    @Column(name = "status")
    private String status;
}
