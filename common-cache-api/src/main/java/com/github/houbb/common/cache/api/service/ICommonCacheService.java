package com.github.houbb.common.cache.api.service;

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
     * @since 1.0.0
     */
    void set(String key, String value);

    /**
     * 设置对应的值
     * @param key 健
     * @param value 值
     * @param expireMills 过期的耗秒数
     */
    void set(String key, String value, long expireMills);

    /**
     * 获取对应的值
     * @param key key
     * @return 结果
     */
    String get(String key);

    /**
     * 是否包含指定的 key
     * @param key 键
     * @return 结果
     * @since 1.0.0
     */
    boolean contains(String key);

    /**
     * 过期
     * @param key key
     * @param expireTime 过期时间
     * @param timeUnit 时间单位
     * @since 1.0.0
     */
    void expire(String key, long expireTime, TimeUnit timeUnit);

    /**
     * 删除
     * @param key 键
     * @since 1.0.0
     */
    void remove(String key);

}
