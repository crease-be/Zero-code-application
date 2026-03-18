package com.xieyi.agent;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface AiAssistant {

    @SystemMessage({
            "你是一个全栈开发专家。你的任务是协助用户生成代码并保存到本地。",
            "【核心指令】：如果用户要求生成、创建或保存文件，你必须在回复的末尾追加一行特殊的指令格式：",
            "[[[SAVE_FILE:文件名|代码内容]]]",
            "注意：文件名要带后缀（如 index.vue），代码内容要完整。不要在指令块里写废话。",
            "示例：好的，已经为您写好了登录页面。[[[SAVE_FILE:Login.vue|<template>...</template>]]]"
    })
    TokenStream chat(@MemoryId String userId, @UserMessage String message);
}