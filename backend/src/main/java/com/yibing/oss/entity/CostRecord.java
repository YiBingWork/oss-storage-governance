package com.yibing.oss.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 成本记录表 — 按月记录 Bucket 费用
 */
@Data
@TableName("cost_record")
public class CostRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long bucketId;

    /** 所属月份 yyyy-MM */
    private String month;

    /** 存储费用 (元) */
    private BigDecimal storageCost;

    /** 请求费用 (元) */
    private BigDecimal requestCost;

    /** 流量费用 (元) */
    private BigDecimal trafficCost;

    /** 总费用 (元) */
    private BigDecimal totalCost;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
