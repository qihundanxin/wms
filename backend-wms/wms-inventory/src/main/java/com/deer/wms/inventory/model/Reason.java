package com.deer.wms.inventory.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 原因信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "reason")
public class Reason {

    /**
     * 原因id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reason_id")
    private Integer reasonId;

    /**
     * 原因
     */
    @Column(name = "reason")
    private String reason;
}
