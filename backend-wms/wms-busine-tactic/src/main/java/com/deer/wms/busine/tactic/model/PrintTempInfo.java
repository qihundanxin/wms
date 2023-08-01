package com.deer.wms.busine.tactic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 打印模板信息
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "print_temp_info")
public class PrintTempInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temp_id")
    private Integer tempId;

    /**
     * 模板名称
     */
    @Column(name = "temp_name")
    private String tempName;

    /**
     * 类型：验收标签；商品标签；货位标签；拣货标签；快递面单；装箱标签；货位组标签；货位标签；二维码/条形码；出库单
     */
    @Column(name = "type")
    private String type;

    /**
     * 设计方式： 1-WMS设计器 2-Ureport设计器
     */
    @Column(name = "sj_type")
    private Integer sjType;

    /**
     * 文件名称
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * 快递公司
     */
    @Column(name = "ship_code")
    private String shipCode;

    /**
     * 模板大小类型
     */
    @Column(name = "temp_size_type")
    private String tempSizeType;

    /**
     * 高度
     */
    @Column(name = "height")
    private Integer height;

    /**
     * 宽度
     */
    @Column(name = "width")
    private Integer width;

    /**
     * 模板html
     */
    @Column(name = "temp_html")
    private String tempHtml;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 0-未激活 1-激活
     */
    @Column(name = "state")
    private Integer state;

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
}
