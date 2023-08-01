package com.deer.wms.review.manage.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 审核节点表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "review_node")
public class ReviewNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_node_id")
    private Integer reviewNodeId;

    /**
     * 节点名称
     */
    @Column(name = "review_node_name")
    private String reviewNodeName;

    /**
     * 审核master id
     */
    @Column(name = "review_master_id")
    private Integer reviewMasterId;

    /**
     * 节点类型 0-与节点 1-或节点
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 单号
     */
    @Column(name = "order_no")
    private Integer orderNo;
}
