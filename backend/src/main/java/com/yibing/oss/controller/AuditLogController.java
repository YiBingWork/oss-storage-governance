package com.yibing.oss.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yibing.oss.entity.AuditLog;
import com.yibing.oss.service.AuditLogService;
import com.yibing.oss.vo.AuditLogQuery;
import com.yibing.oss.vo.AuditLogSummaryVO;
import com.yibing.oss.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @PostMapping("/list")
    public Result<Page<AuditLog>> list(@RequestBody AuditLogQuery query) {
        return Result.success(auditLogService.getAuditLogs(query));
    }

    @GetMapping("/summary")
    public Result<AuditLogSummaryVO> summary() {
        return Result.success(auditLogService.getSummary());
    }
}