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
  background-color: #f5f7fa;
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
</style>
