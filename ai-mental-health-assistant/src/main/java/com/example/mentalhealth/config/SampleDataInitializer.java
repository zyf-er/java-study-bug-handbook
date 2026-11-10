package com.example.mentalhealth.config;

import com.example.mentalhealth.entity.ChatMessage;
import com.example.mentalhealth.entity.ChatSession;
import com.example.mentalhealth.mapper.ChatMessageMapper;
import com.example.mentalhealth.mapper.ChatSessionMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SampleDataInitializer implements ApplicationRunner {

    private final ChatSessionMapper sessionMapper;
    private final ChatMessageMapper messageMapper;

    public SampleDataInitializer(ChatSessionMapper sessionMapper, ChatMessageMapper messageMapper) {
        this.sessionMapper = sessionMapper;
        this.messageMapper = messageMapper;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (sessionMapper.selectCount(null) > 0) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        ChatSession session = new ChatSession();
        session.setTitle("欢迎体验");
        session.setCreatedAt(now);
        session.setUpdatedAt(now);
        sessionMapper.insert(session);

        ChatMessage message = new ChatMessage();
        message.setSessionId(session.getId());
        message.setRole("assistant");
        message.setContent("你好，我可以陪你聊聊近况。这里是学习用途的情绪陪伴服务，不提供医疗诊断。");
        message.setCreatedAt(now);
        messageMapper.insert(message);
    }
}
