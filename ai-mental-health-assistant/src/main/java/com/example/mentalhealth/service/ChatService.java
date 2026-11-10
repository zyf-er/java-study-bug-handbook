package com.example.mentalhealth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.mentalhealth.entity.ChatMessage;
import reactor.core.publisher.Flux;

public interface ChatService {

    ChatMessage sendMessage(Long sessionId, String content);

    IPage<ChatMessage> getMessages(Long sessionId, long page, long size);

    Flux<String> streamMessage(Long sessionId, String content);
}
