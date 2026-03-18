import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      // 拦截所有以 /api 开头的请求，转发到 Spring Boot
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        // 如果后端有 context-path 这里可以做路径重写，当前不需要
      }
    }
  }
})