package com.springtrace.ai.service;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class BharatCodeService {
    private final RestClient client;
    private final ObjectMapper mapper;
    private final LocalDebugAnalyzer localAnalyzer;
    private final String apiKey;
    private final String model;
    private final String chatPath;
    private final int maxTokens;
    private final double temperature;
    private final boolean enabled;

    public BharatCodeService(RestClient bharatCodeRestClient, ObjectMapper mapper,
            LocalDebugAnalyzer localAnalyzer,
            @Value("${bharatcode.api-key:}") String apiKey,
            @Value("${bharatcode.model:qwen-3.8-27b}") String model,
            @Value("${bharatcode.chat-completions-path:/chat/completions}") String chatPath,
            @Value("${bharatcode.max-tokens:6000}") int maxTokens,
            @Value("${bharatcode.temperature:0.2}") double temperature,
            @Value("${bharatcode.enabled:true}") boolean enabled) {
        this.client = bharatCodeRestClient;
        this.mapper = mapper;
        this.localAnalyzer = localAnalyzer;
        this.apiKey = apiKey;
        this.model = model;
        this.chatPath = chatPath;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.enabled = enabled;
    }

    public boolean isConfigured() { return enabled && apiKey != null && !apiKey.isBlank(); }
    public String mode() { return isConfigured() ? "LIVE" : "LOCAL"; }

    public String analyze(String language, String issueType, String content) {
        if (!isConfigured()) return localAnalyzer.analyze(language, issueType, content);

        String systemPrompt = "You are SpringTrace AI, a careful senior Java/Spring debugging engineer. "
                + "Analyze only the supplied evidence. Never claim you executed or compiled code. "
                + "Separate confirmed errors from possible risks. If a line number is unavailable, do not invent one. "
                + "For source code, inspect control flow and business logic, not only exception names. "
                + "Return readable Markdown with these sections: Summary; Findings (severity and exact evidence); "
                + "Root cause; Corrected code (complete relevant method/class when possible); Why the fix works; "
                + "How to verify (tests); Prevention. Preserve the user's language and framework context. "
                + "Never expose secrets from the input in your response.";
        String userPrompt = "Language: " + safe(language) + "\nIssue category: " + safe(issueType)
                + "\n\nAnalyze the following developer input:\n```text\n" + content + "\n```";
        Map<String, Object> payload = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)),
                "temperature", temperature,
                "max_tokens", maxTokens);
        try {
            String raw = client.post().uri(chatPath)
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(headers -> headers.setBearerAuth(apiKey))
                    .body(payload)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        throw new IllegalStateException("BharatCode returned HTTP " + response.getStatusCode().value()
                                + ". Check API access, model access and service availability.");
                    })
                    .body(String.class);
            JsonNode root = mapper.readTree(raw);
            JsonNode choice = root.path("choices").path(0);
            String answer = choice.path("message").path("content").asText("");
            String finishReason = choice.path("finish_reason").asText("");
            if (answer.isBlank()) throw new IllegalStateException("BharatCode returned an empty answer.");
            if ("length".equalsIgnoreCase(finishReason)) {
                answer += "\n\n> **Note:** The model reached its output-token limit. Some sections may be incomplete; increase BHARATCODE_MAX_TOKENS and run again.";
            }
            return answer;
        } catch (RestClientResponseException ex) {
            throw new IllegalStateException("BharatCode request failed (HTTP " + ex.getStatusCode().value()
                    + "). Verify your key and model access. Do not share the key in screenshots.", ex);
        } catch (Exception ex) {
            if (ex instanceof IllegalStateException state) throw state;
            throw new IllegalStateException("Could not get a valid response from BharatCode. Check your connection, API URL, key and model. Details: " + ex.getMessage(), ex);
        }
    }

    private String safe(String value) { return value == null || value.isBlank() ? "Not specified" : value; }
}
