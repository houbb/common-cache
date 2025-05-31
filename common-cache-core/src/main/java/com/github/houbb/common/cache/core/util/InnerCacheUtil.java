package com.github.houbb.common.cache.core.util;

/**
 * 内部工具类
 * @since 1.1.0
 */
public class InnerCacheUtil {

    /**
     * 计算过期时间
     * @param expx 类型
     * @param time 时间
     * @return 结果
     */
    public static long calculateExpireTime(String expx, int time) {
        if (time <= 0) return 0; // 永不过期
        return "EX".equalsIgnoreCase(expx) ?
                System.currentTimeMillis() + time * 1000L : // 秒转毫秒
                System.currentTimeMillis() + time; // 毫秒
    }

}
