package com.yibing.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yibing.oss.entity.AuditLog;
import com.yibing.oss.mapper.AuditLogMapper;
import com.yibing.oss.service.AuditLogService;
import com.yibing.oss.vo.AuditLogQuery;
import com.yibing.oss.vo.AuditLogSummaryVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuditLogServiceImpl extends ServiceImpl<AuditLogMapper, AuditLog> implements AuditLogService {

    @Override
    public Page<AuditLog> getAuditLogs(AuditLogQuery query) {
        Page<AuditLog> page = new Page<>(query.getCurrent(), query.getSize());
        QueryWrapper<AuditLog> queryWrapper = new QueryWrapper<>();

        if (query.getBuckets() != null && !query.getBuckets().isEmpty()) {
            queryWrapper.in("bucket_name", query.getBuckets());
        }

        if (StringUtils.hasText(query.getEventName())) {
            queryWrapper.eq("event_name", query.getEventName());
        }

        if (StringUtils.hasText(query.getStatus())) {
            if ("SUCCESS".equalsIgnoreCase(query.getStatus())) {
                queryWrapper.lt("response_code", 400);
            } else if ("FAILED".equalsIgnoreCase(query.getStatus())) {
                queryWrapper.ge("response_code", 400);
            }
        }

        if (StringUtils.hasText(query.getStartTime())) {
            queryWrapper.ge("request_time", query.getStartTime());
        }

        if (StringUtils.hasText(query.getEndTime())) {
            queryWrapper.le("request_time", query.getEndTime());
        }

        queryWrapper.orderByDesc("request_time");

        return this.page(page, queryWrapper);
    }

    @Override
    public AuditLogSummaryVO getSummary() {
        AuditLogSummaryVO summary = new AuditLogSummaryVO();
        summary.setExceptionRequestsCount(baseMapper.countExceptionRequests());
        summary.setTopIps(baseMapper.getTop5Ips());
        summary.setHighTrafficBuckets(baseMapper.getHighTrafficBuckets());
        return summary;
    }
}