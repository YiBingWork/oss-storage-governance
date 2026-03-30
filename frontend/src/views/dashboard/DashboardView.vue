<template>
  <div class="dashboard">
    <!-- 概览卡片 -->
    <el-row :gutter="20" class="overview-cards">
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="card-content">
            <div class="card-label">Bucket 数量</div>
            <div class="card-value key-number">
              <el-link type="primary" :underline="false" style="font-size: 24px; font-weight: 700; font-family: 'Inter', sans-serif;" @click="showBucketDetails">
                {{ overview.bucketCount ?? '-' }}
              </el-link>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="card-content">
            <div class="card-label">总存储量</div>
            <div class="card-value key-number color-primary">{{ formatBytes(overview.totalStorageBytes) }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="card-content">
            <div class="card-label">总对象数</div>
            <div class="card-value key-number color-primary">{{ formatNumber(overview.totalObjectCount) }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="card-content">
            <div class="card-label">本月费用</div>
            <div class="card-value key-number color-warning">¥{{ overview.monthCostYuan ?? '-' }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="card-content">
            <div class="card-label">
              小文件占比
              <el-tooltip content="小于 1MB 的文件占比。高占比会影响存储性能和成本" placement="top">
                <el-icon><QuestionFilled /></el-icon>
              </el-tooltip>
            </div>
            <div class="card-value key-number color-danger">{{ overview.smallFileRatio ?? '-' }}%</div>
            <el-progress
              :percentage="overview.smallFileRatio || 0"
              :color="getRatioColor(overview.smallFileRatio)"
              :show-text="false"
              class="metric-progress"
            />
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="card-content">
            <div class="card-label">
              无用数据占比
              <el-tooltip content="长期未访问或符合无用规则的数据占比" placement="top">
                <el-icon><QuestionFilled /></el-icon>
              </el-tooltip>
            </div>
            <div class="card-value key-number color-danger">{{ overview.junkDataRatio ?? '-' }}%</div>
            <el-progress
              :percentage="overview.junkDataRatio || 0"
              :color="getRatioColor(overview.junkDataRatio)"
              :show-text="false"
              class="metric-progress"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 趋势图 -->
    <el-card class="chart-card" shadow="hover">
      <template #header>
        <span>月度费用趋势</span>
      </template>
      <div ref="trendChartRef" class="chart-container"></div>
    </el-card>

    <!-- Bucket 明细弹窗 -->
    <el-dialog v-model="bucketDialogVisible" title="Bucket 列表明细" width="800px">
      <el-table :data="bucketList" v-loading="bucketLoading" height="400">
        <el-table-column prop="bucketName" label="Bucket 名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="region" label="区域" width="120" />
        <el-table-column prop="storageBytes" label="存储量" width="120">
          <template #default="scope">
            {{ formatBytes(scope.row.storageBytes) }}
          </template>
        </el-table-column>
        <el-table-column prop="objectCount" label="对象数" width="120">
          <template #default="scope">
            {{ formatNumber(scope.row.objectCount) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { QuestionFilled } from '@element-plus/icons-vue'
import { getDashboardOverview, getDashboardTrend, getBuckets } from '../../api/modules'

const overview = ref({})
const trendChartRef = ref(null)

const bucketDialogVisible = ref(false)
const bucketList = ref([])
const bucketLoading = ref(false)

const formatBytes = (bytes) => {
  if (!bytes) return '-'
  const units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB']
  let i = 0
  let val = bytes
  while (val >= 1024 && i < units.length - 1) {
    val /= 1024
    i++
  }
  return val.toFixed(2) + ' ' + units[i]
}

const formatNumber = (num) => {
  if (!num) return '-'
  return num.toLocaleString()
}

const getRatioColor = (ratio) => {
  if (!ratio) return '#909399'
  if (ratio >= 50) return '#f56c6c' // danger
  if (ratio >= 20) return '#e6a23c' // warning
  return '#67c23a' // success
}

const showBucketDetails = async () => {
  bucketDialogVisible.value = true
  bucketLoading.value = true
  try {
    const res = await getBuckets()
    bucketList.value = res.data || []
  } catch (err) {
    console.error('Failed to load buckets', err)
  } finally {
    bucketLoading.value = false
  }
}

const loadOverview = async () => {
  try {
    const res = await getDashboardOverview()
    overview.value = res.data || {}
  } catch {
    console.error('Failed to load overview')
  }
}

const loadTrend = async () => {
  try {
    const res = await getDashboardTrend(12)
    const data = res.data || {}
    await nextTick()
    if (trendChartRef.value) {
      const chart = echarts.init(trendChartRef.value)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: data.dates || [] },
        yAxis: { type: 'value', name: '费用 (元)' },
        series: [{
          name: '月度费用',
          type: 'line',
          smooth: true,
          itemStyle: { color: '#3b82f6' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#3b82f6' },
              { offset: 1, color: '#2dd4bf' }
            ]),
            opacity: 0.5
          },
          data: data.costs || []
        }]
      })
      window.addEventListener('resize', () => chart.resize())
    }
  } catch {
    console.error('Failed to load trend')
  }
}

onMounted(() => {
  loadOverview()
  loadTrend()
})
</script>

<style scoped>
.overview-cards {
  margin-bottom: 20px;
}
.card-content {
  text-align: center;
  padding: 8px 0;
}
.card-label {
  font-size: 14px;
  color: #64748b;
  margin-bottom: 16px;
  font-weight: 500;
}
.card-value {
  font-size: 28px;
  color: #1e293b;
}
.metric-progress {
  margin-top: 12px;
}
.color-primary {
  color: #3b82f6;
}
.color-warning {
  color: #E6A23C;
}
.color-danger {
  color: #F56C6C;
}
.chart-card {
  margin-bottom: 20px;
}
.chart-container {
  height: 400px;
}
</style>
