package com.github.houbb.common.cache.api.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 抽象实现
 * @since 0.0.4
 * @author dh
 */
public abstract class AbstractCommonCacheService implements ICommonCacheService {

    @Override
    public void set(String key, String value) {
        this.set(key, value, 0);
    }

    @Override
    public void expire(String key, long expireTime, TimeUnit timeUnit) {
        long currentMills = System.currentTimeMillis();
        long actualMills = currentMills + timeUnit.toMillis(expireTime);

        this.expireAt(key, actualMills);
    }

    @Override
    public Object eval(String script, List<String> keys, List<String> params) {
        return eval(script, keys.size(), getParams(keys, params));
    }

    @Override
    public Object eval(String script) {
        return this.eval(script, 0);
    }

    protected static String[] getParams(List<String> keys, List<String> args) {
        int keyCount = keys.size();
        int argCount = args.size();
        String[] params = new String[keyCount + args.size()];

        int i;
        for(i = 0; i < keyCount; ++i) {
            params[i] = keys.get(i);
        }

        for(i = 0; i < argCount; ++i) {
            params[keyCount + i] = args.get(i);
        }

        return params;
    }

}
