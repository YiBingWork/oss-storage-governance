package com.yibing.oss.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yibing.oss.entity.CostRecord;
import com.yibing.oss.service.CostService;
import com.yibing.oss.vo.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cost")
@RequiredArgsConstructor
public class CostController {

    private final CostService costService;

    @GetMapping("/page")
    public R<IPage<CostRecord>> page(
            @RequestParam(required = false) Long bucketId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return R.ok(costService.pageByBucket(bucketId, page, size));
    }

    @GetMapping("/trend")
    public R<List<Map<String, Object>>> trend(@RequestParam(defaultValue = "12") int months) {
        return R.ok(costService.monthlyCostTrend(months));
    }
}
