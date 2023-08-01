package com.deer.wms.busine.tactic.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 路线规则
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "route_tactic")
public class RouteTactic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 路线
     */
    @Column(name = "route")
    private Integer route;

    /**
     * 规则编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 最小重量
     */
    @Column(name = "min_weight")
    private Double minWeight;

    /**
     * 最大重量
     */
    @Column(name = "max_weight")
    private Double maxWeight;

    /**
     * 最小体积
     */
    @Column(name = "min_size")
    private Double minSize;

    /**
     * 最大体积
     */
    @Column(name = "max_size")
    private Double maxSize;

    /**
     * 承运人Id
     */
    @Column(name = "carrier_id")
    private Integer carrierId;

    /**
     * 承运人
     */
    @Column(name = "carrier_name")
    private String carrierName;

    /**
     * 货主id
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 仓库id
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 状态 1-激活 !=1 未激活
     */
    @Column(name = "state")
    private Integer state;
}
