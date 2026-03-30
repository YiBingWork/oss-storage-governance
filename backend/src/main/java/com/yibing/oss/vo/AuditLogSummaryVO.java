package com.yibing.oss.vo;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class AuditLogSummaryVO {
    private Long exceptionRequestsCount;
    private List<Map<String, Object>> topIps;
    private List<Map<String, Object>> highTrafficBuckets;
}