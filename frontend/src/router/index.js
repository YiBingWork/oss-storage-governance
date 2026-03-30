import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('../layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/dashboard/DashboardView.vue'),
        meta: { title: '全局看板' }
      },
      {
        path: 'cost',
        name: 'CostCenter',
        component: () => import('../views/cost/CostView.vue'),
        meta: { title: '成本中心' }
      },
      {
        path: 'table-insight',
        name: 'TableInsight',
        component: () => import('../views/table-insight/TableInsightView.vue'),
        meta: { title: '表级分析' }
      },
      {
        path: 'small-file',
        name: 'SmallFile',
        component: () => import('../views/small-file/SmallFileView.vue'),
        meta: { title: '小文件治理' }
      },
      {
        path: 'lifecycle',
        name: 'Lifecycle',
        component: () => import('../views/lifecycle/LifecycleView.vue'),
        meta: { title: '生命周期建议' }
      },
      {
        path: 'junk-data',
        name: 'JunkData',
        component: () => import('../views/junk-data/JunkDataView.vue'),
        meta: { title: '无用数据识别' }
      },
      {
        path: 'audit-log',
        name: 'AuditLog',
        component: () => import('../views/audit-log/AuditLogView.vue'),
        meta: { title: '日志查询' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
