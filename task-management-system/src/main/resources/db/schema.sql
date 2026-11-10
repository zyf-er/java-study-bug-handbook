CREATE DATABASE IF NOT EXISTS task_management
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE task_management;

CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    title VARCHAR(200) NOT NULL COMMENT '任务标题',
    description VARCHAR(2000) NULL COMMENT '任务描述',
    status VARCHAR(20) NOT NULL DEFAULT 'TODO' COMMENT '任务状态',
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM' COMMENT '优先级',
    due_date DATE NULL COMMENT '截止日期',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_tasks_status_priority (status, priority),
    KEY idx_tasks_due_date (due_date),
    KEY idx_tasks_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务表';

INSERT INTO tasks (title, description, status, priority, due_date) VALUES
    ('整理开发环境', '安装并检查项目所需的开发工具', 'DONE', 'LOW', '2026-11-10'),
    ('学习 Spring Boot', '了解控制器、服务和依赖注入', 'IN_PROGRESS', 'HIGH', '2026-11-15'),
    ('实现任务分页查询', '支持标题、状态和优先级筛选', 'TODO', 'MEDIUM', '2026-11-18'),
    ('编写接口测试', '覆盖任务创建、查询和状态修改', 'TODO', 'MEDIUM', '2026-11-20'),
    ('完善项目文档', '说明初始化数据库和启动服务的步骤', 'TODO', 'LOW', '2026-11-22');

