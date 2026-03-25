package com.yibing.oss.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表分区级分析记录
 */
@Data
@TableName("table_partition_insight")
public class TablePartitionInsight {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long bucketId;

    /** 所属表级前缀 / 表路径 */
    private String tablePrefix;

    /** 分区路径（例如：dt=2025-03-21）*/
    private String partitionName;

    /** 完整路径（tablePrefix + partitionName）*/
    private String fullPrefix;

    /** 该分区下对象数 */
    private Long objectCount;

    /** 该分区下存储量 (bytes) */
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
