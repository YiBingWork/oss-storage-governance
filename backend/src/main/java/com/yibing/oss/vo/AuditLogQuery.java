package com.yibing.oss.vo;

import lombok.Data;
import java.util.List;

@Data
public class AuditLogQuery {
    private List<String> buckets;
    private String eventName;
    private String status; // e.g. "SUCCESS", "FAILED"
    private String startTime; // format yyyy-MM-dd HH:mm:ss
    private String endTime;

    // Pagination parameters
    private Integer current = 1;
    private Integer size = 20;
}