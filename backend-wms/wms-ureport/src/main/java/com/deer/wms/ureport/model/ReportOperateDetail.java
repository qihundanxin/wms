package com.deer.wms.ureport.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 装箱详情
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "report_operate_detail")
public class ReportOperateDetail {
    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_operate_detail_id")
    private Integer reportOperateDetailId;

    /**
     * 操作类型： 1-验收 2-上架 3-拣货
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 创建用户id
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建人
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 物料编码
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 物料名称
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * 商品图片
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 仓库ID
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 仓库名称
     */
    @Column(name = "ware_name")
    private String wareName;

    /**
     * 装箱记录id
     */
    @Column(name = "report_operate_id")
    private Integer reportOperateId;
}
