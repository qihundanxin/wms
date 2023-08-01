package com.deer.wms.busine.tactic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 波次策略
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "wave_tactic_dianshang")
public class WaveTacticDianshang {
    // 兼容旧的String类型日期
    public String getLastTime() {
        return DateUtils.dateToStrNullable(this.lastTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setLastTime(String lastTime) {
        this.lastTime = DateUtils.strToDate(lastTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wave_tactic_id")
    private Integer waveTacticId;

    /**
     * 波次策略代码
     */
    @Column(name = "wave_tactic_code")
    private String waveTacticCode;
    /**
     * 波次策略名称
     */
    @Column(name = "wave_tactic_name")
    private String waveTacticName;

    /**
     * 优先级  数字越大 等级越高
     */
    @Column(name = "priority_level")
    private Integer priorityLevel;

    /**
     * 0-未激活，1-激活
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 仓库ID
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 客户
     */
    @Column(name = "customer_id")
    private Integer customerId;

    /**
     * 承运商
     */
    @Column(name = "carrier_id")
    private Integer carrierId;

    /**
     * 订单类型
     */
    @Column(name = "so_type")
    private Integer soType;

    /**
     * 订单结构  1:一单一品 2-一单多品 3：一单一品一件  4：一单一品多件
     */
    @Column(name = "so_structure")
    private Integer soStructure;

    /**
     * 订单发运截止时间
     */
    @Column(name = "so_deadline")
    private Date soDeadline;

    /**
     * 最大订单数
     */
    @Column(name = "so_count_limit")
    private Integer soCountLimit;

    /**
     * 最大SKU数
     */
    @Column(name = "item_count")
    private Integer itemCount;

    /**
     * 是否播种，1-是，0-否
     */
    @Column(name = "is_seeding")
    private Integer isSeeding;

    /**
     * 最长等待时间
     */
    @Column(name = "max_wait_time")
    private Integer maxWaitTime;

    /**
     * 最后时限
     */
    @Column(name = "last_time")
    private Date lastTime;
}
