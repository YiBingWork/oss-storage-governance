<template>
  <div class="audit-log-analysis">
    <el-card shadow="hover">
      <template #header>
        <div class="header-container">
          <span>日志分析</span>
        </div>
      </template>
      <div class="analysis-content">
        <el-empty description="日志分析模块正在建设中，敬请期待..." />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'

onMounted(() => {
  // 占位功能，后续可以结合 getAuditLogSummary 或其他 API 进行日志数据的图表分析
  console.log('Audit Log Analysis view mounted.')
})
</script>

<style scoped>
.audit-log-analysis {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.analysis-content {
  min-height: 400px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
