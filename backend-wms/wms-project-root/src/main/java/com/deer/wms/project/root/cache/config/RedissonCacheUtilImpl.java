package com.deer.wms.project.root.cache.config;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.deer.wms.project.root.util.CacheTool;

import lombok.RequiredArgsConstructor;

/**
 * 使用Redisson实现缓存工具
 * 
 * @author xuesinuo
 * 
 */
@RequiredArgsConstructor
@Component
public class RedissonCacheUtilImpl implements CacheTool {
    private final RedissonClient redisson;

    private String keyFormatter(String key, String id) {
        if (key == null) {
            key = "";
        }
        key = key.trim();
        if (id == null) {
            id = "";
        }
        id = id.trim();
        return key + id;
    }

    private String[] keyFormatter(String key, String[] ids) {
        for (int i = 0; i < ids.length; i++) {
            ids[i] = keyFormatter(key, ids[i]);
        }
        return ids;
    }

    private String keyPattern(String key) {
        if (key.length() > 1 && CacheTool.SEPARATOR.equals(key.substring(key.length() - 1))) {
            return key + "*";
        }
        return null;
    }

    @Override
    public Boolean expire(String key, String id, long time) {
        id = this.keyFormatter(key, id);
        if (time > 0) {
            redisson.getKeys().expire(id, time, TimeUnit.SECONDS);
        }
        return true;
    }

    @Override
    public Long getExpire(String key, String id) {
        id = this.keyFormatter(key, id);
        long ttl = redisson.getBucket(id).remainTimeToLive();
        if (ttl == -2L) {
            return null;
        }
        if (ttl == -1L) {
            return -1L;
        }
        return ttl / 1000L;
    }

    @Override
    public boolean has(String key, String id) {
        id = this.keyFormatter(key, id);
        return redisson.getBucket(id).isExists();
    }

    @Override
    public long countByKey(String key) {
        String keyPattern = this.keyPattern(key);
        if (keyPattern == null) {
            if (this.has(key, "")) {
                return 1L;
            }
            return 0L;
        }
        return redisson.getKeys().getKeysStreamByPattern(keyPattern).count();
    }

    @Override
    public void del(String key, String... ids) {
        if (ids != null && ids.length > 0) {
            ids = this.keyFormatter(key, ids);
            redisson.getKeys().delete(ids);
        } else {
            String id = this.keyFormatter(key, "");
            redisson.getKeys().delete(id);
        }
    }

    @Override
    public void delByKey(String key) {
        String keyPattern = this.keyPattern(key);
        if (keyPattern != null) {
            redisson.getKeys().deleteByPattern(keyPattern);
        } else {
            redisson.getKeys().delete(this.keyFormatter(key, ""));
        }
    }

    @Override
    public <T> T getObject(String key, String id) {
        id = this.keyFormatter(key, id);
        RBucket<T> rBucket = redisson.getBucket(id);
        return rBucket.get();
    }

    @Override
    public <T> void setObject(String key, String id, T value, long time) {
        id = this.keyFormatter(key, id);
        if (time <= 0) {
            return;
        }
        RBucket<T> rBucket = redisson.getBucket(id);
        rBucket.set(value);
        rBucket.expire(time, TimeUnit.SECONDS);
    }

    @Override
    public <T> List<T> getList(String key, String id) {
        id = this.keyFormatter(key, id);
        RList<T> rList = redisson.getList(id);
        List<T> resultList = rList.stream().map(t -> t).collect(Collectors.toList());
        return resultList;
    }

    @Override
    public <T> void addListItem(String key, String id, T value, long time) {
        id = this.keyFormatter(key, id);
        RList<T> rList = redisson.getList(id);
        rList.add(value);
        rList.expire(time, TimeUnit.SECONDS);
    }

    @Override
    public <T> void addListItem(String key, String id, T value, int index, long time) {
        id = this.keyFormatter(key, id);
        RList<T> rList = redisson.getList(id);
        rList.add(index, value);
        rList.expire(time, TimeUnit.SECONDS);
    }

    @Override
    public <T> T removeListItem(String key, String id, int index) {
        id = this.keyFormatter(key, id);
        RList<T> rList = redisson.getList(id);
        return rList.remove(index);
    }

    @Override
    public int getListSize(String key, String id) {
        id = this.keyFormatter(key, id);
        return redisson.getList(id).size();
    }

    @Override
    public <T> T getListItem(String key, String id, int index) {
        id = this.keyFormatter(key, id);
        RList<T> rList = redisson.getList(id);
        if (rList.size() <= index) {
            return null;
        }
        return rList.get(index);
    }

    @Override
    public long getCounter(String key, String id) {
        id = this.keyFormatter(key, id);
        RAtomicLong rAtomicLong = redisson.getAtomicLong(id);
        return rAtomicLong.get();
    }

    @Override
    public long addCounter(String key, String id, long delta) {
        id = this.keyFormatter(key, id);
        RAtomicLong rAtomicLong = redisson.getAtomicLong(id);
        return rAtomicLong.getAndAdd(delta);
    }
}
