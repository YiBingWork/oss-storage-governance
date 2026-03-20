package com.yibing.oss.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yibing.oss.entity.CostRecord;

import java.util.List;
import java.util.Map;

public interface CostService extends IService<CostRecord> {

    IPage<CostRecord> pageByBucket(Long bucketId, int page, int size);

    List<Map<String, Object>> monthlyCostTrend(int months);
}
