package com.yibing.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yibing.oss.entity.CostRecord;
import com.yibing.oss.mapper.CostRecordMapper;
import com.yibing.oss.service.CostService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CostServiceImpl extends ServiceImpl<CostRecordMapper, CostRecord> implements CostService {

    @Override
    public IPage<CostRecord> pageByBucket(Long bucketId, int page, int size) {
        LambdaQueryWrapper<CostRecord> wrapper = new LambdaQueryWrapper<>();
        if (bucketId != null) {
            wrapper.eq(CostRecord::getBucketId, bucketId);
        }
        wrapper.orderByDesc(CostRecord::getMonth);
        return page(new Page<>(page, size), wrapper);
    }

    @Override
    public List<Map<String, Object>> monthlyCostTrend(int months) {
        return baseMapper.monthlyCostTrend(months);
    }
}
