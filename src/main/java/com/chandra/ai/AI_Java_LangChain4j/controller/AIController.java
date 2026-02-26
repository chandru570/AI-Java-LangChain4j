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

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final SupportAssistant assistant;
    private final Optional<ClaudeAssistant> claudeAssistant;

    @Value("${langchain4j.anthropic.api-key:}")
    private String anthropicApiKey;

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
                return "Error calling Claude: " + e.getMessage() +
                       "\nAPI Key configured: " + (anthropicApiKey != null && !anthropicApiKey.isEmpty()) +
                       "\nPlease verify your ANTHROPIC_API_KEY is set correctly and the model is available.";
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
        if (anthropicApiKey != null && !anthropicApiKey.isEmpty()) {
            status.append("- API Key prefix: ").append(anthropicApiKey.substring(0, Math.min(10, anthropicApiKey.length()))).append("...\n");
        }
        return status.toString();
    }
}

