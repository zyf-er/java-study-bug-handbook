# AI 心理健康陪伴助手（纯后端）

Java 17、Spring Boot、MyBatis-Plus、Spring AI、H2 文件数据库的后端学习项目。默认使用 MOCK AI，无需 API Key。服务仅用于情绪陪伴，不提供疾病诊断或治疗建议。

## 启动

在项目目录运行：

```powershell
mvn spring-boot:run
```

应用默认监听 `http://localhost:8081`，启动时自动创建 `./data/mental-health-assistant.mv.db` 数据库、表及示例会话；数据在重启后保留。连接数据库控制台可将 `spring.h2.console.enabled=true` 和 `spring.h2.console.path=/h2-console` 加到本地配置后访问。

Swagger UI：<http://localhost:8081/swagger-ui/index.html>

## API 示例

```powershell
# 创建会话
curl.exe -X POST http://localhost:8081/api/chat/sessions `
  -H "Content-Type: application/json" -d "{\"title\":\"今天的心情\"}"

# 查询会话列表及详情
curl.exe http://localhost:8081/api/chat/sessions
curl.exe http://localhost:8081/api/chat/sessions/1

# 发送消息（会保存用户消息和 MOCK AI 回复）
curl.exe -X POST http://localhost:8081/api/chat/sessions/1/messages `
  -H "Content-Type: application/json" -d "{\"content\":\"最近有些紧张\"}"

# 分页查询消息
curl.exe "http://localhost:8081/api/chat/sessions/1/messages?page=1&size=20"

# 本地安全 Agent 工具示例
curl.exe -X POST http://localhost:8081/api/chat/agent/check-in `
  -H "Content-Type: application/json" -d "{\"topic\":\"工作压力\"}"

# SSE 流式对话
curl.exe -N -X POST http://localhost:8081/api/chat/sessions/1/stream `
  -H "Content-Type: application/json" -H "Accept: text/event-stream" `
  -d "{\"content\":\"我想聊聊最近的生活\"}"

# 删除会话
curl.exe -X DELETE http://localhost:8081/api/chat/sessions/1
```

所有业务端点统一使用 `/api/chat` 前缀。SSE 事件使用 `message`、`done`；流生成失败时返回不含聊天内容的 `error` 事件。连接取消时下游会取消订阅；只有完整结束的 AI 回复才会保存。

## 真实模型与数据库切换

默认 `ASSISTANT_AI_MODE=mock`。配置兼容 OpenAI API 的模型时设置环境变量 `ASSISTANT_AI_MODE=openai`、`AI_CHAT_MODEL=openai` 和 `AI_API_KEY`；密钥只从环境变量读取。MOCK 模式不需要任何模型凭据。

切换 MySQL 时启用 `mysql` Spring profile，并配置 `DB_URL`、`DB_USERNAME`、`DB_PASSWORD`。MySQL 驱动与对应的建表、初始化脚本已提供，文件位于 `src/main/resources/db/schema-mysql.sql` 与 `data-mysql.sql`。默认配置不要求 MySQL。

## 验证

```powershell
mvn clean package
mvn test
```
