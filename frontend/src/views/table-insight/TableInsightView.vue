<template>
  <div class="table-insight">
    <el-card shadow="hover">
      <template #header>
        <div class="table-header">
          <span>表级 / 前缀级分析</span>
          <div>
            <el-select v-model="query.bucketId" placeholder="选择 Bucket" clearable style="width: 200px; margin-right: 10px" @change="loadData">
              <el-option v-for="b in buckets" :key="b.id" :label="b.bucketName" :value="b.id" />
            </el-select>
            <el-input v-model="query.prefix" placeholder="前缀搜索" clearable style="width: 200px" @change="loadData" />
          </div>
        </div>
      </template>

      <el-table :data="records" stripe>
        <el-table-column prop="prefix" label="前缀 / 表路径" min-width="250" />
        <el-table-column prop="objectCount" label="对象数" width="120">
          <template #default="{ row }">{{ row.objectCount?.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column label="存储量" width="120">
          <template #default="{ row }">{{ formatBytes(row.storageBytes) }}</template>
        </el-table-column>
        <el-table-column label="平均文件大小" width="130">
          <template #default="{ row }">{{ formatBytes(row.avgFileSizeBytes) }}</template>
        </el-table-column>
        <el-table-column prop="lastModifiedTime" label="最后修改" width="180" />
        <el-table-column prop="owner" label="负责人" width="100" />
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
import { getTableInsightPage, getBuckets } from '../../api/modules'

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

const loadData = async () => {
  try {
    const res = await getTableInsightPage(query.value)
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
