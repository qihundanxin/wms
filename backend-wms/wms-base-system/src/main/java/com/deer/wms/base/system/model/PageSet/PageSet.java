package com.deer.wms.base.system.model.PageSet;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 页面显示表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "page_set")
public class PageSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "page_set_code")
    private String pageSetCode;

    @Column(name = "page_set_name")
    private String pageSetName;

    @Column(name = "value")
    private String value;

    @Column(name = "table_name")
    private String tableName;
}
