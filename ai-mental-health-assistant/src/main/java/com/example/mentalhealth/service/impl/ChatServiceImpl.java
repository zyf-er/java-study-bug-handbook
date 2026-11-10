package com.example.mentalhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mentalhealth.ai.AiChatService;
import com.example.mentalhealth.ai.ChatTurn;
import com.example.mentalhealth.ai.CrisisSupportService;
import com.example.mentalhealth.entity.ChatMessage;
import com.example.mentalhealth.entity.ChatSession;
import com.example.mentalhealth.exception.ResourceNotFoundException;
import com.example.mentalhealth.mapper.ChatMessageMapper;
import com.example.mentalhealth.mapper.ChatSessionMapper;
import com.example.mentalhealth.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ChatServiceImpl implements ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);
    private static final int CONTEXT_MESSAGE_LIMIT = 20;

    private final ChatSessionMapper sessionMapper;
    private final ChatMessageMapper messageMapper;
    private final AiChatService aiChatService;
    private final CrisisSupportService crisisSupportService;

    public ChatServiceImpl(
            ChatSessionMapper sessionMapper,
            ChatMessageMapper messageMapper,
            AiChatService aiChatService,
            CrisisSupportService crisisSupportService) {
        this.sessionMapper = sessionMapper;
        this.messageMapper = messageMapper;
        this.aiChatService = aiChatService;
        this.crisisSupportService = crisisSupportService;
    }

    @Override
    @Transactional
    public ChatMessage sendMessage(Long sessionId, String content) {
        ChatSession session = getSession(sessionId);
        ChatMessage userMessage = saveMessage(sessionId, "user", content);

        List<ChatTurn> history = loadContext(sessionId);
        String reply = crisisSupportService.isCrisis(content)
                ? crisisSupportService.response()
                : aiChatService.generate(history);

        ChatMessage assistantMessage = saveMessage(sessionId, "assistant", reply);
        touch(session);
        return assistantMessage;
    }

    @Override
    public IPage<ChatMessage> getMessages(Long sessionId, long page, long size) {
        getSession(sessionId);
        return messageMapper.selectPage(new Page<>(page, size),
                new QueryWrapper<ChatMessage>()
                        .eq("session_id", sessionId)
                        .orderByAsc("created_at", "id"));
    }

    @Override
    public Flux<String> streamMessage(Long sessionId, String content) {
        ChatSession session = getSession(sessionId);
        saveMessage(sessionId, "user", content);
        List<ChatTurn> history = loadContext(sessionId);
        if (crisisSupportService.isCrisis(content)) {
            String reply = crisisSupportService.response();
            return Flux.just(reply).doOnComplete(() -> persistAssistantReply(session, sessionId, reply));
        }
        AtomicReference<StringBuilder> reply = new AtomicReference<>(new StringBuilder());
        return aiChatService.stream(history)
                .doOnNext(reply.get()::append)
                .doOnComplete(() -> persistAssistantReply(session, sessionId, reply.get().toString()))
                .doOnError(exception -> log.warn("AI stream failed for session {}", sessionId));
    }

    private void persistAssistantReply(ChatSession session, Long sessionId, String reply) {
        saveMessage(sessionId, "assistant", reply);
        touch(session);
    }

    private ChatSession getSession(Long sessionId) {
        ChatSession session = sessionMapper.selectById(sessionId);
        if (session == null) {
            throw new ResourceNotFoundException("会话不存在");
        }
        return session;
    }

    private List<ChatTurn> loadContext(Long sessionId) {
        List<ChatMessage> recent = messageMapper.selectList(
                new QueryWrapper<ChatMessage>()
                        .eq("session_id", sessionId)
                        .orderByDesc("created_at", "id")
                        .last("LIMIT " + CONTEXT_MESSAGE_LIMIT));
        Collections.reverse(recent);
        List<ChatTurn> turns = new ArrayList<>(recent.size());
        for (ChatMessage message : recent) {
            turns.add(new ChatTurn(message.getRole(), message.getContent()));
        }
        return turns;
    }

    private ChatMessage saveMessage(Long sessionId, String role, String content) {
        ChatMessage message = new ChatMessage();
        message.setSessionId(sessionId);
        message.setRole(role);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());
        messageMapper.insert(message);
        return message;
    }

    private void touch(ChatSession session) {
        session.setUpdatedAt(LocalDateTime.now());
        sessionMapper.updateById(session);
    }
}
