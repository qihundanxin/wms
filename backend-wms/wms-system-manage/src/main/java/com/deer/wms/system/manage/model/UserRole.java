package com.deer.wms.system.manage.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 用户角色信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "user_role")
public class UserRole {

    /**
     * 用户角色信息id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private Integer userRoleId;

    /**
     * 用户信息id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 角色信息id
     */
    @Column(name = "role_id")
    private Integer roleId;

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
