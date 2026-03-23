package com.yibing.oss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yibing.oss.entity.BucketInfo;
import org.apache.ibatis.annotations.Select;

public interface BucketInfoMapper extends BaseMapper<BucketInfo> {

    @Select("SELECT COUNT(*) FROM bucket_info WHERE deleted = 0")
    Integer countBuckets();

    @Select("SELECT COALESCE(SUM(storage_bytes), 0) FROM bucket_info WHERE deleted = 0")
    Long sumStorageBytes();

    @Select("SELECT COALESCE(SUM(object_count), 0) FROM bucket_info WHERE deleted = 0")
    Long sumObjectCount();
}
