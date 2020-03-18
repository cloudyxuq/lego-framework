package com.sccl.lego.framework.service;

import redis.clients.jedis.JedisPubSub;
import redis.clients.util.Pool;
import redis.clients.jedis.ScanResult;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存服务
 */
public interface ICacheService extends IService {

	public enum BitOperation {
		AND, OR, XOR, NOT;
	}
	/**
	 * 是否是共享缓存
	 * @return
	 */
	boolean isShareCache();
    /**
     * key是否在缓存中
     * @param key
     * @return
     */
    boolean isExists(String key);

    /**
     * 根据key 获取对象
     * @param key
     * @param clazz
     * @return
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 获取长整类型的值
     * @param key
     * @return
     */
    Long getLong(String key);

    /**
     * 根据keys删除缓存中的对象，
     * @param keys
     * @return
     */
    boolean del(String... keys);

    /**
     * String 操作
     * @param key
     * @param value
     * @return
     */
    boolean set(String key, Object value);
    /**
     *
     * @param key
     * @param value
     * @param timeout	超时时间，单位：秒
     * @return
     */
    boolean set(String key, Object value, long timeout);

    String get(String key);

    boolean exists(String key);
    /**
     * 设置key过期
     * @param key
     * @param timeout	超时时间，单位：秒
     * @return
     */
    boolean expire(String key, long timeout);
    /**
     * 设置key在指定时间过期
     * @param key
     * @param time
     * @return
     */
    boolean expireAt(String key, Date time);

    String getSet(String key, String value);

    boolean setnx(String key, Object value);

    boolean mset(Map<String, Object> map);

    Long decrBy(String key, long value);

    Long decr(String key);

    Long incrBy(String key, long value);

    Double incrByFloat(String key, double value);

    Long incr(String key);
    /**
     * 当天有效的自增计数
     * @param key
     * @return
     */
    Long incrForToday(String key);

    /**
     * 自增计数
     * @param key
     * @param timeout	超时时间，单位：秒
     * @return
     */
    Long incrForTime(String key, long timeout);
    /**
     * 自增计数(以最后一次操作时间进行超时顺延)
     * @param key
     * @param timeout
     * @return
     */
    Long incrForTimeWithDelay(String key, long timeout);

    Long append(String key, String value);

    String substr(String key, int start, int end);
    /**
     * 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)
     * @param key
     * @param offset
     * @param value
     * @return
     */
    boolean setBit(String key, long offset, boolean value);
    /**
     * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)
     * 当 offset 比字符串值的长度大，或者 key 不存在时，返回 0
     * @param key
     * @param offset
     * @return
     */
    boolean getBit(String key, long offset);
    /**
     * 计算给定字符串中，被设置为 1 的比特位的数量
     * @param key
     * @return
     */
    Long bitCount(String key);
    /**
     * 计算给定字符串中，被设置为 1 的比特位的数量
     * @param key
     * @param start		开始位置，都可以使用负数值：比如 -1 表示最后一个位，而 -2 表示倒数第二个位
     * @param end
     * @return
     */
    Long bitCount(String key, final long start, final long end);
    /**
     * 对一个或多个保存二进制位的字符串 key 进行位元操作，并将结果保存到 destkey 上
     * @param op	com.mortals.framework.service.ICacheService.BitOperation
     * @param destKey
     * @param keys
     * @return
     */
    long bitop(BitOperation op, String destKey, String... keys);

    // hash操作===========
    boolean hset(String key, String field, Object value);

    <T> T hget(String key, String field);

    <T> T hget(String key, String field, Class<T> clazz);

    boolean hsetnx(String key, String field, Object value);

    boolean hmset(String key, Map<String, Object> hash);

    List<String> hmget(String key, String... fields);

    <T> List<T> hmget(String key, Class<T> clazz, String... fields);

    Long hincrBy(String key, String field, long value);

    Double hincrByFloat(final String key, final String field, final double value);
    /**
     * 增加计数（具有有效期）
     * @param key
     * @param field
     * @param value
     * @param timeout		有效期，单位：秒
     * @return
     */
    Long hincrByForTime(String key, String field, long value, long timeout);

    boolean hexists(String key, String field);

    void hdel(String key, String... field);

    Long hlen(String key);

    Set<String> hkeys(String key);

    List<String> hvals(String key);

    Map<String, String> hgetAll(String key);

    <T> Map<String, T> hgetAll(String key, Class<T> clazz);

    ScanResult<Map.Entry<String, String>> hscan(String key, long cursor);

    ScanResult<String> scan(String pattern, String cursor);
    /**
     * set集合添加元素
     * @param key
     * @param values
     * @return
     */
    Long sadd(String key, Object... values);
    /**
     * set集合添加元素(延时过期)
     * @param key
     * @param timeout
     * @param values
     * @return
     */
    Long saddForTimeWithDelay(String key, long timeout, Object... values);
    /**
     * 移除集合中的元素
     * @param key
     * @param values
     * @return
     */
    Long srem(String key, Object... values);
    /**
     * 返回集合 key中的所有成员
     * @param key
     * @return
     */
    Set<String> smembers(String key);
    /**
     * 判断 member 元素是否集合 key 的成员
     * @param key
     * @param members
     * @return
     */
    boolean sismembers(String key, Object members);

    String spop(String key);

    <T> T spop(String key, Class<T> clazz);
    /**
     * 返回集合 key中的所有成员
     * @param key
     * @param clazz
     * @return
     */
    <T> Set<T> smembers(String key, Class<T> clazz);

    /******************** List(列表) start ******************/
    /**
     * 随机返回集合 key中的指定成员数
     * 只提供 key 参数时，返回一个元素；如果集合为空，返回 nil
     * 如果提供了 count 参数，那么返回一个数组；如果集合为空，返回空数组
     * @param key
     * @param count	返回成员个数
     * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。
     * 如果 count 大于等于集合基数，那么返回整个集合
     * 如果 count 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值
     * @return
     */
    List<String> srandmember(String key, Long count);
    /**
     * 随机返回集合 key中的指定成员数
     * 只提供 key 参数时，返回一个元素；如果集合为空，返回 nil
     * 如果提供了 count 参数，那么返回一个数组；如果集合为空，返回空数组
     * @param key
     * @param count	返回成员个数
     * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。
     * 如果 count 大于等于集合基数，那么返回整个集合
     * 如果 count 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值
     * @param clazz
     * @return
     */
    <T> List<T> srandmember(String key, Long count, Class<T> clazz);
    /**
     * 将一个或多个值 value 插入到列表 key 的表头(最左边)
     * @param key
     * @param values
     * @return
     */
    Long lpush(String key, Object... values);
    /**
     * 将一个或多个值 value 插入到列表 key 的表尾(最右边)
     * @param key
     * @param values
     * @return
     */
    Long rpush(String key, Object... values);
    /**
     * 将一个或多个值 value 插入到列表 key 的表头
     * @param key
     * @param timeout	key的过期时间
     * @param values
     * @return
     */
    Long lpushForTime(String key, long timeout, Object... values);
    /**
     * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定
     * @param key
     * @return
     */
    List<String> lrange(String key);
    /**
     * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定
     * @param key
     * @param clazz
     * @return
     */
    <T> List<T> lrange(String key, Class<T> clazz);
    /**
     * 根据参数 count 的值，移除列表中与参数 value 相等的元素
     * @param key
     * @param count
     * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count
     * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值
     * count = 0 : 移除表中所有与 value 相等的值
     * @param value
     * @return
     */
    long lrem(String key, long count, Object value);
    /**
     * 移除并返回列表 key 的头元素
     * @param key
     * @return	列表的头元素，当 key 不存在时，返回 nil
     */
    <T> T lpop(String key, Class<T> clazz);
    /**
     * 移除并返回列表 key 的尾元素
     * @param key
     * @return	列表的尾元素，当 key 不存在时，返回 nil
     */
    <T> T rpop(String key, Class<T> clazz);
    /**
     * 取出第一个非空列表的头元素(lpop的阻塞式)
     * @param key
     * @param timeout 超时时间，单位：秒
     * @return
     */
    <T> T blpop(String key, long timeout, Class<T> clazz);
    /**
     * 取出第一个非空列表的尾部元素(rpop的阻塞式)
     * @param key
     * @param timeout	超时时间，单位：秒
     * @return
     */
    <T> T brpop(String key, long timeout, Class<T> clazz);

    /******************** List(列表) end   ******************/

    /******************** SortedSet（有序集合） start   ******************/
    boolean zadd(String key, double score, Object value);
    /**
     * 返回有序集 key 中，指定区间内的成员
     * 其中成员的位置按 score 值递增(从小到大)来排序
     * @param key
     * @param start
     * @param end
     * @return
     */
    List<String> zrange(String key, long start, long end);
    /**
     * 返回有序集 key 中，指定区间内的成员
     * 其中成员的位置按 score 值递增(从小到大)来排序
     * @param key
     * @param start
     * @param end
     * @param clazz
     * @return
     */
    <T> List<T> zrange(String key, long start, long end, Class<T> clazz);
    /**
     * 返回有序集 key 中，指定区间内的成员
     * 其中成员的位置按 score 值递减(从大到小)来排列
     * @param key
     * @param start
     * @param end
     * @return
     */
    List<String> zrevrange(String key, long start, long end);
    /**
     * 返回有序集 key 中，指定区间内的成员
     * 其中成员的位置按 score 值递减(从大到小)来排列
     * @param key
     * @param start
     * @param end
     * @param clazz
     * @return
     */
    <T> List<T> zrevrange(String key, long start, long end, Class<T> clazz);
    /**
     * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略
     * @param key
     * @param values
     * @return
     */
    long zrem(String key, Object... values);
    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员
     * @param key
     * @param min
     * @param max
     * @return
     */
    Set<String> zrangebyscore(String key, double min, double max);
    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    Set<String> zrangebyscore(String key, double min, double max, long offset, long count);
    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员，包括成员的score值
     * @param key
     * @param min
     * @param max
     * @return
     */
    Set<?> zrangebyscoreWithScores(String key, double min, double max);
    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员，包括成员的score值
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    Set<?> zrangebyscoreWithScores(String key, double min, double max, long offset, long count);
    /******************** SortedSet（有序集合） end     ******************/
    /**
     * 消息发布
     * @param channel
     * @param message
     */
    void publish(String channel, Serializable message);
    /**
     * 订阅消息(独占线程)
     * @param channel
     * @param jedisPubSub
     */
    void subscribe(String channel, JedisPubSub jedisPubSub);

    /**
     * 获取线程池，注意：需要手动关闭
     * @return
     */
    Pool getPool();
}
