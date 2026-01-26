package com.chandra.ai.AI_Java_LangChain4j.config;

import dev.langchain4j.data.message.ChatMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, List<ChatMessage>> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, List<ChatMessage>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Use standard Java serialization or JSON for the messages
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}