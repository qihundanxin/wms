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
 * 验收记录归档
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "accept_record_file")
public class AcceptRecordFile {

    // 兼容旧的String类型日期
    public String getAcceptTime() {
        return DateUtils.dateToStrNullable(this.acceptTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = DateUtils.strToDate(acceptTime);
    }

    public String getFileTime() {
        return DateUtils.dateToStrNullable(this.fileTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setFileTime(String fileTime) {
        this.fileTime = DateUtils.strToDate(fileTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accept_record_file_id")
    private Integer acceptRecordFileId;

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
     * 类型：0-默认验收 1-采购入库 2-新建入库单 3-盲收 4-冻结
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
    @Column(name = "asn_detail_no")
    private String asnDetailNo;

    /**
     * 采购详情单号
     */
    @Column(name = "request_detail_no")
    private String requestDetailNo;

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
