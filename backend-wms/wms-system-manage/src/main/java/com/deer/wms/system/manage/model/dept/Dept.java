package com.deer.wms.system.manage.model.dept;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 部门信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "dept")
public class Dept {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Integer deptId;

    /**
     * 部门名
     */
    @Column(name = "dept_name")
    private String deptName;

    /**
     * 部门编码
     */
    @Column(name = "dept_code")
    private String deptCode;

    /**
     * 父级ID 
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;
}
