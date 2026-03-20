package com.yibing.oss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yibing.oss.entity.JunkDataRecord;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

public interface JunkDataRecordMapper extends BaseMapper<JunkDataRecord> {

    @Select("SELECT COUNT(*) * 100.0 / NULLIF((SELECT SUM(object_count) FROM bucket_info WHERE deleted = 0), 0) FROM junk_data_record WHERE status = 'PENDING'")
    BigDecimal calcJunkDataRatio();
}
