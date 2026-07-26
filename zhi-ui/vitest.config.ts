import { defineConfig } from 'vitest/config'
import vue from '@vitejs/plugin-vue'
import autoImport from 'unplugin-auto-import/vite'
import { resolve } from 'path'

export default defineConfig({
  plugins: [
    vue(),
    autoImport({
      imports: [
        'vue',
        'vue-router',
        'pinia',
        {
          '@/utils/safe-watch': ['safeWatch', 'safeWatchEffect']
        }
      ],
      dts: false
    })
  ],
  test: {
    globals: true,
    environment: 'jsdom',
    envDir: '.', // Load .env files from root directory
    // 测试超时时间（毫秒）
    timeout: 10000,
    // 启用并行测试执行
    pool: 'threads',
    // 线程池配置（Vitest 4 使用顶层 worker 配置）
    maxWorkers: 4,
    minWorkers: 2,
    // 最大并发测试数
    maxConcurrency: 4,
    // 启用测试缓存
    cache: true,
    // 测试隔离（每个测试文件独立运行）
    isolate: true,
    // 覆盖率配置
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html'],
      // 测试失败时也生成报告
      reportOnFailure: true,
      exclude: [
        'node_modules/',
        'dist/',
        '**/*.spec.ts',
        '**/*.d.ts',
        '**/*.config.*',
        '**/mock/**',
        'src/utils/request.ts', // axios 封装
        'src/main.ts',
        // 测试文件应该被排除（本身不需要测试覆盖）
        'src/**/*.test.ts',
        'src/**/*.spec.ts'
      ],
      lines: 70,
      functions: 70,
      branches: 70,
      statements: 70
    },
    include: ['**/*.{test,spec}.{js,ts,vue}'],
    exclude: ['node_modules', 'dist', 'build']
  },
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    },
    extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
  }
})
