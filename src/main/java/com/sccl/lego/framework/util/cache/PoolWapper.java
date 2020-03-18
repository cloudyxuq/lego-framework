package com.sccl.lego.framework.util.cache;

import redis.clients.util.Pool;

public interface PoolWapper {
    /**
     * 获取线程池实例
     * @return
     */
    public Pool getPool();
}
