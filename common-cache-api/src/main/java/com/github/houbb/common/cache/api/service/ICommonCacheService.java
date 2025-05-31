package com.github.houbb.common.cache.api.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 通用缓存接口
 *
 * @author binbin.hou
 * @since 0.0.1
 */
public interface ICommonCacheService {

    /**
     * 设置
     * @param key key
     * @param value 值
     * @since 0.0.1
     */
    void set(String key, String value);

    /**
     * 设置对应的值
     * @param key 健
     * @param value 值
     * @param expireMills 过期的耗秒数  过期时间小于等于0，认为不过期
     */
    void set(String key, String value, long expireMills);

    /**
     * 设置对应的值
     * @param key 键
     * @param value 值
     * @param nxxx NX
     * @param expx PX
     * @param time 过期时间，单位毫秒
     * @return 结果
     * @since 0.0.5
     */
    String set(String key, String value, String nxxx, String expx, int time);

    /**
     * 获取对应的值
     * @param key key
     * @return 结果
     */
    String get(String key);

    /**
     * 是否包含指定的 key
     * 同 exists
     * @param key 键
     * @return 结果
     * @since 0.0.1
     */
    boolean contains(String key);

    /**
     * 删除
     * 同 del
     * @param key 键
     * @since 0.0.1
     */
    void remove(String key);

    /**
     * 只有当存在时，且为预期值才删除
     * @param key key
     * @param expectValue 值
     * @return 结果
     * @since 1.1.0
     */
    boolean removeEx(final String key, final Object expectValue);

    /**
     * key 的存活时间
     *
     * In Redis 2.8 or newer, if the Key does not have an associated expire, -1 is returned or if the Key does not exists, -2 is returned.
     * @param key 获取 key
     * @return 结果
     * @since 0.0.3
     */
    long ttl(String key);

    /**
     * 过期
     * @param key key
     * @param expireTime 过期时间
     * @param timeUnit 时间单位
     * @since 0.0.1
     */
    void expire(String key, long expireTime, TimeUnit timeUnit);

    /**
     * 指定过期时间
     * @param key 键
     * @param unixTime 时间
     * @since 0.0.3
     */
    void expireAt(String key, long unixTime);

    /**
     * 获取过期的 UNIX 时间，不存在时返回 null
     * @param key 键
     * @return 时间
     * @since 0.0.3
     */
    long expireAt(String key);

    /**
     * EVAL 对应的值
     * @param script 脚本
     * @param keyCount key 总数
     * @param params 值
     * @return 结果
     * @since 0.0.4
     */
    Object eval(String script, int keyCount, String... params);

    /**
     * 执行脚本
     * @param script 脚本
     * @param keys 键
     * @param params 值
     * @return 结果
     * @since 0.0.4
     */
    Object eval(String script, List<String> keys, List<String> params);

    /**
     * EVAL 脚本
     * @param script 脚本
     * @return 结果
     * @since 0.0.4
     */
    Object eval(String script);

}
