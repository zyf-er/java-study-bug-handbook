INSERT INTO chat_session (title, created_at, updated_at)
SELECT '示例对话', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM chat_session WHERE title = '示例对话');

INSERT INTO chat_message (session_id, role, content, created_at)
SELECT s.id, 'assistant',
       '你好，我可以陪你聊聊近况。这里是学习用途的情绪陪伴服务，不提供医疗诊断。',
       CURRENT_TIMESTAMP
FROM chat_session s
WHERE s.title = '示例对话'
  AND NOT EXISTS (
      SELECT 1 FROM chat_message m
      WHERE m.session_id = s.id AND m.role = 'assistant'
  );
