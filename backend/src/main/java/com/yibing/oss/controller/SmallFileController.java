package com.yibing.oss.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yibing.oss.entity.SmallFileRecord;
import com.yibing.oss.service.SmallFileService;
import com.yibing.oss.vo.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/small-file")
@RequiredArgsConstructor
public class SmallFileController {

    private final SmallFileService smallFileService;

    @GetMapping("/page")
    public R<IPage<SmallFileRecord>> page(
            @RequestParam(required = false) Long bucketId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return R.ok(smallFileService.pageByBucket(bucketId, status, page, size));
    }

    @GetMapping("/{id}")
    public R<SmallFileRecord> get(@PathVariable Long id) {
        return R.ok(smallFileService.getById(id));
    }
}
