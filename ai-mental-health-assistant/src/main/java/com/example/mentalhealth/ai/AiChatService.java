package com.example.mentalhealth.ai;

import reactor.core.publisher.Flux;

import java.util.List;

public interface AiChatService {

    String generate(List<ChatTurn> history);

    Flux<String> stream(List<ChatTurn> history);
}
