<template>
  <div
      class="gemini-layout"
      :class="{
      'sidebar-closed': !isSidebarOpen,
      'right-panel-closed': !isRightPanelOpen
    }"
  >
    <!-- 左侧导航/历史栏（可折叠） -->
    <aside class="gemini-sidebar">
      <div class="sidebar-top">
        <button class="icon-btn menu-toggle" @click="isSidebarOpen = !isSidebarOpen">
          <svg viewBox="0 0 24 24" width="24" height="24" fill="currentColor">
            <path d="M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z" />
          </svg>
        </button>
        <div class="sidebar-title" v-if="isSidebarOpen">ZeroCode</div>
      </div>

      <div class="sidebar-action" v-if="isSidebarOpen">
        <button class="new-chat-pill" @click="clearChat">
          <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
            <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z" />
          </svg>
          <span>新对话</span>
        </button>
      </div>

      <!-- 搜索框（UI占位） -->
      <div class="sidebar-search" v-if="isSidebarOpen">
        <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
          <path
              d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0 0 16 9.5 6.5 6.5 0 1 0 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"
          />
        </svg>
        <input type="text" placeholder="搜索历史..." />
      </div>

      <div class="sidebar-history" v-if="isSidebarOpen">
        <div class="history-group">
          <div class="history-label">今天</div>
          <div class="history-item active">
            <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
              <path
                  d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H5.17L4 17.17V4h16v12z"
              />
            </svg>
            <span class="truncate">ZeroCode 核心架构生成</span>
          </div>
          <div class="history-item">
            <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
              <path
                  d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H5.17L4 17.17V4h16v12z"
              />
            </svg>
            <span class="truncate">API 路由设计讨论</span>
          </div>
        </div>
        <div class="history-group">
          <div class="history-label">上周</div>
          <div class="history-item">
            <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
              <path
                  d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H5.17L4 17.17V4h16v12z"
              />
            </svg>
            <span class="truncate">数据库模型优化</span>
          </div>
        </div>
      </div>

      <div class="sidebar-footer" v-if="isSidebarOpen">
        <div class="user-info">
          <div class="user-avatar-small">解</div>
          <span class="user-name">解衣</span>
        </div>
      </div>
    </aside>

    <!-- 中间主对话区域 -->
    <main class="gemini-main">
      <header class="gemini-header">
        <div class="header-left">
          <button
              v-if="!isSidebarOpen"
              class="icon-btn"
              @click="isSidebarOpen = true"
          >
            <svg viewBox="0 0 24 24" width="24" height="24" fill="currentColor">
              <path d="M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z" />
            </svg>
          </button>
          <div class="model-selector">
            <span>ZeroCode Advanced</span>
            <svg viewBox="0 0 24 24" width="18" height="18" fill="currentColor">
              <path d="M7 10l5 5 5-5z" />
            </svg>
          </div>
        </div>
        <div class="header-right">
          <button class="icon-btn" @click="isRightPanelOpen = !isRightPanelOpen">
            <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
              <path
                  d="M3 17v2h6v-2H3zM3 5v2h10V5H3zm10 16v-2h8v-2h-8v-2h-2v6h2zM7 9v2H3v2h4v2h2V9H7zm14 4v-2H11v2h10zm-6-4h2V7h4V5h-4V3h-2v6z"
              />
            </svg>
          </button>
        </div>
      </header>

      <section class="gemini-chat-container" ref="messageListRef">
        <div class="chat-content-width">
          <div v-if="messages.length === 0" class="gemini-welcome">
            <h1 class="greeting-gradient">你好，解衣</h1>
            <h2 class="greeting-sub">今天想写点什么代码？</h2>
          </div>

          <div class="message-feed" v-else>
            <div
                v-for="(msg, index) in messages"
                :key="index"
                :class="['gemini-msg', msg.role]"
            >
              <div v-if="msg.role === 'user'" class="user-bubble-wrapper">
                <div class="user-bubble">{{ msg.content }}</div>
              </div>

              <div v-else class="ai-text-wrapper">
                <div class="ai-icon">
                  <svg viewBox="0 0 24 24" width="24" height="24" fill="url(#sparkle-grad)">
                    <defs>
                      <linearGradient
                          id="sparkle-grad"
                          x1="0%"
                          y1="0%"
                          x2="100%"
                          y2="100%"
                      >
                        <stop offset="0%" stop-color="#4285f4" />
                        <stop offset="50%" stop-color="#9b72cb" />
                        <stop offset="100%" stop-color="#d96570" />
                      </linearGradient>
                    </defs>
                    <path
                        d="M19.28 14.65l-1.42 1.42-3.53-3.54 1.42-1.42 3.53 3.54zM22 11h-4.5v-2H22v2zm-6.55-4.82l-3.53-3.54 1.42-1.42 3.53 3.54-1.42 1.42zM12 1v4.5h-2V1h2zm-4.82 6.55l-3.54-3.53 1.42-1.42 3.54 3.53-1.42 1.42zM2 13v-2h4.5v2H2zm4.82 6.55l3.53-3.54 1.42 1.42-3.53 3.54-1.42-1.42zM13 23h-2v-4.5h2V23zm5.03-2.03l-1.42-1.42 3.54-3.53 1.42 1.42-3.54 3.53z"
                    />
                  </svg>
                </div>
                <div class="ai-content">
                  <div
                      class="markdown-body"
                      v-html="renderMarkdown(msg.content)"
                  ></div>

                  <div
                      v-if="msg.content.includes('[[[SAVE_FILE')"
                      class="action-chip"
                  >
                    <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
                      <path
                          d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"
                      />
                    </svg>
                    本地文件已入库
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <div class="gemini-input-area">
        <div class="input-pill-container">
          <div class="input-pill" :class="{ 'is-focused': isInputFocused }">
            <button class="attach-btn">
              <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
                <path
                    d="M16.5 6v11.5c0 2.21-1.79 4-4 4s-4-1.79-4-4V5c0-1.38 1.12-2.5 2.5-2.5s2.5 1.12 2.5 2.5v10.5c0 .55-.45 1-1 1s-1-.45-1-1V6H10v9.5c0 1.38 1.12 2.5 2.5 2.5s2.5-1.12 2.5-2.5V5c0-2.21-1.79-4-4-4S7 2.79 7 5v12.5c0 3.04 2.46 5.5 5.5 5.5s5.5-2.46 5.5-5.5V6h-1.5z"
                />
              </svg>
            </button>
            <textarea
                ref="inputRef"
                v-model="userInput"
                @focus="isInputFocused = true"
                @blur="isInputFocused = false"
                @input="handleAutoHeight"
                @keydown.enter.prevent="handleEnterKey"
                placeholder="输入代码生成指令... (Shift+Enter 换行)"
                rows="1"
            ></textarea>
            <div class="pill-actions">
              <button
                  class="send-btn"
                  :class="{ active: userInput.trim() }"
                  :disabled="!userInput.trim() || isGenerating"
                  @click="sendMessage"
              >
                <svg viewBox="0 0 24 24" width="24" height="24" fill="currentColor">
                  <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z" />
                </svg>
              </button>
            </div>
          </div>
          <div class="legal-text">
            AI 生成的代码可能存在瑕疵，请在生产环境前进行测试。
          </div>
        </div>
      </div>
    </main>

    <!-- 右侧工具面板（可折叠） -->
    <aside class="gemini-right-panel">
      <div class="panel-header">
        <span v-if="isRightPanelOpen">工具</span>
        <button class="icon-btn" @click="isRightPanelOpen = !isRightPanelOpen">
          <svg viewBox="0 0 24 24" width="18" height="18" fill="currentColor">
            <path
                v-if="isRightPanelOpen"
                d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"
            />
            <path
                v-else
                d="M7 10l5 5 5-5z"
            />
          </svg>
        </button>
      </div>

      <div class="panel-content" v-if="isRightPanelOpen">
        <!-- 上下文卡片 -->
        <div class="context-card">
          <div class="card-title">
            <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
              <path
                  d="M14 2H6c-1.1 0-1.99.9-1.99 2L4 20c0 1.1.89 2 1.99 2H18c1.1 0 2-.9 2-2V8l-6-6zm2 16H8v-2h8v2zm0-4H8v-2h8v2zm-3-5V3.5L18.5 9H13z"
              />
            </svg>
            <span>当前上下文</span>
          </div>
          <div class="card-body">
            <div class="context-item">
              <span class="context-label">文件</span>
              <span class="context-value">src/components/Chat.vue</span>
            </div>
            <div class="context-item">
              <span class="context-label">选中</span>
              <span class="context-value code-snip">function sendMessage() {...}</span>
            </div>
          </div>
        </div>

        <!-- 会话摘要 -->
        <div class="summary-card">
          <div class="card-title">
            <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
              <path
                  d="M21 6h-2v2h-2V6h-2V4h2V2h2v2h2v2zm-10 3c1.66 0 3-1.34 3-3s-1.34-3-3-3-3 1.34-3 3 1.34 3 3 3zm0 4c-2.33 0-7 1.17-7 3.5V17h14v-2.5c0-2.33-4.67-3.5-7-3.5z"
              />
            </svg>
            <span>会话摘要</span>
          </div>
          <div class="card-body">
            <p>讨论 ZeroCode 核心架构，生成初始代码结构。</p>
          </div>
        </div>

        <!-- 快捷命令 -->
        <div class="quick-commands">
          <div class="card-title">快捷命令</div>
          <div class="command-list">
            <div class="command-item">
              <span class="command-key">/plan</span>
              <span class="command-desc">生成实现计划</span>
            </div>
            <div class="command-item">
              <span class="command-key">/explain</span>
              <span class="command-desc">解释选中代码</span>
            </div>
            <div class="command-item">
              <span class="command-key">/test</span>
              <span class="command-desc">生成单元测试</span>
            </div>
          </div>
        </div>
      </div>
    </aside>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick } from 'vue'
import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css' // 浅色高亮风格

const isSidebarOpen = ref(true)
const isRightPanelOpen = ref(true) // 新增右侧面板开关
const isInputFocused = ref(false)
const isGenerating = ref(false)
const userInput = ref('')
const messages = ref<any[]>([])
const messageListRef = ref<HTMLElement | null>(null)
const inputRef = ref<HTMLTextAreaElement | null>(null)

// Markdown 解析器 (同原逻辑)
const md = new MarkdownIt({
  highlight: (code, lang) => {
    const validLang = lang && hljs.getLanguage(lang) ? lang : 'plaintext'
    return `<div class="code-block-wrapper">
              <div class="code-bar"><span class="lang-text">${validLang}</span></div>
              <pre class="hljs"><code>${hljs.highlight(code, { language: validLang }).value}</code></pre>
            </div>`
  }
})

const renderMarkdown = (content: string) => {
  if (!content) return ''
  return md.render(content.replace(/\[\[\[SAVE_FILE:.*?\]\]\]/g, ''))
}

const handleAutoHeight = () => {
  const el = inputRef.value
  if (el) {
    el.style.height = 'auto'
    el.style.height = el.scrollHeight + 'px'
  }
}

const handleEnterKey = (e: KeyboardEvent) => {
  if (!e.shiftKey) sendMessage()
}

const scrollToBottom = async () => {
  await nextTick()
  if (messageListRef.value) {
    messageListRef.value.scrollTo({
      top: messageListRef.value.scrollHeight,
      behavior: 'smooth'
    })
  }
}

const sendMessage = async () => {
  if (!userInput.value.trim() || isGenerating.value) return

  const text = userInput.value
  userInput.value = ''
  if (inputRef.value) inputRef.value.style.height = 'auto'
  isGenerating.value = true

  messages.value.push({ role: 'user', content: text })
  messages.value.push({ role: 'ai', content: '' })
  scrollToBottom()

  try {
    const url = `/api/stream/chat?message=${encodeURIComponent(text)}`
    const eventSource = new EventSource(url)

    eventSource.onmessage = (e) => {
      const aiMsg = messages.value[messages.value.length - 1]
      aiMsg.content += e.data.replace(/\\n/g, '\n')
      scrollToBottom()
    }

    eventSource.onerror = (err) => {
      console.error("SSE Error:", err)
      eventSource.close()
      isGenerating.value = false
    }
  } catch (error) {
    console.error("Send failed:", error)
    isGenerating.value = false
  }
}

const clearChat = () => { messages.value = [] }
</script>

<style>
/* ----- 全局变量 & 重置 ----- */
:root {
  --g-bg-main: #ffffff;
  --g-bg-sidebar: #f8fafd;
  --g-bg-rightpanel: #f8fafd;
  --g-bg-bubble: #f0f4f9;
  --g-text-main: #1f1f1f;
  --g-text-secondary: #5f6368;
  --g-border: #e3e3e3;
  --g-accent: #0b57d0;
  --g-accent-light: #e8f0fe;
  --g-font-sans: -apple-system, BlinkMacSystemFont, 'Google Sans', 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
  --g-font-mono: 'Fira Code', 'IBM Plex Mono', 'Roboto Mono', 'SF Mono', monospace;
  --g-grad: linear-gradient(74deg, #4285f4 0, #9b72cb 9%, #d96570 20%, #d96570 24%, #9b72cb 35%, #4285f4 44%, #9b72cb 50%, #d96570 56%, #d96570 76%, #9b72cb 85%, #4285f4 100%);
}

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body, html {
  height: 100%;
  width: 100%;
  overflow: hidden;
  background: var(--g-bg-main);
  color: var(--g-text-main);
  font-family: var(--g-font-sans);
}

/* ----- 三栏布局容器 ----- */
.gemini-layout {
  display: flex;
  width: 100vw;
  height: 100vh;
}

/* ----- 左侧边栏 (可折叠) ----- */
.gemini-sidebar {
  width: 280px;
  background: var(--g-bg-sidebar);
  display: flex;
  flex-direction: column;
  transition: width 0.2s ease;
  flex-shrink: 0;
  overflow: hidden;
  border-right: 1px solid var(--g-border);
}
.sidebar-closed .gemini-sidebar {
  width: 0;
  border-right: none;
}

.sidebar-top {
  padding: 12px 16px;
  height: 64px;
  display: flex;
  align-items: center;
  gap: 12px;
}
.sidebar-title {
  font-size: 20px;
  font-weight: 500;
  color: var(--g-text-main);
  white-space: nowrap;
}

.sidebar-action {
  padding: 0 16px 16px;
}
.new-chat-pill {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #fff;
  border: 1px solid var(--g-border);
  padding: 10px 16px;
  border-radius: 24px;
  font-size: 14px;
  font-weight: 500;
  color: var(--g-text-main);
  cursor: pointer;
  transition: 0.2s;
  white-space: nowrap;
  width: 100%;
}
.new-chat-pill:hover {
  background: #f1f3f4;
  border-color: #d0d0d0;
}

.sidebar-search {
  padding: 0 16px 16px;
  display: flex;
  align-items: center;
  background: #fff;
  border: 1px solid var(--g-border);
  border-radius: 24px;
  margin: 0 16px 16px;
}
.sidebar-search svg {
  margin-left: 8px;
  color: var(--g-text-secondary);
}
.sidebar-search input {
  border: none;
  outline: none;
  background: transparent;
  padding: 8px 8px 8px 4px;
  width: 100%;
  font-size: 14px;
}
.sidebar-search input::placeholder {
  color: #9aa0a6;
}

.sidebar-history {
  flex: 1;
  overflow-y: auto;
  padding: 0 16px;
}
.history-group {
  margin-bottom: 16px;
}
.history-label {
  font-size: 12px;
  font-weight: 500;
  color: var(--g-text-secondary);
  margin-bottom: 8px;
  padding-left: 8px;
}
.history-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 12px;
  border-radius: 24px;
  font-size: 14px;
  color: var(--g-text-main);
  cursor: pointer;
  transition: 0.2s;
}
.history-item:hover {
  background: rgba(0, 0, 0, 0.04);
}
.history-item.active {
  background: var(--g-accent-light);
  color: var(--g-accent);
}
.truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid var(--g-border);
}
.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}
.user-avatar-small {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #9b72cb;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 500;
}
.user-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--g-text-main);
}

/* ----- 中间主区域 ----- */
.gemini-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
  background: #fff;
}

.gemini-header {
  height: 64px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  border-bottom: 1px solid var(--g-border);
}
.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}
.model-selector {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 16px;
  font-weight: 500;
  color: var(--g-text-main);
  cursor: pointer;
  padding: 6px 10px;
  border-radius: 8px;
  transition: background 0.2s;
}
.model-selector:hover {
  background: var(--g-bg-sidebar);
}
.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}
.icon-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: none;
  background: transparent;
  cursor: pointer;
  color: var(--g-text-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s;
}
.icon-btn:hover {
  background: rgba(0, 0, 0, 0.05);
}

/* 对话容器 */
.gemini-chat-container {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
}
.chat-content-width {
  width: 100%;
  max-width: 850px;
  padding: 0 20px;
  display: flex;
  flex-direction: column;
}

.gemini-welcome {
  margin-top: 15vh;
  margin-bottom: 40px;
}
.greeting-gradient {
  font-size: 56px;
  font-weight: 500;
  letter-spacing: -1px;
  background: var(--g-grad);
  background-size: 400% 100%;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 8px;
}
.greeting-sub {
  font-size: 56px;
  font-weight: 500;
  color: #c4c7c5;
  letter-spacing: -1px;
}

.message-feed {
  display: flex;
  flex-direction: column;
  gap: 32px;
  padding-bottom: 20px;
}

.user-bubble-wrapper {
  display: flex;
  justify-content: flex-end;
  width: 100%;
}
.user-bubble {
  background: var(--g-bg-bubble);
  color: var(--g-text-main);
  font-size: 16px;
  line-height: 1.6;
  padding: 12px 20px;
  border-radius: 24px;
  max-width: 80%;
  word-break: break-word;
}

.ai-text-wrapper {
  display: flex;
  gap: 16px;
  width: 100%;
}
.ai-icon {
  width: 32px;
  height: 32px;
  flex-shrink: 0;
  display: flex;
  align-items: flex-start;
  margin-top: 2px;
}
.ai-content {
  flex: 1;
  min-width: 0;
  padding-top: 4px;
}

.markdown-body {
  font-size: 16px;
  line-height: 1.7;
  color: var(--g-text-main);
}
.markdown-body p {
  margin-bottom: 16px;
}
.markdown-body code {
  font-family: var(--g-font-mono);
  font-size: 14px;
  background: #f1f3f4;
  padding: 2px 6px;
  border-radius: 6px;
}

.code-block-wrapper {
  margin: 16px 0;
  border-radius: 12px;
  overflow: hidden;
  background: #f8f9fa;
  border: 1px solid #e0e0e0;
}
.code-bar {
  background: #f0f4f9;
  padding: 8px 16px;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.lang-text {
  font-size: 12px;
  color: var(--g-text-secondary);
  font-family: var(--g-font-mono);
  text-transform: lowercase;
}
.hljs {
  padding: 16px !important;
  background: transparent !important;
  font-size: 14px;
  font-family: var(--g-font-mono);
}

.action-chip {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  border-radius: 8px;
  border: 1px solid var(--g-border);
  font-size: 13px;
  font-weight: 500;
  color: var(--g-text-secondary);
  margin-top: 16px;
}

/* 底部输入 */
.gemini-input-area {
  width: 100%;
  display: flex;
  justify-content: center;
  padding: 0 20px 20px;
}
.input-pill-container {
  width: 100%;
  max-width: 850px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.input-pill {
  width: 100%;
  background: var(--g-bg-bubble);
  border-radius: 32px;
  padding: 4px 4px 4px 16px;
  display: flex;
  align-items: flex-end;
  border: 1px solid transparent;
  transition: background 0.2s, box-shadow 0.2s, border-color 0.2s;
}
.input-pill.is-focused {
  background: #fff;
  border-color: #d0d0d0;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}
.attach-btn {
  background: transparent;
  border: none;
  padding: 8px 4px 8px 0;
  color: var(--g-text-secondary);
  cursor: pointer;
  display: flex;
  align-items: center;
  border-radius: 50%;
}
.attach-btn:hover {
  color: var(--g-accent);
}
textarea {
  flex: 1;
  background: transparent;
  border: none;
  outline: none;
  resize: none;
  font-size: 16px;
  line-height: 1.5;
  padding: 10px 0;
  max-height: 200px;
  color: var(--g-text-main);
  font-family: var(--g-font-sans);
}
textarea::placeholder {
  color: #9aa0a6;
}
.pill-actions {
  display: flex;
  align-items: center;
  padding-left: 8px;
}
.send-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: none;
  background: transparent;
  color: #c4c7c5;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: color 0.2s;
}
.send-btn.active {
  color: var(--g-accent);
}
.send-btn:disabled {
  cursor: not-allowed;
}
.legal-text {
  font-size: 12px;
  color: #9aa0a6;
  margin-top: 12px;
  text-align: center;
}

/* ----- 右侧面板 (可折叠) ----- */
.gemini-right-panel {
  width: 280px;
  background: var(--g-bg-rightpanel);
  display: flex;
  flex-direction: column;
  transition: width 0.2s ease;
  flex-shrink: 0;
  overflow: hidden;
  border-left: 1px solid var(--g-border);
}
.right-panel-closed .gemini-right-panel {
  width: 0;
  border-left: none;
}

.panel-header {
  height: 64px;
  padding: 0 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--g-border);
  font-weight: 500;
  color: var(--g-text-main);
}

.panel-content {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.context-card,
.summary-card,
.quick-commands {
  background: #fff;
  border-radius: 12px;
  border: 1px solid var(--g-border);
  overflow: hidden;
}
.card-title {
  padding: 12px 16px;
  background: #f8fafd;
  border-bottom: 1px solid var(--g-border);
  font-size: 14px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--g-text-main);
}
.card-body {
  padding: 16px;
}
.context-item {
  display: flex;
  flex-direction: column;
  margin-bottom: 12px;
  font-size: 13px;
}
.context-label {
  color: var(--g-text-secondary);
  margin-bottom: 4px;
}
.context-value {
  color: var(--g-text-main);
  word-break: break-word;
}
.code-snip {
  font-family: var(--g-font-mono);
  background: #f1f3f4;
  padding: 4px 8px;
  border-radius: 6px;
}
.summary-card p {
  font-size: 13px;
  color: var(--g-text-main);
  line-height: 1.5;
}
.command-list {
  padding: 8px 0;
}
.command-item {
  display: flex;
  align-items: center;
  padding: 8px 16px;
  gap: 12px;
  font-size: 13px;
}
.command-key {
  font-family: var(--g-font-mono);
  background: var(--g-accent-light);
  color: var(--g-accent);
  padding: 2px 8px;
  border-radius: 12px;
  font-weight: 500;
}
.command-desc {
  color: var(--g-text-secondary);
}

::-webkit-scrollbar {
  width: 6px;
}
::-webkit-scrollbar-thumb {
  background: #dadce0;
  border-radius: 10px;
}
</style>