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

- 基于 HashMap 的实现

> [变更日志](https://github.com/houbb/common-cache/blob/master/CHANGELOG.md)

# 快速开始

## maven 引入

```xml
<dependency>
    <group>com.github.houbb</group>
    <artifact>common-cache-core</artifact>
    <version>${最新版本}</version>
</dependency>
```

## 方法

方法说明如下：

| 方法 | 说明 | 返回值 |
|:---|:---|:---|
| set(String key, String value) | 设置对应的值 | void |
| set(String key, String value, long expireMills) | 设置对应的值，指定过期时间 | void |
| get(String key) | 获取值 | String |
| contains(String key) | 是否包含 | boolean |
| expire(String key, long expireTime, TimeUnit timeUnit) | 指定过期信息 | void |
| remove(String key) | 移除指定的 key | void |

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

# Road-Map

- [ ] 添加基于 redis 的 cache 实现

- [ ] 添加基于 mysql 的 cache 实现
