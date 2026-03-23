package com.yibing.oss.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 小文件分析记录
 */
@Data
@TableName("small_file_record")
public class SmallFileRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long bucketId;

    /** OSS 前缀 */
    private String prefix;

    /** 小文件数量 (< 阈值) */
    private Long smallFileCount;

    /** 小文件总大小 (bytes) */
    private Long smallFileTotalBytes;

    /** 阈值 (bytes)，如 1MB */
    private Long thresholdBytes;

    /** 占该前缀对象总数百分比 */
    private BigDecimal ratio;

    /** 治理状态: PENDING / MERGING / DONE */
    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
