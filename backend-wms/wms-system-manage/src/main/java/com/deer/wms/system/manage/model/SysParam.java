package com.deer.wms.system.manage.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 系统参数表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "sys_param")
public class SysParam {

    /**
     * 系统参数ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sys_param_id")
    private Integer sysParamId;

    /**
     * 参数编码
     */
    @Column(name = "param_code")
    private String paramCode;

    /**
     * 系统参数名
     */
    @Column(name = "param_name")
    private String paramName;

    /**
     * 参数值
     */
    @Column(name = "value")
    private String value;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 0-开关  1-选择  2-输入数字   3-输入String
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 所属模块
     */
    @Column(name = "module_code")
    private String moduleCode;
}
