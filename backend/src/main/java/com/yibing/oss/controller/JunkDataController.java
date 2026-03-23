package com.yibing.oss.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yibing.oss.entity.JunkDataRecord;
import com.yibing.oss.service.JunkDataService;
import com.yibing.oss.vo.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/junk-data")
@RequiredArgsConstructor
public class JunkDataController {

    private final JunkDataService junkDataService;

    @GetMapping("/page")
    public R<IPage<JunkDataRecord>> page(
            @RequestParam(required = false) Long bucketId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String reason,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return R.ok(junkDataService.pageByBucket(bucketId, status, reason, page, size));
    }

    @PutMapping("/{id}/delete")
    public R<Boolean> markDeleted(@PathVariable Long id) {
        return R.ok(junkDataService.markDeleted(id));
    }

    @PutMapping("/{id}/retain")
    public R<Boolean> markRetained(@PathVariable Long id) {
        return R.ok(junkDataService.markRetained(id));
    }
}
