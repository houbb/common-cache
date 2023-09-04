package com.github.houbb.common.cache.core.util;

import com.github.houbb.common.cache.api.service.ICommonCacheService;
import com.github.houbb.common.cache.core.constant.CommonCacheConst;
import com.github.houbb.common.proxy.core.support.proxy.CommonProxyHelper;
import com.github.houbb.heaven.util.common.ArgUtil;

/**
 * 通用缓存工具类
 *
 * @since 0.0.7
 */
public class CommonCacheUtil {

    /**
     * 获取代理
     * @param rawCache 原始的缓存
     * @return 结果
     * @since 0.0.7
     */
    public static ICommonCacheService proxy(final ICommonCacheService rawCache) {
        ArgUtil.notNull(rawCache, "cache");

        return CommonProxyHelper.getProxy(rawCache, CommonCacheConst.COMMON_CACHE_GROUP);
    }

}
