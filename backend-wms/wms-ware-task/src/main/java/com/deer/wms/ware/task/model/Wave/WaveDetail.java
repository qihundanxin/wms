package com.deer.wms.ware.task.model.Wave;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 波次detail表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "wave_detail")
public class WaveDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wave_detail_id")
    private Integer waveDetailId;

    /**
     * 出库单ID
     */
    @Column(name = "so_master_id")
    private Integer soMasterId;

    /**
     * 波次masterId
     */
    @Column(name = "wave_master_id")
    private Integer waveMasterId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;
}
