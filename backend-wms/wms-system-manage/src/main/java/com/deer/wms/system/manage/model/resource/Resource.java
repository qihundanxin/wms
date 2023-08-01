package com.deer.wms.system.manage.model.resource;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 资源信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "resource")
public class Resource {

    /**
     * 资源信息id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Integer resourceId;

    /**
     * 资源代码
     */
    @Column(name = "resource_code")
    private String resourceCode;

    /**
     * 资源名称
     */
    @Column(name = "resource_name")
    private String resourceName;

    /**
     * 父级资源信息id，一级资源为-1
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 资源类型：M=模块；F=功能；B=按钮；
     */
    @Column(name = "resource_type")
    private String resourceType;

    /**
     * 是否权限控制：0=否；1=是；默认是
     */
    @Column(name = "control")
    private Boolean control;

    /**
     * 资源显示区域：L=左边部位；T=顶部；I=首页；默认L
     */
    @Column(name = "region")
    private String region;

    /**
     * 层级
     */
    @Column(name = "level")
    private Integer level;

    /**
     * 图标
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 样式
     */
    @Column(name = "style")
    private String style;

    /**
     * 排序号
     */
    @Column(name = "sort_number")
    private Integer sortNumber;

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

    /**
     * 1-仓库作业 2-基础信息  3-报表
     */
    @Column(name = "resource_group")
    private Integer resourceGroup;

    /**
     * 权限标识
     */
    @Column(name = "authority_code")
    private String  authorityCode;
}
