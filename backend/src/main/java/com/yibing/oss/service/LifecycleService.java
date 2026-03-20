package com.yibing.oss.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yibing.oss.entity.LifecycleAdvice;

public interface LifecycleService extends IService<LifecycleAdvice> {

    IPage<LifecycleAdvice> pageByBucket(Long bucketId, String status, int page, int size);

    boolean applyAdvice(Long id);

    boolean ignoreAdvice(Long id);
}
