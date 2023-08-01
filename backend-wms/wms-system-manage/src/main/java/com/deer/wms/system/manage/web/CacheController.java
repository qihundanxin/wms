package com.deer.wms.system.manage.web;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.system.manage.model.CacheClearByKeysParam;
import com.deer.wms.system.manage.model.CacheKeysResult;
import com.deer.wms.system.manage.model.CacheMessage;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

/**
 * 缓存
 * 
 * @author xuesinuo
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys/cache")
public class CacheController {

    private final CacheTool cache;

    /**
     * 查询当前系统的缓存项
     * 
     * @return 缓存Keys
     */
    @GetMapping("/keys")
    @Authority("sys_cache_keys")
    public Result getCacheKeys() {
        Field[] cacheKeyFields = CacheKey.class.getFields();
        List<CacheMessage> cacheMessageList = new ArrayList<>(cacheKeyFields.length);
        for (Field field : cacheKeyFields) {
            int modifiers = field.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                CacheMessage cacheMessage = new CacheMessage();
                cacheMessage.setCacheName(field.getName());
                try {
                    cacheMessage.setCacheKey(Np.i(field.get(null)).toString());
                } catch (IllegalArgumentException | IllegalAccessException e) {}
                if (StrUtil.isNotBlank(cacheMessage.getCacheKey())) {
                    long count = cache.countByKey(cacheMessage.getCacheKey());
                    cacheMessage.setCount(count);
                } else {
                    cacheMessage.setCount(0L);
                }
                cacheMessageList.add(cacheMessage);
            }
        }
        CacheKeysResult result = new CacheKeysResult();
        result.setCacheMessageList(cacheMessageList);
        return ResultGenerator.genSuccessResult(result);
    }

    /**
     * 按照Keys清理缓存
     * 
     * @return 清理结果
     */
    @PostMapping("/clear")
    @Authority("sys_cache_clear")
    public Result clearByCacheKeys(@RequestBody CacheClearByKeysParam param) {
        if (param == null || param.getKeys() == null || param.getKeys().size() == 0) {
            return ResultGenerator.genSuccessResult();
        }
        for (String cacheKey : param.getKeys()) {
            if (cacheKey == null) {
                continue;
            }
            cache.delByKey(cacheKey);
        }
        return ResultGenerator.genSuccessResult();
    }
}
