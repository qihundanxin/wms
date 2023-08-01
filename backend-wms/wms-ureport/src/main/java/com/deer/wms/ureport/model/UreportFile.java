package com.deer.wms.ureport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * Ureport 文件
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "ureport_file")
public class UreportFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 文件名称
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * 内容
     */
    // 数据库类型为mediumblob文件
    @Column(name = "content")
    private byte[] content;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 资源id
     */
    @Column(name = "resource_id")
    private Integer resourceId;
}
