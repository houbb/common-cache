# common-cache

[common-cache](https://github.com/houbb/common-cache) 是一款为 java 设计的通用缓存。

[![Build Status](https://travis-ci.com/houbb/common-cache.svg?branch=master)](https://travis-ci.com/houbb/common-cache)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/common-cache/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/common-cache)
[![](https://img.shields.io/badge/license-Apache2-FF0080.svg)](https://github.com/houbb/common-cache/blob/master/LICENSE.txt)
[![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.svg?v=103)](https://github.com/houbb/common-cache)

## 创作目的

自己实现的很多开源工具中，都会用到 cache。

每次写都会导致重复，为了便于复用，统一实现一套 cache 整合。

## 特性

- 统一的标准 cache 接口

- 基于本地 Map 的实现

- 基于 mysql 实现

- 基于 redis 实现

> [变更日志](https://github.com/houbb/common-cache/blob/master/CHANGELOG.md)

# 快速开始

## maven 引入

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>common-cache-core</artifactId>
    <version>1.1.0</version>
</dependency>
```

## 方法

方法说明如下：

| 方法                                                                        | 说明                             | 返回值             |
|:--------------------------------------------------------------------------|:-------------------------------|:----------------|
| set(String key, String value)                                             | 设置对应的值                         | void            |
| set(String key, String value, long expireMills)                           | 设置对应的值，指定过期时间                  | void            |
| set(String key, String value, String nxxx, String expx, long expireMills) | 设置对应的值，指定过期时间。支持 nx            | String "OK"代表成功 |
| get(String key)                                                           | 获取值                            | String          |
| contains(String key)                                                      | 是否包含                           | boolean         |
| expire(String key, long expireTime, TimeUnit timeUnit)                    | 指定过期时间                         | void            |
| expireAt(String key, long unixTime)                                       | 指定 key 的过期时间                   | void            |
| remove(String key)                                                        | 移除指定的 key                      | void            |
| removeEx(String key, Object value)                                        | 移除指定的 key，且 value 也等于预期值       | void            |
| ttl(String key)                                                           | 获取指定 key 的存活时间，不过期返回-1，不存在返回-2 | long            |
| expireAt(String key)                                                      | 获取 key 的过期时间，不过期返回-1，不存在返回-2   | long            |

## 使用

基于 map 的本地实现

```java
String key = "test";
String value = "123";

final ICommonCacheService commonCacheService = new CommonCacheServiceMap();
commonCacheService.set(key, value);
String getVal = commonCacheService.get(key);
Assert.assertEquals(value, getVal);
```

# mysql

## 准备工作

执行建表语句

```sql
CREATE DATABASE common_cache;
use common_cache;

CREATE TABLE t_common_cache
(
    id               bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    cache_key         varchar(128) NOT NULL COMMENT '缓存键',
    cache_value      varchar(32)  NOT NULL DEFAULT '' COMMENT '缓存值',
    cache_expire_time bigint(20) NOT NULL DEFAULT 0 COMMENT '到期时间',
    create_user      varchar(32)  NOT NULL DEFAULT 'SYSTEM' COMMENT '创建者',
    update_user      varchar(32)  NOT NULL DEFAULT 'SYSTEM' COMMENT '更新者',
    create_time      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_cache_key (cache_key)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='数据库缓存表';
```

## 测试

初始化缓存

```java
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
```

测试例子：

```java
ICommonCacheService commonCacheService = buildCommonCache();

commonCacheService.set("key", "value");
Assert.assertEquals("value", commonCacheService.get("key"));
```

# Road-Map

- [x] 添加基于 redis 的 cache 实现

- [x] 添加基于 mysql 的 cache 实现

## 开源矩阵

下面是一些从防止重复提交相关，整个系列的开源矩阵规划。

| 名称 | 介绍 | 状态  |
|:---|:---|:----|
| [resubmit](https://github.com/houbb/resubmit) | 防止重复提交核心库 | 已开源 |
| [rate-limit](https://github.com/houbb/rate-limit) | 限流核心库 | 已开源 |
| [cache](https://github.com/houbb/cache) | 手写渐进式 redis | 已开源 |
| [lock](https://github.com/houbb/lock) | 开箱即用的分布式锁 | 已开源 |
| [common-cache](https://github.com/houbb/common-cache) | 通用缓存标准定义 | 已开源 |
| [redis-config](https://github.com/houbb/redis-config) | 兼容各种常见的 redis 配置模式 | 已开源 |
| [quota-server](https://github.com/houbb/quota-server) | 限额限次核心服务 | 待开始 |
| [quota-admin](https://github.com/houbb/quota-admin) | 限额限次控台 | 待开始 |
| [flow-control-server](https://github.com/houbb/flow-control-server) | 流控核心服务 | 待开始 |
| [flow-control-admin](https://github.com/houbb/flow-control-admin) | 流控控台 | 待开始 |