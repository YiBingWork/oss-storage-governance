package com.yibing.oss.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Bucket 信息表
 */
@Data
@TableName("bucket_info")
public class BucketInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** Bucket 名称 */
    private String bucketName;

    /** 所属区域 */
    private String region;

    /** 存储类型：Standard / IA / Archive */
    private String storageClass;

    /** 当前存储量 (bytes) */
    private Long storageBytes;

    /** 对象数量 */
    private Long objectCount;

    /** 归属团队/业务线 */
    private String ownerTeam;

    /** 最近一次扫描时间 */
    private LocalDateTime lastScanTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
