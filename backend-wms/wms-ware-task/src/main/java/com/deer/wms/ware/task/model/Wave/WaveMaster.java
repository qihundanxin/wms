package com.deer.wms.ware.task.model.Wave;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 波次master表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "wave_master")
public class WaveMaster {
    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getAllotTime() {
        return DateUtils.dateToStrNullable(this.allotTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setAllotTime(String allotTime) {
        this.allotTime = DateUtils.strToDate(allotTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wave_id")
    private Integer waveId;

    /**
     * 波次编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 波次策略代码
     */
    @Column(name = "wave_tactic_code")
    private String waveTacticCode;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

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
    @Column(name = "organization_idbak")
    private Integer organizationIdbak;

    /**
     * 货主名称
     */
    @Column(name = "organization_namebak")
    private String organizationNamebak;

    /**
     * 0-订单接收中  1-拣货中  2-拣货完成 3-复合完成
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 订单总数
     */
    @Column(name = "total_bill_count")
    private Integer totalBillCount;

    /**
     * sku总数
     */
    @Column(name = "total_sku_count")
    private Integer totalSkuCount;

    /**
     * 分配用户id
     */
    @Column(name = "allot_user_id")
    private Integer allotUserId;

    /**
     * 分配用户
     */
    @Column(name = "allot_user_name")
    private String allotUserName;

    /**
     * 分配时间
     */
    @Column(name = "allot_time")
    private Date allotTime;

    /**
     * 播种墙
     */
    @Column(name = "seeding_wall_code")
    private String seedingWallCode;

    /**
     * 拣货容器
     */
    @Column(name = "box_code")
    private String boxCode;

    /**
     * 完结方式：1-时间到完结，2-sku数量上限完结，3-订单数量上限完结
     */
    @Column(name = "end_type")
    private Integer endType;

    /**
     * 分拣方式：0-边拣边分 1-先拣后分
     */
    @Column(name = "is_seeding")
    private Integer isSeeding;
}
