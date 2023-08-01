package com.deer.wms.system.manage.model.resource;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 资源路径信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "resource_url")
public class ResourceUrl {

    /**
     * 路径信息id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "url_id")
    private Integer urlId;

    /**
     * 资源信息id
     */
    @Column(name = "resource_id")
    private Integer resourceId;

    /**
     * 资源类型：F=功能；B=按钮；
     */
    @Column(name = "resource_type")
    private String resourceType;

    /**
     * 路径类型：P=页面；I=接口；
     */
    @Column(name = "url_type")
    private String urlType;

    /**
     * 请求方式：GET；POST；PUT；DELETE；
     */
    @Column(name = "method")
    private String method;

    /**
     * 路径地址
     */
    @Column(name = "url")
    private String url;

    /**
     * 信息状态：normal=正常的；invalid=无效的；deleted=已删除；默认正常的
     */
    @Column(name = "state")
    private String state;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;
}
