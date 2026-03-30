<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="aside">
      <div class="logo">
        <h2>OSS 治理平台</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#1e293b"
        text-color="#cbd5e1"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Monitor /></el-icon>
          <span>全局看板</span>
        </el-menu-item>
        <el-menu-item index="/cost">
          <el-icon><Coin /></el-icon>
          <span>成本中心</span>
        </el-menu-item>
        <el-menu-item index="/table-insight">
          <el-icon><Grid /></el-icon>
          <span>表级分析</span>
        </el-menu-item>
        <el-menu-item index="/small-file">
          <el-icon><Document /></el-icon>
          <span>小文件治理</span>
        </el-menu-item>
        <el-menu-item index="/lifecycle">
          <el-icon><Timer /></el-icon>
          <span>生命周期建议</span>
        </el-menu-item>
        <el-menu-item index="/junk-data">
          <el-icon><Delete /></el-icon>
          <span>无用数据识别</span>
        </el-menu-item>

        <el-sub-menu index="audit-log-module">
          <template #title>
            <el-icon><DocumentCopy /></el-icon>
            <span>审计日志</span>
          </template>
          <el-menu-item index="/audit-log">日志查询</el-menu-item>
          <el-menu-item index="/audit-log-analysis">日志分析</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <span class="page-title">{{ currentTitle }}</span>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Monitor, Coin, Grid, Document, Timer, Delete, DocumentCopy } from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || 'OSS 存储治理')
</script>

<style scoped>
.layout-container {
  height: 100vh;
}
.aside {
  background-color: #1e293b;
  overflow: hidden;
  box-shadow: 2px 0 8px 0 rgba(29, 35, 41, 0.05);
}
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}
.logo h2 {
  margin: 0;
  font-size: 16px;
  white-space: nowrap;
}
.header {
  background: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  padding: 0 20px;
}
.page-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}
.main {
  background: #f8fafc;
  padding: 24px;
}
</style>
