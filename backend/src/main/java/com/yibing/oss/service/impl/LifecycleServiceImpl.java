package com.yibing.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yibing.oss.entity.LifecycleAdvice;
import com.yibing.oss.mapper.LifecycleAdviceMapper;
import com.yibing.oss.service.LifecycleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LifecycleServiceImpl extends ServiceImpl<LifecycleAdviceMapper, LifecycleAdvice> implements LifecycleService {

    @Override
    public IPage<LifecycleAdvice> pageByBucket(Long bucketId, String status, int page, int size) {
        LambdaQueryWrapper<LifecycleAdvice> wrapper = new LambdaQueryWrapper<>();
        if (bucketId != null) {
            wrapper.eq(LifecycleAdvice::getBucketId, bucketId);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(LifecycleAdvice::getStatus, status);
        }
        wrapper.orderByDesc(LifecycleAdvice::getEstimatedMonthlySaving);
        return page(new Page<>(page, size), wrapper);
    }

    @Override
    public boolean applyAdvice(Long id) {
        LifecycleAdvice advice = getById(id);
        if (advice == null) {
            return false;
        }
        advice.setStatus("APPLIED");
        return updateById(advice);
    }

    @Override
    public boolean ignoreAdvice(Long id) {
        LifecycleAdvice advice = getById(id);
        if (advice == null) {
            return false;
        }
        advice.setStatus("IGNORED");
        return updateById(advice);
    }
}
