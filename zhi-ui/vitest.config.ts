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
      // 只统计可单测的逻辑模块 .ts 文件（页面模板由 E2E 覆盖），保证门槛真实可执行
      include: [
        'src/stores/**/*.ts',
        'src/utils/**/*.ts',
        'src/api/**/*.ts',
        'src/components/**/*.ts'
      ],
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
      // Vitest 4 使用 thresholds 生效（旧式顶层字段会被忽略）
      // 分阶段目标：先让门槛真实生效，随测试补充逐步上调到 70%
      thresholds: {
        lines: 55,
        functions: 64,
        branches: 40,
        statements: 55
      }
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
