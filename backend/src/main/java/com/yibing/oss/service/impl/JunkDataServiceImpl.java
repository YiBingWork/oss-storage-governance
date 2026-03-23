package com.yibing.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yibing.oss.entity.JunkDataRecord;
import com.yibing.oss.mapper.JunkDataRecordMapper;
import com.yibing.oss.service.JunkDataService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class JunkDataServiceImpl extends ServiceImpl<JunkDataRecordMapper, JunkDataRecord> implements JunkDataService {

    @Override
    public IPage<JunkDataRecord> pageByBucket(Long bucketId, String status, String reason, int page, int size) {
        LambdaQueryWrapper<JunkDataRecord> wrapper = new LambdaQueryWrapper<>();
        if (bucketId != null) {
            wrapper.eq(JunkDataRecord::getBucketId, bucketId);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(JunkDataRecord::getStatus, status);
        }
        if (StringUtils.hasText(reason)) {
            wrapper.eq(JunkDataRecord::getReason, reason);
        }
        wrapper.orderByDesc(JunkDataRecord::getSizeBytes);
        return page(new Page<>(page, size), wrapper);
    }

    @Override
    public boolean markDeleted(Long id) {
        JunkDataRecord record = getById(id);
        if (record == null) {
            return false;
        }
        record.setStatus("DELETED");
        return updateById(record);
    }

    @Override
    public boolean markRetained(Long id) {
        JunkDataRecord record = getById(id);
        if (record == null) {
            return false;
        }
        record.setStatus("RETAINED");
        return updateById(record);
    }
}
