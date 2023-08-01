package com.deer.wms.ware.task.model.SO;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * SO发运单master 归档
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "so_master_file")
public class SoMasterFile {
    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getDeliveryTime() {
        return DateUtils.dateToStrNullable(this.deliveryTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = DateUtils.strToDate(deliveryTime);
    }

    public String getExpectTimeFm() {
        return DateUtils.dateToStrNullable(this.expectTimeFm, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setExpectTimeFm(String expectTimeFm) {
        this.expectTimeFm = DateUtils.strToDate(expectTimeFm);
    }

    public String getExpectTimeTo() {
        return DateUtils.dateToStrNullable(this.expectTimeTo, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setExpectTimeTo(String expectTimeTo) {
        this.expectTimeTo = DateUtils.strToDate(expectTimeTo);
    }


    public String getFileTime() {
        return DateUtils.dateToStrNullable(this.fileTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setFileTime(String fileTime) {
        this.fileTime = DateUtils.strToDate(fileTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "so_master_file_id")
    private Integer soMasterFileId;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 0-初始化 1-部分预分配 2-预分配完成 3-部分分配 4-分配完成 5-部分拣货 6-拣货完成 7-部分发运 8-完全发运 9-取消 10-完成 11-已合并
     */
    @Column(name = "state")
    private Integer state;

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
     * 客户ID
     */
    @Column(name = "customer_id")
    private Integer customerId;

    /**
     * 客户名称
     */
    @Column(name = "customer_name")
    private String customerName;

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

    /**
     * 交货时间
     */
    @Column(name = "delivery_time")
    private Date deliveryTime;

    /**
     * 预期发货时间FM
     */
    @Column(name = "expect_time_fm")
    private Date expectTimeFm;

    /**
     * 预期发货时间TO
     */
    @Column(name = "expect_time_to")
    private Date expectTimeTo;

    /**
     * 优先级
     */
    @Column(name = "order_no")
    private Integer orderNo;

    /**
     * 审核状态 2-等待审核 1-审核通过 0-审核中 -1 -审核退回
     */
    @Column(name = "case_state")
    private Integer caseState;

    /**
     * 单据来源 1-新建
     */
    @Column(name = "bill_source")
    private Integer billSource;

    /**
     * 0-提交 -2保存为草稿
     */
    @Column(name = "insert_type")
    private Integer insertType;

    /**
     * 自定义1
     */
    @Column(name = "so_udf_hs1")
    private String soUdfHs1;

    /**
     * 自定义2
     */
    @Column(name = "so_udf_hs2")
    private String soUdfHs2;

    /**
     * 自定义3
     */
    @Column(name = "so_udf_hs3")
    private String soUdfHs3;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 订单类型
     */
    @Column(name = "so_type")
    private Integer soType;

    /**
     * 收货人ID
     */
    @Column(name = "receiving_address_id")
    private Integer receivingAddressId;

    /**
     * 收货联系人
     */
    @Column(name = "link_man")
    private String linkMan;

    /**
     * 联系电话
     */
    @Column(name = "link_phone")
    private String linkPhone;

    /**
     * 所在省
     */
    @Column(name = "province")
    private String province;

    /**
     * 所在城
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
     * 合并单号
     */
    @Column(name = "merge_no")
    private String mergeNo;

    /**
     * 归档时间
     */
    @Column(name = "file_time")
    private Date fileTime;

    /**
     * 归档人ID
     */
    @Column(name = "file_user_id")
    private Integer fileUserId;

    /**
     * 归档人
     */
    @Column(name = "file_user_name")
    private String fileUserName;
}
