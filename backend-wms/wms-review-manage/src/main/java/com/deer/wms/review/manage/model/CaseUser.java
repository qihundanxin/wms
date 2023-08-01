package com.deer.wms.review.manage.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 审核人
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "case_user")
public class CaseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_user_id")
    private Integer caseUserId;

    /**
     * 审核详情id
     */
    @Column(name = "case_detail_id")
    private Integer caseDetailId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 用户名称
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 0-待处理   -1 不通过   1 通过
     */
    @Column(name = "review_result")
    private Boolean reviewResult;

    /**
     * 描述
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 审核时间
     */
    @Column(name = "review_time")
    private Date reviewTime;
}
