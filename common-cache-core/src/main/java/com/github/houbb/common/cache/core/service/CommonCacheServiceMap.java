package com.github.houbb.common.cache.core.service;

import com.github.houbb.common.cache.api.service.ICommonCacheService;
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
 * @since 1.0.0
 */
public class CommonCacheServiceMap implements ICommonCacheService {

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

    public CommonCacheServiceMap() {
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
                .scheduleAtFixedRate(cleanTask, 10, 10,
                        TimeUnit.SECONDS);
    }

    @Override
    public synchronized void set(String key, String value) {
        this.set(key, value, 0);
    }

    @Override
    public synchronized void set(String key, String value, long expireMills) {
        long actualMills = 0;
        if(expireMills <= 0) {
            LOG.info("过期时间小于0，认为不过期");
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
    public synchronized void expire(String key, long expireTime, TimeUnit timeUnit) {
        //判断 key 是否存在
        if(contains(key)) {
            long currentMills = System.currentTimeMillis();
            long actualMills = currentMills + timeUnit.toMillis(expireTime);

            CommonCacheValueDto dto = cacheMap.get(key);
            dto.setExpireTime(actualMills);

            cacheMap.put(key, dto);
        }
    }

    @Override
    public synchronized void remove(String key) {
        cacheMap.remove(key);
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
