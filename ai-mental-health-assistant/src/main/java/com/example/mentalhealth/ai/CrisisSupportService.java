package com.example.mentalhealth.ai;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class CrisisSupportService {

    private static final List<String> CRISIS_PHRASES = List.of(
            "自杀", "自伤", "不想活", "结束生命", "伤害自己", "suicide", "kill myself", "self-harm"
    );
    private static final String CRISIS_REPLY =
            "听到你正经历这些，我很在意你的安全。我不能替代专业帮助；如果你此刻有立即危险，请尽快联系当地急救服务或前往急诊，并联系一位可信任的人陪着你。你也可以联系当地心理危机支持服务。";

    public boolean isCrisis(String content) {
        String normalized = content.toLowerCase(Locale.ROOT);
        return CRISIS_PHRASES.stream().anyMatch(normalized::contains);
    }

    public String response() {
        return CRISIS_REPLY;
    }
}
