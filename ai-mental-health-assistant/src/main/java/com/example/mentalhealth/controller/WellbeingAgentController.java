package com.example.mentalhealth.controller;

import com.example.mentalhealth.agent.WellbeingAgentService;
import com.example.mentalhealth.common.Result;
import com.example.mentalhealth.dto.AgentCheckInRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat/agent")
public class WellbeingAgentController {

    private final WellbeingAgentService agentService;

    public WellbeingAgentController(WellbeingAgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/check-in")
    public Result<String> checkIn(@Valid @RequestBody(required = false) AgentCheckInRequest request) {
        String topic = request == null ? null : request.topic();
        return Result.success(agentService.invokeCheckInPrompt(topic));
    }
}
