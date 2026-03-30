import api from './index'

// 获取审计日志列表
export function getAuditLogs(data) {
  return api({
    url: '/audit-logs/list',
    method: 'post',
    data
  })
}

// 获取审计日志核心统计数据
export function getAuditLogSummary() {
  return api({
    url: '/audit-logs/summary',
    method: 'get'
  })
}
