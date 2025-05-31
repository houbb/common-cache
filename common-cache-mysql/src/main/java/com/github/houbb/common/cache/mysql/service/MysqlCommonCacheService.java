package com.github.houbb.common.cache.mysql.service;

import com.github.houbb.common.cache.api.service.AbstractCommonCacheService;
import com.github.houbb.common.cache.core.util.InnerCacheUtil;
import com.github.houbb.common.cache.mysql.model.TCommonCache;
import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.jdbc.api.dal.IMapper;
import com.github.houbb.jdbc.common.dal.DefaultMapper;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import javax.sql.DataSource;

public class MysqlCommonCacheService extends AbstractCommonCacheService {

    /**
     * 日志信息
     * @since 0.0.1
     */
    private static final Log LOG = LogFactory.getLog(MysqlCommonCacheService.class);

    private final IMapper mapper;

    public MysqlCommonCacheService(DataSource dataSource) {
        this.mapper = new DefaultMapper(dataSource);
    }

    protected TCommonCache getCommonCacheAndCheck(String key) {
        ArgUtil.notEmpty(key, "key");

        String sql = "SELECT * from t_common_cache where cache_key = ?";
        TCommonCache cache = mapper.selectOne(sql, TCommonCache.class, key);
        if(cache == null) {
            return null;
        }

        // 判断是否过期
        boolean isExpired = isCacheExpired(cache);
        if(isExpired) {
            // 删除
            remove(key);
            return null;
        }

        return cache;
    }

    /**
     * 缓存是否过期
     * @param cache 缓存
     * @return 结果
     */
    protected boolean isCacheExpired(TCommonCache cache) {
        long expireTime = cache.getCacheExpireTime();
        if(expireTime <= 0) {
            return false;
        }

        final long now = System.currentTimeMillis();
        return expireTime <= now;
    }

    @Override
    public void set(String key, String value, long expireMills) {
        ArgUtil.notEmpty(key, "key");

        long actualMills = 0;
        if(expireMills <= 0) {
            LOG.debug("[Cache] 过期时间小于等于0，认为不过期");
        } else {
            long currentMills = System.currentTimeMillis();
            actualMills = currentMills + expireMills;
        }

        String sql = "INSERT INTO t_common_cache (cache_key, cache_value, cache_expire_time) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE cache_value = ?, cache_expire_time= ?, update_time = CURRENT_TIMESTAMP";

        mapper.executeUpdate(sql, key, value, actualMills, value, actualMills);
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, int time) {
        ArgUtil.notEmpty(key, "key");

        long expireTime = InnerCacheUtil.calculateExpireTime(expx, time); // 根据expx单位计算毫秒时间戳
        String result = null;

        if ("NX".equalsIgnoreCase(nxxx)) {
            // 仅当Key不存在时插入
            String sql = "INSERT INTO t_common_cache (cache_key, cache_value, cache_expire_time) " +
                    "SELECT ?, ?, ? FROM DUAL WHERE NOT EXISTS " +
                    "(SELECT 1 FROM t_common_cache WHERE cache_key = ?)";

            try {
                int count = mapper.executeUpdate(sql, key, value, expireTime, key);
                if (count > 0) {
                    result = "OK";
                }
            } catch (Exception e) {
                // 唯一键冲突视为NX条件失败（不抛异常）
                LOG.warn("SET NX key={}, value={} FAILED", key, value, e);
            }
        } else if ("XX".equalsIgnoreCase(nxxx)) {
            // 仅当Key存在时更新
            String sql = "UPDATE t_common_cache SET cache_value = ?, cache_expire_time = ?, update_time = CURRENT_TIMESTAMP " +
                    "WHERE cache_key = ?";

            try {
                int count = mapper.executeUpdate(sql, value, expireTime, key);
                if (count > 0) {
                    result = "OK";
                }
            } catch (Exception e) {
                // 唯一键冲突视为NX条件失败（不抛异常）
                LOG.warn("SET XX key={}, value={} FAILED", key, value, e);
            }
        }

        return result; // 成功返回"OK"，失败返回null
    }



    @Override
    public String get(String key) {
        ArgUtil.notEmpty(key, "key");

        TCommonCache cache = getCommonCacheAndCheck(key);
        if(cache == null) {
            return null;
        }

        return cache.getCacheValue();
    }

    @Override
    public boolean contains(String key) {
        ArgUtil.notEmpty(key, "key");

        TCommonCache cache = getCommonCacheAndCheck(key);
        return cache != null;
    }

    @Override
    public void remove(String key) {
        ArgUtil.notEmpty(key, "key");
        String sql = "DELETE from t_common_cache where cache_key = ?";
        mapper.executeUpdate(sql, key);
    }

    @Override
    public boolean removeEx(String key, Object expectValue) {
        ArgUtil.notEmpty(key, "key");

        if(expectValue == null) {
            String sql = "DELETE from t_common_cache where cache_key = ? AND cache_value is NULL";

            return mapper.executeUpdate(sql, key) > 0;
        }

        String expectValueStr = String.valueOf(expectValue);
        String sql = "DELETE from t_common_cache where cache_key = ? and cache_value = ?";
        return mapper.executeUpdate(sql, key, expectValueStr) > 0;
    }



    @Override
    public void expireAt(String key, long unixTime) {
        ArgUtil.notEmpty(key, "key");

        String sql = "UPDATE t_common_cache SET cache_expire_time = ? where cache_key = ?";
        mapper.executeUpdate(sql, key, unixTime);
    }

    @Override
    public long expireAt(String key) {
        ArgUtil.notEmpty(key, "key");

        TCommonCache cache = getCommonCacheAndCheck(key);

        // 信息不存在
        if(cache == null) {
            return -2;
        }

        Long expireTime = cache.getCacheExpireTime();
        if(expireTime == null) {
            return -1;
        }

        return expireTime;
    }

    @Override
    public Object eval(String script, int keyCount, String... params) {
        throw new UnsupportedOperationException();
    }

}
