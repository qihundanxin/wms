package com.deer.wms.project.root.sync.bean;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.deer.wms.project.root.sync.pojo.SyncParam;
import com.deer.wms.project.root.util.SyncTool;

@Component
public class SyncLockByRedisson implements SyncTool {

    private RedissonClient redisson;

    public SyncLockByRedisson(RedissonClient redisson) {
        this.redisson = redisson;
    }

    public RLock getLock(SyncParam param) {
        // method level lock
        String lockName = param.getLockName();
        if(lockName == null || lockName.trim().equals("")) {
            throw new RuntimeException("getLock(param), param lose 'lockName'.");
        }

        // param level lock
        String lockParam = param.getLockParam();
        if(lockParam == null) {
            lockParam = "";
        } else {
            lockParam = lockParam.trim();
        }

        // use lock
        RLock rLock;
        switch (param.getLockMode()) {
        case RANDOM:
            rLock = redisson.getLock("sync:" + lockName + lockParam);
            break;
        case FAIR:
            rLock = redisson.getFairLock("sync:" + lockName + lockParam);
            break;
        default:
            rLock = redisson.getLock("sync:" + lockName + lockParam);
            break;
        }
        rLock.lock(param.getTimeout(), param.getTimeoutUnit());
        return rLock;
    }

    public void clearLock(RLock redissonLock) {
        if (redissonLock.isLocked() && redissonLock.isHeldByCurrentThread()) {
            redissonLock.unlock();
        }
    }

    @Override
    public Closer lock(SyncParam param) {
        RLock redissonLock = this.getLock(param);
        SyncLockByRedisson _this = this;
        return new Closer() {
            @Override
            public void close() {
                _this.clearLock(redissonLock);
            }
        };
    }

    @Override
    public Closer lock(String lockName, String lockParam) {
        return this.lock(new SyncParam(lockName, lockParam));
    }
}