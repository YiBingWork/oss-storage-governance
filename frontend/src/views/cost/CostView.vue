<template>
  <div class="cost-center">
    <!-- 费用趋势图 -->
    <el-card class="chart-card" shadow="hover">
      <template #header>
        <span>月度费用趋势</span>
      </template>
      <div ref="costChartRef" class="chart-container"></div>
    </el-card>

    <!-- 费用明细表格 -->
    <el-card shadow="hover">
      <template #header>
        <div class="table-header">
          <span>费用明细</span>
          <el-select v-model="query.bucketId" placeholder="选择 Bucket" clearable style="width: 200px" @change="loadData">
            <el-option v-for="b in buckets" :key="b.id" :label="b.bucketName" :value="b.id" />
          </el-select>
        </div>
      </template>
      <el-table :data="records" stripe>
        <el-table-column prop="month" label="月份" width="120" />
        <el-table-column label="存储费用 (元)">
          <template #default="{ row }">
            <span class="key-number">{{ row.storageCost }}</span>
          </template>
        </el-table-column>
        <el-table-column label="请求费用 (元)">
          <template #default="{ row }">
            <span class="key-number">{{ row.requestCost }}</span>
          </template>
        </el-table-column>
        <el-table-column label="流量费用 (元)">
          <template #default="{ row }">
            <span class="key-number">{{ row.trafficCost }}</span>
          </template>
        </el-table-column>
        <el-table-column label="总费用 (元)">
          <template #default="{ row }">
            <span class="key-number" style="color: #F56C6C">{{ row.totalCost }}</span>
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
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getCostPage, getCostTrend, getBuckets } from '../../api/modules'

const buckets = ref([])
const records = ref([])
const total = ref(0)
const costChartRef = ref(null)
const query = ref({ bucketId: null, page: 1, size: 20 })

const loadBuckets = async () => {
  try {
    const res = await getBuckets()
    buckets.value = res.data || []
  } catch { /* empty */ }
}

const loadData = async () => {
  try {
    const res = await getCostPage(query.value)
    const pageData = res.data || {}
    records.value = pageData.records || []
    total.value = pageData.total || 0
  } catch { /* empty */ }
}

const loadChart = async () => {
  try {
    const res = await getCostTrend(12)
    const trendData = res.data || []
    await nextTick()
    if (costChartRef.value) {
      const chart = echarts.init(costChartRef.value)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: trendData.map((d) => d.month) },
        yAxis: { type: 'value', name: '费用 (元)' },
        series: [{
          name: '总费用',
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
          data: trendData.map((d) => d.total)
        }]
      })
      window.addEventListener('resize', () => chart.resize())
    }
  } catch { /* empty */ }
}

onMounted(() => {
  loadBuckets()
  loadData()
  loadChart()
})
</script>

<style scoped>
.chart-card { margin-bottom: 20px; }
.chart-container { height: 350px; }
.table-header { display: flex; justify-content: space-between; align-items: center; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
