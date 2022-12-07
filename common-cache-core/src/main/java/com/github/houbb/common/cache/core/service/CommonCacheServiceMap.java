package com.github.houbb.common.cache.core.service;

import com.github.houbb.common.cache.api.service.AbstractCommonCacheService;
import com.github.houbb.common.cache.core.dto.CommonCacheValueDto;
import com.github.houbb.common.cache.core.support.clean.CommonCacheCleanTask;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 基于 map 的本地实现
 *
 * @author binbin.hou
 * @since 0.0.1
 */
public class CommonCacheServiceMap extends AbstractCommonCacheService {

    /**
     * 日志信息
     * @since 0.0.1
     */
    private static final Log LOG = LogFactory.getLog(CommonCacheServiceMap.class);

    /**
     * 存储信息
     * @since 0.0.1
     */
    private Map<String, CommonCacheValueDto> cacheMap;

    /**
     * 清空任务-延迟秒数
     */
    private final long cleanDelaySeconds;

    /**
     * 清空任务-周期秒数
     */
    private final long cleanPeriodSeconds;

    public CommonCacheServiceMap() {
        this(10, 60);
    }

    public CommonCacheServiceMap(long cleanDelaySeconds, long cleanPeriodSeconds) {
        this.cleanDelaySeconds = cleanDelaySeconds;
        this.cleanPeriodSeconds = cleanPeriodSeconds;
        this.initMap();
        this.initCleanTask();
    }

    /**
     * 初始化缓存
     */
    protected void initMap() {
        this.cacheMap = new ConcurrentHashMap<>();
    }

    /**
     * 初始化清理任务
     */
    protected void initCleanTask() {
        final Runnable cleanTask = new CommonCacheCleanTask(this.cacheMap);

        // 这里的调度参数，没有必要暴露。
        // 采用和 redis 类似的惰性淘汰即可。
        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(cleanTask, cleanDelaySeconds, cleanPeriodSeconds,
                        TimeUnit.SECONDS);
    }

    @Override
    public synchronized void set(String key, String value, long expireMills) {
        long actualMills = 0;
        if(expireMills <= 0) {
            LOG.info("过期时间小于等于0，认为不过期");
        } else {
            long currentMills = System.currentTimeMillis();
            actualMills = currentMills + expireMills;
        }

        CommonCacheValueDto dto = CommonCacheValueDto.of(value, actualMills);
        cacheMap.put(key, dto);
    }

    @Override
    public String get(String key) {
        checkExpireAndRemove(key);

        CommonCacheValueDto dto = cacheMap.get(key);
        if(dto == null) {
            return null;
        }

        return dto.getValue();
    }

    @Override
    public boolean contains(String key) {
        checkExpireAndRemove(key);

        return cacheMap.containsKey(key);
    }

    @Override
    public synchronized void remove(String key) {
        cacheMap.remove(key);
    }

    /**
     *
     * @param key 获取 key
     * @return 结果
     */
    @Override
    public long ttl(String key) {
        checkExpireAndRemove(key);

        CommonCacheValueDto dto = cacheMap.get(key);
        // 信息不存在
        if(dto == null) {
            return -2L;
        }
        // 没有指定过期时间
        Long expireTime = dto.getExpireTime();
        if(expireTime == null) {
            return -1L;
        }

        // 获取真实的过期时间
        long currentTime = System.currentTimeMillis();
        return expireTime - currentTime;
    }

    @Override
    public void expireAt(String key, long unixTime) {
        //判断 key 是否存在
        if(contains(key)) {
            CommonCacheValueDto dto = cacheMap.get(key);
            dto.setExpireTime(unixTime);

            cacheMap.put(key, dto);
        }
    }

    @Override
    public long expireAt(String key) {
        checkExpireAndRemove(key);

        CommonCacheValueDto dto = cacheMap.get(key);
        // 信息不存在
        if(dto == null) {
            return -2;
        }

        Long expireTime = dto.getExpireTime();
        if(expireTime == null) {
            return -1;
        }

        return expireTime;
    }

    @Override
    public Object eval(String var1, int var2, String... var3) {
        throw new UnsupportedOperationException();
    }

    /**
     * 当一个信息过期的时候，将其清空。惰性淘汰
     * @param key 键
     */
    private synchronized void checkExpireAndRemove(String key) {
        //1. 获取
        CommonCacheValueDto dto = cacheMap.get(key);
        if(dto == null) {
            return;
        }

        Long expireTime = dto.getExpireTime();
        if(expireTime != null) {
            long currentMills = System.currentTimeMillis();
            if(expireTime <= currentMills) {
                cacheMap.remove(key);
            }
        }
    }


}
