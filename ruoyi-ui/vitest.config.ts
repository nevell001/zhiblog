import { defineConfig } from 'vitest/config'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  test: {
    globals: true,
    environment: 'jsdom',
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html'],
      exclude: [
        'node_modules/',
        'dist/',
        '**/*.spec.ts',
        '**/*.test.ts',
        '**/*.d.ts',
        '**/*.config.*',
        '**/mock/**',
        'src/utils/request.ts', // axios 封装
        'src/main.ts'
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
    }
  }
})
