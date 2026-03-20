# OSS Storage Governance Platform

阿里云 OSS 存储治理 Web 化平台 — 大数据资产治理平台

## 技术栈

- **后端：** Java 17 + Spring Boot 3.2 + MyBatis-Plus 3.5
- **前端：** Vue 3 + Element Plus + ECharts 5
- **数据库：** MySQL 8.0
- **部署：** Docker Compose

## 功能模块

1. **全局看板 (Global Dashboard)** — Bucket 概览、存储量、对象数、月度费用、小文件/无用数据占比、费用趋势图
2. **成本中心 (Cost Center)** — 按月查看各 Bucket 存储/请求/流量费用明细及趋势
3. **表级分析 (Table-Level Insight)** — 按 OSS 前缀（表路径）查看对象数、存储量、平均文件大小
4. **小文件治理 (Small File Center)** — 识别小文件集中的前缀，展示数量、占比及治理状态
5. **生命周期建议 (Lifecycle Advisor)** — 基于访问频率建议存储类型转换，预估节省费用
6. **无用数据识别 (Junk Data Detection)** — 识别过期、无访问、临时、重复数据，支持删除/保留操作

## 项目结构

```
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/com/yibing/oss/
│   │   ├── config/             # 配置（CORS、MyBatis-Plus、自动填充）
│   │   ├── controller/         # REST API 控制器
│   │   ├── entity/             # 数据库实体
│   │   ├── mapper/             # MyBatis-Plus Mapper
│   │   ├── service/            # 业务逻辑接口
│   │   ├── service/impl/       # 业务逻辑实现
│   │   └── vo/                 # 视图对象 / 响应包装
│   └── Dockerfile
├── frontend/                   # Vue 3 前端
│   ├── src/
│   │   ├── api/                # Axios API 封装
│   │   ├── layouts/            # 布局组件（侧边栏导航）
│   │   ├── router/             # Vue Router 路由
│   │   └── views/              # 6 大模块页面
│   ├── nginx.conf
│   └── Dockerfile
├── db/
│   ├── schema.sql              # 数据库建表脚本
│   └── data.sql                # 示例数据
└── docker-compose.yml          # 一键部署
```

## 快速启动

### Docker Compose（推荐）

```bash
docker-compose up -d
```

启动后访问：
- 前端页面：http://localhost
- 后端 API：http://localhost:8080/api

### 本地开发

#### 后端

```bash
cd backend
mvn clean package -DskipTests
java -jar target/oss-storage-governance-1.0.0-SNAPSHOT.jar
```

#### 前端

```bash
cd frontend
npm install
npm run dev
```

开发模式下前端运行在 http://localhost:5173，API 请求会代理到 http://localhost:8080。

## API 概览

| 模块 | 端点 | 方法 | 说明 |
|------|------|------|------|
| 看板 | `/api/dashboard/overview` | GET | 全局概览数据 |
| 看板 | `/api/dashboard/trend` | GET | 费用趋势 |
| Bucket | `/api/buckets` | GET | Bucket 列表 |
| 成本 | `/api/cost/page` | GET | 费用分页查询 |
| 成本 | `/api/cost/trend` | GET | 月度费用趋势 |
| 表级分析 | `/api/table-insight/page` | GET | 前缀级分页查询 |
| 小文件 | `/api/small-file/page` | GET | 小文件分页查询 |
| 生命周期 | `/api/lifecycle/page` | GET | 建议分页查询 |
| 生命周期 | `/api/lifecycle/{id}/apply` | PUT | 应用建议 |
| 生命周期 | `/api/lifecycle/{id}/ignore` | PUT | 忽略建议 |
| 无用数据 | `/api/junk-data/page` | GET | 无用数据分页 |
| 无用数据 | `/api/junk-data/{id}/delete` | PUT | 标记删除 |
| 无用数据 | `/api/junk-data/{id}/retain` | PUT | 标记保留 |