package com.deer.wms.project.root.constant;

/**
 * RedisKey值用于直接操作Redis时，Key的前缀<br>
 * KEY一旦指定，切勿修改<br>
 * 为缩短前缀，Key的真实值为『"ck" + 顺序编号』<br>
 * 
 * 有无冒号，依照使用场景，看后续是否需要根据Key区分缓存
 * 
 * @author xuesinuo
 */
public interface CacheKey {
    /** 定时任务 */
    String CRON = "ck1:";
    /** 客户信息 */
    String ALL_CARRIER = "ck2";
    /** 货主信息 */
    String ALL_ORGANIZATION = "ck3";
    /** 数据字典 */
    String DATA_DICT_DETAIL = "ck4:";
    /** 登录Token(废弃) */
    @Deprecated
    String ACCESS_TOKEN = "ck5:";
    /** 系统参数List */
    String ALL_SYS_PARAM = "ck6";
    /** 系统参数Item */
    String SYS_PARAM = "ck7:";
    /** 批次 */
    String BATCH_TACTIC = "ck8:";
    /** 包装 */
    String PACK_CODE = "ck9:";
    /** 月台 */
    String PLATFORM = "ck10:";
    /** 供应商 */
    String ALL_SUPPLIER_MANAGE = "ck11";
    /** 系统参数DtoList */
    String ALL_SYS_PARAM_DTO = "ck12";
    /** 仓库 */
    String ALL_WARE_INFO = "ck13";
    /** 客户 */
    String ALL_CUSTOMER = "ck14";
    /** SKU与PQ产品名称对照关系 */
    String SKU_PQ_PRODUCT = "ck15:";
    /** 图片访问路径的缓存 */
    String S3_FILE = "ck16:";
    /** 登录Token(新) */
    String NEW_ACCESS_TOKEN = "ck17:";
    /** 登录Token(新) */
    String NEW_ACCESS_USERID = "ck18:";
    /** 用户权限 */
    String USER_AUTH = "ck19:";
    /** pda登录Token */
    String PDA_ACCESS_TOKEN = "ck20:";
    /** pda登录用户id */
    String PDA_ACCESS_USERID = "ck21:";
    /** pda用户权限 */
    String PDA_USER_AUTH = "ck22:";
}
