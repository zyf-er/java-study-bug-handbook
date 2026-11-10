package com.example.mentalhealth.service;

import com.example.mentalhealth.entity.ChatSession;

import java.util.List;

public interface ChatSessionService {

    ChatSession create(String title);

    List<ChatSession> list();

    ChatSession getRequired(Long id);

    void delete(Long id);
}
