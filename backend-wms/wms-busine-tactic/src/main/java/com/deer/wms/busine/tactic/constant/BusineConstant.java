package com.deer.wms.busine.tactic.constant;

public class BusineConstant {
    /**
     * 项目基础包名称，根据公司实际项目修改
     */
    public static final String BASE_PACKAGE = "com.deer.wms.busine.tactic";

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
     * 波次策略状态
     */
    public static final int WAVE_TACTIC_SATE_ACTIVE = 0; // 激活
    public static final int WAVE_TACTIC_SATE_INACTIVE = -1; // 未激活
    public static final int WAVE_TACTIC_SATE_ACTIVE_ONCE = 1; // 激活一次
}

