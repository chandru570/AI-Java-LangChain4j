package com.chandra.ai.AI_Java_LangChain4j.controller;

import com.chandra.ai.AI_Java_LangChain4j.service.SupportAssistant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final SupportAssistant assistant;

    public AIController(SupportAssistant assistant) {
        this.assistant = assistant;
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String userId, @RequestParam String message) {
        return assistant.chat(userId, message);
    }
}