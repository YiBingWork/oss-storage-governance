package com.yibing.oss.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yibing.oss.entity.TablePartitionInsight;

public interface TablePartitionInsightService extends IService<TablePartitionInsight> {

    IPage<TablePartitionInsight> pageByBucketAndPrefix(Long bucketId, String prefix, int page, int size);
}
