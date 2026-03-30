<template>
  <router-view />
</template>

<script setup>
</script>

<style>
body {
  margin: 0;
  padding: 0;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
    'Microsoft YaHei', Arial, sans-serif;
  background-color: #f8fafc;
}

.key-number {
  font-family: 'Inter', 'Roboto', sans-serif;
  font-weight: 600;
}

/* Global Card Styles */
.el-card {
  border-radius: 12px !important;
  border: 1px solid #e2e8f0 !important;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03) !important;
  transition: all 0.3s ease !important;
}

.el-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.05), 0 4px 6px -2px rgba(0, 0, 0, 0.03) !important;
}

/* Global Table Styles */
.el-table {
  --el-table-header-bg-color: #f0f9eb;
  --el-table-header-text-color: #303133;
  --el-table-row-hover-bg-color: #ecf5ff;
}

.el-table th.el-table__cell {
  background-color: var(--el-table-header-bg-color) !important;
  color: var(--el-table-header-text-color) !important;
  font-weight: 600;
}

.el-table .el-table__row--striped td.el-table__cell {
  background-color: #fafafa !important;
}

.el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell {
  background-color: #fafafa !important;
}

.el-table__row {
  transition: background-color 0.3s ease;
}

.el-table__row:hover > td.el-table__cell {
  background-color: var(--el-table-row-hover-bg-color) !important;
}
</style>
