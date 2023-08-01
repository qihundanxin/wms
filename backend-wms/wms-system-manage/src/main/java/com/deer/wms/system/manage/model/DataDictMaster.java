package com.deer.wms.system.manage.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 数据字典信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "data_dict_master")
public class DataDictMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_dict_master_id")
    private Integer dataDictMasterId;

    /**
     * 数据字典编码
     */
    @Column(name = "data_dict_code")
    private String dataDictCode;

    /**
     * 数据字典名称
     */
    @Column(name = "data_dict_name")
    private String dataDictName;

    /**
     * 0-可以修改  1-不可修改
     */
    @Column(name = "type")
    private Integer type;
}
