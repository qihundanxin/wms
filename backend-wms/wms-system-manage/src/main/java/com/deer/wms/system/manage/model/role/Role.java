package com.deer.wms.system.manage.model.role;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 角色信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "role")
public class Role {

    /**
     * 角色信息id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    /**
     * 角色代码
     */
    @Column(name = "role_code")
    private String roleCode;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 父级角色id，默认为0
     */
    @Column(name = "parent_role_id")
    private Integer parentRoleId;

    /**
     * 角色级别，默认1
     */
    @Column(name = "role_level")
    private Integer roleLevel;

    /**
     * 角色描述
     */
    @Column(name = "role_describe")
    private String roleDescribe;

    /**
     * 信息状态：normal=正常的；invalid=无效的；deleted=已删除；默认正常的
     */
    @Column(name = "state")
    private String state;

    /**
     * 创建人：如果为-1表示系统初始化
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人
     */
    @Column(name = "modify_user_id")
    private Integer modifyUserId;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 部门ID
     */
    @Column(name = "dept_id")
    private Integer deptId;
}
