package com.deer.wms.ware.task.constant;

public class WareTaskConstant {
    /**
     * 项目基础包名称，根据公司实际项目修改
     */
    public static final String BASE_PACKAGE = "com.deer.wms.ware.task";

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

    public static final int BOX_DETAIL_ORIGIN = 0;
    public static final int BOX_DETAIL_LOADED = 1;

    public static final int BATCH_SCAN_LIMIT = 20; //多少数以上可以批发发运
}
