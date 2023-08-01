package com.deer.wms.review.manage.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 审核人信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "review_user")
public class ReviewUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 审核节点id
     */
    @Column(name = "review_node_id")
    private Integer reviewNodeId;

    /**
     * 审核人id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 审核人民称
     */
    @Column(name = "user_name")
    private String userName;
}
