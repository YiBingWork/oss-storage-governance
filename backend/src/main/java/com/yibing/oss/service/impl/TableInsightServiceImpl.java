package com.yibing.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yibing.oss.entity.TableInsight;
import com.yibing.oss.mapper.TableInsightMapper;
import com.yibing.oss.service.TableInsightService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TableInsightServiceImpl extends ServiceImpl<TableInsightMapper, TableInsight> implements TableInsightService {

    @Override
    public IPage<TableInsight> pageByBucket(Long bucketId, String prefix, int page, int size) {
        LambdaQueryWrapper<TableInsight> wrapper = new LambdaQueryWrapper<>();
        if (bucketId != null) {
            wrapper.eq(TableInsight::getBucketId, bucketId);
        }
        if (StringUtils.hasText(prefix)) {
            wrapper.likeRight(TableInsight::getPrefix, prefix);
        }
        wrapper.orderByDesc(TableInsight::getStorageBytes);
        return page(new Page<>(page, size), wrapper);
    }
}
