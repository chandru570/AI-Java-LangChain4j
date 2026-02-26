package com.chandra.ai.AI_Java_LangChain4j.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import com.chandra.ai.AI_Java_LangChain4j.service.ClaudeAssistant;
import com.chandra.ai.AI_Java_LangChain4j.service.SupportAssistant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class LangChainConfig {

    @Value("${langchain4j.google-ai.gemini.api-key:test-key}")
    private String geminiApiKey;

    @Value("${langchain4j.anthropic.api-key:}")
    private String anthropicApiKey;

    @Bean("gemimiChatModel")
    @Primary
    public ChatModel gemimiChatModel() {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(geminiApiKey)
                .modelName("gemini-2.5-flash")
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean("claudeChatModel")
    @ConditionalOnProperty(name = "langchain4j.anthropic.api-key", matchIfMissing = false)
    public ChatModel claudeChatModel() {
        return AnthropicChatModel.builder()
                .apiKey(anthropicApiKey)
                .modelName("claude-opus-4-1")
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider(ChatMemoryStore redisStore) {
        return memoryId -> MessageWindowChatMemory.builder()
                .maxMessages(10)
                .id(memoryId)
                .chatMemoryStore(redisStore)
                .build();
    }

    @Bean
    public SupportAssistant supportAssistant(
            @Qualifier("gemimiChatModel") ChatModel geminiModel,
            ChatMemoryProvider chatMemoryProvider) {
        return AiServices.builder(SupportAssistant.class)
                .chatModel(geminiModel)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "langchain4j.anthropic.api-key", matchIfMissing = false)
    public ClaudeAssistant claudeAssistant(
            @Qualifier("claudeChatModel") ChatModel claudeModel,
            ChatMemoryProvider chatMemoryProvider) {
        return AiServices.builder(ClaudeAssistant.class)
                .chatModel(claudeModel)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }
}