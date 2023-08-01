package com.deer.wms.project.root.constant;

/**
 * 缓存时长统一管理，匹配{@link CacheKey}
 * 
 * @author xuesinuo
 *
 */
public interface CacheTime {
    long ALL_CARRIER = 30L * 24L * 60L * 60L;
    long ALL_ORGANIZATION = 30L * 24L * 60L * 60L;
    long DATA_DICT_DETAIL = 30L * 24L * 60L * 60L;
    long ACCESS_TOKEN = 8L * 60L * 60L;
    long ALL_SYS_PARAM = 30L * 24L * 60L * 60L;
    long SYS_PARAM = 30L * 24L * 60L * 60L;
    long BATCH_TACTIC = 30L * 24L * 60L * 60L;
    long PACK_CODE = 30L * 24L * 60L * 60L;
    long PLATFORM = 30L * 24L * 60L * 60L;
    long ALL_SUPPLIER_MANAGE = 30L * 24L * 60L * 60L;
    long ALL_SYS_PARAM_DTO = 30L * 24L * 60L * 60L;
    long ALL_WARE_INFO = 30L * 24L * 60L * 60L;
    long ALL_CUSTOMER = 30L * 24L * 60L * 60L;
    long SKU_PQ_PRODUCT = 30L * 24L * 60L * 60L;
}
