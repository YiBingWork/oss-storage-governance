package com.yibing.oss.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yibing.oss.entity.SmallFileRecord;

public interface SmallFileService extends IService<SmallFileRecord> {

    IPage<SmallFileRecord> pageByBucket(Long bucketId, String status, int page, int size);
}
