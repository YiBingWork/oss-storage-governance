package com.yibing.oss.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yibing.oss.entity.AuditLog;
import com.yibing.oss.vo.AuditLogQuery;
import com.yibing.oss.vo.AuditLogSummaryVO;

public interface AuditLogService extends IService<AuditLog> {
    Page<AuditLog> getAuditLogs(AuditLogQuery query);
    AuditLogSummaryVO getSummary();
}