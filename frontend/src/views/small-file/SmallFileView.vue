<template>
  <div class="small-file">
    <el-card shadow="hover">
      <template #header>
        <div class="table-header">
          <span>小文件治理中心</span>
          <div>
            <el-select v-model="query.bucketId" placeholder="选择 Bucket" clearable style="width: 200px; margin-right: 10px" @change="loadData">
              <el-option v-for="b in buckets" :key="b.id" :label="b.bucketName" :value="b.id" />
            </el-select>
            <el-select v-model="query.status" placeholder="状态" clearable style="width: 140px" @change="loadData">
              <el-option label="待处理" value="PENDING" />
              <el-option label="合并中" value="MERGING" />
              <el-option label="已完成" value="DONE" />
            </el-select>
          </div>
        </div>
      </template>

      <el-table :data="records" stripe>
        <el-table-column prop="prefix" label="前缀" min-width="250" />
        <el-table-column prop="smallFileCount" label="小文件数" width="120">
          <template #default="{ row }"><span class="key-number">{{ row.smallFileCount?.toLocaleString() }}</span></template>
        </el-table-column>
        <el-table-column label="小文件总大小" width="130">
          <template #default="{ row }"><span class="key-number">{{ formatBytes(row.smallFileTotalBytes) }}</span></template>
        </el-table-column>
        <el-table-column label="阈值" width="100">
          <template #default="{ row }"><span class="key-number">{{ formatBytes(row.thresholdBytes) }}</span></template>
        </el-table-column>
        <el-table-column label="占比 (%)" width="100">
          <template #default="{ row }"><span class="key-number">{{ row.ratio }}</span></template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)">{{ statusLabel(row.status) }}</el-tag>
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
import { getSmallFilePage, getBuckets } from '../../api/modules'

const buckets = ref([])
const records = ref([])
const total = ref(0)
const query = ref({ bucketId: null, status: '', page: 1, size: 20 })

const formatBytes = (bytes) => {
  if (!bytes) return '-'
  const units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB']
  let i = 0; let val = bytes
  while (val >= 1024 && i < units.length - 1) { val /= 1024; i++ }
  return val.toFixed(2) + ' ' + units[i]
}

const statusType = (s) => ({ PENDING: 'warning', MERGING: '', DONE: 'success' }[s] || 'info')
const statusLabel = (s) => ({ PENDING: '待处理', MERGING: '合并中', DONE: '已完成' }[s] || s)

const loadBuckets = async () => {
  try { const res = await getBuckets(); buckets.value = res.data || [] } catch { /* empty */ }
}

const loadData = async () => {
  try {
    const res = await getSmallFilePage(query.value)
    const pageData = res.data || {}
    records.value = pageData.records || []
    total.value = pageData.total || 0
  } catch { /* empty */ }
}

onMounted(() => { loadBuckets(); loadData() })
</script>

<style scoped>
.table-header { display: flex; justify-content: space-between; align-items: center; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
