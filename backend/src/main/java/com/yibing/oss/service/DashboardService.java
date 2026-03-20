package com.yibing.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yibing.oss.entity.BucketInfo;
import com.yibing.oss.vo.DashboardOverviewVO;
import com.yibing.oss.vo.StorageTrendVO;

public interface DashboardService extends IService<BucketInfo> {

    DashboardOverviewVO getOverview();

    StorageTrendVO getStorageTrend(int months);
}
