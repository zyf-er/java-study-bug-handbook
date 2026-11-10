package com.example.mentalhealth.ai;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@ConditionalOnProperty(prefix = "assistant.ai", name = "mode", havingValue = "mock", matchIfMissing = true)
public class MockAiChatService implements AiChatService {

    private static final String MOCK_REPLY =
            "谢谢你愿意分享。听起来这件事对你很重要。你愿意多说说此刻最希望得到怎样的支持吗？";

    @Override
    public String generate(List<ChatTurn> history) {
        return MOCK_REPLY;
    }

    @Override
    public Flux<String> stream(List<ChatTurn> history) {
        return Flux.just(
                "谢谢你愿意分享。",
                "听起来这件事对你很重要。",
                "你愿意多说说此刻最希望得到怎样的支持吗？"
        );
    }
}
