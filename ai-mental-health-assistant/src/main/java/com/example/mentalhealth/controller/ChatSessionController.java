package com.example.mentalhealth.controller;

import com.example.mentalhealth.common.Result;
import com.example.mentalhealth.dto.CreateSessionRequest;
import com.example.mentalhealth.entity.ChatMessage;
import com.example.mentalhealth.entity.ChatSession;
import com.example.mentalhealth.service.ChatService;
import com.example.mentalhealth.service.ChatSessionService;
import com.example.mentalhealth.vo.ChatMessageVO;
import com.example.mentalhealth.vo.ChatSessionDetailVO;
import com.example.mentalhealth.vo.ChatSessionVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat/sessions")
public class ChatSessionController {

    private final ChatSessionService sessionService;
    private final ChatService chatService;

    public ChatSessionController(ChatSessionService sessionService, ChatService chatService) {
        this.sessionService = sessionService;
        this.chatService = chatService;
    }

    @PostMapping
    public Result<ChatSessionVO> create(@Valid @RequestBody(required = false) CreateSessionRequest request) {
        String title = request == null ? null : request.title();
        return Result.success(toVO(sessionService.create(title)));
    }

    @GetMapping
    public Result<List<ChatSessionVO>> list() {
        return Result.success(sessionService.list().stream().map(this::toVO).toList());
    }

    @GetMapping("/{id}")
    public Result<ChatSessionDetailVO> get(@PathVariable Long id) {
        ChatSession session = sessionService.getRequired(id);
        List<ChatMessageVO> messages = chatService.getMessages(id, 1, 100).getRecords()
                .stream().map(this::toVO).toList();
        return Result.success(new ChatSessionDetailVO(toVO(session), messages));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sessionService.delete(id);
        return Result.success(null);
    }

    private ChatSessionVO toVO(ChatSession session) {
        return new ChatSessionVO(session.getId(), session.getTitle(),
                session.getCreatedAt(), session.getUpdatedAt());
    }

    private ChatMessageVO toVO(ChatMessage message) {
        return new ChatMessageVO(message.getId(), message.getSessionId(), message.getRole(),
                message.getContent(), message.getCreatedAt());
    }
}
