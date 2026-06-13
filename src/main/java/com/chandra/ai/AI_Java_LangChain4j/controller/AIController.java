package com.chandra.ai.AI_Java_LangChain4j.controller;

import com.chandra.ai.AI_Java_LangChain4j.service.ClaudeAssistant;
import com.chandra.ai.AI_Java_LangChain4j.service.SupportAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final SupportAssistant assistant;
    private final Optional<ClaudeAssistant> claudeAssistant;

    @Value("${langchain4j.anthropic.api-key:}")
    private String anthropicApiKey;

    private static final Logger logger = LoggerFactory.getLogger(AIController.class);

    public AIController(SupportAssistant assistant, @Autowired(required = false) ClaudeAssistant claudeAssistant) {
        this.assistant = assistant;
        this.claudeAssistant = Optional.ofNullable(claudeAssistant);
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String userId, @RequestParam String message) {
        return assistant.chat(userId, message);
    }

    @GetMapping("/ask-claude")
    public String askClaude(@RequestParam String userId, @RequestParam String message) {
        if (claudeAssistant.isPresent()) {
            try {
                return claudeAssistant.get().chat(userId, message);
            } catch (Exception e) {
                // Log full exception with stacktrace for server-side diagnostics
                logger.error("Error calling Claude assistant for user {}: {}", userId, e.getMessage(), e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error calling Claude service. Please contact support.");
            }
        }
        return "Claude assistant is not configured. Please set ANTHROPIC_API_KEY environment variable.";
    }

    @GetMapping("/health")
    public String health() {
        StringBuilder status = new StringBuilder();
        status.append("Health Check:\n");
        status.append("- Gemini Assistant: ").append("Available\n");
        status.append("- Claude Assistant: ").append(claudeAssistant.isPresent() ? "Available" : "Not Available").append("\n");
        status.append("- ANTHROPIC_API_KEY configured: ").append(anthropicApiKey != null && !anthropicApiKey.isEmpty()).append("\n");
        return status.toString();
    }
}

