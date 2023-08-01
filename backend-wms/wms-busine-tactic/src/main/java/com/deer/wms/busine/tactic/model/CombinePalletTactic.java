package com.deer.wms.busine.tactic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 合托策略
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "combine_pallet_tactic")
public class CombinePalletTactic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 是否允许混料
     */
    @Column(name = "mix_item")
    private Integer mixItem;

    /**
     * 是否允许混批次
     */
    @Column(name = "mix_batch")
    private Integer mixBatch;

    /**
     * 最大体积
     */
    @Column(name = "size_limit")
    private Double sizeLimit;

    /**
     * 托盘最大容量
     */
    @Column(name = "quantity_limit")
    private Double quantityLimit;

    /**
     * 最大重量
     */
    @Column(name = "weight_limit")
    private Double weightLimit;

    /**
     * 1 最少搬运 2 最少托盘
     */
    @Column(name = "type")
    private Integer type;
}
