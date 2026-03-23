<template>
  <div class="junk-data">
    <el-card shadow="hover">
      <template #header>
        <div class="table-header">
          <span>无用数据识别</span>
          <div>
            <el-select v-model="query.bucketId" placeholder="选择 Bucket" clearable style="width: 200px; margin-right: 10px" @change="loadData">
              <el-option v-for="b in buckets" :key="b.id" :label="b.bucketName" :value="b.id" />
            </el-select>
            <el-select v-model="query.reason" placeholder="原因" clearable style="width: 140px; margin-right: 10px" @change="loadData">
              <el-option label="已过期" value="EXPIRED" />
              <el-option label="无访问" value="NO_ACCESS" />
              <el-option label="临时文件" value="TEMP_FILE" />
              <el-option label="重复" value="DUPLICATE" />
            </el-select>
            <el-select v-model="query.status" placeholder="状态" clearable style="width: 140px" @change="loadData">
              <el-option label="待处理" value="PENDING" />
              <el-option label="已删除" value="DELETED" />
              <el-option label="已保留" value="RETAINED" />
            </el-select>
          </div>
        </div>
      </template>

      <el-table :data="records" stripe>
        <el-table-column prop="objectKey" label="对象 Key" min-width="300" show-overflow-tooltip />
        <el-table-column label="大小" width="120">
          <template #default="{ row }">{{ formatBytes(row.sizeBytes) }}</template>
        </el-table-column>
        <el-table-column prop="reason" label="原因" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ reasonLabel(row.reason) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastAccessTime" label="最后访问" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <template v-if="row.status === 'PENDING'">
              <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
              <el-button type="info" size="small" @click="handleRetain(row.id)">保留</el-button>
            </template>
            <span v-else>—</span>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pagination"
        v-model:current-page="query.page"
        v-model:page-size="query.size"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="loadData"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getJunkDataPage, markJunkDeleted, markJunkRetained, getBuckets } from '../../api/modules'

const buckets = ref([])
const records = ref([])
const total = ref(0)
const query = ref({ bucketId: null, status: '', reason: '', page: 1, size: 20 })

const formatBytes = (bytes) => {
  if (!bytes) return '-'
  const units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB']
  let i = 0; let val = bytes
  while (val >= 1024 && i < units.length - 1) { val /= 1024; i++ }
  return val.toFixed(2) + ' ' + units[i]
}

const reasonLabel = (r) => ({ EXPIRED: '已过期', NO_ACCESS: '无访问', TEMP_FILE: '临时文件', DUPLICATE: '重复' }[r] || r)
const statusType = (s) => ({ PENDING: 'warning', DELETED: 'danger', RETAINED: 'success' }[s] || 'info')
const statusLabel = (s) => ({ PENDING: '待处理', DELETED: '已删除', RETAINED: '已保留' }[s] || s)

const loadBuckets = async () => {
  try { const res = await getBuckets(); buckets.value = res.data || [] } catch { /* empty */ }
}

const loadData = async () => {
  try {
    const res = await getJunkDataPage(query.value)
    const pageData = res.data || {}
    records.value = pageData.records || []
    total.value = pageData.total || 0
  } catch { /* empty */ }
}

const handleDelete = async (id) => {
  try {
    await markJunkDeleted(id)
    ElMessage.success('已标记删除')
    loadData()
  } catch { ElMessage.error('操作失败') }
}

const handleRetain = async (id) => {
  try {
    await markJunkRetained(id)
    ElMessage.success('已保留')
    loadData()
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => { loadBuckets(); loadData() })
</script>

<style scoped>
.table-header { display: flex; justify-content: space-between; align-items: center; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
