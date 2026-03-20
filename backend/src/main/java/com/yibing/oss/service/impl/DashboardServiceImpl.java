package com.yibing.oss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yibing.oss.entity.BucketInfo;
import com.yibing.oss.mapper.BucketInfoMapper;
import com.yibing.oss.mapper.CostRecordMapper;
import com.yibing.oss.mapper.JunkDataRecordMapper;
import com.yibing.oss.mapper.SmallFileRecordMapper;
import com.yibing.oss.service.DashboardService;
import com.yibing.oss.vo.DashboardOverviewVO;
import com.yibing.oss.vo.StorageTrendVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl extends ServiceImpl<BucketInfoMapper, BucketInfo> implements DashboardService {

    private final BucketInfoMapper bucketInfoMapper;
    private final CostRecordMapper costRecordMapper;
    private final SmallFileRecordMapper smallFileRecordMapper;
    private final JunkDataRecordMapper junkDataRecordMapper;

    @Override
    public DashboardOverviewVO getOverview() {
        DashboardOverviewVO vo = new DashboardOverviewVO();
        vo.setBucketCount(bucketInfoMapper.countBuckets());
        vo.setTotalStorageBytes(bucketInfoMapper.sumStorageBytes());
        vo.setTotalObjectCount(bucketInfoMapper.sumObjectCount());

        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        vo.setMonthCostYuan(costRecordMapper.sumCostByMonth(currentMonth));

        BigDecimal smallFileRatio = smallFileRecordMapper.calcSmallFileRatio();
        vo.setSmallFileRatio(smallFileRatio != null ? smallFileRatio : BigDecimal.ZERO);

        BigDecimal junkDataRatio = junkDataRecordMapper.calcJunkDataRatio();
        vo.setJunkDataRatio(junkDataRatio != null ? junkDataRatio : BigDecimal.ZERO);

        return vo;
    }

    @Override
    public StorageTrendVO getStorageTrend(int months) {
        List<Map<String, Object>> trends = costRecordMapper.monthlyCostTrend(months);
        StorageTrendVO vo = new StorageTrendVO();
        List<String> dates = new ArrayList<>();
        List<BigDecimal> costs = new ArrayList<>();

        for (Map<String, Object> row : trends) {
            dates.add(String.valueOf(row.get("month")));
            costs.add((BigDecimal) row.get("total"));
        }
        vo.setDates(dates);
        vo.setCosts(costs);
        return vo;
    }
}
