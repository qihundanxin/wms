package com.deer.wms.pdaApi.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * demo
 * 
 * @author xuesinuo
 *
 */
@Data
@Table(name = "demo")
public class PdaDemoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "demo_id")
    private Long id;

    /**
     * 描述
     */
    @Column(name = "name")
    private String name;
}