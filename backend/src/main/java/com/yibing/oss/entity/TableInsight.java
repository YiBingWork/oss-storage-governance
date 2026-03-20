package com.yibing.oss.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表级(前缀级)分析记录
 */
@Data
@TableName("table_insight")
public class TableInsight {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long bucketId;

    /** OSS 前缀 / Hive 表路径 */
    private String prefix;

    /** 该前缀下对象数 */
    private Long objectCount;

    /** 该前缀下存储量 (bytes) */
    private Long storageBytes;

    /** 平均文件大小 (bytes) */
    private Long avgFileSizeBytes;

    /** 最后修改时间 */
    private LocalDateTime lastModifiedTime;

    /** 数据所属负责人 */
    private String owner;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
