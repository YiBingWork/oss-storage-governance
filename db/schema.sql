-- ============================================================
-- OSS Storage Governance Platform — MySQL Schema
-- ============================================================

CREATE DATABASE IF NOT EXISTS oss_governance
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE oss_governance;

-- 1. Bucket 信息表
CREATE TABLE IF NOT EXISTS bucket_info (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    bucket_name     VARCHAR(128)  NOT NULL COMMENT 'Bucket 名称',
    region          VARCHAR(64)   NOT NULL COMMENT '所属区域',
    storage_class   VARCHAR(32)   NOT NULL DEFAULT 'Standard' COMMENT '存储类型',
    storage_bytes   BIGINT        NOT NULL DEFAULT 0 COMMENT '当前存储量 (bytes)',
    object_count    BIGINT        NOT NULL DEFAULT 0 COMMENT '对象数量',
    owner_team      VARCHAR(128)  DEFAULT NULL COMMENT '归属团队',
    last_scan_time  DATETIME      DEFAULT NULL COMMENT '最近扫描时间',
    create_time     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted         TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_bucket_name (bucket_name),
    INDEX idx_owner_team (owner_team)
) ENGINE=InnoDB COMMENT='Bucket 信息表';

-- 2. 成本记录表
CREATE TABLE IF NOT EXISTS cost_record (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    bucket_id       BIGINT        NOT NULL COMMENT '关联 bucket_info.id',
    month           VARCHAR(7)    NOT NULL COMMENT '月份 yyyy-MM',
    storage_cost    DECIMAL(12,4) NOT NULL DEFAULT 0 COMMENT '存储费用',
    request_cost    DECIMAL(12,4) NOT NULL DEFAULT 0 COMMENT '请求费用',
    traffic_cost    DECIMAL(12,4) NOT NULL DEFAULT 0 COMMENT '流量费用',
    total_cost      DECIMAL(12,4) NOT NULL DEFAULT 0 COMMENT '总费用',
    create_time     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_bucket_month (bucket_id, month)
) ENGINE=InnoDB COMMENT='成本记录表';

-- 3. 表级分析表
CREATE TABLE IF NOT EXISTS table_insight (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    bucket_id           BIGINT        NOT NULL,
    prefix              VARCHAR(512)  NOT NULL COMMENT 'OSS 前缀 / 表路径',
    object_count        BIGINT        NOT NULL DEFAULT 0,
    storage_bytes       BIGINT        NOT NULL DEFAULT 0,
    avg_file_size_bytes BIGINT        NOT NULL DEFAULT 0,
    last_modified_time  DATETIME      DEFAULT NULL,
    owner               VARCHAR(128)  DEFAULT NULL COMMENT '数据负责人',
    create_time         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_bucket_prefix (bucket_id, prefix(191))
) ENGINE=InnoDB COMMENT='表级分析表';

-- 4. 小文件记录表
CREATE TABLE IF NOT EXISTS small_file_record (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    bucket_id            BIGINT        NOT NULL,
    prefix               VARCHAR(512)  NOT NULL,
    small_file_count     BIGINT        NOT NULL DEFAULT 0,
    small_file_total_bytes BIGINT      NOT NULL DEFAULT 0,
    threshold_bytes      BIGINT        NOT NULL DEFAULT 1048576 COMMENT '阈值默认 1MB',
    ratio                DECIMAL(5,2)  NOT NULL DEFAULT 0 COMMENT '小文件占比',
    status               VARCHAR(16)   NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING / MERGING / DONE',
    create_time          DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time          DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_bucket_status (bucket_id, status)
) ENGINE=InnoDB COMMENT='小文件记录表';

-- 5. 生命周期建议表
CREATE TABLE IF NOT EXISTS lifecycle_advice (
    id                        BIGINT AUTO_INCREMENT PRIMARY KEY,
    bucket_id                 BIGINT        NOT NULL,
    prefix                    VARCHAR(512)  NOT NULL,
    current_storage_class     VARCHAR(32)   NOT NULL,
    suggested_storage_class   VARCHAR(32)   NOT NULL,
    days_since_last_access    INT           NOT NULL DEFAULT 0,
    estimated_monthly_saving  DECIMAL(12,4) NOT NULL DEFAULT 0,
    status                    VARCHAR(16)   NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING / APPLIED / IGNORED',
    create_time               DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time               DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_bucket_status (bucket_id, status)
) ENGINE=InnoDB COMMENT='生命周期建议表';

-- 6. 无用数据记录表
CREATE TABLE IF NOT EXISTS junk_data_record (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    bucket_id        BIGINT        NOT NULL,
    object_key       VARCHAR(1024) NOT NULL COMMENT 'OSS 对象 Key',
    size_bytes       BIGINT        NOT NULL DEFAULT 0,
    reason           VARCHAR(32)   NOT NULL COMMENT 'EXPIRED / NO_ACCESS / TEMP_FILE / DUPLICATE',
    last_access_time DATETIME      DEFAULT NULL,
    status           VARCHAR(16)   NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING / DELETED / RETAINED',
    create_time      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_bucket_status (bucket_id, status),
    INDEX idx_reason (reason)
) ENGINE=InnoDB COMMENT='无用数据记录表';

-- 7. 表分区级分析表
CREATE TABLE IF NOT EXISTS table_partition_insight (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    bucket_id           BIGINT        NOT NULL,
    table_prefix        VARCHAR(512)  NOT NULL COMMENT '表级前缀',
    partition_name      VARCHAR(256)  NOT NULL COMMENT '分区路径',
    full_prefix         VARCHAR(512)  NOT NULL COMMENT '完整路径',
    object_count        BIGINT        NOT NULL DEFAULT 0,
    storage_bytes       BIGINT        NOT NULL DEFAULT 0,
    avg_file_size_bytes BIGINT        NOT NULL DEFAULT 0,
    last_modified_time  DATETIME      DEFAULT NULL,
    owner               VARCHAR(128)  DEFAULT NULL COMMENT '数据负责人',
    create_time         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_bucket_full_prefix (bucket_id, full_prefix(191))
) ENGINE=InnoDB COMMENT='表分区级分析表';
