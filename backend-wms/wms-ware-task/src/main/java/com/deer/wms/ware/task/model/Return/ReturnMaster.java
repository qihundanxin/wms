package com.deer.wms.ware.task.model.Return;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 退货master信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "return_master")
public class ReturnMaster {
    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_master_id")
    private Integer returnMasterId;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 仓库id
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 仓库名称
     */
    @Column(name = "ware_name")
    private String wareName;

    /**
     * 货主ID
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 货主名称
     */
    @Column(name = "organization_name")
    private String organizationName;

    /**
     * 供应商ID
     */
    @Column(name = "supplier_id")
    private Integer supplierId;

    /**
     * 供应商名字
     */
    @Column(name = "supplier_name")
    private String supplierName;

    /**
     * 承运方ID
     */
    @Column(name = "carrier_id")
    private Integer carrierId;

    /**
     * 承运方
     */
    @Column(name = "carrier_name")
    private String carrierName;

    /**
     * 收货联系人
     */
    @Column(name = "link_man")
    private String linkMan;

    /**
     * 所在省份
     */
    @Column(name = "province")
    private String province;

    /**
     * 所在市、县
     */
    @Column(name = "city")
    private String city;

    /**
     * 所在区
     */
    @Column(name = "area")
    private String area;

    /**
     * 详细地址
     */
    @Column(name = "detail_address")
    private String detailAddress;

    /**
     * 审核状态   2-等待审核  1-审核通过 0-审核中 -1 -审核退回
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 单据来源 
     */
    @Column(name = "bill_source")
    private Integer billSource;

    /**
     * 0-提交  -2保存为草稿
     */
    @Column(name = "insert_type")
    private Integer insertType;

    /**
     * 退货自定义1
     */
    @Column(name = "return_udf_hs1")
    private String returnUdfHs1;

    /**
     * 退货自定义2
     */
    @Column(name = "return_udf_hs2")
    private String returnUdfHs2;

    /**
     * 退货自定义3
     */
    @Column(name = "return_udf_hs3")
    private String returnUdfHs3;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建人
     */
    @Column(name = "create_user_name")
    private String createUserName;
}
