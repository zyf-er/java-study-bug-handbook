# Task Management System

基于 Spring Boot 3 的纯后端任务管理项目，适合学习 Controller、Service、MyBatis-Plus 和 Jakarta Validation 的分层开发方式。

## 技术栈

- Java 17 语言级别、Spring Boot 3.3.5、Maven
- MySQL 8、MyBatis-Plus
- Lombok、Jakarta Validation、SpringDoc OpenAPI

## 目录结构

```text
src/main/java/com/example/taskmanagement/
├── common/       # 统一响应和状态/优先级枚举
├── config/       # 分页和 OpenAPI 配置
├── controller/   # HTTP 接口
├── dto/          # 请求参数
├── entity/       # 数据库实体
├── exception/    # 业务异常和统一异常处理
├── mapper/       # MyBatis-Plus 数据访问
├── service/      # 业务接口及实现
└── vo/           # 响应对象
src/main/resources/
├── application.yaml
└── db/schema.sql
```

## 数据库初始化

1. 确认本机 MySQL 8 已启动。
2. 使用 MySQL Workbench 或命令行执行 `src/main/resources/db/schema.sql`。脚本会创建 `task_management` 数据库、`tasks` 表和索引，并插入 5 条示例任务。
3. 本机运行凭据保存在项目根目录的 `application-local.yaml`（该文件已加入 `.gitignore`，不会提交）。可复制 `application-local.yaml.example` 后填写本机 MySQL 密码。项目基础配置已设置 localhost、3306、数据库名和 root 用户，不需要设置环境变量。

```powershell
Copy-Item application-local.yaml.example application-local.yaml
# 编辑 application-local.yaml，将 password 改为本机 MySQL 密码
```

## 启动

在项目目录运行：

```powershell
mvn clean package
mvn spring-boot:run
```

也可以运行打包后的应用：

```powershell
java -jar target/task-management-system-1.0.0.jar
```

服务默认监听 `http://localhost:8081`。数据库连接信息错误或数据库尚未初始化时，应用会在启动日志中报告连接/表缺失原因。

## Swagger UI

启动后访问 [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)，可查看并调用全部任务接口。

## API

统一响应格式为 `{ "code": 200, "message": "操作成功", "data": ... }`。

| 方法 | 路径 | 功能 |
|---|---|---|
| POST | `/api/tasks` | 新增任务 |
| GET | `/api/tasks?page=1&size=10&title=学习&status=TODO&priority=HIGH` | 分页查询和筛选 |
| GET | `/api/tasks/{id}` | 查询详情 |
| PUT | `/api/tasks/{id}` | 修改任务 |
| DELETE | `/api/tasks/{id}` | 删除任务 |
| PATCH | `/api/tasks/{id}/status` | 修改任务状态 |

`status` 可选值：`TODO`、`IN_PROGRESS`、`DONE`；`priority` 可选值：`LOW`、`MEDIUM`、`HIGH`。创建和修改请求示例：

```json
{
  "title": "学习 Spring Boot",
  "description": "实现任务管理接口",
  "status": "TODO",
  "priority": "MEDIUM",
  "dueDate": "2026-11-30"
}
```

分页 `size` 最大为 100。校验错误返回 HTTP 400，不存在的任务返回 HTTP 404。
