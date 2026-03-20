package com.yibing.oss.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yibing.oss.entity.TableInsight;

public interface TableInsightService extends IService<TableInsight> {

    IPage<TableInsight> pageByBucket(Long bucketId, String prefix, int page, int size);
}
