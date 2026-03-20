package com.yibing.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yibing.oss.entity.SmallFileRecord;
import com.yibing.oss.mapper.SmallFileRecordMapper;
import com.yibing.oss.service.SmallFileService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SmallFileServiceImpl extends ServiceImpl<SmallFileRecordMapper, SmallFileRecord> implements SmallFileService {

    @Override
    public IPage<SmallFileRecord> pageByBucket(Long bucketId, String status, int page, int size) {
        LambdaQueryWrapper<SmallFileRecord> wrapper = new LambdaQueryWrapper<>();
        if (bucketId != null) {
            wrapper.eq(SmallFileRecord::getBucketId, bucketId);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(SmallFileRecord::getStatus, status);
        }
        wrapper.orderByDesc(SmallFileRecord::getSmallFileCount);
        return page(new Page<>(page, size), wrapper);
    }
}
