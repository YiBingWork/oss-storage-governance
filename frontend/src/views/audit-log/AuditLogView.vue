<template>
  <div class="audit-log-view">
    <!-- Header Filter -->
    <el-card class="filter-card">
      <el-form :inline="true" :model="queryForm">
        <el-form-item label="Bucket:">
          <el-select
            v-model="queryForm.buckets"
            multiple
            collapse-tags
            collapse-tags-tooltip
            placeholder="Select Buckets"
            style="width: 240px"
          >
            <el-option label="data-warehouse-prod" value="data-warehouse-prod" />
            <el-option label="data-warehouse-staging" value="data-warehouse-staging" />
            <el-option label="user-logs-prod" value="user-logs-prod" />
            <el-option label="ml-training-data" value="ml-training-data" />
            <el-option label="backup-archive" value="backup-archive" />
          </el-select>
        </el-form-item>

        <el-form-item label="操作类型:">
          <el-select v-model="queryForm.eventName" placeholder="All Events" clearable style="width: 160px">
            <el-option label="PutObject" value="PutObject" />
            <el-option label="GetObject" value="GetObject" />
            <el-option label="DeleteObject" value="DeleteObject" />
            <el-option label="DeleteObjects" value="DeleteObjects" />
            <el-option label="GetBucketPolicy" value="GetBucketPolicy" />
            <el-option label="DeleteBucket" value="DeleteBucket" />
          </el-select>
        </el-form-item>

        <el-form-item label="状态:">
          <el-select v-model="queryForm.status" placeholder="All Status" clearable style="width: 120px">
            <el-option label="Success (2xx)" value="SUCCESS" />
            <el-option label="Failed (4xx/5xx)" value="FAILED" />
          </el-select>
        </el-form-item>

        <el-form-item label="时间范围:">
          <el-radio-group v-model="timeSpan" @change="handleTimeSpanChange" class="time-quick-select">
            <el-radio-button label="all">全部</el-radio-button>
            <el-radio-button label="1h">1小时</el-radio-button>
            <el-radio-button label="24h">24小时</el-radio-button>
            <el-radio-button label="7d">7天</el-radio-button>
            <el-radio-button label="custom">自定义</el-radio-button>
          </el-radio-group>
          <el-date-picker
            v-if="timeSpan === 'custom'"
            v-model="customTimeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="margin-left: 12px; width: 320px;"
            @change="handleCustomTimeChange"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="fetchData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Summary Info -->
    <el-row :gutter="20" class="summary-cards">
      <el-col :span="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-header">异常请求总数（4xx/5xx）</div>
          <div class="stat-value danger">{{ summary.exceptionRequestsCount || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-header">高频操作 IP TOP 5</div>
          <div class="stat-list">
            <div v-for="(item, index) in summary.topIps" :key="index" class="list-item">
              <span class="ip">{{ item.sourceIp }}</span>
              <span class="count">{{ item.count }} 次</span>
            </div>
            <div v-if="!summary.topIps || summary.topIps.length === 0" class="empty-list">-</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-header">高额流量 Bucket</div>
          <div class="stat-list">
             <div v-for="(item, index) in summary.highTrafficBuckets" :key="index" class="list-item">
              <span class="bucket">{{ item.bucketName }}</span>
              <span class="count">{{ item.requestCount }} 请求</span>
            </div>
            <div v-if="!summary.highTrafficBuckets || summary.highTrafficBuckets.length === 0" class="empty-list">-</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Main Table -->
    <el-card class="table-card">
      <el-table :data="tableData" v-loading="loading" stripe style="width: 100%">
        <el-table-column type="expand">
          <template #default="props">
            <div class="json-viewer-container">
              <div class="json-header">
                <span>原始审计报文</span>
                <el-button size="small" type="primary" plain @click="copyJson(props.row.rawLogJson)">一键复制</el-button>
              </div>
              <pre class="json-pre">{{ formatJson(props.row.rawLogJson) }}</pre>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="请求时间" prop="requestTime" width="180">
          <template #default="{ row }">
            <div style="display: flex; align-items: center;">
              <el-icon v-if="isHighRiskEvent(row.eventName)" color="#F56C6C" style="margin-right: 6px;" title="高危操作"><WarningFilled /></el-icon>
              <span>{{ row.requestTime }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="Bucket" prop="bucketName" width="180" show-overflow-tooltip />
        <el-table-column label="操作者 (AccessKey)" prop="accessKeyId" width="200" show-overflow-tooltip />
        <el-table-column label="源 IP" prop="sourceIp" width="140" />
        <el-table-column label="事件名称" prop="eventName" width="160">
           <template #default="{ row }">
             <el-tag :type="isHighRiskEvent(row.eventName) ? 'danger' : 'info'" size="small">{{ row.eventName }}</el-tag>
           </template>
        </el-table-column>
        <el-table-column label="Object Key" prop="objectKey" show-overflow-tooltip min-width="200">
          <template #default="{ row }">
             <span v-if="row.objectKey">{{ row.objectKey }}</span>
             <span v-else style="color: #909399">-</span>
          </template>
        </el-table-column>
        <el-table-column label="状态码" prop="responseCode" width="100">
           <template #default="{ row }">
             <el-tag :type="row.responseCode >= 400 ? 'danger' : 'success'">{{ row.responseCode }}</el-tag>
           </template>
        </el-table-column>
        <el-table-column label="延迟(ms)" prop="latencyMs" width="100" />
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryForm.current"
          v-model:page-size="queryForm.size"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { WarningFilled } from '@element-plus/icons-vue'
import { getAuditLogs, getAuditLogSummary } from '../../api/auditLog'
import dayjs from 'dayjs'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const summary = ref({
  exceptionRequestsCount: 0,
  topIps: [],
  highTrafficBuckets: []
})

const timeSpan = ref('all')
const customTimeRange = ref(null)

const queryForm = reactive({
  buckets: [],
  eventName: '',
  status: '',
  startTime: '',
  endTime: '',
  current: 1,
  size: 20
})

const isHighRiskEvent = (eventName) => {
  return ['DeleteBucket', 'DeleteObjects'].includes(eventName)
}

const handleTimeSpanChange = (val) => {
  if (val === 'custom') return

  const now = dayjs()
  // Default end time to now for quick ranges
  queryForm.endTime = now.format('YYYY-MM-DD HH:mm:ss')
  // For sample data purposes (all inserted as 2025-03-24), we should not limit by time by default,
  // or use a much wider range if user clicks these. Let's adjust to subtract from 2026 for demo purpose.
  // Actually, to make sure mock data shows up, when click quick times, we will just use a generic wide range.
  if (val === '1h') {
    queryForm.startTime = now.subtract(1, 'hour').format('YYYY-MM-DD HH:mm:ss')
  } else if (val === '24h') {
    queryForm.startTime = now.subtract(24, 'hour').format('YYYY-MM-DD HH:mm:ss')
  } else if (val === '7d') {
    queryForm.startTime = now.subtract(7, 'day').format('YYYY-MM-DD HH:mm:ss')
  } else if (val === 'all') {
    queryForm.startTime = ''
    queryForm.endTime = ''
  }
  fetchData()
}

const handleCustomTimeChange = (val) => {
  if (val && val.length === 2) {
    queryForm.startTime = val[0]
    queryForm.endTime = val[1]
    fetchData()
  } else {
    queryForm.startTime = ''
    queryForm.endTime = ''
  }
}

const resetQuery = () => {
  queryForm.buckets = []
  queryForm.eventName = ''
  queryForm.status = ''
  timeSpan.value = 'all'
  customTimeRange.value = null
  handleTimeSpanChange('all')
}

const fetchSummary = async () => {
  try {
    const res = await getAuditLogSummary()
    if (res.code === 200) {
      summary.value = res.data
    }
  } catch (error) {
    console.error('Failed to fetch summary:', error)
  }
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getAuditLogs(queryForm)
    if (res.code === 200) {
      tableData.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    console.error('Failed to fetch table data:', error)
  } finally {
    loading.value = false
  }
}

const handleSizeChange = (val) => {
  queryForm.size = val
  fetchData()
}

const handleCurrentChange = (val) => {
  queryForm.current = val
  fetchData()
}

const formatJson = (jsonStr) => {
  try {
    const obj = JSON.parse(jsonStr)
    return JSON.stringify(obj, null, 2)
  } catch (e) {
    return jsonStr
  }
}

const copyJson = (text) => {
  try {
    const formattedText = formatJson(text)
    navigator.clipboard.writeText(formattedText)
    ElMessage.success('复制成功')
  } catch (err) {
    ElMessage.error('复制失败，请手动选择复制')
  }
}

onMounted(() => {
  handleTimeSpanChange('all') // initialize time and fetch data
  fetchSummary()
})
</script>

<style scoped>
.audit-log-view {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.filter-card {
  border-radius: 12px;
}

.time-quick-select {
  margin-right: 12px;
}

.stat-card {
  border-radius: 12px;
  height: 140px;
}

.stat-header {
  font-size: 14px;
  color: #64748b;
  margin-bottom: 12px;
  font-weight: 500;
}

.stat-value {
  font-family: 'Inter', 'Roboto', sans-serif;
  font-size: 32px;
  font-weight: 600;
}

.stat-value.danger {
  color: #F56C6C;
}

.stat-list {
  font-size: 13px;
  color: #334155;
  display: flex;
  flex-direction: column;
  gap: 6px;
  max-height: 80px;
  overflow-y: auto;
}

.list-item {
  display: flex;
  justify-content: space-between;
}

.list-item .ip, .list-item .bucket {
  color: #409EFF;
}

.list-item .count {
  font-family: 'Inter', 'Roboto', sans-serif;
  color: #64748b;
}

.empty-list {
  color: #94a3b8;
  font-style: italic;
}

.table-card {
  border-radius: 12px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.json-viewer-container {
  padding: 16px;
  background-color: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  margin: 0 40px;
}

.json-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  font-weight: 600;
  color: #475569;
}

.json-pre {
  margin: 0;
  font-family: 'Menlo', 'Monaco', 'Courier New', monospace;
  font-size: 13px;
  color: #334155;
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>
