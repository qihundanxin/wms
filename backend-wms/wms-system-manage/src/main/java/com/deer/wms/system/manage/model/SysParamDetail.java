package com.deer.wms.system.manage.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 系统参数详情表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "sys_param_detail")
public class SysParamDetail {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sys_param_detail_id")
    private Integer sysParamDetailId;

    /**
     * 外键
     */
    @Column(name = "sys_param_id")
    private Integer sysParamId;

    /**
     * 参数值
     */
    @Column(name = "detail_value")
    private String detailValue;

    /**
     * 参数值含义
     */
    @Column(name = "detail_name")
    private String detailName;
}
