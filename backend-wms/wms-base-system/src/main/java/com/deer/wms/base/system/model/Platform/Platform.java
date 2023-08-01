package com.deer.wms.base.system.model.Platform;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 月台
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "platform")
public class Platform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 月台编码
     */
    @Column(name = "platform_code")
    private String platformCode;

    /**
     * 仓库id
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 上午时段 -开始时间
     */
    @Column(name = "start_time_one")
    private String startTimeOne;

    /**
     * 上午时段 - 结束时间
     */
    @Column(name = "end_time_one")
    private String endTimeOne;

    /**
     * 下午时段 -开始时间
     */
    @Column(name = "start_time_two")
    private String startTimeTwo;

    /**
     * 下午时段 -结束时间
     */
    @Column(name = "end_time_two")
    private String endTimeTwo;

    /**
     * 收货标志 0-fales 1-true
     */
    @Column(name = "receiving")
    private Boolean receiving;

    /**
     * 发货标志 0-fales 1-true
     */
    @Column(name = "delivery")
    private Boolean delivery;
}
