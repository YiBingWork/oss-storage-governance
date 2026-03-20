package com.yibing.oss.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yibing.oss.entity.BucketInfo;
import com.yibing.oss.service.DashboardService;
import com.yibing.oss.vo.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/buckets")
@RequiredArgsConstructor
public class BucketController {

    private final DashboardService dashboardService;

    @GetMapping
    public R<?> list() {
        return R.ok(dashboardService.list());
    }

    @GetMapping("/{id}")
    public R<BucketInfo> get(@PathVariable Long id) {
        return R.ok(dashboardService.getById(id));
    }
}
