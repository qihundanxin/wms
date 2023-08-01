package com.deer.wms.ASN.manage.model.in;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 验收记录表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "accept_record")
public class AcceptRecord {

    // 兼容旧的String类型日期
    public String getAcceptTime() {
        return DateUtils.dateToStrNullable(this.acceptTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = DateUtils.strToDate(acceptTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accept_record_id")
    private Integer acceptRecordId;

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
     * 批次ID
     */
    @Column(name = "batch_id")
    private Integer batchId;

    /**
     * 批次名称
     */
    @Column(name = "batch_name")
    private String batchName;

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
     * 型号
     */
    @Column(name = "model")
    private String model;

    /**
     * 规格
     */
    @Column(name = "spec")
    private String spec;

    /**
     * 商品图片
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 验收人ID
     */
    @Column(name = "accept_user_id")
    private Integer acceptUserId;

    /**
     * 验收人
     */
    @Column(name = "accept_user_name")
    private String acceptUserName;

    /**
     * 验收时间
     */
    @Column(name = "accept_time")
    private Date acceptTime;

    /**
     * 追踪号
     */
    @Column(name = "box_code")
    private String boxCode;

    /**
     * 货位编码
     */
    @Column(name = "cell_code")
    private String cellCode;

    /**
     * 包装ID
     */
    @Column(name = "pack_detail_id")
    private Integer packDetailId;

    /**
     * 包装详细描述
     */
    @Column(name = "pack_describe")
    private String packDescribe;

    /**
     * 转换系数
     */
    @Column(name = "trans_ratio")
    private Double transRatio;

    /**
     * 数量
     */
    @Column(name = "quantity")
    private Double quantity;

    /**
     * 预期数量
     */
    @Column(name = "expect_quantity")
    private Double expectQuantity;

    /**
     * 预期包装详情ID
     */
    @Column(name = "expect_pack_detail_id")
    private Integer expectPackDetailId;

    /**
     * 预期包装描述
     */
    @Column(name = "expect_pack_describe")
    private String expectPackDescribe;

    /**
     * 预期转换系数
     */
    @Column(name = "expect_trans_ratio")
    private Double expectTransRatio;

    /**
     * 类型：0-默认验收 1-采购入库 2-ASN单 3-盲收 4-冻结
     */
    @Column(name = "bill_type")
    private Integer billType;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * asn详情单号
     */
    @Column(name = "asn_bill_no")
    private String asnBillNo;

    /**
     * asn详情单号
     */
    @Column(name = "asn_detail_no")
    private String asnDetailNo;

    /**
     * 采购单单号
     */
    @Column(name = "request_bill_no")
    private String requestBillNo;

    /**
     * 采购详情单号
     */
    @Column(name = "request_detail_detail_no")
    private String requestDetailDetailNo;

    /**
     * 异常入库id
     */
    @Column(name = "accept_ex_id")
    private Integer acceptExId;

    /**
     * 库存id
     */
    @Column(name = "inventory_id")
    private Integer inventoryId;

    /**
     * 快递单号
     */
    @Column(name = "ship_bill_no")
    private String shipBillNo;

    /**
     * 0-默认 1-同步erp中 2-同步成功
     */
    @Column(name = "state")
    private Integer state;
}
