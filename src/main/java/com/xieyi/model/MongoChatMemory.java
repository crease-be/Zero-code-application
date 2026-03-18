package com.xieyi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// 这个注解告诉 Spring，将数据存入 mongodb 的 chat_memory 集合中
@Document(collection = "chat_memory")
public class MongoChatMemory {
    
    @Id
    private String id; // 我们将使用用户的 ID 或 Session ID 作为记忆的唯一标识
    
    private String messagesJson; // LangChain4j 提供了将复杂消息对象序列化为 JSON 字符串的工具

    public MongoChatMemory() {}

    public MongoChatMemory(String id, String messagesJson) {
        this.id = id;
        this.messagesJson = messagesJson;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMessagesJson() { return messagesJson; }
    public void setMessagesJson(String messagesJson) { this.messagesJson = messagesJson; }
}