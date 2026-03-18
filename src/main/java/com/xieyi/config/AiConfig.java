package com.xieyi.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    /**
     * 配置 ChatMemoryProvider，LangChain4j 会在每次对话时调用它
     */
    @Bean
    public ChatMemoryProvider chatMemoryProvider(ChatMemoryStore store) {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId) // 传入用户 ID
                .maxMessages(20) // 依然保留最近的 20 条，防止 Token 消耗过大
                .chatMemoryStore(store) // 挂载我们刚刚写的 MongoDB 存储实现！
                .build();
    }
}