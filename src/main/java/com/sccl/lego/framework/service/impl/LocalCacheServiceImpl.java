package com.sccl.lego.framework.service.impl;

import com.sccl.lego.framework.service.ICacheService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.ScanResult;
import redis.clients.util.Pool;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Title: 本地缓存 </p>
 * <p>Description:   </p>
 * <p>Company: </p>
 * <p>@author xuq </p>
 * <p>@version 1.0.0 </p>
 * <p>@date 17/01/201811:08 AM
 */
public class LocalCacheServiceImpl implements ICacheService{

    /** 本地主缓存hash */
    private static final Map<String, Object> cacheMainMap = Collections.synchronizedMap(new HashMap<String, Object>());
    Lock lock = new ReentrantLock();

    @Override
    public boolean isShareCache()
    {
        return false;
    }

    /**
     * 获取hash类型缓存
     * @param key
     * @param init 缓存中key不存在时，是否创建并缓存对应hash，true创建，false否
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getCacheHashMap(String key, boolean init) {
        Object obj = cacheMainMap.get(key);
        Map<String, Object> secMap = Collections.synchronizedMap(new HashMap<String, Object>());
        if (obj == null) {
            if (init) {
                cacheMainMap.put(key, secMap);
                return secMap;
            } else {
                return null;
            }
        }
        if (obj instanceof Map) {
            secMap = (Map<String, Object>) obj;
        } else {
            throw new RuntimeException("value type error:" + obj.getClass());
        }
        return secMap;
    }

    @Override
    public boolean isExists(String key) {
        return cacheMainMap.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, Class<T> clazz) {
        return (T) cacheMainMap.get(key);
    }

    @Override
    public Long getLong(String key) {
        return (Long) cacheMainMap.get(key);
    }

    @Override
    public boolean del(String... keys) {
        for (Iterator<String> iterator = cacheMainMap.keySet().iterator(); iterator.hasNext();) {
            String type = iterator.next();
            for (String key : keys) {
                if (StringUtils.equals(type, key)) {
                    iterator.remove();
                }
            }
        }
        return true;
    }

    @Override
    public boolean set(String key, Object value) {
        cacheMainMap.put(key, value);
        return true;
    }

    @Override
    public boolean mset(Map<String, Object> map)
    {
        cacheMainMap.putAll(map);
        return true;
    }

    @Override
    public String get(String key) {
        return (String) cacheMainMap.get(key);
    }

    @Override
    public boolean exists(String key) {
        return cacheMainMap.containsKey(key);
    }

    @Override
    public String getSet(String key, String value) {
        String oldKey = (String) cacheMainMap.get(key);
        cacheMainMap.put(key, value);
        return oldKey;
    }

    @Override
    public boolean setnx(String key, Object value) {
        if (!cacheMainMap.containsKey(key)) {
            cacheMainMap.put(key, value);
            return true;
        }
        return false;
    }

    @Override
    public Long decrBy(String key, long value) {
        lock.lock();
        try {
            String obj = (String) cacheMainMap.get(key);
            if (obj != null && !NumberUtils.isNumber(obj)) {
                throw new RuntimeException("old value type error:" + obj);
            }
            Long newVal = Long.valueOf(NumberUtils.toLong(obj, 0) - value);
            cacheMainMap.put(key, newVal.toString());
            return newVal;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Long decr(String key) {
        return decrBy(key, 1);
    }

    @Override
    public Long incrBy(String key, long value) {
        lock.lock();
        try {
            String obj = (String) cacheMainMap.get(key);
            if (obj != null && !NumberUtils.isNumber(obj)) {
                throw new RuntimeException("old value type error:" + obj);
            }
            Long newVal = Long.valueOf(NumberUtils.toLong(obj, 0) + value);
            cacheMainMap.put(key, newVal.toString());
            return newVal;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Double incrByFloat(String key, double value) {
        lock.lock();
        try {
            String obj = (String) cacheMainMap.get(key);
            if (obj != null && !NumberUtils.isNumber(obj)) {
                throw new RuntimeException("old value type error:" + obj);
            }
            Double newVal = Double.valueOf(NumberUtils.toDouble(obj, 0) - value);
            cacheMainMap.put(key, newVal.toString());
            return newVal;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Long incr(String key) {
        return incrBy(key, 1);
    }

    @Override
    public Long incrForToday(String key)
    {
        Long ret = incrBy(key, 1);
        if(ret != null && ret.longValue() == 1)
        {
            //添加有效期设置
        }
        return ret;
    }

    @Override
    public Long append(String key, String value) {
        lock.lock();
        try {
            String obj = (String) cacheMainMap.get(key);
            StringUtils.defaultIfEmpty(obj, "");
            obj = obj + value;
            cacheMainMap.put(key, obj);
            return Long.valueOf(obj.length());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String substr(String key, int start, int end) {
        String obj = (String) cacheMainMap.get(key);
        String newStr = obj.substring(start, end);
        return newStr;
    }

    @Override
    public boolean hset(String key, String field, Object value) {
        getCacheHashMap(key, true).put(field, value);
        return true;
    }

    @Override
    public <T> T hget(String key, String field) {
        Map<String, Object> hash = getCacheHashMap(key, false);
        return (T) (hash != null ? hash.get(field) : "");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T hget(String key, String field, Class<T> clazz) {
        Map<String, Object> hash = getCacheHashMap(key, false);
        return (T) (hash != null ? hash.get(field) : null);
    }

    @Override
    public boolean hsetnx(String key, String field, Object value) {
        Map<String, Object> hash = getCacheHashMap(key, true);
        if (!hash.containsKey(field)) {
            hash.put(field, value);
        }
        return true;
    }

    @Override
    public boolean hmset(String key, Map<String, Object> hash) {
        Map<String, Object> _hash = getCacheHashMap(key, true);
        _hash.putAll(hash);
        return true;
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        Map<String, Object> hash = getCacheHashMap(key, false);
        if (hash == null)
            return new ArrayList<>();
        List<String> retList = new ArrayList<>();
        for (String field : fields) {
            if (hash.containsKey(field))
                retList.add((String) hash.get(field));
        }
        return retList;
    }

    @Override
    public <T> List<T> hmget(String key, Class<T> clazz, String... fields)
    {
        Map<String, Object> hash = getCacheHashMap(key, false);
        if (hash == null)
            return new ArrayList<>();
        List<T> retList = new ArrayList<>();
        for (String field : fields) {
            if (hash.containsKey(field))
                retList.add((T) hash.get(field));
        }
        return retList;
    }


    @Override
    public Long hincrBy(String key, String field, long value) {
        Map<String, Object> _hash = getCacheHashMap(key, true);
        lock.lock();
        try {
            String obj = (String) _hash.get(field);
            if (obj != null && !NumberUtils.isNumber(obj)) {
                throw new RuntimeException("old value type error:" + obj);
            }
            Long newVal = Long.valueOf(NumberUtils.toLong(obj, 0) - value);
            _hash.put(field, newVal.toString());
            return newVal;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Double hincrByFloat(String key, String field, double value) {
        Map<String, Object> _hash = getCacheHashMap(key, true);
        lock.lock();
        try {
            String obj = (String) _hash.get(field);
            if (obj != null && !NumberUtils.isNumber(obj)) {
                throw new RuntimeException("old value type error:" + obj);
            }
            Double newVal = Double.valueOf(NumberUtils.toDouble(obj, 0) - value);
            _hash.put(field, newVal.toString());
            return newVal;
        } finally {
            lock.unlock();
        }
    }


    @Override
    public Long hincrByForTime(String key, String field, long value, long timeout)
    {
        boolean hasKey = hexists(key, field);
        Long ret = hincrBy(key, field, value);
        if(!hasKey)
        {
            //添加有效期设置
        }
        return ret;
    }


    @Override
    public boolean hexists(String key, String field) {
        Map<String, Object> hash = getCacheHashMap(key, false);
        if (hash != null && hash.containsKey(field))
            return true;
        return false;
    }

    @Override
    public void hdel(String key, String ... fields) {
        Map<String, Object> hash = getCacheHashMap(key, false);
        if (hash != null) {
            lock.lock();
            try {
                for (String obj : fields) {
                    hash.remove(obj);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public Long hlen(String key) {
        Map<String, Object> hash = getCacheHashMap(key, false);
        return (hash != null ? hash.size() : 0L);
    }

    @Override
    public Set<String> hkeys(String key) {
        Map<String, Object> hash = getCacheHashMap(key, false);
        if (hash != null)
            return hash.keySet();
        return new HashSet<>();
    }

    @Override
    public List<String> hvals(String key) {
        Map<String, Object> hash = getCacheHashMap(key, false);
        List<String> retList = new ArrayList<>();
        if (hash != null) {
            for (Object obj : hash.values()) {
                retList.add((String) obj);
            }
        }
        return retList;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        Map<String, Object> hash = getCacheHashMap(key, false);
        Map<String, String> retMap = new HashMap<>();
        if (hash != null) {
            for (String field : hash.keySet()) {
                retMap.put(field, (String) hash.get(field));
            }
        }
        return retMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Map<String, T> hgetAll(String key, Class<T> clazz) {
        Map<String, Object> hash = getCacheHashMap(key, false);
        Map<String, T> retMap = new HashMap<>();
        if (hash != null) {
            for (String field : hash.keySet()) {
                retMap.put(field, (T) hash.get(field));
            }
        }
        return retMap;
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, long cursor) {
        this.checkMethod();
        return null;
    }

    @Override
    public ScanResult<String> scan(String pattern, String cursor) {
        this.checkMethod();
        return null;
    }

    @Override
    public Long sadd(String key, Object... values)
    {
        Set<Object> set = this.get(key, Set.class);
        if(set == null)
        {
            set = new HashSet<Object>();
            this.set(key, set);
        }
        if(values == null || values.length == 0)
        {
            return 0L;
        }
        for(Object value : values)
        {
            set.add(value);
        }
        return (long)values.length;
    }

    @Override
    public Long srem(String key, Object ... values)
    {
        Set<Object> set = this.get(key, Set.class);
        if(set == null || set.isEmpty())
        {
            return 0L;
        }
        if(values == null || values.length == 0)
        {
            return 0L;
        }
        long ret = 0;
        for(Object value : values)
        {
            if(set.remove(value))
            {
                ret++;
            }
        }
        return ret;
    }

    @Override
    public <T> Set<T> smembers(String key, Class<T> clazz)
    {
        return this.get(key, Set.class);
    }

    @Override
    public <T> List<T> srandmember(String key, Long count, Class<T> clazz)
    {
        //实现，功能与性能均未测试，大队列时不适合使用
        List<T> ret = new ArrayList<T>();
        Set<T> set = this.smembers(key, clazz);
        List<T> tmp = new ArrayList<>(set);
        if(count == null || count.longValue() == 0)
        {
            if(set != null && set.size() > 0)
            {
                int index = (int)Math.floor(Math.random() * set.size());
                ret.add(tmp.get(index));
            }
        }
        else if(count.longValue() < 0)//随机取，允许重复
        {
            // 初始化随机数
            Random rand = new Random();
            int size = Math.abs(count.intValue());
            for(int i = 0; i < size; i++)
            {
                ret.add(tmp.get(rand.nextInt(size)));
            }
        }
        else
        {
            Set<Integer> indexs = new HashSet<Integer>();
            // 初始化随机数
            Random rand = new Random();
            int size = Math.abs(count.intValue());
            while(true)
            {
                int index = rand.nextInt(size);
                if(indexs.contains(index))
                {
                    continue;
                }
                ret.add(tmp.get(index));
                if(ret.size() >= size)
                {
                    break;
                }
            }
        }
        tmp.clear();
        return ret;
    }

    private void checkMethod()
    {
        throw new RuntimeException("该方法暂未实现");
    }

    @Override
    @Deprecated
    public String spop(String key)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public <T> T spop(String key, Class<T> clazz)
    {
        this.checkMethod();
        return null;
    }


    private long nextLong(Random rng, long n) {
        // error checking and 2^x checking removed for simplicity.
        long bits, val;
        do {
            bits = (rng.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (bits-val+(n-1) < 0L);
        return val;
    }


    @Override
    public Long lpush(String key, Object... values)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public Long lpushForTime(String key, long timeout, Object... values)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public <T> List<T> lrange(String key, Class<T> clazz)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public long lrem(String key, long count, Object value)
    {
        this.checkMethod();
        return 0;
    }


    @Override
    public Long incrForTime(String key, long timeout)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public boolean set(String key, Object value, long timeout)
    {
        return false;
    }


    @Override
    public boolean expire(String key, long timeout) {
        this.checkMethod();
        return false;
    }


    @Override
    public boolean expireAt(String key, Date time) {
        this.checkMethod();
        return false;
    }


    @Override
    public Long incrForTimeWithDelay(String key, long timeout) {
        return null;
    }


    @Override
    public Long saddForTimeWithDelay(String key, long timeout, Object... values) {
        return null;
    }


    @Override
    public Long rpush(String key, Object... values)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public <T> T lpop(String key, Class<T> clazz)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public <T> T rpop(String key, Class<T> clazz)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public <T> T blpop(String key, long timeout, Class<T> clazz)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public <T> T brpop(String key, long timeout, Class<T> clazz)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public boolean zadd(String key, double score, Object value)
    {
        this.checkMethod();
        return false;
    }


    @Override
    public <T> List<T> zrange(String key, long start, long end, Class<T> clazz)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public <T> List<T> zrevrange(String key, long start, long end, Class<T> clazz)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public long zrem(String key, Object... values)
    {
        this.checkMethod();
        return 0;
    }


    @Override
    public Set<String> smembers(String key)
    {
        return this.get(key, Set.class);
    }


    @Override
    public boolean sismembers(String key, Object members)
    {
        Set<Object> set = this.get(key, Set.class);
        if(set != null)
        {
            return set.contains(members);
        }
        return false;
    }


    @Override
    public List<String> srandmember(String key, Long count)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public List<String> lrange(String key)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public List<String> zrange(String key, long start, long end)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public List<String> zrevrange(String key, long start, long end)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public Set<String> zrangebyscore(String key, double min, double max)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public Set<String> zrangebyscore(String key, double min, double max, long offset, long count)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public Set<?> zrangebyscoreWithScores(String key, double min, double max)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public Set<?> zrangebyscoreWithScores(String key, double min, double max, long offset, long count)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public boolean setBit(String key, long offset, boolean value)
    {
        this.checkMethod();
        return false;
    }


    @Override
    public boolean getBit(String key, long offset)
    {
        this.checkMethod();
        return false;
    }


    @Override
    public Long bitCount(String key)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public Long bitCount(String key, long start, long end)
    {
        this.checkMethod();
        return null;
    }


    @Override
    public long bitop(BitOperation op, String destKey, String... keys)
    {
        this.checkMethod();
        return 0;
    }


    @Override
    public void publish(String channel, Serializable message)
    {
        this.checkMethod();
    }


    @Override
    public void subscribe(String channel, JedisPubSub jedisPubSub)
    {
        this.checkMethod();
    }

    @Override
    public Pool getPool() {
        //本地缓存不使用
        return null;
    }
}
