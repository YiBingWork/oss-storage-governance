package com.yibing.oss.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yibing.oss.entity.TableInsight;
import com.yibing.oss.entity.TablePartitionInsight;
import com.yibing.oss.service.TableInsightService;
import com.yibing.oss.service.TablePartitionInsightService;
import com.yibing.oss.vo.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/table-insight")
@RequiredArgsConstructor
public class TableInsightController {

    private final TableInsightService tableInsightService;
    private final TablePartitionInsightService tablePartitionInsightService;

    @GetMapping("/page")
    public R<IPage<TableInsight>> page(
            @RequestParam(required = false) Long bucketId,
            @RequestParam(required = false) String prefix,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return R.ok(tableInsightService.pageByBucket(bucketId, prefix, page, size));
    }

    @GetMapping("/{id}")
    public R<TableInsight> get(@PathVariable Long id) {
        return R.ok(tableInsightService.getById(id));
    }

    @GetMapping("/partition/page")
    public R<IPage<TablePartitionInsight>> partitionPage(
            @RequestParam(required = false) Long bucketId,
            @RequestParam(required = false) String prefix,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return R.ok(tablePartitionInsightService.pageByBucketAndPrefix(bucketId, prefix, page, size));
    }
}
