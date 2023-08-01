package com.deer.wms.base.system.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 搜索，点击相关记录
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "select_product")
public class SelectProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "select_product_id")
    private Integer selectProductId;

    /**
     * 关键词
     */
    @Column(name = "target_keyword")
    private String targetKeyword;

    /**
     * 搜索/付款
     */
    @Column(name = "paid_order_count")
    private Double paidOrderCount;

    /**
     * 点击率
     */
    @Column(name = "click_through_rate")
    private Double clickThroughRate;

    @Column(name = "l")
    private String l;

    /**
     * 搜索次数
     */
    @Column(name = "search_popularity")
    private Double searchPopularity;

    /**
     * 付款率
     */
    @Column(name = "payment_rate")
    private Double paymentRate;
}
