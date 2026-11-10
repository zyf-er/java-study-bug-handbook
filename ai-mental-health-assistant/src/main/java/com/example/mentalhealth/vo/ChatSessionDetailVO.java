package com.example.mentalhealth.vo;

import java.util.List;

public record ChatSessionDetailVO(ChatSessionVO session, List<ChatMessageVO> messages) {
}
