package com.deer.wms.ware.task.model.WCS;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "wcs_info")
public class WcsInfo {

    // 原实体包含以下无效属性
//  conveyorAll
//  wcsIP
//  wcsPort
//  createTime

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 任务号
     */
    @Column(name = "task_no")
    private String taskNo;
}
