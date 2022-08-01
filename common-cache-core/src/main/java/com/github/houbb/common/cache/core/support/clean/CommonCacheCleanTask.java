package com.github.houbb.common.cache.core.support.clean;

import com.github.houbb.common.cache.core.dto.CommonCacheValueDto;
import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.Map;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class CommonCacheCleanTask implements Runnable {

    /**
     * 日志信息
     * @since 0.0.1
     */
    private static final Log LOG = LogFactory.getLog(CommonCacheCleanTask.class);

    private final Map<String, CommonCacheValueDto> map;

    public CommonCacheCleanTask(Map<String, CommonCacheValueDto> map) {
        ArgUtil.notNull(map, "map");
        this.map = map;
    }

    @Override
    public void run() {
        LOG.info("[Cache] 开始清理过期数据");

        // 当前时间固定，不需要考虑删除的耗时
        // 毕竟最多相差 1s，但是和系统的时钟交互是比删除耗时多的。
        long currentMills = System.currentTimeMillis();

        // 这种简单的实现存在一个问题，如果数量很多的时候，会比较慢。
        for(Map.Entry<String, CommonCacheValueDto> entry : map.entrySet()) {
            Long expireTime = entry.getValue().getExpireTime();
            if(expireTime == null) {
                continue;
            }

            if(currentMills >= expireTime) {
                final String key = entry.getKey();
                map.remove(key);
                LOG.info("[Cache] 移除 key: {}", key);
            }
        }

        LOG.info("[Cache] 结束清理过期数据");
    }

}
