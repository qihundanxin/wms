package com.deer.wms.base.system.model.Currency;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 货币信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id")
    private Integer currencyId;

    /**
     * 币种类型
     */
    @Column(name = "currency_type")
    private String currencyType;

    /**
     * 币种名称
     */
    @Column(name = "currency_name")
    private String currencyName;
}
