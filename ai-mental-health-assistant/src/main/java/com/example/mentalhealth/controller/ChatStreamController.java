package com.example.mentalhealth.controller;

import com.example.mentalhealth.dto.SendMessageRequest;
import com.example.mentalhealth.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chat/sessions/{id}")
public class ChatStreamController {

    private final ChatService chatService;

    public ChatStreamController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> stream(
            @PathVariable Long id, @Valid @RequestBody SendMessageRequest request) {
        return chatService.streamMessage(id, request.content())
                .map(content -> ServerSentEvent.builder(content).event("message").build())
                .onErrorResume(exception -> Flux.just(
                        ServerSentEvent.builder("当前暂时无法回复，请稍后再试。")
                                .event("error").build()))
                .concatWithValues(ServerSentEvent.builder("[DONE]").event("done").build());
    }
}
