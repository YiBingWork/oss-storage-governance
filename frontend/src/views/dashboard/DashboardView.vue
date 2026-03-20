<template>
  <div class="dashboard">
    <!-- 概览卡片 -->
    <el-row :gutter="20" class="overview-cards">
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="card-content">
            <div class="card-label">Bucket 数量</div>
            <div class="card-value">{{ overview.bucketCount ?? '-' }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="card-content">
            <div class="card-label">总存储量</div>
            <div class="card-value">{{ formatBytes(overview.totalStorageBytes) }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="card-content">
            <div class="card-label">总对象数</div>
            <div class="card-value">{{ formatNumber(overview.totalObjectCount) }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="card-content">
            <div class="card-label">本月费用</div>
            <div class="card-value">¥{{ overview.monthCostYuan ?? '-' }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="card-content">
            <div class="card-label">小文件占比</div>
            <div class="card-value">{{ overview.smallFileRatio ?? '-' }}%</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="card-content">
            <div class="card-label">无用数据占比</div>
            <div class="card-value">{{ overview.junkDataRatio ?? '-' }}%</div>
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
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getDashboardOverview, getDashboardTrend } from '../../api/modules'

const overview = ref({})
const trendChartRef = ref(null)

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
          areaStyle: { opacity: 0.3 },
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
}
.card-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}
.card-value {
  font-size: 22px;
  font-weight: 700;
  color: #303133;
}
.chart-card {
  margin-bottom: 20px;
}
.chart-container {
  height: 400px;
}
</style>
