package com.github.houbb.common.cache.test.filter;

import com.github.houbb.common.cache.core.constant.CommonCacheConst;
import com.github.houbb.common.filter.annotation.FilterActive;
import com.github.houbb.common.filter.api.CommonFilter;
import com.github.houbb.common.filter.api.Invocation;
import com.github.houbb.common.filter.api.Invoker;
import com.github.houbb.common.filter.api.Result;
import com.github.houbb.common.filter.exception.CommonFilterException;
import com.github.houbb.common.proxy.core.core.impl.CommonProxyInvocation;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.Arrays;

@FilterActive(group = CommonCacheConst.COMMON_CACHE_GROUP, order = CommonCacheConst.COMMON_CACHE_ORDER)
public class MyCommonCacheFilter implements CommonFilter {

    private static final Log log = LogFactory.getLog(MyCommonCacheFilter.class);

    @Override
    public Result invoke(Invoker invoker, Invocation invocation) throws CommonFilterException {
        CommonProxyInvocation proxyInvocation = (CommonProxyInvocation) invocation;

        Result result = invoker.invoke(invocation);
        log.info("method {}() params {} and result {}", proxyInvocation.getMethod().getName(),
                Arrays.toString(proxyInvocation.getParams()), result);
        return result;
    }

}
