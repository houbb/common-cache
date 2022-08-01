package com.github.houbb.common.cache.core.dto;

import java.io.Serializable;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class CommonCacheValueDto implements Serializable {

    /**
     * 对应的值
     */
    private String value;

    /**
     * 对应的过期时间
     */
    private Long expireTime;

    public static CommonCacheValueDto of(String value, Long expireTime) {
        CommonCacheValueDto dto = new CommonCacheValueDto();
        dto.setValue(value);

        // 设置过期时间
        if(expireTime != null
            && expireTime > 0) {
            dto.setExpireTime(expireTime);
        }

        return dto;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "CommonCacheValueDto{" +
                "value='" + value + '\'' +
                ", expireTime=" + expireTime +
                '}';
    }

}
