package com.deer.wms.ware.task.model.SO;

import java.util.Date;

import javax.persistence.*;

import com.deer.wms.base.system.enums.SendState;
import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * SO发运单 master
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "so_master")
public class SoMaster {
    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getOutTime() {
        return DateUtils.dateToStrNullable(this.outTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setOutTime(String outTime) {
        this.outTime = DateUtils.strToDate(outTime);
    }

    public String getOrderTime() {
        return DateUtils.dateToStrNullable(this.orderTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = DateUtils.strToDate(orderTime);
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

    public String getActualDeliveryTime() {
        return DateUtils.dateToStrNullable(this.actualDeliveryTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setActualDeliveryTime(String actualDeliveryTime) {
        this.actualDeliveryTime = DateUtils.strToDate(actualDeliveryTime);
    }

    public String getCheckTime() {
        return DateUtils.dateToStrNullable(this.checkTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = DateUtils.strToDate(checkTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "so_master_id")
    private Integer soMasterId;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 0-初始化 1-审核通过 2-预分配完成 3-部分分配 4-分配完成 5-部分拣货 6-拣货完成 7-复核完成 8-完全发运  9-取消 10-完成  11-已合并 12 部分发运
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 发运状态
     */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "send_state")
    private SendState sendState;
    /**
     * 仓库ID
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 仓库编码
     */
    @Column(name = "ware_code")
    private String wareCode;

    /**
     * 仓库名称
     */
    @Column(name = "ware_name")
    private String wareName;

    /**
     * 中文申请品名
     */
    @Column(name = "cn_declaration_name")
    private String cnDeclarationName;

    /**
     * 英文申请品名
     */
    @Column(name = "declaration_name")
    private String declarationName;

    /**
     * 货主ID
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 货主编码
     */
    @Column(name = "organization_code")
    private String organizationCode;

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

    @Column(name = "out_user_id")
    private Integer outUserId;

    @Column(name = "out_user_name")
    private String outUserName;

    @Column(name = "out_time")
    private Date outTime;

    /**
     * 下单时间
     */
    @Column(name = "order_time")
    private Date orderTime;

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
     * 审核状态 2-等待审核 1-审核通过 0-审核中 -1 -审核退回
     */
    @Column(name = "case_state")
    private Integer caseState;

    /**
     * 单据来源 1-新建 2-接口 3-调拨
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
     * 快递公司
     */
    @Column(name = "ship_code")
    private String shipCode;

    /**
     * 快递单号
     */
    @Column(name = "ship_bill_code")
    private String shipBillCode;

    /**
     * 面单地址
     */
    @Column(name = "express_bill_url")
    private String expressBillUrl;

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
     * 联系人邮政编码
     */
    @Column(name = "link_post_code")
    private String linkPostCode;

    /**
     * 所属国家
     */
    @Column(name = "country")
    private String country;

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
     * 快递联系人
     */
    @Column(name = "ship_link_man")
    private String shipLinkMan;

    /**
     * 快递联系电话
     */
    @Column(name = "ship_link_phone")
    private String shipLinkPhone;

    /**
     * 快递邮政编码
     */
    @Column(name = "ship_post_code")
    private String shipPostCode;

    /**
     * 快递所属国家
     */
    @Column(name = "ship_country")
    private String shipCountry;

    /**
     * 快递所属省份
     */
    @Column(name = "ship_province")
    private String shipProvince;

    /**
     * 快递所属城市
     */
    @Column(name = "ship_city")
    private String shipCity;

    /**
     * 快递所属区域
     */
    @Column(name = "ship_area")
    private String shipArea;

    /**
     * 快递详细地址
     */
    @Column(name = "ship_detail_address")
    private String shipDetailAddress;

    /**
     * 合并单号
     */
    @Column(name = "merge_no")
    private String mergeNo;

    /**
     * 实际发货时间
     */
    @Column(name = "actual_delivery_time")
    private Date actualDeliveryTime;

    /**
     * 调拨单号
     */
    @Column(name = "allot_bill_no")
    private String allotBillNo;

    /**
     * 生成加工单：1-是 0-否
     */
    @Column(name = "manufacture_type")
    private Integer manufactureType;

    /**
     * 总价格
     */
    @Column(name = "total_price")
    private Double totalPrice;

    /**
     * 审核人id
     */
    @Column(name = "check_user_id")
    private Integer checkUserId;

    /**
     * 审核时间
     */
    @Column(name = "check_time")
    private Date checkTime;

    /**
     * 审核人
     */
    @Column(name = "check_user_name")
    private String checkUserName;

    /**
     * 0-默认 1-同步中 2-已同步
     */
    @Column(name = "erp_state")
    private Integer erpState;

    /**
     * 预生成电子面单pdf
     */
    @Column(name = "dzmd_pdf")
    private String dzmdPdf;

    /**
     * 重量
     */
    @Column(name = "weight")
    private Double weight;

    /**
     * 高度
     */
    @Column(name = "height")
    private Double height;

    /**
     * 宽度
     */
    @Column(name = "width")
    private Double width;

    /**
     * 长度
     */
    @Column(name = "length")
    private Double length;

    /**
     * 最后里程 排序编码
     */
    @Column(name = "last_mile_sorting_code")
    private String lastMileSortingCode;

    /**
     * 最后里程 目的地
     */
    @Column(name = "last_mile_destination")
    private String lastMileDestination;

    /**
     * 最后里程 线路
     */
    @Column(name = "last_mile_line_code")
    private String lastMileLineCode;

    /**
     * 明细行数
     */
    @Column(name = "so_structure")
    private Integer soStructure;

    /**
     * 交易号
     */
    @Column(name = "trade_no")
    private String tradeNo;

    /**
     * 店铺
     */
    @Column(name = "shop")
    private String shop;

    /**
     * 优先级数值
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * 播种墙格子
     */
    @Column(name = "seeding_cell_code")
    private String seedingCellCode;

    /**
     * 播种篮子
     */
    @Column(name = "box_code")
    private String boxCode;

    /**
     * 店铺订单日序号
     */
    @Column(name = "shop_daily_count")
    private Integer shopDailyCount;

    /**
     * pdfUrl
     */
    @Column(name = "pdf_url")
    private String pdfUrl;

    /**
     * 打印状态
     */
    @Column(name = "print_state")
    private Integer printState;

    /**
     * 波次masterId
     */
    @Column(name = "wave_master_id")
    private Integer waveMasterId;

    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;
}
