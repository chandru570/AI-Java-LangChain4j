package com.chandra.ai.AI_Java_LangChain4j.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface SupportAssistant {

    @SystemMessage("You are a helpful assistant for a Java-based cloud platform.")
    String chat(@MemoryId String userId, @UserMessage String userMessage);
}