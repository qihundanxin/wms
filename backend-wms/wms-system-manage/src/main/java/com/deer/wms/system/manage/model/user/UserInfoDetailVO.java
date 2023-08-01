package com.deer.wms.system.manage.model.user;

import java.util.List;

import lombok.ToString;

/**
 * 账户详细信息的视图类
 * <p>
 * Created by guo on 2018/8/11.
 */
@ToString
public class UserInfoDetailVO {
    /**
     * 用户信息id
     */
    private Integer userId;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 性别：1=先生；2=女士；
     */
    private Integer gender;

    /**
     * 账户创建人姓名
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 最后修改时间
     */
    private String modifyTime;

    /**
     * 账户角色id
     */
    private List<Integer> roleIds;

    /**
     * 账户角色名称
     */
    private List<String> roleNames;

    /**
     * 账户货主id
     */
    private List<Integer> organizationIds;

    /**
     * 账户货主名称
     */
    private List<String> organizationNames;

    /**
     * 账户仓库id
     */
    private List<Integer> wareIds;

    /**
     * 账户仓库名称
     */
    private List<String> wareNames;

    /**
     * 工号
     */
    private String jobNumber;

    /**
     * 部门id
     */
    private Integer deptId;

    /**
     * 部门
     */
    private String deptName;

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    public List<Integer> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(List<Integer> organizationIds) {
        this.organizationIds = organizationIds;
    }

    public List<String> getOrganizationNames() {
        return organizationNames;
    }

    public void setOrganizationNames(List<String> organizationNames) {
        this.organizationNames = organizationNames;
    }

    public List<Integer> getWareIds() {
        return wareIds;
    }

    public void setWareIds(List<Integer> wareIds) {
        this.wareIds = wareIds;
    }

    public List<String> getWareNames() {
        return wareNames;
    }

    public void setWareNames(List<String> wareNames) {
        this.wareNames = wareNames;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

}
