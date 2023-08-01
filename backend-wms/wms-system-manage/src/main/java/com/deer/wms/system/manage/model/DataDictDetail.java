package com.deer.wms.system.manage.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 数据字典详细信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "data_dict_detail")
public class DataDictDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_dict_detail_id")
    private Integer dataDictDetailId;

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
     * 字典钥匙
     */
    @Column(name = "dict_key")
    private String dictKey;

    /**
     * 字典值
     */
    @Column(name = "dict_value")
    private String dictValue;

    /**
     * 图标类型
     */
    @Column(name = "icon_class")
    private String iconClass;
}
