package com.chandra.ai.AI_Java_LangChain4j.service;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Component
public class RedisChatMemoryStore implements ChatMemoryStore {

    // Use StringRedisTemplate instead of a complex object template
    private final StringRedisTemplate redisTemplate;

    public RedisChatMemoryStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final Logger logger = LoggerFactory.getLogger(RedisChatMemoryStore.class);

    @Value("${chat.memory.ttl.seconds:604800}")
    private long chatMemoryTtlSeconds;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String key = "chat:" + memoryId;
        String json = redisTemplate.opsForValue().get(key);

        try {
            return (json != null) ? ChatMessageDeserializer.messagesFromJson(json) : new java.util.ArrayList<>();
        } catch (Exception e) {
            logger.error("Failed to deserialize chat messages for key {}: {}", key, e.getMessage(), e);
            return new java.util.ArrayList<>();
        }
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String key = "chat:" + memoryId;
        String json = ChatMessageSerializer.messagesToJson(messages);
        // Store with TTL to avoid indefinite growth (configurable)
        redisTemplate.opsForValue().set(key, json, Duration.ofSeconds(chatMemoryTtlSeconds));
    }

    @Override
    public void deleteMessages(Object memoryId) {
        redisTemplate.delete("chat:" + memoryId);
    }
}