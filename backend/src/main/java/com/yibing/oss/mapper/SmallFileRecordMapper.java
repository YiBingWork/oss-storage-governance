package com.yibing.oss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yibing.oss.entity.SmallFileRecord;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

public interface SmallFileRecordMapper extends BaseMapper<SmallFileRecord> {

    @Select("SELECT COALESCE(SUM(small_file_count), 0) * 100.0 / NULLIF((SELECT SUM(object_count) FROM bucket_info WHERE deleted = 0), 0) FROM small_file_record")
    BigDecimal calcSmallFileRatio();
}
