package com.deer.wms.ASN.manage.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 异常入库信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "accept_ex")
public class AcceptEx {

    // 兼容旧的String类型日期
    public String getReturnTime() {
        return DateUtils.dateToStrNullable(this.returnTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = DateUtils.strToDate(returnTime);
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accept_ex_id")
    private Integer acceptExId;

    /**
     * 异常数量
     */
    @Column(name = "quantity")
    private Double quantity;

    /**
     * 订单数量
     */
    @Column(name = "order_quantity")
    private Double orderQuantity;

    /**
     * 已入库数量
     */
    @Column(name = "in_quantity")
    private Double inQuantity;

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
     * 规格
     */
    @Column(name = "spec")
    private String spec;

    /**
     * 型号
     */
    @Column(name = "model")
    private String model;

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
     * 异常类型：1-包裹异常；2-商品异常
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 异常原因：1-多发；2-少发；3-错发；4-破损；5-其他
     */
    @Column(name = "reason")
    private Integer reason;

    /**
     * 描述
     */
    @Column(name = "memo")
    private String memo;

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
     * 创建人名称
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 状态：0-初始化 1-提交ERP 2-待处理 3-已经处理
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 快递单号
     */
    @Column(name = "ship_bill_no")
    private String shipBillNo;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 详情单号
     */
    @Column(name = "bill_detail_no")
    private String billDetailNo;

    /**
     * 处理方案：1-全部退件;2-全部弃件;3-全部入库;4-部分入库部分退件;5-部分入库部分弃件
     */
    @Column(name = "fangan")
    private Integer fangan;

    /**
     * 退库数量
     */
    @Column(name = "return_quantity")
    private Double returnQuantity;

    /**
     * erp返回-物流公司
     */
    @Column(name = "return_ship_code")
    private String returnShipCode;

    /**
     * erp返回-快递单号
     */
    @Column(name = "return_ship_bill_code")
    private String returnShipBillCode;

    /**
     * erp返回-联系人
     */
    @Column(name = "return_link_man")
    private String returnLinkMan;

    /**
     * erp返回-联系电话
     */
    @Column(name = "return_link_phone")
    private String returnLinkPhone;

    /**
     * erp返回-快递电子面单地址
     */
    @Column(name = "return_express_bill_url")
    private String returnExpressBillUrl;

    /**
     * erp返回-国家
     */
    @Column(name = "return_country")
    private String returnCountry;

    /**
     * erp返回-省份
     */
    @Column(name = "return_province")
    private String returnProvince;

    /**
     * erp返回-城市
     */
    @Column(name = "return_city")
    private String returnCity;

    /**
     * erp返回-区域
     */
    @Column(name = "return_area")
    private String returnArea;

    /**
     * erp返回-详细地址
     */
    @Column(name = "return_detail_address")
    private String returnDetailAddress;

    /**
     * erp返回-状态：0-需要同步 1-同步中 2-同步成功
     */
    @Column(name = "return_state")
    private Integer returnState;

    /**
     * erp返回-时间
     */
    @Column(name = "return_time")
    private Date returnTime;

    /**
     * erp返回-用户名称
     */
    @Column(name = "return_user_name")
    private String returnUserName;

    /**
     * erp返回-用户id
     */
    @Column(name = "return_user_id")
    private Integer returnUserId;

    /**
     * erp处理方案备注
     */
    @Column(name = "erp_memo")
    private String erpMemo;
}
