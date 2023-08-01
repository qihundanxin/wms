package com.deer.wms.system.manage.model.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 用户信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "user_info")
public class UserInfo {
    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getModifyTime() {
        return DateUtils.dateToStrNullable(this.modifyTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = DateUtils.strToDate(modifyTime);
    }

    /**
     * 用户信息id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 登录帐号
     */
    @Column(name = "account")
    private String account;

    /**
     * 登录密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 密码盐值
     */
    @Column(name = "salt")
    private String salt;

    /**
     * 姓名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 性别：1=先生；2=女士；
     */
    @Column(name = "gender")
    private Integer gender;

    /**
     * 手机号码
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 电子邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 头像地址
     */
    @Column(name = "icon_url")
    private String iconUrl;

    /**
     * 跳转地址
     */
    @Column(name = "url")
    private String url;

    /**
     * 跳转地址名称
     */
    @Column(name = "url_name")
    private String urlName;

    /**
     * 账户状态：enable=启用；disable=停用；默认启用
     */
    @Column(name = "account_status")
    private String accountStatus;

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
     * 是否管理员：0=不是；1=是；默认0
     */
    // 数据库类型为Boolen
    @Column(name = "is_admin")
    private Integer isAdmin;

    /**
     * 部门ID
     */
    @Column(name = "dept_id")
    private Integer deptId;

    /**
     * 工号
     */
    @Column(name = "job_number")
    private String jobNumber;

    /**
     * token
     */
    @Column(name = "token")
    private String token;

    /**
     * 1、操作员工 2、第三方APP
     */
    @Column(name = "user_type")
    private Integer userType;
}
