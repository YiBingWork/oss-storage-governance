<template>
  <div class="lifecycle">
    <el-card shadow="hover">
      <template #header>
        <div class="table-header">
          <span>生命周期建议</span>
          <div>
            <el-select v-model="query.bucketId" placeholder="选择 Bucket" clearable style="width: 200px; margin-right: 10px" @change="loadData">
              <el-option v-for="b in buckets" :key="b.id" :label="b.bucketName" :value="b.id" />
            </el-select>
            <el-select v-model="query.status" placeholder="状态" clearable style="width: 140px" @change="loadData">
              <el-option label="待处理" value="PENDING" />
              <el-option label="已应用" value="APPLIED" />
              <el-option label="已忽略" value="IGNORED" />
            </el-select>
          </div>
        </div>
      </template>

      <el-table :data="records" stripe>
        <el-table-column prop="prefix" label="前缀" min-width="250" />
        <el-table-column prop="currentStorageClass" label="当前存储类型" width="130" />
        <el-table-column prop="suggestedStorageClass" label="建议存储类型" width="130" />
        <el-table-column label="未访问天数" width="120">
          <template #default="{ row }"><span class="key-number">{{ row.daysSinceLastAccess }}</span></template>
        </el-table-column>
        <el-table-column label="预估月省 (元)" width="130">
          <template #default="{ row }"><span class="key-number" style="color: #67C23A">+{{ row.estimatedMonthlySaving }}</span></template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <template v-if="row.status === 'PENDING'">
              <el-button type="primary" size="small" @click="handleApply(row.id)">应用</el-button>
              <el-button type="info" size="small" @click="handleIgnore(row.id)">忽略</el-button>
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
        layout="total, prev, pager, next, jumper"
        @current-change="loadData"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getLifecyclePage, applyLifecycleAdvice, ignoreLifecycleAdvice, getBuckets } from '../../api/modules'

const buckets = ref([])
const records = ref([])
const total = ref(0)
const query = ref({ bucketId: null, status: '', page: 1, size: 20 })

const statusType = (s) => ({ PENDING: 'warning', APPLIED: 'success', IGNORED: 'info' }[s] || 'info')
const statusLabel = (s) => ({ PENDING: '待处理', APPLIED: '已应用', IGNORED: '已忽略' }[s] || s)

const loadBuckets = async () => {
  try { const res = await getBuckets(); buckets.value = res.data || [] } catch { /* empty */ }
}

const loadData = async () => {
  try {
    const res = await getLifecyclePage(query.value)
    const pageData = res.data || {}
    records.value = pageData.records || []
    total.value = pageData.total || 0
  } catch { /* empty */ }
}

const handleApply = async (id) => {
  try {
    await applyLifecycleAdvice(id)
    ElMessage.success('已应用')
    loadData()
  } catch { ElMessage.error('操作失败') }
}

const handleIgnore = async (id) => {
  try {
    await ignoreLifecycleAdvice(id)
    ElMessage.success('已忽略')
    loadData()
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => { loadBuckets(); loadData() })
</script>

<style scoped>
.table-header { display: flex; justify-content: space-between; align-items: center; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
