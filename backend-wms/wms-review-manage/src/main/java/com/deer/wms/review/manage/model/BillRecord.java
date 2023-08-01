package com.deer.wms.review.manage.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 订单跟踪记录
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "bill_record")
public class BillRecord {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getModifyTime() {
        return DateUtils.dateToStrNullable(this.modifyTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = DateUtils.strToDate(modifyTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_record_id")
    private Integer billRecordId;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 节点名称： -11：拣货退回订单；-1：SO修改；1：生成ASN；2：ASN草稿；4：ASN验收；5：ASN修改；5： SO修改；6：ASN归档；7：采购新增；
     * 8：采购草稿；11：采购归档；12：QC质检；14：采购单修改；15：QC质检新增；16：QC质检草稿；17：QC质检审核；19：QC质检归档；
     * 21：ASN生成质检；22：SO新增；23：SO草稿；26：SO归档；27：SO修改；29：冻结新增；30：冻结单草稿；31：冻结审核；33：冻结修改；
     * 34：单条释放；35：冻结单整体释放；36：库存调整新增；37：库存调整审核；41： 调拨生成ASN；42：调拨生成SO；43：库存调整单草稿；
     * 43：销售新增；44：销售草稿】；45：销售审核；46：生成SO；47：退货新增；48：退货草稿；49：退货审核；50：退货修改；51：库存冻结单删除
     *  60:创建invoice信息; 61:删除invoice信息;62:更新invoice信息;
     */
    @Column(name = "record_type")
    private Integer recordType;

    /**
     * 操作时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 操作人员Id
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 操作人员名称
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 结束时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 结束人id
     */
    @Column(name = "modify_user_id")
    private Integer modifyUserId;

    /**
     * 结束人名称
     */
    @Column(name = "modify_user_name")
    private String modifyUserName;

    /**
     * 结果
     */
    @Column(name = "result")
    private String result;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;
}
