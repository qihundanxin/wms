package com.deer.wms.system.manage.model.permission;

import lombok.Data;
import lombok.ToString;

/**
 * 权限菜单信息类
 * <p>
 * Created by guo on 2018/8/11.
 */
@Data
public class Menu {
    /**
     * 菜单id
     */
    private Integer id;

    /**
     * 父级菜单id
     */
    private Integer parentId;

    /**
     * 菜单代码
     */
    private String code;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 样式
     */
    private String style;

    /**
     * 菜单路径
     */
    private String url;

    /**
     * 菜单层级
     */
    private Integer level;

    /**
     * group
     */
    private Integer resourceGroup;

    /**
     * 类型：M-模块 F-页面 B-按钮
     */
    private String resourceType;

    /**
     * 权限标识
     */
    private String authorityCode;

}