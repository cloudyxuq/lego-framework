package com.sccl.lego.framework.service;


import com.sccl.lego.framework.exception.AppException;

/**
 * 序列号服务对象
 */
public interface SerialNumberService {
    /**
     * 获取key的值
     *
     * @param key
     * @return
     * @throws AppException
     */
    Object get(String key) throws AppException;

    /**
     * 设置key为当前值
     *
     * @param key
     * @param value
     * @throws AppException
     */
    boolean set(String key, Object value) throws AppException;

    /**
     * 自增当前流水号并返回
     *
     * @param key
     * @return
     * @throws AppException
     */
    Long incrementAndGet(String key) throws AppException;

    /**
     * 自增当前流水号并返回，且当前键的基础自增值在今天有效
     *
     * @param key
     * @return
     */
    Long incrementAndGetForToday(String key) throws AppException;

    /**
     * 自增当前流水号并返回
     *
     * @param key
     * @param timeout key超时时间，单位：秒
     * @return
     * @throws AppException
     */
    Long incrementAndGet(String key, long timeout) throws AppException;

    /**
     * 自增当前流水号并返回
     *
     * @param key
     * @param length 返回流水号的总长度（在左边补0）
     * @return
     * @throws AppException
     */
    String incrementAndGet(String key, int length) throws AppException;

    /**
     * 自增当前流水号并返回，且当前键的基础自增值在今天有效
     *
     * @param key
     * @param length 返回流水号的总长度（在左边补0）
     * @return
     * @throws AppException
     */
    String incrementAndGetForToday(String key, int length) throws AppException;

    /**
     * 自增当前流水号并返回
     *
     * @param key
     * @param length
     * @param timeout key超时时间，单位：秒
     * @return
     * @throws AppException
     */
    String incrementAndGet(String key, int length, long timeout) throws AppException;
}
