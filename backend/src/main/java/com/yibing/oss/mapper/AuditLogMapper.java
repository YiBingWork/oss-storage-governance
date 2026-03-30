package com.yibing.oss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yibing.oss.entity.AuditLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AuditLogMapper extends BaseMapper<AuditLog> {

    @Select("SELECT COUNT(*) FROM audit_logs WHERE response_code >= 400")
    Long countExceptionRequests();

    @Select("SELECT source_ip AS sourceIp, COUNT(*) as count FROM audit_logs GROUP BY source_ip ORDER BY count DESC LIMIT 5")
    List<Map<String, Object>> getTop5Ips();

    @Select("SELECT bucket_name AS bucketName, COUNT(*) as requestCount FROM audit_logs GROUP BY bucket_name ORDER BY requestCount DESC LIMIT 3")
    List<Map<String, Object>> getHighTrafficBuckets();

}