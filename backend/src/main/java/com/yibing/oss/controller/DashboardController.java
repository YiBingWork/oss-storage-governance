package com.yibing.oss.controller;

import com.yibing.oss.service.DashboardService;
import com.yibing.oss.vo.DashboardOverviewVO;
import com.yibing.oss.vo.R;
import com.yibing.oss.vo.StorageTrendVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    public R<DashboardOverviewVO> overview() {
        return R.ok(dashboardService.getOverview());
    }

    @GetMapping("/trend")
    public R<StorageTrendVO> trend(@RequestParam(defaultValue = "12") int months) {
        return R.ok(dashboardService.getStorageTrend(months));
    }
}
