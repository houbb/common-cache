package com.github.houbb.common.cache.core.constant;

/**
 * @since 0.0.7
 */
public class CommonCacheConst {

    /**
     * 通用缓存
     */
    public static final String COMMON_CACHE_GROUP = "common-cache-group";

    /**
     * 通用缓存
     */
    public static final int COMMON_CACHE_ORDER = Integer.MIN_VALUE + 1000;


    /**
     * nxxx – NX|XX, NX -- Only set the key if it does not already exist. XX -- Only set the key if it already exist.
     * expx – EX|PX, expire time units: EX = seconds; PX = milliseconds
     *
     * @since 1.0.1
     */
    public static final String NX = "NX";

    public static final String XX = "XX";

    public static final String EX = "EX";

    public static final String PX = "PX";

    /**
     * 成功
     * @since 1.0.1
     */
    public static final String OK = "OK";

    /**
     * 失败
     * @since 1.0.1
     */
    public static final String FAIL = null;

}
