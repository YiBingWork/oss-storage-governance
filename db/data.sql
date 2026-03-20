-- ============================================================
-- 示例数据 — 用于演示和开发环境
-- ============================================================

USE oss_governance;

-- Bucket
INSERT INTO bucket_info (bucket_name, region, storage_class, storage_bytes, object_count, owner_team) VALUES
('data-warehouse-prod', 'oss-cn-hangzhou', 'Standard', 5368709120000, 12500000, '数据平台组'),
('data-warehouse-staging', 'oss-cn-hangzhou', 'Standard', 1073741824000, 3200000, '数据平台组'),
('user-logs-prod', 'oss-cn-shanghai', 'IA', 2147483648000, 45000000, '日志中心'),
('ml-training-data', 'oss-cn-beijing', 'Standard', 10737418240000, 800000, 'AI 团队'),
('backup-archive', 'oss-cn-hangzhou', 'Archive', 21474836480000, 2000000, '运维组');

-- 成本记录
INSERT INTO cost_record (bucket_id, month, storage_cost, request_cost, traffic_cost, total_cost) VALUES
(1, '2025-01', 1200.00, 350.00, 180.00, 1730.00),
(1, '2025-02', 1250.00, 320.00, 200.00, 1770.00),
(1, '2025-03', 1300.00, 380.00, 210.00, 1890.00),
(2, '2025-01', 240.00, 80.00, 40.00, 360.00),
(2, '2025-02', 250.00, 75.00, 45.00, 370.00),
(2, '2025-03', 260.00, 90.00, 50.00, 400.00),
(3, '2025-01', 450.00, 600.00, 120.00, 1170.00),
(3, '2025-02', 470.00, 580.00, 130.00, 1180.00),
(3, '2025-03', 500.00, 620.00, 140.00, 1260.00),
(4, '2025-01', 2400.00, 150.00, 300.00, 2850.00),
(4, '2025-02', 2500.00, 160.00, 280.00, 2940.00),
(4, '2025-03', 2600.00, 170.00, 320.00, 3090.00),
(5, '2025-01', 800.00, 20.00, 10.00, 830.00),
(5, '2025-02', 820.00, 18.00, 12.00, 850.00),
(5, '2025-03', 850.00, 22.00, 15.00, 887.00);

-- 表级分析
INSERT INTO table_insight (bucket_id, prefix, object_count, storage_bytes, avg_file_size_bytes, last_modified_time, owner) VALUES
(1, 'dw/ods/user_action/', 3500000, 1500000000000, 428571, '2025-03-15 10:00:00', '张三'),
(1, 'dw/dwd/order_detail/', 2000000, 800000000000, 400000, '2025-03-14 08:00:00', '李四'),
(1, 'dw/ads/report_daily/', 500000, 200000000000, 400000, '2025-03-15 12:00:00', '王五'),
(2, 'staging/temp_etl/', 1200000, 400000000000, 333333, '2025-03-10 06:00:00', '张三'),
(3, 'logs/app/2025/', 20000000, 1000000000000, 50000, '2025-03-15 23:59:00', '运维'),
(4, 'training/image_set_v3/', 300000, 5000000000000, 16666666, '2025-02-20 14:00:00', '赵六');

-- 小文件记录
INSERT INTO small_file_record (bucket_id, prefix, small_file_count, small_file_total_bytes, threshold_bytes, ratio, status) VALUES
(1, 'dw/ods/user_action/', 2800000, 280000000000, 1048576, 80.00, 'PENDING'),
(3, 'logs/app/2025/', 18000000, 180000000000, 1048576, 90.00, 'PENDING'),
(2, 'staging/temp_etl/', 900000, 90000000000, 1048576, 75.00, 'MERGING');

-- 生命周期建议
INSERT INTO lifecycle_advice (bucket_id, prefix, current_storage_class, suggested_storage_class, days_since_last_access, estimated_monthly_saving, status) VALUES
(1, 'dw/ods/user_action/', 'Standard', 'IA', 90, 450.00, 'PENDING'),
(2, 'staging/temp_etl/', 'Standard', 'Archive', 180, 200.00, 'PENDING'),
(4, 'training/image_set_v3/', 'Standard', 'IA', 60, 800.00, 'PENDING'),
(5, 'backup-archive/', 'Archive', 'DeepArchive', 365, 300.00, 'PENDING');

-- 无用数据记录
INSERT INTO junk_data_record (bucket_id, object_key, size_bytes, reason, last_access_time, status) VALUES
(2, 'staging/temp_etl/_temporary/task_202501/', 50000000000, 'TEMP_FILE', '2025-01-15 00:00:00', 'PENDING'),
(2, 'staging/temp_etl/_temporary/task_202502/', 40000000000, 'TEMP_FILE', '2025-02-10 00:00:00', 'PENDING'),
(1, 'dw/ods/user_action/dt=2023-01-01/', 120000000000, 'EXPIRED', '2023-06-01 00:00:00', 'PENDING'),
(3, 'logs/app/2023/', 500000000000, 'NO_ACCESS', '2023-12-31 00:00:00', 'PENDING'),
(1, 'dw/tmp/debug_output.csv', 2000000, 'TEMP_FILE', '2025-01-20 00:00:00', 'PENDING');
