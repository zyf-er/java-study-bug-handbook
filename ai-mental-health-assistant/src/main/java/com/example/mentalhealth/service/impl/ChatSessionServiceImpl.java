package com.example.mentalhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mentalhealth.entity.ChatMessage;
import com.example.mentalhealth.entity.ChatSession;
import com.example.mentalhealth.exception.ResourceNotFoundException;
import com.example.mentalhealth.mapper.ChatMessageMapper;
import com.example.mentalhealth.mapper.ChatSessionMapper;
import com.example.mentalhealth.service.ChatSessionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatSessionServiceImpl implements ChatSessionService {

    private final ChatSessionMapper sessionMapper;
    private final ChatMessageMapper messageMapper;

    public ChatSessionServiceImpl(ChatSessionMapper sessionMapper, ChatMessageMapper messageMapper) {
        this.sessionMapper = sessionMapper;
        this.messageMapper = messageMapper;
    }

    @Override
    @Transactional
    public ChatSession create(String title) {
        LocalDateTime now = LocalDateTime.now();
        ChatSession session = new ChatSession();
        session.setTitle(title == null || title.isBlank() ? "新的对话" : title.trim());
        session.setCreatedAt(now);
        session.setUpdatedAt(now);
        sessionMapper.insert(session);
        return session;
    }

    @Override
    public List<ChatSession> list() {
        return sessionMapper.selectList(new QueryWrapper<ChatSession>().orderByDesc("updated_at"));
    }

    @Override
    public ChatSession getRequired(Long id) {
        ChatSession session = sessionMapper.selectById(id);
        if (session == null) {
            throw new ResourceNotFoundException("会话不存在");
        }
        return session;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getRequired(id);
        messageMapper.delete(new QueryWrapper<ChatMessage>().eq("session_id", id));
        sessionMapper.deleteById(id);
    }
}
