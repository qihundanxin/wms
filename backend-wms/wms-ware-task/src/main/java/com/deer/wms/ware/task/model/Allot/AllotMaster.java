package com.deer.wms.ware.task.model.Allot;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 调拨单信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "allot_master")
public class AllotMaster {

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allot_master_id")
    private Integer allotMasterId;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 状态 -1审核被退回 0-初始化 1-部分调拨  2-全部调拨
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 仓库ID
     */
    @Column(name = "from_ware_id")
    private Integer fromWareId;

    /**
     * 仓库名称
     */
    @Column(name = "from_ware_name")
    private String fromWareName;

    /**
     * 调入仓库ID
     */
    @Column(name = "to_ware_id")
    private Integer toWareId;

    /**
     * 调入仓库名称
     */
    @Column(name = "to_ware_name")
    private String toWareName;

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
     * 要求交货时间
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
     * 审核状态   2-等待审核  1-审核通过 0-审核中 -1 -审核退回 
     */
    @Column(name = "case_state")
    private Integer caseState;

    /**
     * 0-提交  -2保存为草稿
     */
    @Column(name = "insert_type")
    private Integer insertType;

    /**
     * 自定义1
     */
    @Column(name = "allot_udf_hs1")
    private String allotUdfHs1;

    /**
     * 自定义2
     */
    @Column(name = "allot_udf_hs2")
    private String allotUdfHs2;

    /**
     * 自定义3
     */
    @Column(name = "allot_udf_hs3")
    private String allotUdfHs3;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

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
}
