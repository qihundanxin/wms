package com.deer.wms.busine.tactic.model.pickTactic;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 拣货策略
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "pick_tactic")
public class PickTactic {
    // 兼容旧的 getter setter
    public Integer getStorageChooseEA() {
        return storageChooseEa;
    }

    public void setStorageChooseEA(Integer storageChooseEa) {
        this.storageChooseEa = storageChooseEa;
    }

    public Integer getStorageChooseCS() {
        return storageChooseCs;
    }

    public void setStorageChooseCS(Integer storageChooseCs) {
        this.storageChooseCs = storageChooseCs;
    }

    /**
     * 领料策略Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pick_tactic_id")
    private Integer pickTacticId;

    /**
     * 规则编码
     */
    @Column(name = "allocation_rule_code")
    private String allocationRuleCode;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 存储位向上拆解
     */
    @Column(name = "up_dismantle")
    private Integer upDismantle;

    /**
     * 存储位向下拆解
     */
    @Column(name = "down_dismantle")
    private Integer downDismantle;

    /**
     * 存储位是否拆包
     */
    @Column(name = "whether_dismantle")
    private Integer whetherDismantle;

    /**
     * 存储位清仓优先
     */
    @Column(name = "clearance_priority")
    private Integer clearancePriority;

    /**
     * 存储位最优算法
     */
    @Column(name = "optimal_algorithm")
    private Integer optimalAlgorithm;

    /**
     * 拣货位超量分配
     */
    @Column(name = "excessive_allocation")
    private Integer excessiveAllocation;

    /**
     * 拣货位自动补货
     */
    @Column(name = "auto_replenishment")
    private Integer autoReplenishment;

    /**
     * 动态拣货位
     */
    @Column(name = "dynamic_pick_cell")
    private Integer dynamicPickCell;

    /**
     * 存储位拣选(EA)
     */
    @Column(name = "storage_choose_EA")
    private Integer storageChooseEa;

    /**
     * 存储位拣选(CS)
     */
    @Column(name = "storage_choose_CS")
    private Integer storageChooseCs;
}
