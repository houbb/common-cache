package com.github.houbb.common.cache.test;

import com.github.houbb.common.cache.api.service.ICommonCacheService;
import com.github.houbb.common.cache.core.service.CommonCacheServiceMap;
import com.github.houbb.common.cache.core.util.CommonCacheUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author binbin.hou
 * @since 0.0.1
 */
public class CommonCacheServiceMapTest {

    @Test
    public void commonTest() {
        String key = "test";
        String value = "123";

        final ICommonCacheService commonCacheService = new CommonCacheServiceMap();
        commonCacheService.set(key, value);
        String getVal = commonCacheService.get(key);
        Assert.assertEquals(value, getVal);
    }

    @Test
    public void proxyTest() {
        String key = "test";
        String value = "123";

        final ICommonCacheService commonCacheService = CommonCacheUtil.proxy(new CommonCacheServiceMap());
        commonCacheService.set(key, value);
        String getVal = commonCacheService.get(key);
        Assert.assertEquals(value, getVal);
    }

}
