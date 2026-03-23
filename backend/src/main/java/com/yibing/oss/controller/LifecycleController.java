package com.yibing.oss.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yibing.oss.entity.LifecycleAdvice;
import com.yibing.oss.service.LifecycleService;
import com.yibing.oss.vo.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lifecycle")
@RequiredArgsConstructor
public class LifecycleController {

    private final LifecycleService lifecycleService;

    @GetMapping("/page")
    public R<IPage<LifecycleAdvice>> page(
            @RequestParam(required = false) Long bucketId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return R.ok(lifecycleService.pageByBucket(bucketId, status, page, size));
    }

    @PutMapping("/{id}/apply")
    public R<Boolean> apply(@PathVariable Long id) {
        return R.ok(lifecycleService.applyAdvice(id));
    }

    @PutMapping("/{id}/ignore")
    public R<Boolean> ignore(@PathVariable Long id) {
        return R.ok(lifecycleService.ignoreAdvice(id));
    }
}
