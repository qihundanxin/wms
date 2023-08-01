package com.deer.wms.base.system.model.Pack;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 包装详细信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "pack_detail")
public class PackDetail {
    // 兼容旧的 getter setter
    public Boolean getBoxing() {
        return this.isBoxing;
    }

    public void setBoxing(Boolean isBoxing) {
        this.isBoxing = isBoxing;
    }

    public Boolean getStorageLabel() {
        return this.isStorageLabel;
    }

    public void setStorageLabel(Boolean isStorageLabel) {
        this.isStorageLabel = isStorageLabel;
    }

    public Boolean getReplenishLabel() {
        return this.isReplenishLabel;
    }

    public void setReplenishLabel(Boolean isReplenishLabel) {
        this.isReplenishLabel = isReplenishLabel;
    }

    public Boolean getOutboundLabel() {
        return this.isOutboundLabel;
    }

    public void setOutboundLabel(Boolean isOutboundLabel) {
        this.isOutboundLabel = isOutboundLabel;
    }

    public Boolean getSerialNumber() {
        return this.isSerialNumber;
    }

    public void setSerialNumber(Boolean isSerialNumber) {
        this.isSerialNumber = isSerialNumber;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pack_detail_id")
    private Integer packDetailId;

    /**
     * 包装描述
     */
    @Column(name = "pack_describe")
    private String packDescribe;

    /**
     * 包装编码
     */
    @Column(name = "pack_code")
    private String packCode;

    /**
     * 装箱 false-0 true-1
     */
    @Column(name = "is_boxing")
    private Boolean isBoxing;

    /**
     * 入库标签 false-0 true-1
     */
    @Column(name = "is_storage_label")
    private Boolean isStorageLabel;

    /**
     * 补货标签 false-0 true-1
     */
    @Column(name = "is_replenish_label")
    private Boolean isReplenishLabel;

    /**
     * 出库标签 false-0 true-1
     */
    @Column(name = "is_outbound_label")
    private Boolean isOutboundLabel;

    /**
     * 序列号 false-0 true-1
     */
    @Column(name = "is_serial_number")
    private Boolean isSerialNumber;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 转换系数
     */
    @Column(name = "trans_ratio")
    private Double transRatio;

    /**
     * 包装ID
     */
    @Column(name = "pack_id")
    private Integer packId;

    /**
     * 0-所有 1-主单位 2-内包装 3-箱 4-栈板 5-其他
     */
    @Column(name = "pack_level")
    private Integer packLevel;

    /**
     * 0-不启动 1-启用
     */
    @Column(name = "state")
    private Boolean state;
}
