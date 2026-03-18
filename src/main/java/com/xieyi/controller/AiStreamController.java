package com.xieyi.controller;

import com.xieyi.agent.AiAssistant;
import com.xieyi.tool.FileGeneratorTool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/stream")
public class AiStreamController {

    private final AiAssistant aiAssistant;
    private final FileGeneratorTool fileTool;

    public AiStreamController(AiAssistant aiAssistant, FileGeneratorTool fileTool) {
        this.aiAssistant = aiAssistant;
        this.fileTool = fileTool;
    }

    @GetMapping(value = "/chat", produces = "text/event-stream;charset=UTF-8")
    public SseEmitter streamChat(@RequestParam(defaultValue = "你好") String message) {
        SseEmitter emitter = new SseEmitter(0L);
        String currentUserId = "user_001";

        // 用于收集 AI 输出的完整字符串，以便后续解析指令
        StringBuilder fullResponse = new StringBuilder();

        aiAssistant.chat(currentUserId, message)
                .onNext(token -> {
                    try {
                        fullResponse.append(token); // 存入缓存
                        emitter.send(token);       // 发给前端展示
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                })
                .onComplete(response -> {
                    // 当流结束时，立刻检查有没有隐藏指令
                    processCustomCommand(fullResponse.toString());
                    emitter.complete();
                })
                .onError(emitter::completeWithError)
                .start();

        return emitter;
    }

    /**
     * 简单的字符串解析逻辑：寻找 [[[SAVE_FILE:文件名|内容]]]
     */
    private void processCustomCommand(String fullText) {
        String prefix = "[[[SAVE_FILE:";
        String suffix = "]]]";

        if (fullText.contains(prefix) && fullText.contains(suffix)) {
            try {
                int start = fullText.indexOf(prefix) + prefix.length();
                int end = fullText.lastIndexOf(suffix);

                String rawCommand = fullText.substring(start, end);
                // 使用第一个 "|" 分割文件名和内容
                String[] parts = rawCommand.split("\\|", 2);

                if (parts.length == 2) {
                    String filename = parts[0].trim();
                    String content = parts[1].trim();

                    // 调用 IO 工具保存
                    fileTool.saveCodeToFile(filename, content);
                }
            } catch (Exception e) {
                System.err.println("解析保存指令失败: " + e.getMessage());
            }
        }
    }
}