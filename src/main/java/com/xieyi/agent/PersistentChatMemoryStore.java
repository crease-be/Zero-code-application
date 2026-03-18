package com.xieyi.agent;

import com.xieyi.model.MongoChatMemory;
import com.xieyi.repository.MongoChatMemoryRepository;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersistentChatMemoryStore implements ChatMemoryStore {

    private final MongoChatMemoryRepository repository;

    public PersistentChatMemoryStore(MongoChatMemoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        // 1. 根据 ID 从 MongoDB 查找记忆
        MongoChatMemory memory = repository.findById((String) memoryId).orElse(null);
        if (memory == null || memory.getMessagesJson() == null) {
            return new ArrayList<>();
        }
        // 2. 将 JSON 字符串反序列化为 LangChain4j 认识的 ChatMessage 列表
        return ChatMessageDeserializer.messagesFromJson(memory.getMessagesJson());
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        // 1. 将最新的消息列表序列化为 JSON 字符串
        String json = ChatMessageSerializer.messagesToJson(messages);
        // 2. 保存或更新到 MongoDB
        MongoChatMemory memory = new MongoChatMemory((String) memoryId, json);
        repository.save(memory);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        repository.deleteById((String) memoryId);
    }
}