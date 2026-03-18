package com.xieyi.controller;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class AiTestController {

    // LangChain4j 会根据你的 application.yml 自动注入这个大模型实例
    private final ChatLanguageModel chatLanguageModel;

    public AiTestController(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(defaultValue = "你好，请做个简短的自我介绍") String message) {
        // 调用 DeepSeek 生成回复
        return chatLanguageModel.generate(message);
    }
}