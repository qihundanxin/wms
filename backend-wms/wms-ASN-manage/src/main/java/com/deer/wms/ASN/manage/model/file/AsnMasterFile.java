package com.deer.wms.ASN.manage.model.file;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * ASN信息归档
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "asn_master_file")
public class AsnMasterFile {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getExpectTime() {
        return DateUtils.dateToStrNullable(this.expectTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = DateUtils.strToDate(expectTime);
    }

    public String getFileTime() {
        return DateUtils.dateToStrNullable(this.fileTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setFileTime(String fileTime) {
        this.fileTime = DateUtils.strToDate(fileTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asn_master_file_id")
    private Integer asnMasterFileId;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * ASN类型
     */
    @Column(name = "bill_type")
    private Integer billType;

    /**
     * 采购单号
     */
    @Column(name = "request_buy_bill_no")
    private String requestBuyBillNo;

    /**
     * 状态 2-等待审核 1-审核通过 0-审核中 -1 -审核退回
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 优先级
     */
    @Column(name = "order_no")
    private Integer orderNo;

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
     * ASN状态 0-初始化 1-已码盘  2-部分收货  3-全部收货  4-ASN完成
     */
    @Column(name = "asn_state")
    private Integer asnState;

    /**
     * 预期到货时间
     */
    @Column(name = "expect_time")
    private Date expectTime;

    /**
     * 参考信息1
     */
    @Column(name = "asn_udf_hs1")
    private String asnUdfHs1;

    /**
     * 参考信息2
     */
    @Column(name = "asn_udf_hs2")
    private String asnUdfHs2;

    /**
     * 参考信息3
     */
    @Column(name = "asn_udf_hs3")
    private String asnUdfHs3;

    /**
     * 供应商ID
     */
    @Column(name = "supplier_id")
    private Integer supplierId;

    /**
     * 供应商名称
     */
    @Column(name = "supplier_name")
    private String supplierName;

    /**
     * 承运方编码
     */
    @Column(name = "carrier_code")
    private String carrierCode;

    /**
     * 质检状态
     */
    @Column(name = "quality_state")
    private Integer qualityState;

    /**
     * 月台编码
     */
    @Column(name = "platform_code")
    private String platformCode;

    /**
     * 业务员
     */
    @Column(name = "salesman")
    private String salesman;

    /**
     * 装货地址
     */
    @Column(name = "loading_place")
    private String loadingPlace;

    /**
     * 交货地
     */
    @Column(name = "delivery_place")
    private String deliveryPlace;

    /**
     * 运输描述
     */
    @Column(name = "traffic_describe")
    private String trafficDescribe;

    /**
     * 0-提交  -2保存为草稿
     */
    @Column(name = "insert_type")
    private Integer insertType;

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

    /**
     * 单据来源 1-新建 0-采购单生成
     */
    @Column(name = "bill_source")
    private Integer billSource;

    /**
     * 是否需要序列号 0-不需要 1-需要
     */
    @Column(name = "is_serial_no")
    private Integer isSerialNo;

    /**
     * 是否需要打印 0-不需要 1-需要
     */
    @Column(name = "is_print")
    private Integer isPrint;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 单据类型：24-采购入库 25-紧急采购 44-退货入库
     */
    @Column(name = "asn_type")
    private Integer asnType;
}
