<template>
  <div class="table-insight">
    <el-card shadow="hover">
      <template #header>
        <div class="table-header">
          <span>表级 / 前缀级分析</span>
          <div>
            <el-button type="primary" plain @click="configDialogVisible = true" style="margin-right: 16px;">小文件判定配置</el-button>
            <el-radio-group v-model="viewMode" @change="onViewModeChange" style="margin-right: 16px;">
              <el-radio-button label="table">按表查看</el-radio-button>
              <el-radio-button label="partition">按表分区查看</el-radio-button>
            </el-radio-group>
            <el-select v-model="query.bucketId" placeholder="选择 Bucket" clearable style="width: 200px; margin-right: 10px" @change="loadData">
              <el-option v-for="b in buckets" :key="b.id" :label="b.bucketName" :value="b.id" />
            </el-select>
            <el-input v-model="query.prefix" placeholder="前缀搜索" clearable style="width: 200px" @change="loadData" />
          </div>
        </div>
      </template>

      <el-dialog v-model="configDialogVisible" title="小文件判定配置" width="450px">
        <el-form :model="config" label-width="120px">
          <el-form-item label="极严重 (MB) <">
            <el-input-number v-model="config.extremelySevere" :min="1" />
          </el-form-item>
          <el-form-item label="严重 (MB) <">
            <el-input-number v-model="config.severe" :min="config.extremelySevere + 1" />
          </el-form-item>
          <el-form-item label="轻微 (MB) <">
            <el-input-number v-model="config.slight" :min="config.severe + 1" />
          </el-form-item>
          <div style="margin-left: 120px; font-size: 12px; color: #909399;">大于等于轻微阈值即为：健康</div>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="configDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="saveConfig">保存</el-button>
          </span>
        </template>
      </el-dialog>

      <el-table :data="records" stripe>
        <el-table-column v-if="viewMode === 'table'" prop="prefix" label="前缀 / 表路径" min-width="250" />
        <el-table-column v-if="viewMode === 'partition'" prop="tablePrefix" label="表级前缀" min-width="200" />
        <el-table-column v-if="viewMode === 'partition'" prop="partitionName" label="分区路径" min-width="150" />
        <el-table-column prop="objectCount" label="对象数" width="120">
          <template #default="{ row }"><span class="key-number">{{ row.objectCount?.toLocaleString() }}</span></template>
        </el-table-column>
        <el-table-column label="存储量" width="120">
          <template #default="{ row }"><span class="key-number">{{ formatBytes(row.storageBytes) }}</span></template>
        </el-table-column>
        <el-table-column label="平均文件大小" width="130">
          <template #default="{ row }"><span class="key-number">{{ formatBytes(row.avgFileSizeBytes) }}</span></template>
        </el-table-column>
        <el-table-column label="小文件健康度" width="150">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.avgFileSizeBytes)" effect="light">
              {{ getStatusLabel(row.avgFileSizeBytes) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastModifiedTime" label="最后修改" width="180" />
        <el-table-column prop="owner" label="负责人" width="100" />
      </el-table>

      <el-pagination
        class="pagination"
        v-model:current-page="query.page"
        v-model:page-size="query.size"
        :total="total"
        layout="total, prev, pager, next, jumper"
        @current-change="loadData"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getTableInsightPage, getTablePartitionInsightPage, getBuckets } from '../../api/modules'
import { ElMessage } from 'element-plus'

const viewMode = ref('table') // 'table' or 'partition'
const configDialogVisible = ref(false)
const config = ref({
  extremelySevere: 10,
  severe: 64,
  slight: 128
})

const buckets = ref([])
const records = ref([])
const total = ref(0)
const query = ref({ bucketId: null, prefix: '', page: 1, size: 20 })

const formatBytes = (bytes) => {
  if (!bytes) return '-'
  const units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB']
  let i = 0; let val = bytes
  while (val >= 1024 && i < units.length - 1) { val /= 1024; i++ }
  return val.toFixed(2) + ' ' + units[i]
}

const loadBuckets = async () => {
  try { const res = await getBuckets(); buckets.value = res.data || [] } catch { /* empty */ }
}

const getStatusLabel = (bytes) => {
  if (bytes === null || bytes === undefined) return '-'
  const mb = bytes / 1024 / 1024
  if (mb < config.value.extremelySevere) return `极严重 (<${config.value.extremelySevere}MB)`
  if (mb < config.value.severe) return `严重 (<${config.value.severe}MB)`
  if (mb < config.value.slight) return `轻微 (<${config.value.slight}MB)`
  return '健康'
}

const getStatusType = (bytes) => {
  if (bytes === null || bytes === undefined) return 'info'
  const mb = bytes / 1024 / 1024
  if (mb < config.value.extremelySevere) return 'danger'
  if (mb < config.value.severe) return 'warning'
  if (mb < config.value.slight) return 'info'
  return 'success'
}

const loadConfig = () => {
  const saved = localStorage.getItem('smallFileConfig')
  if (saved) {
    try {
      config.value = JSON.parse(saved)
    } catch { /* empty */ }
  }
}

const saveConfig = () => {
  localStorage.setItem('smallFileConfig', JSON.stringify(config.value))
  configDialogVisible.value = false
  ElMessage.success('配置已保存')
}

const loadData = async () => {
  try {
    const apiCall = viewMode.value === 'partition' ? getTablePartitionInsightPage : getTableInsightPage
    const res = await apiCall(query.value)
    const pageData = res.data || {}
    records.value = pageData.records || []
    total.value = pageData.total || 0
  } catch { /* empty */ }
}

const onViewModeChange = () => {
  query.value.page = 1
  loadData()
}

onMounted(() => { loadConfig(); loadBuckets(); loadData() })
</script>

<style scoped>
.table-header { display: flex; justify-content: space-between; align-items: center; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
