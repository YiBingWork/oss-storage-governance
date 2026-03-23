package com.yibing.oss.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 全局看板概览 VO
 */
@Data
public class DashboardOverviewVO {

    /** Bucket 总数 */
    private Integer bucketCount;

    /** 总存储量 (bytes) */
    private Long totalStorageBytes;

    /** 总对象数 */
    private Long totalObjectCount;

    /** 本月费用 (元) */
    private BigDecimal monthCostYuan;

    /** 小文件占比 (%) */
    private BigDecimal smallFileRatio;

    /** 无用数据占比 (%) */
    private BigDecimal junkDataRatio;
}
