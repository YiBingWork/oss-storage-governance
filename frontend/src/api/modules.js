import api from './index'

// 全局看板
export const getDashboardOverview = () => api.get('/dashboard/overview')
export const getDashboardTrend = (months = 12) => api.get('/dashboard/trend', { params: { months } })

// Bucket
export const getBuckets = () => api.get('/buckets')
export const getBucket = (id) => api.get(`/buckets/${id}`)

// 成本中心
export const getCostPage = (params) => api.get('/cost/page', { params })
export const getCostTrend = (months = 12) => api.get('/cost/trend', { params: { months } })

// 表级分析
export const getTableInsightPage = (params) => api.get('/table-insight/page', { params })
export const getTablePartitionInsightPage = (params) => api.get('/table-insight/partition/page', { params })
export const getTableInsight = (id) => api.get(`/table-insight/${id}`)

// 小文件治理
export const getSmallFilePage = (params) => api.get('/small-file/page', { params })
export const getSmallFile = (id) => api.get(`/small-file/${id}`)

// 生命周期建议
export const getLifecyclePage = (params) => api.get('/lifecycle/page', { params })
export const applyLifecycleAdvice = (id) => api.put(`/lifecycle/${id}/apply`)
export const ignoreLifecycleAdvice = (id) => api.put(`/lifecycle/${id}/ignore`)

// 无用数据识别
export const getJunkDataPage = (params) => api.get('/junk-data/page', { params })
export const markJunkDeleted = (id) => api.put(`/junk-data/${id}/delete`)
export const markJunkRetained = (id) => api.put(`/junk-data/${id}/retain`)
