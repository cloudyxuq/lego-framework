package com.sccl.lego.framework.service.impl;

import com.alibaba.fastjson.JSON;
import com.sccl.lego.framework.service.ICacheService;
import com.sccl.lego.framework.util.DateUtils;
import com.sccl.lego.framework.util.StringUtils;
import com.sccl.lego.framework.util.cache.PoolWapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.*;
import redis.clients.util.Pool;

import java.io.Serializable;
import java.util.*;
@Slf4j
public class RedisCacheServiceImpl implements ICacheService {


    @Autowired
    private PoolWapper poolWapper;
    /**
     * key 前缀规则
     */
    private String keyPreRule = null;

    @Override
    public boolean isShareCache() {
        return true;
    }

    public PoolWapper getPoolWapper() {
        return poolWapper;
    }

    public void setPoolWapper(PoolWapper poolWapper) {
        this.poolWapper = poolWapper;
    }

    @Override
    public boolean del(String... keys) {
        boolean ret = true;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            if (keys == null || keys.length == 0) {
                return true;
            }

            for (String key : keys) {
                if (!isValidKey(key)) {
                    ret = false;
                    break;
                }
                jedis.del(key);
            }
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public boolean isExists(String key) {
        boolean isExists = false;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            if (!isValidKey(key)) {
                return isExists;
            }
            isExists = jedis.exists(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return isExists;
    }

    @Override
    public boolean set(String key, Object value) {
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            if (!isValidKey(key)) {
                return false;
            }
            if (value instanceof String) {
                jedis.set(key, value.toString());
            } else {
                String objectJson = convertObjs(value);
                jedis.set(key, objectJson);
            }
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return true;
    }

    @Override
    public boolean setnx(String key, Object value) {
        Long result = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            if (!isValidKey(key)) {
                return false;
            }
            String objectJson = convertObjs(value);
            result = jedis.setnx(key, objectJson);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return result > 0 ? true : false;
    }

    public boolean set(String key, Object value, int secs) {
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            if (!isValidKey(key)) {
                return false;
            }
            String objectJson = convertObjs(value);
            jedis.set(key, objectJson, "", "EX", secs);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return true;
    }

    public boolean setBit(String key, long offset, boolean value) {
        Boolean ret = false;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.setbit(key, offset, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    public boolean getBit(String key, long offset) {
        Boolean ret = false;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.getbit(key, offset);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    private byte[] rawKey(String key) {
        //jedis不使用此函数
        return null;
    }

    public Long bitCount(String key) {
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.bitcount(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    public Long bitCount(String key, final long start, final long end) {
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.bitcount(key, start, end);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    public long bitop(final BitOperation op, String destKey, String... keys) {
        long ret = 0l;
        if (op == null) {
            return 0l;
        }
        if (keys == null || keys.length == 0) {
            return 0l;
        }
        final List<String> list = new ArrayList<>();
        for (String key : keys) {
            if (StringUtils.isNotEmpty(key)) {
                list.add(key);
            }
        }

        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            BitOP bop = null;
            switch (op) {
                case AND:
                    bop = BitOP.AND;
                    break;
                case OR:
                    bop = BitOP.OR;
                    break;
                case XOR:
                    bop = BitOP.XOR;
                    break;
                case NOT:
                    bop = BitOP.NOT;
                    break;
            }
            ret = jedis.bitop(bop, destKey, list.toArray(new String[]{}));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public boolean mset(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return false;
        }
        List<String> argList = new ArrayList<>();
        for (Map.Entry<String, Object> mm : map.entrySet()) {
            String key = mm.getKey();
            if (!isValidKey(key)) {
                continue;
            }
            String objectJson = convertObjs(mm.getValue());

            argList.add(key);
            argList.add(objectJson);
        }
        if (argList.isEmpty()) {
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            jedis.mset(argList.toArray(new String[argList.size()]));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return true;
    }

    @Override
    public Long getLong(String key) {
        Long value = 0L;
        String str = (String) get(key);
        if (StringUtils.isDigest(str)) {
            value = Long.parseLong(str);
        }
        return value;
    }

    /**
     * 根据key 获取对象
     *
     * @param key
     * @return
     */
    @Override
    public <T> T get(String key, Class<T> clazz) {
        if (!isValidKey(key)) {
            return null;
        }
        Jedis jedis = null;
        String value;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            value = jedis.get(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return convertObjs(value, clazz);
    }

    @Override
    public String get(String key) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            value = jedis.get(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return value;
    }

    @Override
    public boolean exists(String key) {
        boolean isExists = false;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            isExists = jedis.exists(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return isExists;
    }

    @Override
    public String getSet(String key, String value) {
        String ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.getSet(key, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Long decrBy(String key, long value) {
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.decrBy(key, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Long decr(String key) {
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.decrBy(key, 1);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Long incrBy(String key, long value) {
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.incrBy(key, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Double incrByFloat(String key, double value) {
        Double ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.incrByFloat(key, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Long incr(String key) {
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.incr(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Long incrForToday(String key) {
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.incr(key);
            if (ret != null && ret.longValue() == 1) {
                //添加有效期设置
                jedis.expireAt(key, DateUtils.getTomorrowFirstTime().getTime());
            }
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Long append(String key, String value) {
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.append(key, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public String substr(String key, int start, int end) {
        String ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.substr(key, start, end);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public boolean hset(String key, String field, Object value) {
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            String objectJson = this.convertObjs(value);
            jedis.hset(key, field, objectJson);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T hget(String key, String field) {
        T ret;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = (T) jedis.hget(key, field);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public <T> T hget(String key, String field, Class<T> clazz) {
        return convertObjs(hget(key, field), clazz);
    }

    @Override
    public boolean hsetnx(String key, String field, Object value) {
        long ret = 0L;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.hsetnx(key, field, this.convertObjs(value));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret > 0 ? true : false;
    }

    @Override
    public boolean hmset(String key, Map<String, Object> hash) {
        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : hash.entrySet()) {
            map.put(entry.getKey(), this.convertObjs(entry.getValue()));
        }
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            jedis.hmset(key, map);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return true;
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        List<String> ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.hmget(key, fields);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public <T> List<T> hmget(String key, Class<T> clazz, String... fields) {
        List<String> list = this.hmget(key, fields);
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<T> retList = new ArrayList<>();
        for (String value : list) {
            retList.add(convertObjs(value, clazz));
        }
        return retList;
    }

    @Override
    public Long hincrBy(String key, String field, long value) {
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.hincrBy(key, field, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Double hincrByFloat(String key, String field, double value) {
        Double ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.hincrByFloat(key, field, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Long hincrByForTime(String key, String field, long value, long timeout) {
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            boolean hasKey = jedis.hexists(key, field);
            ret = jedis.hincrBy(key, field, value);
            if (!hasKey) {
                jedis.pexpire(key, timeout * 1000);
            }
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public boolean hexists(String key, String field) {
        boolean isExists = false;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            isExists = jedis.hexists(key, field);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return isExists;
    }

    @Override
    public void hdel(String key, String... field) {
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            jedis.hdel(key, field);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long hlen(String key) {
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.hlen(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Set<String> hkeys(String key) {
        Set<String> ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.hkeys(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public List<String> hvals(String key) {
        List<String> ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.hvals(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        Map<String, String> ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.hgetAll(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public <T> Map<String, T> hgetAll(String key, Class<T> clazz) {
        Map<String, T> ret = new HashMap<>();
        Map<String, String> map = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            map = jedis.hgetAll(key);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                ret.put(entry.getKey(), convertObjs(entry.getValue(), clazz));
            }
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    public ScanResult<Map.Entry<String, String>> hscan(String key, long cursor) {
        ScanResult<Map.Entry<String, String>> ret = new ScanResult<>(0, new ArrayList<>());
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.hscan(key, String.valueOf(cursor));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    public ScanResult<String> scan(String pattern, String cursor) {
        ScanResult<String> ret = new ScanResult<String>(0, new ArrayList<>());
        Jedis jedis = null;
        try {
            jedis = (Jedis) poolWapper.getPool().getResource();
            ScanParams scanParams = new ScanParams();
            scanParams.match(pattern);
            ret = jedis.scan(cursor,scanParams);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }


    /**
     * 判断是否为有效Key,用于防止缓存穿透
     *
     * @param key 缓存key
     * @return 是否有效
     */
    private boolean isValidKey(String key) {
        if (StringUtils.isEmpty(keyPreRule)) {
            return true;
        }
        boolean result = key.startsWith(keyPreRule);
        log.error("[Jedis]invalid key:" + key);
        return result;
    }

    public String getKeyPreRule() {
        return keyPreRule;
    }

    public void setKeyPreRule(String keyPreRule) {
        this.keyPreRule = keyPreRule;
    }

    public Long sadd(String key, Object... values) {
        if (values == null || values.length == 0) {
            return 0L;
        }
        String[] strs = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            if (value == null) {
                continue;
            }
            strs[i] = this.convertObjs(value);
        }
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.sadd(key, strs);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Long srem(String key, Object... values) {
        if (values == null || values.length == 0) {
            return 0L;
        }
        String[] strs = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            if (value == null) {
                continue;
            }
            strs[i] = this.convertObjs(value);
        }
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.srem(key, strs);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public String spop(String key) {
        String ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.spop(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public <T> T spop(String key, Class<T> clazz) {
        String value = this.spop(key);
        return convertObjs(value, clazz);
    }

    @Override
    public Set<String> smembers(String key) {
        Set<String> ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.smembers(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public boolean sismembers(String key, Object members) {
        boolean ret = false;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.sismember(key, String.valueOf(members));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public <T> Set<T> smembers(String key, Class<T> clazz) {
        Set<String> set = this.smembers(key);
        Set<T> ret = new HashSet<T>();
        for (String value : set) {
            ret.add(convertObjs(value, clazz));
        }
        return ret;
    }

    @Override
    public List<String> srandmember(String key, Long count) {
        List<String> ret = new ArrayList<>();
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            if (count == null || count.longValue() == 0) {
                ret.add(jedis.srandmember(key));
            } else if (count.longValue() < 0) {
                ret.addAll(jedis.srandmember(key, Long.valueOf(Math.abs(count.longValue())).intValue()));
            } else {
                ret.addAll(jedis.srandmember(key, Long.valueOf(count.longValue()).intValue()));
            }
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public <T> List<T> srandmember(String key, Long count, Class<T> clazz) {

        List<T> ret = new ArrayList<>();
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            if (count == null || count.longValue() == 0) {
                String value = jedis.srandmember(key);
                ret.add(convertObjs(value, clazz));
            } else if (count.longValue() < 0) {
                List<String> set = jedis.srandmember(key, Long.valueOf(Math.abs(count.longValue())).intValue());
                for (String value : set) {
                    ret.add(convertObjs(value, clazz));
                }
            } else {
                List<String> set = jedis.srandmember(key, Long.valueOf(count.longValue()).intValue());
                for (String value : set) {
                    ret.add(convertObjs(value, clazz));
                }
            }
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Long lpush(String key, Object... values) {
        if (values == null || values.length == 0) {
            return 0L;
        }
        if (!isValidKey(key)) {
            return 0L;
        }
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            jedis.lpush(key, this.convertObjs(values));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }


    @Override
    public Long lpushForTime(String key, long timeout, Object... values) {
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = this.lpush(key, values);
            if (ret != null && ret.longValue() == values.length) {
                jedis.pexpire(key, timeout * 1000);
            }
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }

        return ret;
    }

    @Override
    public List<String> lrange(String key) {
        List<String> ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.lrange(key, 0, -1);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public <T> List<T> lrange(String key, Class<T> clazz) {
        List<T> ret = null;
        List<String> list = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            list = jedis.lrange(key, 0, -1);
            if (null != list && list.size() > 0) {
                for (String value : list) {
                    ret.add(convertObjs(value, clazz));
                }
            }
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public long lrem(String key, long count, Object value) {
        long ret = 0L;
        if (!isValidKey(key)) {
            return 0L;
        }
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.lrem(key, count, convertObjs(value));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Long incrForTime(String key, long timeout) {
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.incr(key);
            if (ret != null && ret.longValue() == 1) {
                //添加有效期设置
                jedis.pexpire(key, timeout * 1000);
            }
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public boolean set(String key, Object value, long timeout) {
        if (!isValidKey(key)) {
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            if (value instanceof String) {
                jedis.set(key, value.toString());
            } else {
                jedis.set(key, convertObjs(value));
            }
            jedis.pexpire(key, timeout * 1000);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return true;
    }

    @Override
    public boolean expire(String key, long timeout) {
        long ret = 0;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.pexpire(key, timeout * 1000);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret > 0 ? true : false;
    }

    @Override
    public boolean expireAt(String key, Date time) {
        long ret = 0;
        Jedis jedis = null;
        try {
            if (time != null) {
                jedis = (Jedis)poolWapper.getPool().getResource();
                ret = jedis.expireAt(key, time.getTime());
            }
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret > 0 ? true : false;
    }

    /**
     * Transactional 自动加入multi()/exec()
     */
    @Override
    @Transactional
    public Long incrForTimeWithDelay(String key, long timeout) {
        Long ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.incr(key);
            if (null != ret) {
                jedis.pexpire(key, timeout * 1000);
            }
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    @Transactional
    public Long saddForTimeWithDelay(String key, long timeout, Object... values) {
        Long ret = null;
        Jedis jedis = null;
        try {
            ret = this.sadd(key, values);
            jedis = (Jedis)poolWapper.getPool().getResource();
            jedis.pexpire(key, timeout * 1000);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Long rpush(String key, Object... values) {
        Long ret = null;
        if (values == null || values.length == 0) {
            return 0L;
        }
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.rpush(key, convertObjs(values));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }

        return ret;
    }

    @Override
    public <T> T lpop(String key, Class<T> clazz) {
        T ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            String value = jedis.lpop(key);
            ret = convertObjs(value, clazz);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }

        return ret;
    }

    @Override
    public <T> T rpop(String key, Class<T> clazz) {
        T ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            String value = jedis.rpop(key);
            ret = convertObjs(value, clazz);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }

        return ret;
    }

    @Override
    public <T> T blpop(String key, long timeout, Class<T> clazz) {
        T ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            List<String> values = jedis.blpop(Long.valueOf(timeout).intValue(), key);
            ret = convertObjs(values.get(1), clazz);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }

        return ret;
    }

    @Override
    public <T> T brpop(String key, long timeout, Class<T> clazz) {
        T ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            List<String> values = jedis.brpop(Long.valueOf(timeout).intValue(), key);
            ret = convertObjs(values.get(1), clazz);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }

        return ret;
    }

    @Override
    public boolean zadd(String key, double score, Object value) {
        Long ret = 0L;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.zadd(key, score, convertObjs(value));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret > 0 ? true : false;
    }

    @Override
    public List<String> zrange(String key, long start, long end) {
        Set<String> ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.zrange(key, start, end);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret == null ? null : new ArrayList<String>(ret);
    }

    @Override
    public <T> List<T> zrange(String key, long start, long end, Class<T> clazz) {
        List<T> ret = new ArrayList<T>();
        Set<String> values = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            values = jedis.zrange(key, start, end);
            if (values != null && values.size() > 0) {
                for (String value : values) {
                    ret.add(convertObjs(value, clazz));
                }
            }
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public List<String> zrevrange(String key, long start, long end) {
        Set<String> ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.zrevrange(key, start, end);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret == null ? null : new ArrayList<String>(ret);
    }

    @Override
    public <T> List<T> zrevrange(String key, long start, long end, Class<T> clazz) {
        List<T> ret = new ArrayList<T>();
        Set<String> values = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            values = jedis.zrevrange(key, start, end);
            if (values != null && values.size() > 0) {
                for (String value : values) {
                    ret.add(convertObjs(value, clazz));
                }
            }
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public long zrem(String key, Object... values) {
        long ret = 0;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.zrem(key, convertObjs(values));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Set<String> zrangebyscore(String key, double min, double max) {
        Set<String> ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.zrangeByScore(key, min, max);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Set<String> zrangebyscore(String key, double min, double max, long offset, long count) {
        Set<String> ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.zrangeByScore(key, min, max, Long.valueOf(offset).intValue(), Long.valueOf(count).intValue());
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Set<?> zrangebyscoreWithScores(String key, double min, double max) {
        Set<?> ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.zrangeByScoreWithScores(key, min, max);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override
    public Set<?> zrangebyscoreWithScores(String key, double min, double max, long offset, long count) {
        Set<?> ret = null;
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            ret = jedis.zrangeByScoreWithScores(key, min, max, Long.valueOf(offset).intValue(), Long.valueOf(count).intValue());
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return ret;
    }

    public void subscribe(String channel, JedisPubSub jedisPubSub) {
        if (jedisPubSub == null) {
            return;
        }
        if (StringUtils.isEmpty(channel)) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            jedis.subscribe(jedisPubSub, channel);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }

    }

    public void publish(String channel, Serializable message) {
        if (StringUtils.isEmpty(channel)) {
            return;
        }
        if (message == null) {
            return;
        }

        Jedis jedis = null;
        try {
            jedis = (Jedis)poolWapper.getPool().getResource();
            jedis.publish(channel, String.valueOf(message));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    private String convertObjs(Object value) {
        if (value == null) {
            return "";
        }
        if (value.getClass() == String.class
                || value.getClass() == Integer.class
                || value.getClass() == int.class
                || value.getClass() == Long.class
                || value.getClass() == long.class
                || value.getClass() == Double.class
                || value.getClass() == double.class
                || value.getClass() == Float.class
                || value.getClass() == float.class
                ) {
            return value.toString();
        } else {
            return JSON.toJSONString(value);
        }
    }

    private String[] convertObjs(Object... values) {
        String[] strs = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            if (value == null) {
                continue;
            }
            if (value.getClass() == String.class
                    || value.getClass() == Integer.class
                    || value.getClass() == int.class
                    || value.getClass() == Long.class
                    || value.getClass() == long.class
                    || value.getClass() == Double.class
                    || value.getClass() == double.class
                    || value.getClass() == Float.class
                    || value.getClass() == float.class
                    ) {
                strs[i] = value.toString();
            } else {
                strs[i] = JSON.toJSONString(values[i]);
            }
        }
        return strs;
    }

    @SuppressWarnings("unchecked")
    private <T> T convertObjs(String value, Class<T> clazz) {
        if (clazz == String.class) {
            return (T) value;
        } else if (clazz == Integer.class || clazz == int.class) {
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            return (T) new Integer(value);
        } else if (clazz == Long.class || clazz == long.class) {
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            return (T) new Long(value);
        } else if (clazz == Double.class || clazz == double.class) {
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            return (T) new Double(value);
        } else if (clazz == Float.class || clazz == float.class) {
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            return (T) new Float(value);
        } else {
            return JSON.parseObject(value, clazz);
        }
    }

    @Override
    public Pool getPool() {
        return poolWapper.getPool();
    }
}
