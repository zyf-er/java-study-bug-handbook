package com.example.mentalhealth.agent;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

@Service
public class WellbeingAgentService {

    private final Map<String, Function<String, String>> safeTools = Map.of(
            "check_in_prompt", this::checkInPrompt
    );

    public String invokeCheckInPrompt(String topic) {
        return safeTools.get("check_in_prompt").apply(topic == null ? "" : topic);
    }

    private String checkInPrompt(String topic) {
        return topic.isBlank()
                ? "此刻你最想被理解的一件事是什么？"
                : "关于“" + topic + "”，此刻你最希望先得到哪一种支持？";
    }
}
