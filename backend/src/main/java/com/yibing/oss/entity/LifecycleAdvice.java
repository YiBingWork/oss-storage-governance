package com.yibing.oss.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 生命周期建议
 */
@Data
@TableName("lifecycle_advice")
public class LifecycleAdvice {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long bucketId;

    /** OSS 前缀 */
    private String prefix;

    /** 当前存储类型 */
    private String currentStorageClass;

    /** 建议存储类型 */
    private String suggestedStorageClass;

    /** 最后访问距今天数 */
    private Integer daysSinceLastAccess;

    /** 预计月度节省 (元) */
    private BigDecimal estimatedMonthlySaving;

    /** 建议状态: PENDING / APPLIED / IGNORED */
    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
