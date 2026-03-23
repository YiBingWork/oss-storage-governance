package com.yibing.oss.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yibing.oss.entity.JunkDataRecord;

public interface JunkDataService extends IService<JunkDataRecord> {

    IPage<JunkDataRecord> pageByBucket(Long bucketId, String status, String reason, int page, int size);

    boolean markDeleted(Long id);

    boolean markRetained(Long id);
}
