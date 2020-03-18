package com.sccl.lego.framework.util.cache;

import com.sccl.lego.framework.util.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 缓存线程池封装
 */
public class CachePoolWapper implements PoolWapper{

    @Autowired
    private Pool pool;

    public CachePoolWapper(GenericObjectPoolConfig poolConfig,String addr,String password,int timeout,int database, String masterName){
        if (StringUtils.isEmpty(masterName)) {
            pool = new JedisPool(poolConfig, addr.split(":")[0], Integer.parseInt(addr.split(":")[1]), timeout, password, database);
        } else {
            Set<String> sentinels = new HashSet<>(Arrays.asList(addr.split(",")));
            pool = new JedisSentinelPool(masterName, sentinels, poolConfig, timeout, timeout, password, database);
        }
    }

    @Override
    public Pool getPool() {
        return pool;
    }
}
