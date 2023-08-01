package com.deer.wms.inventory.constant;

public class InventoryConstant {
    /**
     * 项目基础包名称，根据公司实际项目修改
     */
    public static final String BASE_PACKAGE = "com.deer.wms.inventory";

    /**
     * Model所在包
     */
    public static final String MODEL_PACKAGE = BASE_PACKAGE + ".model";

    /**
     * Dao所在包
     */
    public static final String DAO_PACKAGE = BASE_PACKAGE + ".dao";

    /**
     * MapperLocations
     */
    public static final String MAPPER_LOCATIONS = "classpath*:com/deer/wms/**/mapper/*.xml";

    /**
     *
     */
    public static final int COMPANY_TYPE_OPERATE = -1;

    /**
     * 事务类型  验收
     */
    public static final Integer TRANSACT_TYPE_ACCEPT = 0;

    /**
     * 事务类型  采购入库
     */
    public static final Integer PICK_TASK = 6;

    /**
     * 事务类型  预约入库
     */
    public static final Integer TRANSACT_TYPE_ASN = 1;

    /**
     * 事务类型  盲收
     */
    public static final Integer TRANSACT_TYPE_BLIND = 3;

    /**
     * 事务类型  上架
     */
    public static final Integer TRANSACT_TYPE_UP = 2;

    /**
     * 事务类型  移库
     */
    public static final Integer TRANSACT_MOVE = 5;

    /**
     * 事务类型  审核
     */
    public static final Integer CASE_REVIEW = 8;

    /**
     * 事务类型  扫描验收
     */
    public static final Integer TRANSACT_TYPE_SCAN = 7;

    /**
     * 事务类型  库存调整
     */
    public static final Integer INVENTORY_ADJUST = 10;
    /**
     * 事务类型 货权转移
     */
    public static final Integer INVENTORY_TRANS_ORG = 11;

    /**
     * 事务类型 库存冻结
     */
    public static final Integer INVENTORY_FREEZE = 12;

    /**
     * 事务类型 验收
     */
    public static final int SERIALNO_CHECKANDACCEPT = 1;

    /**
     * 事务类型 移库  上架
     */
    public static final int SERIALNO_TRANSFER = 2;

    /**
     * 事务类型 移库  上架车1
     */
    public static final int SJC1 = 21;

    /**
     * 事务类型 移库  上架车2
     */
    public static final int SJC2 = 22;

    /**
     * 事务类型 移库
     */
    public static final int YK = 23;

    /**
     * 事务类型 发运
     */
    public static final int SERIALNO_OUT = 3;

    /**
     * 事务类型 分配数迁移
     */
    public static final int PICK_MOVE = 24;


}

