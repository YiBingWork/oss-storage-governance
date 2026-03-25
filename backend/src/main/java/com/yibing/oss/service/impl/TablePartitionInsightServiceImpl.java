package com.yibing.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yibing.oss.entity.TablePartitionInsight;
import com.yibing.oss.mapper.TablePartitionInsightMapper;
import com.yibing.oss.service.TablePartitionInsightService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TablePartitionInsightServiceImpl extends ServiceImpl<TablePartitionInsightMapper, TablePartitionInsight> implements TablePartitionInsightService {

    @Override
    public IPage<TablePartitionInsight> pageByBucketAndPrefix(Long bucketId, String prefix, int page, int size) {
        LambdaQueryWrapper<TablePartitionInsight> wrapper = new LambdaQueryWrapper<>();
        if (bucketId != null) {
            wrapper.eq(TablePartitionInsight::getBucketId, bucketId);
        }
        if (StringUtils.hasText(prefix)) {
            wrapper.likeRight(TablePartitionInsight::getFullPrefix, prefix);
        }
        wrapper.orderByDesc(TablePartitionInsight::getStorageBytes);
        return page(new Page<>(page, size), wrapper);
    }
}
