package com.github.houbb.common.cache.test.mysql;

import com.github.houbb.common.cache.api.service.ICommonCacheService;
import com.github.houbb.common.cache.mysql.service.MysqlCommonCacheService;
import com.github.houbb.thread.pool.bs.JdbcPoolBs;
import com.github.houbb.thread.pool.constant.DriverNameConst;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.sql.DataSource;

@Ignore
public class MysqlTest {

    private ICommonCacheService buildCommonCache() {
        //datasource
        DataSource dataSource = JdbcPoolBs.newInstance()
                .url("jdbc:mysql://127.0.0.1:3306/common_cache")
                .username("admin")
                .password("123456")
                .driverClass(DriverNameConst.MYSQL)
                .pooled();

        return new MysqlCommonCacheService(dataSource);
    }

    @Test
    public void setTest() {
        ICommonCacheService commonCacheService = buildCommonCache();

        commonCacheService.set("key", "value");
        Assert.assertEquals("value", commonCacheService.get("key"));
    }

    @Test
    public void setExpireTest() {
        ICommonCacheService commonCacheService = buildCommonCache();

        commonCacheService.set("key", "value", 100);
        Assert.assertEquals("value", commonCacheService.get("key"));
        Assert.assertTrue("value", commonCacheService.expireAt("key") > 0);


        commonCacheService.set("key0", "value", 0);
        Assert.assertEquals("value", commonCacheService.get("key0"));
        Assert.assertTrue(commonCacheService.expireAt("key0") == 0);
    }

}
