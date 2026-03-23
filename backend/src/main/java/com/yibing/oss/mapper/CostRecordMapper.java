package com.yibing.oss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yibing.oss.entity.CostRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CostRecordMapper extends BaseMapper<CostRecord> {

    @Select("SELECT COALESCE(SUM(total_cost), 0) FROM cost_record WHERE month = #{month}")
    BigDecimal sumCostByMonth(@Param("month") String month);

    @Select("SELECT month, SUM(total_cost) AS total FROM cost_record GROUP BY month ORDER BY month DESC LIMIT #{limit}")
    List<Map<String, Object>> monthlyCostTrend(@Param("limit") int limit);
}
