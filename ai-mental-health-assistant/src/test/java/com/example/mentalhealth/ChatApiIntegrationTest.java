package com.example.mentalhealth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:assistant-test;DB_CLOSE_DELAY=-1",
        "assistant.ai.mode=mock",
        "spring.ai.model.chat=none"
})
class ChatApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createsSessionSendsMessageAndReturnsHistory() throws Exception {
        MvcResult created = mockMvc.perform(post("/api/chat/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"测试会话\"}"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode session = objectMapper.readTree(created.getResponse().getContentAsString()).path("data");
        long sessionId = session.path("id").asLong();

        mockMvc.perform(post("/api/chat/sessions/{id}/messages", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"今天有些紧张\"}"))
                .andExpect(status().isCreated());

        MvcResult history = mockMvc.perform(get("/api/chat/sessions/{id}/messages", sessionId)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode records = objectMapper.readTree(history.getResponse().getContentAsString())
                .path("data").path("records");
        assertThat(records).hasSize(2);
        assertThat(records.get(0).path("role").asText()).isEqualTo("user");
        assertThat(records.get(1).path("role").asText()).isEqualTo("assistant");
    }

    @Test
    void streamEndpointReturnsServerSentEvents() throws Exception {
        MvcResult created = mockMvc.perform(post("/api/chat/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andReturn();
        long sessionId = objectMapper.readTree(created.getResponse().getContentAsString())
                .path("data").path("id").asLong();

        MvcResult pending = mockMvc.perform(post("/api/chat/sessions/{id}/stream", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_EVENT_STREAM)
                        .content("{\"content\":\"想聊聊最近的生活\"}"))
                .andExpect(request().asyncStarted())
                .andReturn();
        pending.getAsyncResult(5000);
        MvcResult stream = mockMvc.perform(asyncDispatch(pending))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(stream.getResponse().getContentType()).contains(MediaType.TEXT_EVENT_STREAM_VALUE);
        assertThat(stream.getResponse().getContentAsString()).contains("event:message", "event:done");

        MvcResult history = mockMvc.perform(get("/api/chat/sessions/{id}/messages", sessionId))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(objectMapper.readTree(history.getResponse().getContentAsString())
                .path("data").path("records")).hasSize(2);

        mockMvc.perform(post("/api/chat/sessions/{id}/stream", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_EVENT_STREAM)
                        .content("{\"content\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("event:error")));
    }
}
