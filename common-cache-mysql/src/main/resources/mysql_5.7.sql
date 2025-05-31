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
