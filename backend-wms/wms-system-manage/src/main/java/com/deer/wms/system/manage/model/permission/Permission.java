package com.deer.wms.system.manage.model.permission;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 权限信息，保存角色、用户权限的信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "permission")
public class Permission {

    /**
     * 权限信息id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Integer permissionId;

    /**
     * 资源信息id
     */
    @Column(name = "resource_id")
    private Integer resourceId;

    /**
     * 授权对象类型：user=用户授权；role=角色授权；
     */
    @Column(name = "object_type")
    private String objectType;

    /**
     * 授权对象信息id：如果是用户授权就是用户信息id，如果是角色授权就是角色信息id
     */
    @Column(name = "object_id")
    private Integer objectId;

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
