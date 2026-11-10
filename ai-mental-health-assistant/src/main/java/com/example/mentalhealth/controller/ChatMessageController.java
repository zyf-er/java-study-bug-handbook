package com.example.mentalhealth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.mentalhealth.common.PageResult;
import com.example.mentalhealth.common.Result;
import com.example.mentalhealth.dto.SendMessageRequest;
import com.example.mentalhealth.entity.ChatMessage;
import com.example.mentalhealth.service.ChatService;
import com.example.mentalhealth.vo.ChatMessageVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Validated
@RestController
@RequestMapping("/api/chat/sessions/{id}/messages")
public class ChatMessageController {

    private final ChatService chatService;

    public ChatMessageController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result<ChatMessageVO> send(
            @PathVariable Long id, @Valid @RequestBody SendMessageRequest request) {
        return Result.success(toVO(chatService.sendMessage(id, request.content())));
    }

    @GetMapping
    public Result<PageResult<ChatMessageVO>> history(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") @Min(1) long page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) long size) {
        IPage<ChatMessage> result = chatService.getMessages(id, page, size);
        PageResult<ChatMessageVO> data = new PageResult<>(result.getCurrent(), result.getSize(),
                result.getTotal(), result.getRecords().stream().map(this::toVO).toList());
        return Result.success(data);
    }

    private ChatMessageVO toVO(ChatMessage message) {
        return new ChatMessageVO(message.getId(), message.getSessionId(), message.getRole(),
                message.getContent(), message.getCreatedAt());
    }
}
