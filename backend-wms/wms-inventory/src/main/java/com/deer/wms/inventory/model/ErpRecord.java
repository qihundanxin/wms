package com.deer.wms.inventory.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * erp 记录
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "erp_record")
public class ErpRecord {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getRequestTime() {
        return DateUtils.dateToStrNullable(this.requestTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = DateUtils.strToDate(requestTime);
    }

    public String getReturnTime() {
        return DateUtils.dateToStrNullable(this.returnTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = DateUtils.strToDate(returnTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "erp_record_id")
    private Integer erpRecordId;

    /**
     * 接口 ：1-入库成功 2-入库异常 3-出库成功 4-退件信息
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 状态：1-同步失败 2-同步成功
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 请求时间
     */
    @Column(name = "request_time")
    private Date requestTime;

    /**
     * 返回时间
     */
    @Column(name = "return_time")
    private Date returnTime;

    /**
     * param
     */
    @Column(name = "param")
    private String param;

    /**
     * code
     */
    @Column(name = "code")
    private String code;

    /**
     * msg
     */
    @Column(name = "msg")
    private String msg;
}
