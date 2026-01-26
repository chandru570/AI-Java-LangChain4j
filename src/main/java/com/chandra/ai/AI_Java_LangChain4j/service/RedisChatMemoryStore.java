package com.chandra.ai.AI_Java_LangChain4j.service;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisChatMemoryStore implements ChatMemoryStore {

    // Use StringRedisTemplate instead of a complex object template
    private final StringRedisTemplate redisTemplate;

    public RedisChatMemoryStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String key = "chat:" + memoryId;
        String json = redisTemplate.opsForValue().get(key);

        return (json != null) ? ChatMessageDeserializer.messagesFromJson(json) : new java.util.ArrayList<>();
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String key = "chat:" + memoryId;
        String json = ChatMessageSerializer.messagesToJson(messages);
        redisTemplate.opsForValue().set(key, json);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        redisTemplate.delete("chat:" + memoryId);
    }
}