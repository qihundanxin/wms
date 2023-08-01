package com.deer.wms.system.manage.model;

import java.util.List;

import lombok.Data;

/**
 * 缓存 —— 按照Keys清理API入参
 * 
 * @author xuesinuo
 *
 */
@Data
public class CacheClearByKeysParam {
    private List<String> keys;
}
