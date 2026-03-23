package com.yibing.oss.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 存储趋势 VO
 */
@Data
public class StorageTrendVO {

    private List<String> dates;
    private List<Long> storageBytes;
    private List<BigDecimal> costs;
}
