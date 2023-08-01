package com.deer.wms.system.manage.model;

import java.util.List;

import lombok.Data;

/**
 * 缓存 —— 查询缓存Key的返回结果
 * 
 * @author xuesinuo
 *
 */
@Data
public class CacheKeysResult {
    private List<CacheMessage> cacheMessageList;
}
