package com.example.mentalhealth.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
@ConditionalOnProperty(prefix = "assistant.ai", name = "mode", havingValue = "openai")
public class OpenAiChatService implements AiChatService {

    private static final String SYSTEM_PROMPT = """
            你是一个温和、尊重边界的心理健康陪伴助手。
            你可以倾听、共情并提出开放式问题，但不得诊断疾病、推荐治疗方案或声称代替专业人士。
            如果用户表达自伤或即时危险，请鼓励其联系当地急救服务、可信任的人或专业支持。
            """;

    private final ChatClient chatClient;

    public OpenAiChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String generate(List<ChatTurn> history) {
        return chatClient.prompt().messages(toMessages(history)).call().content();
    }

    @Override
    public Flux<String> stream(List<ChatTurn> history) {
        return chatClient.prompt().messages(toMessages(history)).stream().content();
    }

    private List<Message> toMessages(List<ChatTurn> history) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(SYSTEM_PROMPT));
        for (ChatTurn turn : history) {
            if ("assistant".equals(turn.role())) {
                messages.add(new AssistantMessage(turn.content()));
            } else {
                messages.add(new UserMessage(turn.content()));
            }
        }
        return messages;
    }
}
